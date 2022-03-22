package com.cs.core.runtime.instancetree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.config.interactor.entity.relationship.RelationshipInstance;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.goldenrecordbucket.dao.GoldenRecordBucketDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.goldenrecord.bucket.IGetRelationshipDataFromSources;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.goldenrecord.ConfigDetailsForGetRelationshipDataModel;
import com.cs.core.runtime.interactor.model.goldenrecord.ContentInfoWithAssetDetailsModel;
import com.cs.core.runtime.interactor.model.goldenrecord.GetRelationshipDataFromSourcesResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IConfigDetailsForGetRelationshipDataModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IContentInfoWithAssetDetailsModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetRelationshipDataFromSourcesRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetRelationshipDataFromSourcesResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.ITargetRelationshipInstancesModel;
import com.cs.core.runtime.interactor.model.goldenrecord.TargetRelationshipInstancesModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForCustomTabStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.usecase.goldenrecord.IGetConfigDetailsForGetRelationshipDataFromSourcesStrategy;
import com.cs.core.util.ConfigUtil;
import com.cs.utils.BaseEntityUtils;

@Service
@SuppressWarnings("unchecked")
public class GetRelationshipDataFromSources
    extends AbstractRuntimeService<IGetRelationshipDataFromSourcesRequestModel, IGetRelationshipDataFromSourcesResponseModel>
    implements IGetRelationshipDataFromSources {
  
  @Autowired
  IGetConfigDetailsForGetRelationshipDataFromSourcesStrategy getConfigDetailsForGetRelationshipDataFromSourcesStrategy;
  
  @Autowired
  RDBMSComponentUtils                                        rdbmsComponentUtils;
  
  @Autowired
  ConfigUtil                                                 configUtil;
  
  @Autowired
  protected IGetConfigDetailsForCustomTabStrategy            getConfigDetailsForCustomTabStrategy;
  
  @Override
  protected IGetRelationshipDataFromSourcesResponseModel executeInternal(IGetRelationshipDataFromSourcesRequestModel model) throws Exception
  {
    IGetRelationshipDataFromSourcesResponseModel returnModel = getRelationshipData(model);
    List<String> contextTagIds = returnModel.getContextTagIds();
    returnModel.setConfigDetails(new ConfigDetailsForGetRelationshipDataModel());
    if (!contextTagIds.isEmpty()) {
      IIdsListParameterModel tagIdsList = new IdsListParameterModel();
      tagIdsList.setIds(contextTagIds);
      IConfigDetailsForGetRelationshipDataModel configDetails = getConfigDetailsForGetRelationshipDataFromSourcesStrategy
          .execute(tagIdsList);
      returnModel.setConfigDetails(configDetails);
    }
    return returnModel;
  }
  
  private IGetRelationshipDataFromSourcesResponseModel getRelationshipData(IGetRelationshipDataFromSourcesRequestModel dataModel)
      throws Exception
  {
    IGetRelationshipDataFromSourcesResponseModel returnModel = new GetRelationshipDataFromSourcesResponseModel();
    String relationshipId = dataModel.getRelationshipId();
    GoldenRecordBucketDAO goldenRecordBucketDAO = new GoldenRecordBucketDAO();
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
    IGoldenRecordDTO goldenRecordDTO = goldenRecordBucketDAO.getGoldenRecordRuleAndBaseEntityIIDs(
        dataModel.getBucketId(), dataModel.getGoldenRecordId());
    List<String> baseEntityIids = goldenRecordDTO.getLinkedBaseEntities().stream()
        .map(baseEntityIID -> String.valueOf(baseEntityIID))
        .collect(Collectors.toList());
    
    if (baseEntityIids.isEmpty()) {
      return returnModel;
    }
    returnModel.setSourceIds(baseEntityIids);
    
    Map<String, ITargetRelationshipInstancesModel> sourceIdRelationshipInstancesMap = returnModel.getSourceIdRelationshipInstancesMap();
    if (sourceIdRelationshipInstancesMap == null) {
      sourceIdRelationshipInstancesMap = new HashMap<>();
    }
    List<String> contextTagIds = returnModel.getContextTagIds();
    if (contextTagIds == null) {
      contextTagIds = new ArrayList<>();
    }
    Set<Long> side2InstanceIds = new HashSet<>();
    
    for (String baseEntityId : baseEntityIids) {
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(baseEntityId));
      IMulticlassificationRequestModel multiclassificationModel = configUtil
          .getConfigRequestModelForCreateInstanceForSingleClone(baseEntityDAO);
      IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsForCustomTabStrategy.execute(multiclassificationModel);
      Map<String, String> propertyCodeVsSideId = getPropertyCodeVsSideIdMap(configDetails.getReferencedRelationshipProperties());
      ITargetRelationshipInstancesModel targetRelationshipInstance = new TargetRelationshipInstancesModel();
      List<IRelationshipInstance> relationshipInstances = new ArrayList<IRelationshipInstance>();
      
      Collection<IPropertyDTO> allEntityProperties = localeCatalogDao.getAllEntityProperties(Long.parseLong(baseEntityId));
      List<IPropertyDTO> relationDTOs = allEntityProperties.stream().filter(x -> x.getRelationSide().ordinal() > 0)
          .collect(Collectors.toList());
      IBaseEntityDTO loadPropertyRecords = baseEntityDAO.loadPropertyRecords(relationDTOs.toArray(new IPropertyDTO[relationDTOs.size()]));
      Set<IPropertyRecordDTO> propertyRecords = loadPropertyRecords.getPropertyRecords();
      
      for (IPropertyRecordDTO propertyRecord : propertyRecords) {
        if (propertyRecord.getProperty().getPropertyCode().equals(relationshipId)) {
          if (propertyCodeVsSideId.get(relationshipId) != null) {
            relationshipInstances = getRelationshipInstance(propertyRecord, propertyCodeVsSideId.get(relationshipId), side2InstanceIds);
            fillContextTagIds(contextTagIds, relationshipInstances);
            targetRelationshipInstance.setRelationshipInstances(relationshipInstances);
            targetRelationshipInstance.setTotalCount((long) relationshipInstances.size());
            sourceIdRelationshipInstancesMap.put(baseEntityId, targetRelationshipInstance);
          }
        }
      }
    }
    returnModel.setSourceIdRelationshipInstancesMap(sourceIdRelationshipInstancesMap);
    
    if (side2InstanceIds.isEmpty()) {
      return returnModel;
    }
    
    Map<String, IContentInfoWithAssetDetailsModel> klassInstances = returnModel.getKlassInstances();
    if(klassInstances == null) {
      klassInstances = new HashMap<>();
    }
    Map<String, IAssetAttributeInstanceInformationModel> referencedAssets = (Map<String, IAssetAttributeInstanceInformationModel>) returnModel.getReferencedAssets();
    if(referencedAssets == null) {
      referencedAssets = new HashMap<>();
    }
    for (Long side2InstanceId : side2InstanceIds) {
      IBaseEntityDTO side2BaseEntityDTO = localeCatalogDao.getEntityByIID(side2InstanceId);
      if (klassInstances.containsKey(String.valueOf(side2InstanceId))) {
        continue;
      }
      IContentInfoWithAssetDetailsModel klassModel = new ContentInfoWithAssetDetailsModel();
      klassModel.setId(String.valueOf(side2InstanceId));
      klassModel.setBaseType(side2BaseEntityDTO.getBaseType().getPrefix());
      klassModel.setName(side2BaseEntityDTO.getBaseEntityName());
      klassInstances.put(klassModel.getId(), klassModel);
      if (BaseType.ASSET.equals(side2BaseEntityDTO.getBaseType())) {
        fillAssetInfo(side2BaseEntityDTO, klassModel);
      }
      else {
        String defaultAssetInstanceId = String.valueOf(side2BaseEntityDTO.getDefaultImageIID());
        if (defaultAssetInstanceId != null && !defaultAssetInstanceId.isEmpty() && !defaultAssetInstanceId.equals(new String("0"))) {
          klassModel.setDefaultAssetInstanceId(defaultAssetInstanceId);
          if (referencedAssets.containsKey(defaultAssetInstanceId)) {
            continue;
          }
          IBaseEntityDTO assetDTO = localeCatalogDao.getEntityByIID(Long.parseLong(defaultAssetInstanceId));
          fillReferencedAsset(referencedAssets, defaultAssetInstanceId, assetDTO);
        }
      }
    }
    returnModel.setKlassInstances(klassInstances);
    returnModel.setReferencedAssets(referencedAssets);
    return returnModel;
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
  
  private Map<String, String> getPropertyCodeVsSideIdMap(Map<String, IReferencedRelationshipPropertiesModel> refRelationshipProperties)
  {
    Map<String, String> propertyCodeVsSideId = new HashMap<>();
    
    for (Entry<String, IReferencedRelationshipPropertiesModel> entry : refRelationshipProperties.entrySet()) {
      IReferencedRelationshipPropertiesModel value = entry.getValue();
      propertyCodeVsSideId.put(entry.getKey(), value.getSide1().getSideId());
    }
    return propertyCodeVsSideId;
  }
  

  protected List<IRelationshipInstance> getRelationshipInstance(IPropertyRecordDTO property,
      String sideId, Set<Long> side2InstanceIds) throws NumberFormatException, Exception
  {
    List<IRelationshipInstance> list = new ArrayList<>();
    IRelationsSetDTO relationsSetDTO = (IRelationsSetDTO) property;
    Set<IEntityRelationDTO> relations = relationsSetDTO.getRelations();
    for (IEntityRelationDTO relation : relations) {
      IRelationshipInstance relationshipInstance = new RelationshipInstance();
      relationshipInstance.setId(relationsSetDTO.getProperty().getPropertyCode());
      relationshipInstance.setRelationshipId(relationsSetDTO.getProperty().getPropertyCode());
      relationshipInstance.setSide2InstanceId(String.valueOf(relation.getOtherSideEntityIID()));
      relationshipInstance.setSideId(sideId);
      side2InstanceIds.add(relation.getOtherSideEntityIID());
      list.add(relationshipInstance);
    }
    return list;
  }
  
  private void fillContextTagIds(List<String> contextTagIds, List<IRelationshipInstance> relationshipInstances)
  {
    for (IRelationshipInstance relationshipInstance : relationshipInstances) {
      List<IContentTagInstance> tags = (List<IContentTagInstance>) relationshipInstance.getTags();
      if (tags == null) {
        return;
      }
      for (IContentTagInstance tag : tags) {
        String tagId = tag.getTagId();
        if (contextTagIds.contains(tagId)) {
          continue;
        }
        contextTagIds.add(tagId);
      }
    }
  }
}
