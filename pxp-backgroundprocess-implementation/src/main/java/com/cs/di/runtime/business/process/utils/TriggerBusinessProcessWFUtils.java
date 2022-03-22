package com.cs.di.runtime.business.process.utils;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.dto.RelationsSetDTO;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.ActionSubTypes;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.BusinessProcessActionType;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.Usecase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * This Utility class help to 
 * Trigger all qualifying 
 * after create and after save 
 * Business Process WF 
 * after import/transfer of entity completed 
 *
 */
public class TriggerBusinessProcessWFUtils {

  private static final String BASE_TYPE         = "baseType";
  private static final String ACTION            = "businessProcessActionType";
  private static final String KLASSIDS          = "klassIds";
  private static final String TAXONOMYIDS       = "taxonomyIds";
  private static final String USERNAME          = "userName";
  private static final String TRANSACTIONID     = "transactionID";
  private static final String SESSIONID         = "sessionID";
  private static final String LOGINTIME         = "loginTime";
  private static final String USERIID           = "userIID";
  private static final String TAGIDS            = "tagIds";
  private static final String ATTRIBUTEIDS      = "attributeIds";
  private static final String ACTIONSUBTYPE     = "actionSubType";
  private static final String KLASS_INSTANCE_ID = "klassInstanceId";
  private static final String USERID            = "userID";
  private static final String USECASE           = "usecase";
  private static final String ACTIONSUBTYPE_ARRAY     = "actionSubTypeArray";
  
  /**
   * This is to trigger AFTER CREATE/AFTER SAVE WF
   * depending on Import performed
   * i.e. create/update
   * 
   * @param baseEntity
   * @param action
   * @throws Exception
   */
  public void triggerBusinessProcessWF(IBaseEntityDTO baseEntity, BusinessProcessActionType action, IUserSessionDTO userSession,
      Set<IPropertyRecordDTO> createdUpdatedPropRec, List<IClassifierDTO> addRemoveclassifiers, List<IClassifierDTO> finalClassifiersList, Usecase usecase)
  {
    try {
      List<String> actionSubTypes = new ArrayList<String>();
      List<String> taxonomyIds = new ArrayList<String>();
      List<String> typeIds = new ArrayList<String>();
      List<String> tagIds = new ArrayList<String>();
      List<String> attributeIds = new ArrayList<String>();
      List<String> relationshipIds = new ArrayList<String>();
      
      // setting transaction data
      Map<String, Object> map = new HashMap<String, Object>();
      if (BaseType.ARTICLE.equals(baseEntity.getBaseType()) || BaseType.ASSET.equals(baseEntity.getBaseType())) {
        // Setting userSession in map
        map.put(USERNAME, userSession.getUserName());
        map.put(TRANSACTIONID, userSession.getTransactionID());
        map.put(SESSIONID, userSession.getSessionID());
        map.put(LOGINTIME, userSession.getLoginTime());
        map.put(USERIID, userSession.getUserIID());
        map.put(USERID, "admin"); // Need to set usercode
        // Base Type used in both CREATE/UPDATE
        map.put(ACTION, action.toString());
        map.put(BASE_TYPE, baseEntity.getBaseType().toString());
        // for Nature Type
        typeIds.add(baseEntity.getNatureClassifier().getClassifierCode());
        // for Non Nature/Taxonomy
        getTaxonomyAndTypeIds(finalClassifiersList, typeIds, taxonomyIds);
        map.put(KLASS_INSTANCE_ID, baseEntity.getBaseEntityIID());
        switch (action) {
          case AFTER_CREATE:
            // setting AFTER_CREATE Action Type
            map.put(KLASSIDS, typeIds);
            map.put(USECASE, usecase.toString());
            // REST call using URL
            triggerBusinessProcessURL(prepareBusinessProcessWFURL(baseEntity), new ObjectMapper().writeValueAsString(map));
            break;
          // update Request ARTICLE/ASSET
          case AFTER_SAVE:
            // setting AFTER_SAVE Action Type
            getTagAndAttributeRelationshipIds(createdUpdatedPropRec, attributeIds, tagIds, relationshipIds);
            map.put(ATTRIBUTEIDS, attributeIds);
            map.put(TAGIDS, tagIds);
            map.put(TAXONOMYIDS, taxonomyIds);
            map.put(KLASSIDS, typeIds);
            map.put(USECASE, usecase.toString());
            actionSubTypes = fetchActionSubType(addRemoveclassifiers, attributeIds, tagIds, relationshipIds);
            if (actionSubTypes.isEmpty())
              return;
            else {
              map.put(ACTIONSUBTYPE_ARRAY,actionSubTypes );
              map.put(ACTIONSUBTYPE, actionSubTypes.get(0));
              try {
                triggerBusinessProcessURL(prepareBusinessProcessWFURL(baseEntity), new ObjectMapper().writeValueAsString(map));
              }
              catch (CSInitializationException | JsonProcessingException e) {
                RDBMSLogger.instance().exception(e);
              }
             
            }
            break;
          default:
            break;
        }
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
      
    }
  }
  
  /**
   * depending on criteria
   * action subtype is decided
   * @param actionSubType
   * @param taxonmyIds
   * @param tagIds
   * @param attributeIds
   * @param relationshipIds
   * @return
   */
  private List<String> fetchActionSubType(List<IClassifierDTO> addedRemovedList, List<String> attributeIds, List<String> tagIds,
      List<String> relationshipIds)
  {
    List<String> actionSubType = new ArrayList<String>();
    if (!tagIds.isEmpty() || !attributeIds.isEmpty())
      actionSubType.add(ActionSubTypes.AFTER_PROPERTIES_SAVE.toString());
    if (!addedRemovedList.isEmpty())
      actionSubType.add(ActionSubTypes.AFTER_CLASSIFICATION_SAVE.toString());
    if (!relationshipIds.isEmpty())
      actionSubType.add(ActionSubTypes.AFTER_RELATIONSHIP_SAVE.toString());
    return actionSubType;
  }
  
  /**
   * prepare typeIds and taxonmyIds for the given classifierDTO
   * 
   * @param baseEntityDTO
   * @param typeIds
   * @param taxonomyIds
   */
  private void getTaxonomyAndTypeIds(List<IClassifierDTO> classifier, List<String> typeIds, List<String> taxonomyIds)
  {
    for (IClassifierDTO classifierDTO : classifier) {
      switch (classifierDTO.getClassifierType()) {
        case CLASS:
          typeIds.add(classifierDTO.getCode());
          break;
        case TAXONOMY:
        // case HIERARCHY:  TODO: PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies
          taxonomyIds.add(classifierDTO.getCode());
          break;
        default:
          break;
      }
    }
  }
  
  /**
   * prepare tagIds and attributeIds for the given classifierDTO
   * 
   * @param baseEntityDTO
   * @param typeIds
   * @param taxonmyIds
   */
  private void getTagAndAttributeRelationshipIds(Set<IPropertyRecordDTO> propertyRecordDTOs, List<String> attributeIds ,List<String> tagIds, List<String> relationship)
  {
    attributeIds.addAll(propertyRecordDTOs.stream().filter(p -> p instanceof ValueRecordDTO).map(IPropertyRecordDTO::getProperty)
        .collect(Collectors.toList()).stream().map(IPropertyDTO::getPropertyCode).collect(Collectors.toList()));
    tagIds.addAll(propertyRecordDTOs.stream().filter(p -> p instanceof TagsRecordDTO).map(IPropertyRecordDTO::getProperty)
        .collect(Collectors.toList()).stream().map(IPropertyDTO::getPropertyCode).collect(Collectors.toList()));
    relationship.addAll(propertyRecordDTOs.stream().filter(p -> p instanceof RelationsSetDTO).map(IPropertyRecordDTO::getProperty)
        .collect(Collectors.toList()).stream().map(IPropertyDTO::getPropertyCode).collect(Collectors.toList()));
  }
  
  /**
   * triggerBusinessProcess using URL and request provided
   * 
   * @param businessProcessURL
   * @param requestBody
   */
  private void triggerBusinessProcessURL(String businessProcessURL, String requestBody)
  {
     if (!businessProcessURL.isEmpty()) {
      WebTarget pxpServer = ClientBuilder.newClient().target(businessProcessURL);
      RDBMSLogger.instance().info("triggerBusinessProcessURL is %s", businessProcessURL);
      try {
        pxpServer.request().post(Entity.entity(requestBody, MediaType.APPLICATION_JSON));
      }
      catch (Exception ex) {
        RDBMSLogger.instance().exception(ex);
      }
    }
    else {
      RDBMSLogger.instance().warn("triggerBusinessProcessURL is Empty ");
    }
  }
  
  /**
   * Prepare URL to call
   * 
   * @return
   * @throws CSInitializationException
   */
  private String prepareBusinessProcessWFURL(IBaseEntityDTO baseEntityDTO)
      throws CSInitializationException
  {
    String tomcatURL = CSProperties.instance().getString("tomcat.server.url");
    String warName = CSProperties.instance().getString("tomcat.war.name");
    return String.format(
        "%s/%s/bgp/businessprocess?organizationId=%s&physicalCatalogId=%s&dataLanguage=%s", tomcatURL,
        warName,baseEntityDTO.getLocaleCatalog().getOrganizationCode(),
        baseEntityDTO.getLocaleCatalog().getCatalogCode(), baseEntityDTO.getBaseLocaleID());
  }
  
  /** This method is to check if attribute/tag 
   * came in updated request while transfer/import 
   * True - needs to be updated/created
   * false - no modification needed  
   * @param originPropertyRecord
   * @param targetPropertyRecord
   * @return
   */
  public static boolean isPropertyUpdatedForWFConfig(IPropertyRecordDTO originPropertyRecord,
      IPropertyRecordDTO targetPropertyRecord)
  {
    switch (originPropertyRecord.getProperty().getSuperType()) {
      case ATTRIBUTE:
        IValueRecordDTO sourceAttribute = (IValueRecordDTO) originPropertyRecord;
        IValueRecordDTO targetAttribute = (IValueRecordDTO) targetPropertyRecord;
        if(targetAttribute.getValue().equals(sourceAttribute.getValue())) {
          return false;
        }
        break;

      case TAGS :
        ITagsRecordDTO sourceTag = (ITagsRecordDTO) originPropertyRecord;
        ITagsRecordDTO targetTag = (ITagsRecordDTO) targetPropertyRecord;
        if(sourceTag.getTags().size()==targetTag.getTags().size() && targetTag.getTags().containsAll(sourceTag.getTags())) {
          return false;
        }
        break;
    }
    return true;
  }
  
  /**
   * This is an extension written to fetch
   * IPropertyRecordDTO from IPropertyDTO 
   * then compare source with target and extract 
   * the list of newly created/modified property records
   * @param attributeTagRelationRecords
   * @param baseEntityDAO
   * @param targetPropertyRecords
   * @param propertySet
   */
  public static void fetchPropertyRecordsToModifyCreate(Set<IPropertyRecordDTO> attributeTagRelationRecords, BaseEntityDAO baseEntityDAO,
      Set<IPropertyRecordDTO> targetPropertyRecords, Set<IPropertyDTO> propertySet)
  {
    Set<IPropertyRecordDTO> sourcePropertyRecords = new HashSet<IPropertyRecordDTO>();
    propertySet.forEach(p -> {
      IBaseEntityDTO sourceDTO;
      try {
        sourceDTO = baseEntityDAO.loadPropertyRecords(p);
        IPropertyRecordDTO propertyRecord = sourceDTO.getPropertyRecord(p.getPropertyIID());
        if (propertyRecord != null) {
          sourcePropertyRecords.add(propertyRecord);
        }
      }
      catch (RDBMSException | CSFormatException e) {
        RDBMSLogger.instance().warn(e.getMessage());
      }
      
    });
    try {
      IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
      baseEntityDAO.getLocaleCatalog().fillAllProperties(List.of(baseEntityDTO));
      sourcePropertyRecords.addAll(baseEntityDTO.getPropertyRecords());
    }
    catch (RDBMSException e) {
      RDBMSLogger.instance().warn(e.getMessage());
    }

    for (IPropertyRecordDTO target : targetPropertyRecords) {
      IPropertyRecordDTO matchingRecordFromSource = sourcePropertyRecords.stream().filter(source -> source.equals(target)).findFirst()
          .orElse(null);
      if (matchingRecordFromSource != null) {
        if (TriggerBusinessProcessWFUtils.isPropertyUpdatedForWFConfig(matchingRecordFromSource, target)) {
          attributeTagRelationRecords.add(target);
        }
      }
      else {
        attributeTagRelationRecords.add(target);
      }
    }
  }
}
