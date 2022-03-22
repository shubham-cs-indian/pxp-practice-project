package com.cs.dam.runtime.assetinstance.smartdocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.config.interactor.entity.relationship.RelationshipInstance;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipInstanceModel;
import com.cs.core.config.interactor.usecase.smartdocument.GetConfigDetailsToFetchDataForSmartDocumentRequestModel;
import com.cs.core.config.interactor.usecase.smartdocument.IGetConfigDetailsToFetchDataForSmartDocumentRequestModel;
import com.cs.core.config.interactor.usecase.smartdocument.IGetConfigDetailsToFetchDataForSmartDocumentResponseModel;
import com.cs.core.config.strategy.usecase.smartdocument.IGetConfigDetailsToFetchDataForSmartDocumentStrategy;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.runtime.builder.BuilderFactory;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.relationship.IElementsRelationshipInformation;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.entity.variants.ContextInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.smartdocument.GetInstancesForSmartDocumentRelationshipFetchResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGetEntityForSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGetInstancesForSmartDocumentRelationshipFetchResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGetInstancesForSmartDocumentResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentKlassInstanceDataModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentTagInstanceDataModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.util.ConfigUtil;
import com.cs.dam.runtime.smartdocument.IGetRelationshipInstancesForSmartDocumentService;
import com.cs.utils.dam.SmartDocumentUtils;

@Service
public class GetRelationshipInstancesForSmartDocumentService
    extends AbstractGetInstancesForSmartDocumentService
    implements IGetRelationshipInstancesForSmartDocumentService {
  
  @Autowired
  protected IGetConfigDetailsToFetchDataForSmartDocumentStrategy getConfigDetailsToFetchDataForSmartDocumentStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                                  rdbmsComponentUtils;
  
  @Autowired
  protected ConfigUtil                                           configUtil;
  
  @Override
  public IListModel<ISmartDocumentKlassInstanceDataModel> executeInternal(
      IGetEntityForSmartDocumentRequestModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
  
  /**
   * Get preset config details for smart documents
   */
  @Override
  protected IGetConfigDetailsToFetchDataForSmartDocumentResponseModel getConfigDetails(
      IGetEntityForSmartDocumentRequestModel dataModel) throws Exception
  {
    IGetConfigDetailsToFetchDataForSmartDocumentRequestModel configRequestModel = new GetConfigDetailsToFetchDataForSmartDocumentRequestModel();
    configRequestModel.setEntityId(dataModel.getEntityId());
    configRequestModel.setPresetId(dataModel.getPresetId());
    configRequestModel.setClassName(CommonConstants.RELATIONSHIP_KLASS);
    IGetConfigDetailsToFetchDataForSmartDocumentResponseModel returnModel = getConfigDetailsToFetchDataForSmartDocumentStrategy
        .execute(configRequestModel);
    return returnModel;
  }
  
  /**
   * Fetch relationship instances for smart document
   */
  @Override
  protected IGetInstancesForSmartDocumentResponseModel getInstancesForSmartDocument(
      IGetEntityForSmartDocumentRequestModel dataModel,
      IGetConfigDetailsToFetchDataForSmartDocumentResponseModel configDetails) throws Exception
  {
    IGetInstancesForSmartDocumentRelationshipFetchResponseModel relationshipInstancesForSmartDocumentResponseModel = new GetInstancesForSmartDocumentRelationshipFetchResponseModel();
    
    List<String> klassInstanceIds = new ArrayList<String>();
    List<IRelationshipInstance> relationshipInstances = new ArrayList<IRelationshipInstance>();
    Map<String, IRelationshipInstance> relationshipInstancesMap = new HashMap<String, IRelationshipInstance>();
    Map<String, IAssetInformationModel> imageAttributeInstanceMap = new HashMap<String, IAssetInformationModel>();
    String instanceId = dataModel.getInstanceId();
    String entityId = dataModel.getEntityId();
    int from = dataModel.getFrom();
    int size = dataModel.getSize();
    String languageCode = configDetails.getPresetConfigDetails()
        .getLanguageCode();
    
    // Get config details of original baseentity and load property records
    IBaseEntityDTO baseEntityDTO = rdbmsComponentUtils.getLocaleCatlogDAO()
        .getEntityByID(instanceId);
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO);
    IGetConfigDetailsForCustomTabModel configDetailsForInstance = getConfigDetailsForKlassInstance(
        languageCode, baseEntityDAO);
    
    Map<String, IReferencedRelationshipModel> referencedRelationship = configDetailsForInstance
        .getReferencedRelationships();
    referencedRelationship.keySet()
        .removeIf(key -> !key.equals(entityId));
    
    IRelationshipInstanceModel relationshipInstanceModel = BuilderFactory
        .newRelationshipInstanceBuilder(rdbmsComponentUtils, configDetailsForInstance)
        .baseEntityDAO(baseEntityDAO)
        .build();
    
    // Fill relationship instances and prepare response model
    baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    Set<IPropertyRecordDTO> propertyRecords = baseEntityDTO.getPropertyRecords();
    fillRelationshipInstancesForSmartDocument(klassInstanceIds, relationshipInstances,
        relationshipInstancesMap, entityId, propertyRecords, relationshipInstanceModel, from, size);
    List<IBaseEntityDTO> baseEntityDTOs = new ArrayList<IBaseEntityDTO>();
    if (klassInstanceIds != null && !klassInstanceIds.isEmpty()) {
      baseEntityDTOs = rdbmsComponentUtils.getLocaleCatlogDAO()
          .getAllEntitiesByIIDList(String.join(",", klassInstanceIds), null, true);
      fillImageAttributeInstanceMap(imageAttributeInstanceMap, baseEntityDTOs);
    }
    fillRelationshipInstancesForSmartDocumentResponseModel(
        relationshipInstancesForSmartDocumentResponseModel, relationshipInstances,
        imageAttributeInstanceMap, baseEntityDTOs);
    
    return relationshipInstancesForSmartDocumentResponseModel;
  }
  
  /**
   * Fill response model relationshipInstancesForSmartDocumentResponseModel
   * 
   * @param relationshipInstancesForSmartDocumentResponseModel
   * @param relationshipInstances
   * @param imageAttributeInstanceMap
   * @param baseEntityDTOs
   */
  private void fillRelationshipInstancesForSmartDocumentResponseModel(
      IGetInstancesForSmartDocumentRelationshipFetchResponseModel relationshipInstancesForSmartDocumentResponseModel,
      List<IRelationshipInstance> relationshipInstances,
      Map<String, IAssetInformationModel> imageAttributeInstanceMap,
      List<IBaseEntityDTO> baseEntityDTOs)
  {
    relationshipInstancesForSmartDocumentResponseModel
        .setRelationshipInstances(relationshipInstances);
    relationshipInstancesForSmartDocumentResponseModel.setKlassInstances(baseEntityDTOs);
    relationshipInstancesForSmartDocumentResponseModel
        .setInstancesImageAttribute(imageAttributeInstanceMap);
  }
  
  /**
   * Fill image information in map imageAttributeInstanceMap for smart document
   * 
   * @param imageAttributeInstanceMap
   * @param baseEntityDTOs
   * @throws Exception
   */
  private void fillImageAttributeInstanceMap(
      Map<String, IAssetInformationModel> imageAttributeInstanceMap,
      List<IBaseEntityDTO> baseEntityDTOs) throws Exception
  {
    for (IBaseEntityDTO baseEntityDTO : baseEntityDTOs) {
      IJSONContent entityExtension = baseEntityDTO.getEntityExtension();
      if (!entityExtension.isEmpty()) {
        String entityExtensionString = entityExtension.toString();
        IAssetInformationModel assetInformation = ObjectMapperUtil.readValue(entityExtensionString,
            AssetInformationModel.class);
        imageAttributeInstanceMap.put(baseEntityDTO.getBaseEntityID(), assetInformation);
      }
    }
  }
  
  /**
   * Fill relationship instances in list relationshipInstances for smart
   * document
   * 
   * @param klassInstanceIds
   * @param relationshipInstances
   * @param relationshipInstancesMap
   * @param entityId
   * @param propertyRecords
   * @param relationshipInstanceModel
   */
  private void fillRelationshipInstancesForSmartDocument(List<String> klassInstanceIds,
      List<IRelationshipInstance> relationshipInstances,
      Map<String, IRelationshipInstance> relationshipInstancesMap, String entityId,
      Set<IPropertyRecordDTO> propertyRecords, IRelationshipInstanceModel relationshipInstanceModel,
      int from, int size)
  {
    IKlassInstanceRelationshipInstance relationshipInstanceEntity = relationshipInstanceModel
        .getContentRelationships()
        .get(0);
    if (relationshipInstanceEntity.getElementIds()
        .size() > size) {
      klassInstanceIds.addAll(relationshipInstanceEntity.getElementIds()
          .subList(from, size));
    }
    else {
      klassInstanceIds.addAll(relationshipInstanceEntity.getElementIds());
    }
    
    List<IKlassInstanceInformationModel> referenceRelationshipInstanceElements = new ArrayList<IKlassInstanceInformationModel>();
    Map<String, List<IKlassInstanceInformationModel>> relationshipInstanceElements = relationshipInstanceModel
        .getReferenceRelationshipInstanceElements();
    if (relationshipInstanceElements != null && !relationshipInstanceElements.isEmpty()) {
      referenceRelationshipInstanceElements = relationshipInstanceModel
          .getReferenceRelationshipInstanceElements()
          .get(relationshipInstanceEntity.getSideId());
      if (referenceRelationshipInstanceElements.size() > size) {
        referenceRelationshipInstanceElements = referenceRelationshipInstanceElements.subList(from,
            size);
      }
    }
    
    // Set all context details in the relationship instance
    for (String elementId : klassInstanceIds) {
      IElementsRelationshipInformation elementsRelationshipInformation = relationshipInstanceEntity
          .getElementsRelationshipInfo()
          .get(elementId);
      Map<String, List<IContentTagInstance>> elementTagMapping = relationshipInstanceEntity
          .getElementTagMapping();
      IRelationshipInstance relationshipInstance = fillTagsAndContextDetailsForRelationshipInstance(
          entityId, relationshipInstanceEntity, elementId, elementsRelationshipInformation,
          elementTagMapping);
      relationshipInstancesMap.put(elementId, relationshipInstance);
    }
    
    // Set remaining information of context details and relationship instance
    // information
    for (IKlassInstanceInformationModel klassInstance : referenceRelationshipInstanceElements) {
      if (klassInstanceIds.contains(klassInstance.getId())) {
        IRelationshipInstance relationshipInstance = relationshipInstancesMap
            .get(klassInstance.getId());
        relationshipInstance.setSide2BaseType(klassInstance.getBaseType());
        relationshipInstance.setSide2InstanceId(klassInstance.getId());
        for (IPropertyRecordDTO propertyRecord : propertyRecords) {
          fillRelationshipInstanceContextInformation(klassInstance, relationshipInstance,
              propertyRecord);
        }
        relationshipInstances.add(relationshipInstance);
      }
    }
  }
  
  /**
   * Fill context information into context of respective relationship instance
   * for smart document
   * 
   * @param klassInstance
   * @param relationshipInstance
   * @param propertyRecord
   */
  private void fillRelationshipInstanceContextInformation(
      IKlassInstanceInformationModel klassInstance, IRelationshipInstance relationshipInstance,
      IPropertyRecordDTO propertyRecord)
  {
    IRelationsSetDTO relationsSetDTO = (IRelationsSetDTO) propertyRecord;
    IEntityRelationDTO entityRelationDTO = relationsSetDTO
        .getRelationByIID(Long.parseLong(klassInstance.getId()));
    if (entityRelationDTO != null) {
      IContextualDataDTO sideContexualData = entityRelationDTO.getContextualObject();
      IContextDTO contextObject = sideContexualData.getContext();
      
      IInstanceTimeRange timeRange = new InstanceTimeRange();
      timeRange.setFrom(sideContexualData.getContextStartTime());
      timeRange.setTo(sideContexualData.getContextEndTime());
      
      IContextInstance context = relationshipInstance.getContext();
      context.setContextId(contextObject.getCode());
      context.setId(Long.toString(sideContexualData.getContextualObjectIID()));
      context.setTimeRange(timeRange);
      
      relationshipInstance.setContext(context);
    }
  }
  
  /**
   * Fill all relationship instances with tags and context details for smart
   * document
   * 
   * @param entityId
   * @param relationshipInstanceEntity
   * @param elementId
   * @param elementsRelationshipInformation
   * @param elementTagMapping
   * @return
   */
  
  private IRelationshipInstance fillTagsAndContextDetailsForRelationshipInstance(String entityId,
      IKlassInstanceRelationshipInstance relationshipInstanceEntity, String elementId,
      IElementsRelationshipInformation elementsRelationshipInformation,
      Map<String, List<IContentTagInstance>> elementTagMapping)
  {
    List<IContentTagInstance> contentTagInstance = elementTagMapping.get(elementId);
    IContextInstance context = new ContextInstance();
    List<String> tagInstanceIds = new ArrayList<String>();
    List<IIdAndBaseType> linkedInstances = new ArrayList<>();
    List<IContentTagInstance> tags = new ArrayList<>();
    
    IRelationshipInstance relationshipInstance = new RelationshipInstance();
    if (elementsRelationshipInformation != null) {
      relationshipInstance
          .setRelationshipObjectId(elementsRelationshipInformation.getRelationshipObjectId());
      relationshipInstance.setCommonRelationshipInstanceId(
          elementsRelationshipInformation.getCommonRelationshipInstanceId());
    }
    
    // Fill tags details and linked instances for context of relationship
    // instance
    if (contentTagInstance != null) {
      for (IContentTagInstance tagInstance : contentTagInstance) {
        IIdAndBaseType linkedInstance = new IdAndBaseType();
        linkedInstance.setId(tagInstance.getId());
        linkedInstance.setBaseType(tagInstance.getBaseType());
        linkedInstances.add(linkedInstance);
        tagInstanceIds.add(tagInstance.getTagId());
      }
      tags.addAll(contentTagInstance);
    }
    context.setTagInstanceIds(tagInstanceIds);
    context.setLinkedInstances(linkedInstances);
    relationshipInstance.setRelationshipId(entityId);
    relationshipInstance.setSideId(relationshipInstanceEntity.getSideId());
    relationshipInstance
        .setCount(Integer.parseInt(Long.toString(relationshipInstanceEntity.getTotalCount())));
    relationshipInstance.setTags(tags);
    relationshipInstance.setContext(context);
    return relationshipInstance;
  }
  
  @Override
  protected void processSmartDocumentModel(
      ISmartDocumentKlassInstanceDataModel smartDocumentKlassInstance, IBaseEntityDTO baseEntityDTO,
      IGetConfigDetailsForCustomTabModel configDetailsForInstance,
      IGetInstancesForSmartDocumentResponseModel response)
  {
    List<IRelationshipInstance> relationshipInstances = ((IGetInstancesForSmartDocumentRelationshipFetchResponseModel) response)
        .getRelationshipInstances();
    for (IRelationshipInstance relationshipInstance : relationshipInstances) {
      if (StringUtils.equals(relationshipInstance.getSide2InstanceId(),
          Long.toString(baseEntityDTO.getBaseEntityIID()))) {
        Map<String, ISmartDocumentTagInstanceDataModel> tags = new HashMap<String, ISmartDocumentTagInstanceDataModel>();
        SmartDocumentUtils.fillTagsInformation(tags, relationshipInstance.getTags());
        SmartDocumentUtils.fillContextInformation(relationshipInstance.getContext(), tags,
            smartDocumentKlassInstance);
        break;
      }
    }
  }
  
}
