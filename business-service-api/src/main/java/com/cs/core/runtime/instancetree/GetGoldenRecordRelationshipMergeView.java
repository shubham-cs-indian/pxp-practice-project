package com.cs.core.runtime.instancetree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffect;
import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffectType;
import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.config.interactor.entity.relationship.RelationshipInstance;
import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForGetKlassInstancesToMergeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.goldenrecord.bucket.IGetGoldenRecordMergeView;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.goldenrecord.ContentInfoWithAssetDetailsModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IConfigDetailsForGetRelationshipDataModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IContentInfoWithAssetDetailsModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleKlassInstancesMergeViewRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleKlassInstancesMergeViewResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IRecommendationModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IRelationshipRecommendationModel;
import com.cs.core.runtime.interactor.model.goldenrecord.ITargetRelationshipInstancesModel;
import com.cs.core.runtime.interactor.model.goldenrecord.RelationshipRecommendationModel;
import com.cs.core.runtime.interactor.model.goldenrecord.TargetRelationshipInstancesModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipSidePropertiesModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.TagInstanceUtils;
import com.cs.core.runtime.strategy.usecase.goldenrecord.IGetConfigDetailsForGetRelationshipDataFromSourcesStrategy;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.BaseEntityUtils;

@Service
public class GetGoldenRecordRelationshipMergeView extends
    AbstractGetGoldenRecordMergeView<IGoldenRecordRuleKlassInstancesMergeViewRequestModel, IGoldenRecordRuleKlassInstancesMergeViewResponseModel>
    implements IGetGoldenRecordMergeView {
  
  @Autowired
  protected IGetConfigDetailsForGetRelationshipDataFromSourcesStrategy getConfigDetailsForGetRelationshipDataFromSourcesStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                                        rdbmsComponentUtils;
  
  @Autowired
  protected GoldenRecordUtils                                          goldenRecordUtils;
  
  @Override
  public void getRecommendedPropertyDataForMergeView(IGoldenRecordRuleKlassInstancesMergeViewRequestModel dataModel,
      IConfigDetailsForGetKlassInstancesToMergeModel configDetails, IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel,
      List<IBaseEntityDTO> baseEntitiesDTOs, ILocaleCatalogDAO localeCatlogDAO) throws Exception
  {
    if (dataModel.getGoldenRecordId() == null) {
      getRecommendedRelationshipDataForMergeView(dataModel, configDetails, localeCatlogDAO, responseModel, baseEntitiesDTOs);
    }
    else {
      getGoldenRecordInstanceRelationshipDataForMergeView(dataModel, configDetails, localeCatlogDAO, responseModel);
    }
    
    List<String> contextTagIds = responseModel.getContextTagIds();
    if (!contextTagIds.isEmpty()) {
      IIdsListParameterModel tagIdsList = new IdsListParameterModel();
      tagIdsList.setIds(contextTagIds);
      IConfigDetailsForGetRelationshipDataModel tagConfigDetails = getConfigDetailsForGetRelationshipDataFromSourcesStrategy
          .execute(tagIdsList);
      configDetails.getReferencedTags().putAll(tagConfigDetails.getReferencedTags());
    }
  }
  
  private void getRecommendedRelationshipDataForMergeView(
      IGoldenRecordRuleKlassInstancesMergeViewRequestModel dataModel, IConfigDetailsForGetKlassInstancesToMergeModel configDetails,
      ILocaleCatalogDAO localeCatlogDAO, IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel, List<IBaseEntityDTO> baseEntityDTOs) throws Exception
  {
    Map<String, List<String>> contentIdVsklassInstanceTypesAndTaxonomys = new HashMap<>();
    Map<String, List<String>> supplierIdVsContentIds = new HashMap<>();

    for (IBaseEntityDTO baseEntityDTO : baseEntityDTOs) {
      Set<String> baseEntityKlassIds = new HashSet<>();
      Set<String> baseEntityTaxonomyIds = new HashSet<>();
      goldenRecordUtils.getKlassIdsAndTaxonomyIdsList(baseEntityDTO, baseEntityKlassIds, baseEntityTaxonomyIds);
      List<String> klassAndTaxonomyIds = new ArrayList<String>(baseEntityKlassIds);
      klassAndTaxonomyIds.addAll(baseEntityTaxonomyIds);
      contentIdVsklassInstanceTypesAndTaxonomys.put(String.valueOf(baseEntityDTO.getBaseEntityIID()), klassAndTaxonomyIds);
      fillSupplierIdVsContentIdsMap(baseEntityDTO.getSourceOrganizationCode(), String.valueOf(baseEntityDTO.getBaseEntityIID()),
          supplierIdVsContentIds);
    }
    
    IMergeEffect mergeEffect = dataModel.getMergeEffect();
    fillRecommendationRealtionship(responseModel, dataModel, mergeEffect.getRelationships(), contentIdVsklassInstanceTypesAndTaxonomys,
        supplierIdVsContentIds, configDetails);
    fillRecommendationRealtionship(responseModel, dataModel, mergeEffect.getNatureRelationships(),
        contentIdVsklassInstanceTypesAndTaxonomys, supplierIdVsContentIds, configDetails);
  }
  
  private void getGoldenRecordInstanceRelationshipDataForMergeView(
      IGoldenRecordRuleKlassInstancesMergeViewRequestModel dataModel, IConfigDetailsForGetKlassInstancesToMergeModel configDetails,
      ILocaleCatalogDAO localeCatlogDAO, IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel) throws Exception
  {
    responseModel.setRecommendation(new HashMap<String, IRecommendationModel>());
    fillGoldenRecordRelationshipInstances(dataModel, responseModel, configDetails, localeCatlogDAO);
  }

  private void fillRecommendationRealtionship(IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel,
      IGoldenRecordRuleKlassInstancesMergeViewRequestModel dataModel, List<IMergeEffectType> relationshipMergeEffect,
      Map<String, List<String>> contentIdVsklassInstanceTypesAndTaxonomys, Map<String, List<String>> supplierIdVsContentIds,
      IConfigDetailsForGetKlassInstancesToMergeModel configDetails)
  {
    Map<String, IReferencedRelationshipPropertiesModel> referencedRelationships = configDetails.getReferencedRelationshipProperties();
    relationshipMergeEffect.forEach(relationshipEffect -> {
      String relationshipId = relationshipEffect.getEntityId();
      List<String> supplierIds = relationshipEffect.getSupplierIds();
      IReferencedRelationshipPropertiesModel referencedRelationship = referencedRelationships.get(relationshipId);
      if (referencedRelationship == null) {
        return;
      }
      try {
        fillRecommendationsForSide(responseModel, relationshipId, dataModel, referencedRelationship.getSide1(),
            contentIdVsklassInstanceTypesAndTaxonomys, supplierIdVsContentIds, supplierIds, configDetails);
        fillRecommendationsForSide(responseModel, relationshipId, dataModel, referencedRelationship.getSide2(),
            contentIdVsklassInstanceTypesAndTaxonomys, supplierIdVsContentIds, supplierIds, configDetails);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
  
  private void fillRecommendationsForSide(IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel, String relationshipId,
      IGoldenRecordRuleKlassInstancesMergeViewRequestModel dataModel, IRelationshipSidePropertiesModel sideData,
      Map<String, List<String>> contentIdVsklassInstanceTypesAndTaxonomys, Map<String, List<String>> supplierIdVsContentIds,
      List<String> supplierIds, IConfigDetailsForGetKlassInstancesToMergeModel configDetails) throws Exception
  {
    Boolean isAutoCreate = dataModel.getIsAutoCreate();
    List<String> klassIds = sideData.getKlassIds();
    ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    Map<String, IRecommendationModel> recommendationMap = responseModel.getRecommendation();
    if(recommendationMap == null) {
      recommendationMap = new HashMap<>();
    }
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    Map<String, String> propertyCodeVsSideId = getPropertyCodeVsSideIdMap(referencedElements);
    
    Boolean isSupplierIdPresent = false;
    for (String supplierId : supplierIds) {
      if (supplierIdVsContentIds.containsKey(supplierId) && !isSupplierIdPresent) {
        isSupplierIdPresent = true;
        List<String> applicableContentIds = getApplicableContentIds(klassIds, supplierIdVsContentIds.get(supplierId),
            contentIdVsklassInstanceTypesAndTaxonomys);
        if (applicableContentIds.isEmpty()) {
          continue;
        }
        for (String baseEntityId : applicableContentIds) {
          IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(baseEntityId));
          Collection<IPropertyDTO> allEntityProperties = localeCatalogDAO.getAllEntityProperties(Long.parseLong(baseEntityId));
          List<IPropertyDTO> relationDTOs = allEntityProperties.stream().filter(x -> x.getRelationSide().ordinal() > 0)
              .collect(Collectors.toList());
          IBaseEntityDTO loadPropertyRecords = baseEntityDAO
              .loadPropertyRecords(relationDTOs.toArray(new IPropertyDTO[relationDTOs.size()]));
          Set<IPropertyRecordDTO> propertyRecords = loadPropertyRecords.getPropertyRecords();
          Boolean isRecommendationFilled = false;
          Map<String, List<String>> baseTypeVsKlassInstanceId = new HashMap<>();
          for (IPropertyRecordDTO propertyRecord : propertyRecords) {
            String propertyCode = propertyRecord.getProperty().getPropertyCode();
            if (propertyCode.equals(relationshipId)) {
              IRelationshipRecommendationModel recommendation = new RelationshipRecommendationModel();
              recommendation.setRelationshipId(relationshipId);
              recommendation.setSupplierId(supplierId);
              recommendation.setcontentId(baseEntityId);
              recommendation.setPropertyId(propertyCodeVsSideId.get(propertyCode));
              recommendation.setPropertyType(CommonConstants.RELATIONSHIP);
              recommendationMap.put(propertyCodeVsSideId.get(propertyCode), recommendation);
              
              isRecommendationFilled = true;
              if (!isAutoCreate) {
                IReferencedSectionRelationshipModel referencedSectionElementModel = (IReferencedSectionRelationshipModel) referencedElements
                    .get(propertyCodeVsSideId.get(propertyCode));
                ITargetRelationshipInstancesModel targetRelationshipInstance = recommendation.getTargetRelationshipInstances();
                if (targetRelationshipInstance == null) {
                  targetRelationshipInstance = new TargetRelationshipInstancesModel();
                }
                List<IRelationshipInstance> relationshipInstances = targetRelationshipInstance.getRelationshipInstances();
                if (relationshipInstances == null) {
                  relationshipInstances = new ArrayList<IRelationshipInstance>();
                }
                if (referencedSectionElementModel != null) {
                  relationshipInstances = getRelationshipInstance(propertyRecord, referencedSectionElementModel,
                      (BaseEntityDAO) baseEntityDAO, baseEntityId);
                }
                fillContextTags(responseModel, relationshipInstances);
                targetRelationshipInstance.setRelationshipInstances(relationshipInstances);
                targetRelationshipInstance.setTotalCount((long) relationshipInstances.size());
                recommendation.setTargetRelationshipInstances(targetRelationshipInstance);
                recommendation.setKlassInstanceIds(getSide2KlassInstanceIds(relationshipInstances));
                fillSide2BaseTypeVsSideI2InstanceIdMap(baseTypeVsKlassInstanceId, relationshipInstances);
                fillKlassInstances(responseModel, baseTypeVsKlassInstanceId);
              }
              break;
            }
          }
          if (isRecommendationFilled) {
            break;
          }
        }
      }
    }
    responseModel.setRecommendation(recommendationMap);
  }
  
  private void fillGoldenRecordRelationshipInstances(IGoldenRecordRuleKlassInstancesMergeViewRequestModel dataModel,
      IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel, IConfigDetailsForGetKlassInstancesToMergeModel configDetails,
      ILocaleCatalogDAO localCatalogDAO) throws Exception
  {
    String goldenRecordId = dataModel.getGoldenRecordId();
    Long goldenRecordIid = Long.parseLong(goldenRecordId);
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(goldenRecordIid);
    
    Collection<IPropertyDTO> allEntityProperties = localCatalogDAO.getAllEntityProperties(Long.parseLong(goldenRecordId));
    List<IPropertyDTO> relationDTOs = allEntityProperties.stream().filter(x -> x.getRelationSide().ordinal() > 0)
        .collect(Collectors.toList());

    IBaseEntityDTO loadPropertyRecords = baseEntityDAO.loadPropertyRecords(relationDTOs.toArray(new IPropertyDTO[relationDTOs.size()]));
    Set<IPropertyRecordDTO> propertyRecords = loadPropertyRecords.getPropertyRecords();
    Map<String, IPropertyRecordDTO> propertyRecordsMap = preparePropertyRecordsMapFromSet(propertyRecords);
    
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    Map<String, String> propertyCodeVsSideId = getPropertyCodeVsSideIdMap(referencedElements);
    Map<String, List<String>> baseTypeVsKlassInstanceId = new HashMap<>();
    
    Map<String, ITargetRelationshipInstancesModel> goldenRecordRelationshipInstances = responseModel.getGoldenRecordRelationshipInstances();
    if (goldenRecordRelationshipInstances == null) {
      goldenRecordRelationshipInstances = new HashMap<>();
    }
    
    Map<String, IReferencedRelationshipPropertiesModel> referencedRelationships = configDetails.getReferencedRelationshipProperties();
    for (String key : referencedRelationships.keySet()) {
      if (key.equals(SystemLevelIds.STANDARD_ARTICLE_GOLDEN_ARTICLE_RELATIONSHIP_ID)) {
        continue;
      }
      
      ITargetRelationshipInstancesModel targetRelationshipInstance = goldenRecordRelationshipInstances.get(propertyCodeVsSideId.get(key));
      if (targetRelationshipInstance == null) {
        targetRelationshipInstance = new TargetRelationshipInstancesModel();
      }
      List<IRelationshipInstance> relationshipInstances = targetRelationshipInstance.getRelationshipInstances();
      if (relationshipInstances == null) {
        relationshipInstances = new ArrayList<IRelationshipInstance>();
      }
      
      IPropertyRecordDTO propertyRecord = propertyRecordsMap.get(key);
      if(propertyRecord != null) {
        IReferencedSectionRelationshipModel referencedSectionElementModel = (IReferencedSectionRelationshipModel) referencedElements
            .get(propertyCodeVsSideId.get(key));
        if (referencedSectionElementModel != null) {
          relationshipInstances = getRelationshipInstance(propertyRecord, referencedSectionElementModel, (BaseEntityDAO) baseEntityDAO,
              goldenRecordId);
        }
        targetRelationshipInstance.setRelationshipInstances(relationshipInstances);
        targetRelationshipInstance.setTotalCount((long) relationshipInstances.size());
        goldenRecordRelationshipInstances.put(propertyCodeVsSideId.get(key), targetRelationshipInstance);
        fillSide2BaseTypeVsSideI2InstanceIdMap(baseTypeVsKlassInstanceId, relationshipInstances);
        fillContextTags(responseModel, relationshipInstances);
      }
    }
    responseModel.setGoldenRecordRelationshipInstances(goldenRecordRelationshipInstances);
    fillKlassInstances(responseModel, baseTypeVsKlassInstanceId);
  }
  
  private Map<String, IPropertyRecordDTO> preparePropertyRecordsMapFromSet(Set<IPropertyRecordDTO> propertyRecords)
  {
    Map<String, IPropertyRecordDTO> propertyRecordsMap = new HashMap<>();
    for (IPropertyRecordDTO propertyRecord : propertyRecords) {
      propertyRecordsMap.put(propertyRecord.getProperty().getCode(), propertyRecord);
    }
    return propertyRecordsMap;
  }
  
  private void fillSide2BaseTypeVsSideI2InstanceIdMap(Map<String, List<String>> baseTypeVsKlassInstanceId,
      List<IRelationshipInstance> relationshipInstances)
  {
    for (IRelationshipInstance relationshipInstance : relationshipInstances) {
      String side2BaseType = relationshipInstance.getSide2BaseType();
      String side2InstanceId = relationshipInstance.getSide2InstanceId();
      
      List<String> side2ContentIds = baseTypeVsKlassInstanceId.get(side2BaseType);
      if (side2ContentIds == null) {
        side2ContentIds = new ArrayList<>();
        baseTypeVsKlassInstanceId.put(side2BaseType, side2ContentIds);
      }
      side2ContentIds.add(side2InstanceId);
    }
  }
  
  private void fillKlassInstances(IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel,
      Map<String, List<String>> baseTypeVsKlassInstanceId) throws RDBMSException
  {
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
    baseTypeVsKlassInstanceId.forEach((baseType, side2ContentIds) -> {
      try {
        List<IBaseEntityDTO> basEntityDtos = localeCatalogDao.getBaseEntitiesByIIDs(side2ContentIds);
        Map<String, IContentInfoWithAssetDetailsModel> klassInstances = responseModel.getKlassInstances();
        Map<String, IAssetAttributeInstanceInformationModel> referencedAssets = (Map<String, IAssetAttributeInstanceInformationModel>) responseModel
            .getReferencedAssets();
        if (klassInstances == null) {
          klassInstances = new HashMap<>();
        }
        if (referencedAssets == null) {
          referencedAssets = new HashMap<>();
        }
        for (IBaseEntityDTO side2BaseEntityDTO : basEntityDtos) {
          if (klassInstances.containsKey(String.valueOf(side2BaseEntityDTO.getBaseEntityIID()))) {
            continue;
          }
          IContentInfoWithAssetDetailsModel klassModel = new ContentInfoWithAssetDetailsModel();
          klassModel.setId(String.valueOf(side2BaseEntityDTO.getBaseEntityIID()));
          klassModel.setBaseType(side2BaseEntityDTO.getBaseType().getPrefix());
          klassModel.setName(side2BaseEntityDTO.getBaseEntityName());
          klassInstances.put(klassModel.getId(), klassModel);
          if (BaseType.ASSET.equals(side2BaseEntityDTO.getBaseType())) {
            fillAssetInfo(side2BaseEntityDTO, klassModel);
          }
          else {
            String defaultAssetInstanceId = String.valueOf(side2BaseEntityDTO.getDefaultImageIID());
            if (defaultAssetInstanceId != null && !defaultAssetInstanceId.isEmpty() && !defaultAssetInstanceId.equals("0")) {
              if (referencedAssets.containsKey(defaultAssetInstanceId)) {
                continue;
              }
              klassModel.setDefaultAssetInstanceId(defaultAssetInstanceId);
              IBaseEntityDTO assetDTO = localeCatalogDao.getEntityByIID(Long.parseLong(defaultAssetInstanceId));
              fillReferencedAsset(referencedAssets, defaultAssetInstanceId, assetDTO);
            }
          }
        }
        responseModel.setKlassInstances(klassInstances);
        responseModel.setReferencedAssets(referencedAssets);
      }
      catch (RDBMSException e) {
        e.printStackTrace();
      }
    });
  }
  
  private void fillReferencedAsset(Map<String, IAssetAttributeInstanceInformationModel> referencedAssets, String defaultAssetInstanceId,
      IBaseEntityDTO assetDTO)
  {
    IAssetAttributeInstanceInformationModel referencedAsset = BaseEntityUtils.fillAssetInformationModel(assetDTO);
    if (referencedAsset != null) {
      referencedAssets.put(defaultAssetInstanceId, referencedAsset);
    }
  }
  
  private void fillAssetInfo(IBaseEntityDTO side2BaseEntityDTO, IContentInfoWithAssetDetailsModel klassModel)
  {
    IAssetAttributeInstanceInformationModel referencedAsset = BaseEntityUtils.fillAssetInformationModel(side2BaseEntityDTO);
    if (referencedAsset != null) {
      klassModel.setThumbKey(referencedAsset.getThumbKey());
      klassModel.setProperties(referencedAsset.getProperties());
      klassModel.setType(referencedAsset.getType());
    }
  }
  
  private Map<String, String> getPropertyCodeVsSideIdMap(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    Map<String, String> propertyCodeVsSideId = new HashMap<>();
    
    for (Entry<String, IReferencedSectionElementModel> entry : referencedElements.entrySet()) {
      IReferencedSectionElementModel value = entry.getValue();
      if (value.getType().equals(CommonConstants.RELATIONSHIP_PROPERTY)) {
        propertyCodeVsSideId.put(value.getPropertyId(), value.getId());
      }
    }
    return propertyCodeVsSideId;
  }
  
  protected List<IRelationshipInstance> getRelationshipInstance(IPropertyRecordDTO property,
      IReferencedSectionRelationshipModel referencedElement, BaseEntityDAO baseEntityDAO, String baseEntityIid)
      throws NumberFormatException, Exception
  {
    List<IRelationshipInstance> list = new ArrayList<>();
    IRelationsSetDTO relationsSetDTO = (IRelationsSetDTO) property;
    Set<IEntityRelationDTO> relations = relationsSetDTO.getRelations();
    for (IEntityRelationDTO relation : relations) {
      IRelationshipInstance relationshipInstance = new RelationshipInstance();
      relationshipInstance.setId(relationsSetDTO.getProperty().getPropertyCode());
      relationshipInstance.setRelationshipId(relationsSetDTO.getProperty().getPropertyCode());
      relationshipInstance.setSide1InstanceId(baseEntityIid);
      relationshipInstance.setSide2InstanceId(String.valueOf(relation.getOtherSideEntityIID()));
      relationshipInstance.setSide2BaseType(referencedElement.getRelationshipSide().getTargetType());
      relationshipInstance.setSideId(referencedElement.getId());
      IContextualDataDTO otherSideContextualDTO = relation.getOtherSideContextualObject();
      if (otherSideContextualDTO != null) {
        List<IContentTagInstance> tags = TagInstanceUtils.getTagInstance(relation, baseEntityDAO);
        relationshipInstance.setTags(tags);
      }
      list.add(relationshipInstance);
    }
    return list;
  }
  
  private List<String> getApplicableContentIds(List<String> klassIds, List<String> contentIds,
      Map<String, List<String>> contentIdVsklassInstanceTypesAndTaxonomys)
  {
    List<String> applicableContentIds = new ArrayList<>();
    for (String contentId : contentIds) {
      if (contentIdVsklassInstanceTypesAndTaxonomys.containsKey(contentId)) {
        List<String> typeAndTaxonomyIds = contentIdVsklassInstanceTypesAndTaxonomys.get(contentId);
        Optional<String> findFirst = typeAndTaxonomyIds.stream().filter(typeId -> klassIds.contains(typeId)).findFirst();
        if (findFirst.isPresent()) {
          applicableContentIds.add(contentId);
        }
      }
    }
    return applicableContentIds;
    
  }
  
  private void fillSupplierIdVsContentIdsMap(String supplierId, String contentId, Map<String, List<String>> supplierIdVsContentIds)
  {
    if(supplierId.equals(IStandardConfig.STANDARD_ORGANIZATION_RCODE))
      supplierId = IStandardConfig.STANDARD_ORGANIZATION_CODE;
    List<String> contentIds = supplierIdVsContentIds.get(supplierId);
    if (contentIds == null) {
      contentIds = new ArrayList<>();
    }
    contentIds.add(contentId);
    supplierIdVsContentIds.put(supplierId, contentIds);
  }
  
  private List<String> getSide2KlassInstanceIds(List<IRelationshipInstance> relationshipInstances)
  {
    List<String> klassInstanceIds = new ArrayList<String>();
    for (IRelationshipInstance relationshipInstance : relationshipInstances) {
      klassInstanceIds.add(relationshipInstance.getSide2InstanceId());
    }
    return klassInstanceIds;
  }
  
  private void fillContextTags(IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel,
      List<IRelationshipInstance> relationshipInstances)
  {
    List<String> contextTagsIds = responseModel.getContextTagIds();
    for (IRelationshipInstance relationshipInstance : relationshipInstances) {
      List<IContentTagInstance> tags = (List<IContentTagInstance>) relationshipInstance.getTags();
      if (tags != null) {
        for (IContentTagInstance tag : tags) {
          String tagId = tag.getTagId();
          if (!contextTagsIds.contains(tagId)) {
            contextTagsIds.add(tagId);
          }
        }
      }
    }
  }
}
