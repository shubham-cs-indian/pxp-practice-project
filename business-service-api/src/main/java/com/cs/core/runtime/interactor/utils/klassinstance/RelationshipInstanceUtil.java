package com.cs.core.runtime.interactor.utils.klassinstance;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.config.interactor.entity.relationship.RelationshipInstance;
import com.cs.core.config.interactor.model.configdetails.GetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsResponseModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipInstanceModel;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.runtime.builder.BuilderFactory;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRelationshipVersion;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.variants.ContextInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.exception.variants.DuplicateVariantExistsException;
import com.cs.core.runtime.interactor.model.context.ICheckDuplicateLinkedVariantRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetPostConfigDetailsStrategy;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Component
public class RelationshipInstanceUtil {
  
  @Autowired
  public IGetPostConfigDetailsStrategy postConfigDetailsForRelationshipsStrategy;
  
  public static IRelationshipInstance getRelationshipInstanceFromRelationshipVersion(
      IRelationshipVersion addedElement, String relationshipId, String sideId,
      String side1InstanceId, String side1BaseType, String side2BaseType) throws RDBMSException
  {
    IRelationshipInstance relationshipInstance = new RelationshipInstance();
    
    String documentId = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP.getPrefix());
    relationshipInstance.setId(documentId);
    relationshipInstance.setOriginalInstanceId(documentId);
    relationshipInstance.setRelationshipId(relationshipId);
    relationshipInstance.setSide1InstanceId(side1InstanceId);
    relationshipInstance.setSide1BaseType(side1BaseType);
    relationshipInstance.setSide2InstanceId(addedElement.getId());
    relationshipInstance.setSide2BaseType(side2BaseType);
    relationshipInstance.setSideId(sideId);
    relationshipInstance.setCount(addedElement.getCount());
    // relationshipInstance.setVersionId(addedElement.getVersionId());
    String contextId = addedElement.getContextId();
    if (contextId != null) {
      IContextInstance contextInstance = new ContextInstance();
      String contextInstanceId = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.CONTEXT.getPrefix());
      contextInstance.setId(contextInstanceId);
      contextInstance.setContextId(contextId);
      contextInstance.setTimeRange(addedElement.getTimeRange());
      List<IContentTagInstance> tags = addedElement.getTags();
      for (IContentTagInstance iContentTagInstance : tags) {
        contextInstance.getTagInstanceIds()
            .add(iContentTagInstance.getId());
        ((ITagInstance) iContentTagInstance).setContextInstanceId(contextInstanceId);
      }
      relationshipInstance.setContext(contextInstance);
      relationshipInstance.setTags(tags);
    }
    return relationshipInstance;
  }
  
  public static IRelationshipInstance getRelationshipInstanceFromRelationshipVersion(
      IRelationshipVersion addedElement, String relationshipId, String sideId,
      String side1InstanceId, String side1BaseType, String side2BaseType, String otherSideId,
      String side1EntityType, String side2EntityType) throws RDBMSException
  {
    IRelationshipInstance relationshipInstance = new RelationshipInstance();
    String documentId = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP.getPrefix());
    relationshipInstance.setId(documentId);
    relationshipInstance.setOriginalInstanceId(documentId);
    relationshipInstance.setRelationshipId(relationshipId);
    relationshipInstance.setSide1InstanceId(side1InstanceId);
    relationshipInstance.setSide1BaseType(side1BaseType);
    relationshipInstance.setSide2InstanceId(addedElement.getId());
    relationshipInstance.setSide2BaseType(side2BaseType);
    relationshipInstance.setSideId(sideId);
    relationshipInstance.setCount(addedElement.getCount());
    // relationshipInstance.setVersionId(addedElement.getVersionId());
    relationshipInstance.setOtherSideId(otherSideId);
    relationshipInstance.setSide1EntityType(side1EntityType);
    relationshipInstance.setSide2EntityType(side2EntityType);
    
    String contextId = addedElement.getContextId();
    if (contextId != null) {
      IContextInstance contextInstance = new ContextInstance();
      contextInstance.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.CONTEXT.getPrefix()));
      contextInstance.setContextId(contextId);
      contextInstance.setTimeRange(addedElement.getTimeRange());
      List<IContentTagInstance> tags = addedElement.getTags();
      for (IContentTagInstance iContentTagInstance : tags) {
        contextInstance.getTagInstanceIds()
            .add(iContentTagInstance.getId());
      }
      relationshipInstance.setContext(contextInstance);
      relationshipInstance.setTags(tags);
    }
    return relationshipInstance;
  }
  
  public void executeGetRelationshipInstance(IGetKlassInstanceCustomTabModel returnModel,
      IGetConfigDetailsForCustomTabModel configDetails, IBaseEntityDAO baseEntityDAO,
      RDBMSComponentUtils rdbmsComponentUtils) throws Exception
  {
    IRelationshipInstanceModel relationshipInstanceModel = BuilderFactory
        .newRelationshipInstanceBuilder(rdbmsComponentUtils, configDetails)
        .baseEntityDAO(baseEntityDAO)
        .build();
    
    returnModel.setContentRelationships(relationshipInstanceModel.getContentRelationships());
    returnModel.setReferenceRelationshipInstanceElements(
        relationshipInstanceModel.getReferenceRelationshipInstanceElements());
    returnModel.setNatureRelationships(relationshipInstanceModel.getNatureRelationships());
    returnModel.setReferenceNatureRelationshipInstanceElements(
        relationshipInstanceModel.getReferenceNatureRelationshipInstanceElements());
   
    for (String lvCode : configDetails.getLinkedVariantCodes()) {
      
      IPropertyDTO relationProperty = ConfigurationDAO.instance().getPropertyByCode(lvCode);
      relationProperty.setRelationSide(RelationSide.SIDE_2);
      
      IBaseEntityDTO result = baseEntityDAO.loadPropertyRecords(relationProperty);
      if (result.getPropertyRecords().isEmpty()) {
        for (String entry : returnModel.getReferenceRelationshipInstanceElements().keySet()) {
          IReferencedSectionElementModel referenceElement = configDetails.getReferencedElements()
              .get(entry);
          if (referenceElement != null) {
            referenceElement.setCouplingType(CommonConstants.LOOSELY_COUPLED);
          }
        }
       
      }
    }
    addKlassTypeConfigDataForRelationshipInstanceElements(returnModel, configDetails);
  }
  
  private void addKlassTypeConfigDataForRelationshipInstanceElements(
      IGetKlassInstanceCustomTabModel returnModel, IGetConfigDetailsForCustomTabModel configDetails) throws Exception
  {
    Set<String> klassIdsSet = new HashSet<>();
    returnModel.getReferenceRelationshipInstanceElements()
        .values()
        .forEach(klassInstances -> {
          klassInstances.forEach(klassInstanceInformation -> {
            klassIdsSet.addAll(klassInstanceInformation.getTypes());
          });
        });
    returnModel.getReferenceNatureRelationshipInstanceElements()
    	.values()
    	.forEach(klassInstances -> {
    	  klassInstances.forEach(klassInstanceInformation -> {
    		klassIdsSet.addAll(klassInstanceInformation.getTypes());
          });
       });
    if (!klassIdsSet.isEmpty()) {
      List<String> klassIdsList = new ArrayList<>(klassIdsSet);
      IGetPostConfigDetailsRequestModel requestModel = new GetPostConfigDetailsRequestModel();
      requestModel.setKlassIds(klassIdsList);
      long starTime = System.currentTimeMillis();
      IGetPostConfigDetailsResponseModel responseModel = postConfigDetailsForRelationshipsStrategy
          .execute(requestModel);
      RDBMSLogger.instance()
          .debug("NA|OrientDB|" + 
              this.getClass().getSimpleName()
              + "|addKlassTypeConfigDataForRelationshipInstanceElements|postConfigDetailsForRelationshipsStrategy| %d ms",
              System.currentTimeMillis() - starTime);
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses = configDetails.getReferencedKlasses();
      referencedKlasses.putAll(responseModel.getReferencedKlasses());
    }
  }
  
  private static final String LINKED_VARIANT_CONTEXT_DUPLICATE_BASE_QUERY = "select count(1) from pxp.relation re join "
      + "pxp.contextualObject co ON  re.side1contextualobjectiid = co.contextualObjectIID where re.side1entityiid = ? "
      + "and co.contextCode = ? and re.propertyiid = ?";
  
  public void validateLinkedVariantContext(ICheckDuplicateLinkedVariantRequestModel dataModel,
      long relationshipPropertyIID) throws RDBMSException
  {
    AtomicBoolean isDuplicate = new AtomicBoolean(false);
    StringBuilder duplicateQuery = new StringBuilder(LINKED_VARIANT_CONTEXT_DUPLICATE_BASE_QUERY);
    Long sourceEntityIID = Long.parseLong(dataModel.getId());
    generateGenericDuplicateQuery(duplicateQuery, dataModel);
    
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          
          PreparedStatement stmt = currentConn.prepareStatement(duplicateQuery.toString());
          stmt.setLong(1, sourceEntityIID);
          stmt.setString(2, dataModel.getContextId());
          stmt.setLong(3, relationshipPropertyIID);
          
          stmt.execute();
          
          IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
          result.next();
          isDuplicate.set(result.getLong("count") > 0);
        });
    if (isDuplicate.get()) {
      throw new DuplicateVariantExistsException();
    }
  }
  
  private void generateGenericDuplicateQuery(StringBuilder baseDuplicateQuery,
      ICheckDuplicateLinkedVariantRequestModel dataModel)
  {
    Set<ITagInstanceValue> contextTagValues = dataModel.getTags()
        .stream()
        .flatMap(tag -> tag.getTagValues()
            .stream())
        .filter(contextTagValue -> contextTagValue.getRelevance() != 0)
        .collect(Collectors.toSet());
    
    // check if time enabled, as these fields are not empty when time enabled.
    Long timeRangeFrom = dataModel.getTimeRange()
        .getFrom();
    Long timeRangeTo = dataModel.getTimeRange()
        .getTo();
    
    Boolean isTimeEnabled = (timeRangeFrom != null && timeRangeFrom != 0)
        && (timeRangeTo != null && timeRangeTo != 0);
    
    if (isTimeEnabled) {
      // the third parameter of function used to generate time range in this
      // query is used to denote
      // mutual inclusion or exclusion.
      baseDuplicateQuery.append(String.format(" and cxttimerange && int8range(%s, %s, '[]')",
          timeRangeFrom, timeRangeTo));
    }
    
    if (contextTagValues.isEmpty()) {
      // context tags are empty for empty tags
      baseDuplicateQuery.append("and (co.cxttags is null or co.cxttags = '') ");
    }
    else {
      String tagQuery = String.format(" and array_length(avals(cxttags), 1) = %d ",
          contextTagValues.size());
      
      Iterator<ITagInstanceValue> tagValuesIterator = contextTagValues.iterator();
      String tagValuesQuery = "";
      while (tagValuesIterator.hasNext()) {
        ITagInstanceValue tagValue = tagValuesIterator.next();
        String tagValueMatchQuery = String.format(" %s=>%s ", tagValue.getTagId(),
            tagValue.getRelevance());
        
        if (tagValuesIterator.hasNext()) {
          tagValuesQuery = tagValuesQuery + tagValueMatchQuery + " , ";
        }
        else {
          tagValuesQuery = tagValuesQuery + tagValueMatchQuery;
        }
      }
      String format = String.format(" and cxttags @> '%s' ", tagValuesQuery);
      baseDuplicateQuery.append(tagQuery + format);
    }
  }
}
