package com.cs.core.runtime.instancetree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.cs.core.runtime.interactor.model.instancetree.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.configdetails.GetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.elastic.das.ISearchDAO;
import com.cs.core.elastic.idto.ISearchResultDTO;
import com.cs.core.rdbms.collection.idao.ICollectionDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.ICollectionDTO;
import com.cs.core.rdbms.entity.idto.ICollectionDTO.CollectionType;
import com.cs.core.rdbms.entity.idto.ISearchDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder.RootFilter;
import com.cs.core.rdbms.entity.idto.ISortDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.constants.SystemLabels;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.GetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetPostConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.SessionContextCustomProxy;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.util.getall.SearchUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetConfigDetailsForGetNewInstanceTreeStrategy;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetPostConfigDetailsForNewInstanceTreeStrategy;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.BaseEntityUtils;

public abstract class AbstractBookmarkInstanceTree<P extends IGetInstanceTreeRequestModel, R extends IIdsListParameterModel>
    extends AbstractRuntimeService<P, R> {
  
  
  @Autowired
  protected SessionContextCustomProxy                       context;
  
  
  @Autowired
  protected IGetPostConfigDetailsForNewInstanceTreeStrategy getPostConfigDetailsForNewInstanceTreeStrategy;
  
  
  @Autowired
  protected IGetConfigDetailsForGetNewInstanceTreeStrategy  getConfigDetailsForGetNewInstanceTreeStrategy;
  @Autowired
  protected RDBMSComponentUtils                             rdbmsComponentUtils;
  
  @Autowired
  protected GetAllUtils                                     getAllUtils;

  @Autowired
  protected InstanceTreeUtils                               instanceTreeUtils;
  
  @Autowired
  protected SearchAssembler                                   searchAssembler;
  
  @Autowired
  protected SearchUtils                                       searchUtils;
  
  protected abstract IGetNewInstanceTreeResponseModel executeRuntimeStrategy(P model, IConfigDetailsForNewInstanceTreeModel configDetails) throws Exception;

  protected abstract IConfigDetailsForNewInstanceTreeModel executeConfigDetailsStrategy(
      IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel) throws Exception;
  
  protected abstract List<String> getModuleEntities(P model) throws Exception;
  
  @SuppressWarnings("unchecked")
  @Override
  protected R executeInternal(P model) throws Exception
  {
    List<String> moduleEntities = getModuleEntities(model);
    
    IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel = prepareConfigRequestModel(model, moduleEntities);
    
    IConfigDetailsForNewInstanceTreeModel configDetails = executeConfigDetailsStrategy(configRequestModel);
    
    prepareRuntimeRequestModel(model, configDetails);
    IGetNewInstanceTreeResponseModel returnModel = executeRuntimeStrategy(model, configDetails);
    instanceTreeUtils.addRuleViolationFilter(model.getIsFilterDataRequired(), returnModel.getFilterData());
    
    fillPostConfigDetails(configDetails, returnModel);
    
    return (R) returnModel;
  }

  protected void prepareRuntimeRequestModel(P model,
      IConfigDetailsForNewInstanceTreeModel configDetails)
  {
    model.setKlassIdsHavingRP(configDetails.getKlassIdsHavingRP());
    model.setTaxonomyIdsHavingRP(configDetails.getTaxonomyIdsHavingRP());
    model.setModuleEntities(configDetails.getAllowedEntities());
    model.setSearchableAttributeIds(configDetails.getSearchableAttributeIds());
    model.setTranslatableAttributeIds(configDetails.getTranslatableAttributeIds());
    model.setFilterData(configDetails.getFilterData());
    model.setMajorTaxonomyIds(configDetails.getMajorTaxonomyIds());
  }

  protected IGetConfigDetailsForGetNewInstanceTreeRequestModel prepareConfigRequestModel(P model, List<String> entities)
  {
    IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel = new GetConfigDetailsForGetNewInstanceTreeRequestModel();
    configRequestModel.setUserId(context.getUserId());
    configRequestModel.setAllowedEntities(entities);
    configRequestModel.setXRayAttributes(model.getXRayAttributes());
    configRequestModel.setXRayTags(model.getXRayTags());
    if(model.getIsFilterDataRequired()){
      fillFilterInfoInConfigRequestModel(model, configRequestModel);
    }
    for (ISortModel sort : model.getSortOptions()) {
      configRequestModel.getAttributeIds().add(sort.getSortField());
    }
    configRequestModel.setKpiId(model.getKpiId());
    return configRequestModel;
  }
  
  protected void fillPostConfigDetails(IConfigDetailsForNewInstanceTreeModel configDetails,
       IGetNewInstanceTreeResponseModel returnModel) throws Exception
  {
    Set<String> klassIds = getKlassIds(returnModel);
    
    List<String> appliedSortAttributeIds = new ArrayList<>();
    for(IAppliedSortModel sortModel:returnModel.getAppliedSortData()) {
      appliedSortAttributeIds.add(sortModel.getSortField());
    }
    IGetPostConfigDetailsRequestModel postConfigDetailsRequestModel = new GetPostConfigDetailsRequestModel();
    postConfigDetailsRequestModel.setKlassIds(new ArrayList<>(klassIds));
    postConfigDetailsRequestModel.setAttributeIds(appliedSortAttributeIds);
    postConfigDetailsRequestModel.setUserId(context.getUserId());
    IGetPostConfigDetailsForNewInstanceTreeModel postConfigDetails = getPostConfigDetailsForNewInstanceTreeStrategy.execute(postConfigDetailsRequestModel);
    returnModel.setReferencedKlasses(postConfigDetails.getReferencedKlasses());
    returnModel.setReferencedAttributes(configDetails.getReferencedAttributes());
    returnModel.setReferencedTags(configDetails.getReferencedTags());
    returnModel.setFunctionPermission(postConfigDetails.getFunctionPermission());
    
    for(IAppliedSortModel sortModel:returnModel.getAppliedSortData()) {
      IAttribute iAttribute = postConfigDetails.getReferencedAttributes().get(sortModel.getSortField());
      if(iAttribute != null) {
        sortModel.setLabel(iAttribute.getLabel());
      }
    }
  }
  
  protected Set<String> getKlassIds(IGetNewInstanceTreeResponseModel returnModel)
  {
    Set<String> klassIds = new HashSet<>();
    returnModel.getChildren()
        .forEach((instance) -> {
          klassIds.addAll(instance.getTypes());
        });
    return klassIds;
  }

  @SuppressWarnings("rawtypes")
  protected void fillFilterInfoInConfigRequestModel(P model,
      IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel)
  {
    List<String> attributeIds = new ArrayList<>(); 
    List<String> tagIds = new ArrayList<>(); 
    List<IPropertyInstanceFilterModel> attributes = model.getAttributes();
    for(IPropertyInstanceFilterModel attribute  : attributes) {
      attributeIds.add(attribute.getId());
    }
    List<IPropertyInstanceFilterModel> tags = model.getTags();
    for(IPropertyInstanceFilterModel tag  : tags) {
      tagIds.add(tag.getId());
    }

    configRequestModel.setAttributeIds(attributeIds);
    configRequestModel.setTagIds(tagIds);
    configRequestModel.setIsFilterDataRequired(model.getIsFilterDataRequired());
  }
  
  protected void prepareResponseModel(IGetInstanceTreeRequestModel dataModel,
      IGetNewInstanceTreeResponseModel responseModel, List<IBaseEntityDTO> baseEntityDTOs) throws Exception 
  {
    responseModel.setFrom(dataModel.getFrom());
    if(!dataModel.getIsFilterDataRequired()) {
      responseModel.setFilterData(new ArrayList<INewApplicableFilterModel>());
    }
    fillReferencedAssets(responseModel, baseEntityDTOs);
    addSortOptionsToResponseModel(dataModel, responseModel);
  }
  
  private void fillReferencedAssets(IGetNewInstanceTreeResponseModel responseModel,
      List<IBaseEntityDTO> baseEntityDTOs) throws Exception
  {
    Map<String, IAssetAttributeInstanceInformationModel> referencedAssets = new HashMap<>();
    for (IBaseEntityDTO baseEntityDTO : baseEntityDTOs) {
      if (!baseEntityDTO.getBaseType()
          .equals(BaseType.ASSET)
          || !baseEntityDTO.getBaseType()
              .equals(BaseType.ATTACHMENT)) {
        long defaultImageIID = baseEntityDTO.getDefaultImageIID();
        if (defaultImageIID != 0l) {
          IBaseEntityDTO baseEntityDTOByIID = rdbmsComponentUtils.getBaseEntityDTO(defaultImageIID);
          List<IAssetAttributeInstanceInformationModel> referencedAssetsList = BaseEntityUtils
              .fillAssetInfoModel(baseEntityDTOByIID);
          for (IAssetAttributeInstanceInformationModel referencedAsset : referencedAssetsList) {
            referencedAssets.put(referencedAsset.getAssetInstanceId(), referencedAsset);
          }
        }
      }
    }
    responseModel.setReferencedAssets(referencedAssets);
  }
  
  private void addSortOptionsToResponseModel(IGetInstanceTreeRequestModel dataModel,
      IGetNewInstanceTreeResponseModel responseModel)
  {
    List<IAppliedSortModel> sortOptions = dataModel.getSortOptions();
    if(sortOptions.isEmpty()) {
      IAppliedSortModel appliedSortModel = new AppliedSortModel();
      appliedSortModel.setCode(SystemLevelIds.LAST_MODIFIED_ATTRIBUTE);
      appliedSortModel.setIsNumeric(false);
      appliedSortModel.setLabel(SystemLabels.LAST_MODIFIED_ATTRIBUTE_LABEL);
      appliedSortModel.setSortField(SystemLevelIds.LAST_MODIFIED_ATTRIBUTE);
      appliedSortModel.setSortOrder(CommonConstants.SORTORDER_DESC);
      sortOptions.add(appliedSortModel);
    }
    responseModel.setAppliedSortData(sortOptions);
  }
  
  protected String generateSearchExpression(IGetInstanceTreeRequestModel dataModel)
  {
    return instanceTreeUtils.generateSearchExpression(dataModel);
  }
  
    protected void populateResponseList(List<IKlassInstanceInformationModel> responseList,
      List<IBaseEntityDTO> listOfDTO, IGetInstanceTreeRequestModel dataModel,
      IConfigDetailsForNewInstanceTreeModel configDetails) throws Exception
  {
    Set<IPropertyDTO> properties = getPropertyDTOs(configDetails);
    for (IBaseEntityDTO baseEntityDTO : listOfDTO) {
      IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModel(baseEntityDTO, rdbmsComponentUtils);
      fillAttributesAndTags(configDetails, baseEntityDTO, properties, klassInstanceInformationModel);
      responseList.add(klassInstanceInformationModel);
    }
  }

  protected void fillAttributesAndTags(IConfigDetailsForNewInstanceTreeModel configDetails,
      IBaseEntityDTO baseEntityDTO, Set<IPropertyDTO> properties,
      IKlassInstanceInformationModel klassInstanceInformationModel) throws Exception
  {
    if (properties != null && !properties.isEmpty()) {
      IBaseEntityDAO baseEntityDAO = this.rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO);
      IPropertyDTO[] propertiesArr = properties.toArray(new IPropertyDTO[properties.size()]);
      KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO,
          configDetails.getReferencedAttributes(), configDetails.getReferencedTags(), rdbmsComponentUtils, propertiesArr);
      
      klassInstanceBuilder.fillAttributeTagProperties(klassInstanceInformationModel);
    }
  }

  protected Set<IPropertyDTO> getPropertyDTOs(IConfigDetailsForNewInstanceTreeModel configDetails) throws Exception
  {
    Set<IPropertyDTO> properties = new HashSet<>();
    
    Set<IPropertyDTO> attributes = configDetails.getReferencedAttributes().values().stream()
        .map(referencedAttribute -> {
          try {
            return RDBMSUtils.newPropertyDTO(referencedAttribute);
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
        }).collect(Collectors.toSet());
    
    Set<IPropertyDTO> tags = configDetails.getReferencedTags().values().stream()
        .map(referencedTag -> {
          try {
            return RDBMSUtils.newPropertyDTO(referencedTag);
          }
          catch (Exception e) {
            throw new RuntimeException(e);
          }
        }).collect(Collectors.toSet());
    
    properties.addAll(attributes);
    properties.addAll(tags);
    return properties;
  }
  
  protected void fillVariantOfInfo(Map<Long, Long> variantIdParentIdMap,
      IKlassInstanceInformationModel klassInstanceInformationModel, Long baseEntityIID)
      throws Exception
  {
    if(variantIdParentIdMap.containsKey(baseEntityIID)) {
      Long parentId = variantIdParentIdMap.get(baseEntityIID);
      klassInstanceInformationModel.setVariantOf(parentId.toString());
      klassInstanceInformationModel.setVariantOfLabel(getName(parentId));
    }
  }
  
  private String getName(Long baseEntityIID) throws Exception
  {
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID);
      IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
      return baseEntityDTO.getBaseEntityName();
  }
  
  protected Map<Long, Long> getVariantIdParentIdMap(List<IBaseEntityDTO> listOfDTO,
      ILocaleCatalogDAO localeCatalogDAO, List<String> linkedVariantRelationshipCodes)
      throws RDBMSException
  {
    Map<Long, Long> variantIdParentIdMap = linkedVariantRelationshipCodes.isEmpty() ? new HashMap<>()
        :localeCatalogDAO.getLinkedVariantIds(listOfDTO, linkedVariantRelationshipCodes);
    return variantIdParentIdMap;
  }
  
  protected IIdsListParameterModel getBaseEntityIIdsAsPerSearchCriteria(IGetBookmarkRequestModel dataModel,
      IConfigDetailsForNewInstanceTreeModel configDetails, ILocaleCatalogDAO localeCatalogDao)
      throws Exception
  {
    List<ISortDTO> sortOptions = getAllUtils.getSortOptions(dataModel.getSortOptions(), configDetails.getReferencedAttributes());
    IIdsListParameterModel responseModel = new IdsListParameterModel();
    List<String> moduleIds = InstanceTreeUtils.getModuleIdByEntityId(configDetails.getAllowedEntities());
    
    ISearchDTOBuilder searchBuilder = localeCatalogDao.getSearchDTOBuilder(searchAssembler.getBaseTypeByModule(moduleIds), RootFilter.TRUE, dataModel.getIsArchivePortal());
    searchBuilder.addSort(sortOptions);

   searchUtils.fillSearchDTO(dataModel, configDetails.getKlassIdsHavingRP(), configDetails.getTaxonomyIdsHavingRP(),
       configDetails.getTranslatableAttributeIds(), searchBuilder, configDetails.getSearchableAttributeIds(), configDetails.getMajorTaxonomyIds());
    
    ISearchDTO searchDTO = searchBuilder.build();
    ISearchDAO searchDAO = localeCatalogDao.openSearchDAO(searchDTO);
    ISearchResultDTO search = searchDAO.search();
    responseModel.getIds().addAll(search.getBaseEntityIIDs());
    responseModel.setAdditionalProperty("totalCount", search.getTotalCount());
    return responseModel;
  }
    

  
  protected IGetBookmarkRequestModel getInstanceTreeRequestModelForBookmark(
      String bookmarkId) throws Exception, RDBMSException
  {
    ICollectionDAO collectionDAO = rdbmsComponentUtils.getCollectionDAO();
    ICollectionDTO dynamicCollectionDTO = collectionDAO.getCollection(Long.parseLong(bookmarkId), CollectionType.dynamicCollection);
    
    IJSONContent searchCriteria = dynamicCollectionDTO.getSearchCriteria();
    IGetBookmarkRequestModel getRequestModel = ObjectMapperUtil.readValue(searchCriteria.toString(), GetBookmarkRequestModel.class);
    return getRequestModel;
  }
  
  
}
