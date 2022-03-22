package com.cs.core.runtime.instancetree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.INumberAttribute;
import com.cs.core.config.interactor.entity.attribute.IUnitAttribute;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.GetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.SearchHitInfoModel;
import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.elastic.das.ISearchDAO;
import com.cs.core.elastic.idto.ISearchResultDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder.RootFilter;
import com.cs.core.rdbms.entity.idto.ISortDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.constants.SystemLabels;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetPostConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.instancetree.AppliedSortModel;
import com.cs.core.runtime.interactor.model.instancetree.IAppliedSortModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.INewApplicableFilterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.searchable.ISearchHitInfoModel;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.util.getall.SearchUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetConfigDetailsForGetNewInstanceTreeStrategy;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetPostConfigDetailsForNewInstanceTreeStrategy;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.BaseEntityUtils;

public abstract class AbstractNewInstanceTree<P extends IGetInstanceTreeRequestModel, R extends IGetNewInstanceTreeResponseModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected ModuleMappingUtil                               moduleMappingUtil;
  
  @Autowired
  protected ISessionContext                                 context;
  
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
  private SearchAssembler                                   searchAssembler;
  
  @Autowired
  private SearchUtils                                       searchUtils;
  
  protected abstract IGetConfigDetailsForGetNewInstanceTreeRequestModel getConfigDetailsRequestModel();
  
  protected abstract IGetNewInstanceTreeResponseModel executeRuntimeStrategy(P model,
      IConfigDetailsForNewInstanceTreeModel configDetails,
      Map<String, List<Map<String, Object>>> idVsHighlightsMap) throws Exception;
  
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
    
    Map<String, List<Map<String, Object>>> idVsHighlightsMap = new HashMap<>();

    IGetNewInstanceTreeResponseModel returnModel = executeRuntimeStrategy(model, configDetails, idVsHighlightsMap);

    instanceTreeUtils.addRuleViolationFilter(model.getIsFilterDataRequired(), returnModel.getFilterData());

    fillPostConfigDetails(configDetails, returnModel, idVsHighlightsMap);

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
    IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel = getConfigDetailsRequestModel();
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
       IGetNewInstanceTreeResponseModel returnModel, Map<String, List<Map<String, Object>>> idVsHighlightsMap) throws Exception
  {
    Set<String> klassIds = getKlassIds(returnModel);
    
    List<String> attributeIds = new ArrayList<>();
    List<String> tagIds = new ArrayList<>();
    for(IAppliedSortModel sortModel:returnModel.getAppliedSortData()) {
      attributeIds.add(sortModel.getSortField());
    }
    
      for(Map.Entry<String, List<Map<String, Object>>> entrySet : idVsHighlightsMap.entrySet())
      {
        for(Map<String, Object> highlightMap : entrySet.getValue())
        {
          String type = (String)highlightMap.get(ISearchHitInfoModel.TYPE);
          String id = (String) highlightMap.get(IConfigEntityInformationModel.ID);
          if(type.equals(Constants.ATTRIBUTE) && !attributeIds.contains(id))
          {
            attributeIds.add(id);
          }
          else if(type.equals(Constants.TAG) && !tagIds.contains(id))
          {
            tagIds.add(id);
          }
        }
      }
    
    IGetPostConfigDetailsRequestModel postConfigDetailsRequestModel = new GetPostConfigDetailsRequestModel();
    postConfigDetailsRequestModel.setKlassIds(new ArrayList<>(klassIds));
    postConfigDetailsRequestModel.setAttributeIds(attributeIds);
    postConfigDetailsRequestModel.setTagIds(tagIds);
    postConfigDetailsRequestModel.setUserId(context.getUserId());
    IGetPostConfigDetailsForNewInstanceTreeModel postConfigDetails = getPostConfigDetailsForNewInstanceTreeStrategy.execute(postConfigDetailsRequestModel);
    returnModel.setReferencedKlasses(postConfigDetails.getReferencedKlasses());
    returnModel.setReferencedAttributes(configDetails.getReferencedAttributes());
    returnModel.setReferencedTags(configDetails.getReferencedTags());
    returnModel.setFunctionPermission(postConfigDetails.getFunctionPermission());

    fillHitsInfoModel(returnModel, idVsHighlightsMap, postConfigDetails, configDetails);
    
    for(IAppliedSortModel sortModel:returnModel.getAppliedSortData()) {
      IAttribute iAttribute = postConfigDetails.getReferencedAttributes().get(sortModel.getSortField());
      if(iAttribute != null) {
        sortModel.setLabel(iAttribute.getLabel());
        sortModel.setIconKey(iAttribute.getIconKey());
        sortModel.setIcon(iAttribute.getIcon());
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  private void fillHitsInfoModel(IGetNewInstanceTreeResponseModel returnModel,
      Map<String, List<Map<String, Object>>> idVsHighlightsMap, IGetPostConfigDetailsForNewInstanceTreeModel postConfigDetails, IConfigDetailsForNewInstanceTreeModel configDetails)
  {
    if(idVsHighlightsMap.isEmpty() || idVsHighlightsMap == null)
    {
      return;
    }
    
    Map<String, IAttribute> postConfigReferencedAttributes = postConfigDetails.getReferencedAttributes();
    Map<String, ITag> postConfigReferencedTags = postConfigDetails.getReferencedTags();
    
    Map<String, IAttribute> configReferencedAttributes = configDetails.getReferencedAttributes();
    Map<String, ITag> configReferencedTags = configDetails.getReferencedTags();
    
    Map<String, IAttribute> referencedAttributes = new HashMap<>();
    Map<String, ITag> referencedTags = new HashMap<>();
    
    referencedAttributes.putAll(postConfigReferencedAttributes);
    referencedAttributes.putAll(configReferencedAttributes);
    
    referencedTags.putAll(postConfigReferencedTags);
    referencedTags.putAll(configReferencedTags);
    
    List<IKlassInstanceInformationModel> childrens = returnModel.getChildren();
    
    for(IKlassInstanceInformationModel child : childrens)
    {
      List<ISearchHitInfoModel> childHits = child.getHits();
      
      List<Map<String, Object>> highlightList = idVsHighlightsMap.get(child.getId());
      for(Map<String, Object> highlightMap : highlightList)
      {
        ISearchHitInfoModel hitInfoModel = new SearchHitInfoModel();
        String type = (String)highlightMap.get(ISearchHitInfoModel.TYPE);
        String id = (String) highlightMap.get(IConfigEntityInformationModel.ID);
        
        if(type.equals(Constants.ATTRIBUTE))
        {
          IAttribute attribute = referencedAttributes.get(id);
          hitInfoModel.setId(id);
          hitInfoModel.setCode((String) highlightMap.get(ISearchHitInfoModel.CODE));
          hitInfoModel.setLabel(attribute.getLabel());
          hitInfoModel.setType(attribute.getType());
          
          if(IUnitAttribute.class.isAssignableFrom(attribute.getClass()))
          {
            IUnitAttribute unitAttribute = (IUnitAttribute) attribute;
            hitInfoModel.setDefaultUnit(unitAttribute.getDefaultUnit());
            hitInfoModel.setPrecision(unitAttribute.getPrecision());
          }
          if(INumberAttribute.class.isAssignableFrom(attribute.getClass()))
          {
            INumberAttribute numberAttribute = (INumberAttribute) attribute;
            hitInfoModel.setPrecision(numberAttribute.getPrecision());
          }
          
          List<ISearchHitInfoModel> values = new ArrayList<>();
          ISearchHitInfoModel valueModel = new SearchHitInfoModel();
          
          String highlightValue = ((List<String>) highlightMap.get(ISearchHitInfoModel.VALUES)).get(0);
          
          valueModel.setLabel(highlightValue);
          values.add(valueModel);
          hitInfoModel.setValues(values);
          
          childHits.add(hitInfoModel);
        }
        
        else if(type.equals(Constants.TAG))
        {
          ITag tag = referencedTags.get(id);
          hitInfoModel.setId(id);
          hitInfoModel.setCode((String) highlightMap.get(ISearchHitInfoModel.CODE));
          hitInfoModel.setLabel(tag.getLabel());
          hitInfoModel.setType(tag.getTagType());
          
          List<ISearchHitInfoModel> values = new ArrayList<>();
          
          List<String> valueList = (List<String>) highlightMap.get(ISearchHitInfoModel.VALUES);
          
          for(String value : valueList)
          {
            for(ITreeEntity children : tag.getChildren())
            {
              if(children.getId().equals(value))
              {
                ISearchHitInfoModel valueModel = new SearchHitInfoModel();
                valueModel.setLabel(((ITag)children).getLabel());
                values.add(valueModel);
                break;
              }
            }
          }
          
          hitInfoModel.setValues(values);
          childHits.add(hitInfoModel);
        }
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
    // TODO: Have to add filter Data and referencedAssets
    if(!dataModel.getIsFilterDataRequired()) {
      responseModel.setFilterData(new ArrayList<INewApplicableFilterModel>());
    }
    fillReferencedAssets(responseModel, baseEntityDTOs);
    addSortOptionsToResponseModel(dataModel, responseModel);
  }
  
  private void fillReferencedAssets(IGetNewInstanceTreeResponseModel responseModel,
      List<IBaseEntityDTO> baseEntityDTOs) throws Exception
  {
    Set<Long> defaultImageIIDs = new HashSet<>();
    Map<String, IAssetAttributeInstanceInformationModel> referencedAssets = new HashMap<>();
    for (IBaseEntityDTO baseEntityDTO : baseEntityDTOs) {
      if (!baseEntityDTO.getBaseType().equals(BaseType.ASSET) || !baseEntityDTO.getBaseType().equals(BaseType.ATTACHMENT)) {
        long defaultImageIID = baseEntityDTO.getDefaultImageIID();
        if (defaultImageIID != 0l) {
          defaultImageIIDs.add(defaultImageIID);
        }
      }
    }

    Map<Long, IJSONContent> entityExtensionByIIDs = rdbmsComponentUtils.getLocaleCatlogDAO().getEntityExtensionByIIDs(defaultImageIIDs);
    for (Map.Entry<Long, IJSONContent> referencedAsset : entityExtensionByIIDs.entrySet()) {
      Long assetIID = referencedAsset.getKey();
      referencedAssets.put(String.valueOf(assetIID), BaseEntityUtils.fillAssetInformationModel(assetIID, referencedAsset.getValue(), null));
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
  
  /*  protected void populateResponseList(List<IKlassInstanceInformationModel> responseList,
      List<IBaseEntityDTO> listOfDTO, IGetInstanceTreeRequestModel dataModel,
      IConfigDetailsForNewInstanceTreeModel configDetails) throws Exception
  {
    Set<IPropertyDTO> properties = getPropertyDTOs(configDetails);
    for (IBaseEntityDTO baseEntityDTO : listOfDTO) {
      IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModel(baseEntityDTO, rdbmsComponentUtils);
      fillAttributesAndTags(configDetails, baseEntityDTO, properties, klassInstanceInformationModel);
      responseList.add(klassInstanceInformationModel);
    }
  }*/

  protected void fillAttributesAndTags(IConfigDetailsForNewInstanceTreeModel configDetails,
      IBaseEntityDTO baseEntityDTO, Set<IPropertyRecordDTO> propertyRecords,
      IKlassInstanceInformationModel klassInstanceInformationModel,Set<IPropertyDTO> propertyDTOs) throws Exception
  {
    if(propertyRecords == null) {
        propertyRecords = new HashSet<>();
      }
      
      for (IPropertyDTO iPropertyDTO : propertyDTOs) {
        if (iPropertyDTO.isTrackingProperty())
          propertyRecords.add( ((BaseEntityDTO)baseEntityDTO).getTrackingValueRecord(iPropertyDTO));
      }
      
      if(propertyRecords.isEmpty()) {
        return;
      }
      IBaseEntityDAO baseEntityDAO = this.rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO);
      KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO,
          configDetails.getReferencedAttributes(), configDetails.getReferencedTags(), rdbmsComponentUtils);
      baseEntityDTO.setPropertyRecords(propertyRecords.toArray( new IPropertyRecordDTO[]{}));
      klassInstanceBuilder.fillAttributeTagPropertiesForInstanceTree(klassInstanceInformationModel, propertyRecords);
  }

  protected Set<IPropertyDTO> getPropertyDTOs(IConfigDetailsForNewInstanceTreeModel configDetails, List<String> xrayAttributes) throws Exception
  {
    Set<IPropertyDTO> properties = new HashSet<>();
    
    Map<String,IAttribute> referencedAttributesCopy = new HashMap<>(configDetails.getReferencedAttributes());
    if(xrayAttributes.isEmpty()) {
      referencedAttributesCopy.remove(SystemLevelIds.NAME_ATTRIBUTE);
      referencedAttributesCopy.remove(SystemLevelIds.LAST_MODIFIED_ATTRIBUTE);
    }
    
    Set<IPropertyDTO> attributes = referencedAttributesCopy.values().stream()
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
  
  protected List<IBaseEntityDTO> getBaseEntityDTOsAsPerSearchCriteria(IGetInstanceTreeRequestModel dataModel,
      IConfigDetailsForNewInstanceTreeModel configDetails, ILocaleCatalogDAO localeCatalogDao, R responseModel,
      Map<String, List<Map<String, Object>>> idVsHighlightsMap)
      throws Exception
  {
    ISearchDTOBuilder searchBuilder = prepareSearchDTOAndSearch(dataModel, configDetails, localeCatalogDao);
    ISearchDTO searchDTO = searchBuilder.build();
    ISearchDAO searchDAO = openSearchDAO(localeCatalogDao, searchDTO);
    ISearchResultDTO search = searchDAO.search();
    idVsHighlightsMap.putAll(search.getHighlightsMap());
    
    List<IBaseEntityDTO> baseEntityDTOs = new ArrayList<>();
    if(dataModel.getIsArchivePortal()) {
      baseEntityDTOs = localeCatalogDao.getBaseEntitiesFromArchive(search.getBaseEntityIIDs());
    }
    else {
      baseEntityDTOs = localeCatalogDao.getBaseEntitiesByIIDs(search.getBaseEntityIIDs());
    }
    
    responseModel.setTotalContents(search.getTotalCount());
    if (dataModel.getIsFilterDataRequired()) {
      List<INewApplicableFilterModel> filterData = getAllUtils.getApplicableFilters(searchDTO,
          dataModel.getFilterData(), configDetails.getTranslatableAttributeIds());
      responseModel.setFilterData(filterData);
    }
    
    return baseEntityDTOs;
  }
  
  protected ISearchDTOBuilder prepareSearchDTOAndSearch(IGetInstanceTreeRequestModel dataModel,
      IConfigDetailsForNewInstanceTreeModel configDetails, ILocaleCatalogDAO localeCatalogDao)
      throws Exception
  {
    List<ISortDTO> sortOptions = getAllUtils.getSortOptions(dataModel.getSortOptions(), configDetails.getReferencedAttributes());
    
    List<String> moduleIds = InstanceTreeUtils.getModuleIdByEntityId(configDetails.getAllowedEntities());
    
    ISearchDTOBuilder searchBuilder = localeCatalogDao.getSearchDTOBuilder(searchAssembler.getBaseTypeByModule(moduleIds), RootFilter.TRUE, dataModel.getIsArchivePortal());
    searchBuilder.addSort(sortOptions);
    
    searchUtils.fillSearchDTO(dataModel, configDetails.getKlassIdsHavingRP(), configDetails.getTaxonomyIdsHavingRP(),
        configDetails.getTranslatableAttributeIds(), searchBuilder, configDetails.getSearchableAttributeIds(),
        configDetails.getMajorTaxonomyIds());
    
    if(!(dataModel.getSelectedTaxonomyIds().isEmpty() && dataModel.getSelectedTypes().isEmpty())) {
      searchBuilder.setIsFromChooseTaxonomy(true);
    }
    
    fillUsecaseSpecificFilters(dataModel,configDetails,searchBuilder);
    
    return searchBuilder;
  }

  protected ISearchDAO openSearchDAO(ILocaleCatalogDAO localeCatalogDao,
      ISearchDTO searchDTO) throws Exception
  {
    return localeCatalogDao.openSearchDAO(searchDTO);
  }

    
  protected void fillUsecaseSpecificFilters(IGetInstanceTreeRequestModel dataModel,
      IConfigDetailsForNewInstanceTreeModel configDetails, ISearchDTOBuilder searchBuilder) throws Exception
  {
    
  }
  
}
