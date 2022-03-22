package com.cs.core.elastic.utils;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.elastic.Index;
import com.cs.core.elastic.ibuilders.ISearchBuilder.Fields;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.ISimpleTrackingDTO;
import com.cs.core.rdbms.tracking.idto.ISimpleTrackingDTO.TrackingAttributes;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * This Util class contains functions that can be used while syncing elastic documents.
 * @author faizan.siddique
 *
 */
public class ElasticSyncUtil {

  public static final String       RED_VOILATION_FILTER                                 = "red";
  public static final String       ORANGE_VOILATION_FILTER                              = "orange";
  public static final String       YELLOW_VOILATION_FILTER                              = "yellow";
  public static final String       GREEN_VOILATION_FILTER                               = "green";

  public static void fillFlatLevelProperties(Map<String, Object> newInstance, IBaseEntityDTO baseEntity) throws RDBMSException
  {
    IContextualDataDTO contextualObject = baseEntity.getContextualObject();

    newInstance.put(Fields.baseentityid.toString(), baseEntity.getBaseEntityID());
    newInstance.put(Fields.baseEntityIid.toString(), baseEntity.getBaseEntityIID());
    newInstance.put(Fields.catalogCode.toString(), baseEntity.getCatalog().getCatalogCode());
    String organizationCode = baseEntity.getCatalog().getOrganizationCode();
    if (StringUtils.isEmpty(organizationCode)) {
      organizationCode = IStandardConfig.STANDARD_ORGANIZATION_RCODE;
    }
    newInstance.put(Fields.organisationCode.toString(), organizationCode);
    newInstance.put(Fields.baseType.toString(), baseEntity.getBaseType());
    newInstance.put(Fields.parentIID.toString(), baseEntity.getParentIID());
    newInstance.put(Fields.topParentIID.toString(), baseEntity.getTopParentIID());
    newInstance.put(Fields.endpointCode.toString(), baseEntity.getEndpointCode());

    newInstance.put(Fields.isRoot.toString(), contextualObject.isNull());
    newInstance.put(Fields.contextID.name(), contextualObject.getContextCode());
    newInstance.put(Fields.contextStartTime.toString(), contextualObject.getContextStartTime());
    newInstance.put(Fields.contextEndTime.toString(), contextualObject.getContextEndTime());

    if (BaseType.ASSET.equals(baseEntity.getBaseType())) {
      newInstance.put(Fields.isExpired.toString(), baseEntity.isExpired());
      newInstance.put(Fields.isDuplicate.toString(), baseEntity.isDuplicate());
    }
  }
  
  public static void fillFlatLevelAttributes(Map<String, Object> independentAttribute, IBaseEntityDTO baseEntity)
  {
    ISimpleTrackingDTO lastModifiedTrack = baseEntity.getLastModifiedTrack();
    independentAttribute.put(Index.text_mapping_prefix + TrackingAttributes.lastmodifiedbyattribute, lastModifiedTrack.getWho());
    independentAttribute.put(Index.number_mapping_prefix + TrackingAttributes.lastmodifiedattribute, lastModifiedTrack.getWhen());

    ISimpleTrackingDTO createdTrack = baseEntity.getCreatedTrack();
    independentAttribute.put(Index.text_mapping_prefix + TrackingAttributes.createdbyattribute, createdTrack.getWho());
    independentAttribute.put(Index.number_mapping_prefix + TrackingAttributes.createdonattribute, createdTrack.getWhen());
  }
  
  public static void fillClassifiers(Map<String, Object> instance, ILocaleCatalogDAO localeCatalogDAO, IBaseEntityDTO baseEntity) throws RDBMSException
  {
    Set<IClassifierDTO> otherClassifiers = baseEntity.getOtherClassifiers();

    Set<String> classIds = new HashSet<>();
    Set<String> taxonomyIds = new HashSet<>();
    for (IClassifierDTO classifier : otherClassifiers) {
      if (classifier.getClassifierType().equals(ClassifierType.CLASS)) {
        classIds.add(classifier.getCode());
      }
      else {
        taxonomyIds.add(classifier.getCode());
      }
    }
    classIds.add(baseEntity.getNatureClassifier().getCode());
    instance.put(Fields.classIds.toString(), classIds);

    Set<String> allTaxonomyIds = localeCatalogDAO.getAllTaxonomyParents(taxonomyIds);
    instance.put(Fields.taxonomyIds.toString(), allTaxonomyIds);
  }
  
  public static void fillCollectionId(Map<String, Object> instance, ILocaleCatalogDAO localeCatalogDAO, Long baseEntityIID) throws RDBMSException
  {
    Set<String> allCollectionIds =localeCatalogDAO.openCollection().getAllCollectionIds(baseEntityIID);
    instance.put(Fields.collectionIds.toString(), allCollectionIds);
  }
  
  public static void fillColorViolations(Map<String, Object> instance, ILocaleCatalogDAO localeCatalogDAO, Long baseEntityIID) throws RDBMSException
  {
    Map<String, List<String>> violationVSLocalesMap = localeCatalogDAO.getViolationVSLocalesMap(baseEntityIID);    
    
    List<String> redViolations = violationVSLocalesMap.get(RED_VOILATION_FILTER);
    List<String> orangeViolations = violationVSLocalesMap.get(ORANGE_VOILATION_FILTER);
    List<String> yellowViolations = violationVSLocalesMap.get(YELLOW_VOILATION_FILTER);
    
    if(redViolations == null)
        redViolations = new ArrayList<String>();
    instance.put(Fields.red.name(), redViolations);
    
    if(orangeViolations == null)
        orangeViolations = new ArrayList<String>();
    instance.put(Fields.orange.name(), orangeViolations);
  
    if(yellowViolations == null)
        yellowViolations = new ArrayList<String>();
    instance.put(Fields.yellow.name(), yellowViolations);
     
  }
  
}
