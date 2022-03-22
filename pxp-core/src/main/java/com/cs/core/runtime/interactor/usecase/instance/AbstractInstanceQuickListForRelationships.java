package com.cs.core.runtime.interactor.usecase.instance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.config.standard.IConfigMap;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.IUnitAttribute;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.attributiontaxonomy.ITaxonomyInformationModel;
import com.cs.core.config.interactor.model.configdetails.GetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IGetDefaultKlassesModel;
import com.cs.core.config.interactor.model.configdetails.IGetFilterAndSortDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetPostConfigDetailsResponseModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.ICategoryTreeInformationModel;
import com.cs.core.config.interactor.model.klass.GetDefaultKlassesModel;
import com.cs.core.config.interactor.model.klass.GetFilterInfoModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.taxonomy.ApplicableFilterModel;
import com.cs.core.config.interactor.model.taxonomy.IApplicableFilterModel;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.config.strategy.usecase.attribute.IGetAttributesStrategy;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.IGetAttributionTaxonomiesForQuickListStrategy;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.IGetTaxonomiesStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetDefaultKlassesForModulesStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetFilterAndSortDataStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetKlassesTreeStrategy;
import com.cs.core.config.strategy.usecase.tag.IGetTagsByIdStrategy;
import com.cs.core.data.Text;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.RelationSide;
import com.cs.core.rdbms.entity.dto.SortDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.entity.idto.ISortDTO;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForQuickListModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipInstanceQuickListModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;
import com.cs.core.runtime.interactor.model.searchable.ISearchHitInfoModel;
import com.cs.core.runtime.interactor.model.searchable.InstanceSearchModel;
import com.cs.core.runtime.interactor.model.targetinstance.GetTargetKlassesModel;
import com.cs.core.runtime.interactor.model.targetinstance.IGetTargetKlassesModel;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeForQuicklistModel;
import com.cs.core.runtime.interactor.model.typeswitch.GetAllowedTypesForModulesModel;
import com.cs.core.runtime.interactor.model.typeswitch.IGetAllowedTypesForModulesModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RelationshipRecordBuilder;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetConfigDetailsForQuicklistStrategy;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetPostConfigDetailsStrategy;
import com.cs.core.runtime.strategy.utils.PermissionUtils;

public abstract class AbstractInstanceQuickListForRelationships<P extends IRelationshipInstanceQuickListModel, R extends IGetKlassInstanceTreeModel>
    extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  protected ISessionContext                               context;
  
  @Autowired
  protected IGetConfigDetailsForQuicklistStrategy         getConfigDetailsForQuicklistStrategy;
  
  @Autowired
  protected IGetKlassesTreeStrategy                       getKlassesTreeStrategy;
  
  @Autowired
  protected IGetFilterAndSortDataStrategy                 getFilterAndSortDataForKlassStrategy;
  
  @Autowired
  protected IGetTaxonomiesStrategy                        getAllTypeTaxonomiesStrategy;
  
  @Autowired
  protected IGetDefaultKlassesForModulesStrategy          getDefaultKlassesForModulesStrategy;
  
  @Autowired
  protected IGetAttributesStrategy                        getAttributesByIdsStrategy;
  
  @Autowired
  protected IGetTagsByIdStrategy                          getTagsByIdsStrategy;
  
  @Autowired
  protected IGetPostConfigDetailsStrategy                 postConfigDetailsForRelationshipsStrategy;
  
  @Autowired
  protected IGetAttributionTaxonomiesForQuickListStrategy getAttributionTaxonomiesForQuickListStrategy;
  
  @Autowired
  protected PermissionUtils                               permissionUtils;
  
  @Autowired
  RDBMSComponentUtils                                     rdbmsComponentUtils;
  
  @Autowired
  SearchAssembler                                         searchAssembler;
  
  @Autowired
  GetAllUtils                                             getAllUtils;
  
  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected R executeInternal(P dataModel) throws Exception
  {
    String loginUserId = context.getUserId();
    dataModel.setCurrentUserId(loginUserId);
    
    String parentTaxonomyId = dataModel.getParentTaxonomyId();
    parentTaxonomyId = parentTaxonomyId == null ? "" : parentTaxonomyId;
    
    IGetTargetKlassesModel allowedTypesModel = new GetTargetKlassesModel();
    allowedTypesModel.setKlassId(dataModel.getTypeKlassId());
    allowedTypesModel.setRelationshipId(dataModel.getRelationshipId());
    allowedTypesModel.setXRayAttributes(dataModel.getXRayAttributes());
    allowedTypesModel.setXRayTags(dataModel.getXRayTags());
    allowedTypesModel.setSideId(dataModel.getSideId());
    IConfigDetailsForQuickListModel configDetails = getConfigDetails(allowedTypesModel, dataModel);
    
    dataModel.setDimensionalTagIds(configDetails.getDisplayTagIds());
    dataModel.setRelevanceTagIds(configDetails.getRelevanceTagIds());
    dataModel.setKlassIdsHavingRP(configDetails.getKlassIdsHavingRP());
    dataModel.setEntities(configDetails.getEntities());
    dataModel.setTaxonomyIdsHavingRP(configDetails.getTaxonomyIdsHavingRP());
    
    List<String> targetKlassIds = new ArrayList<>();
    targetKlassIds.addAll(configDetails.getAllowedTypes());
    dataModel.setKlassType(configDetails.getKlassType());
    
    dataModel.setAllowedTypes(targetKlassIds);
    ICategoryTreeInformationModel categoryTreeInfoModel = null;
    
    if (!parentTaxonomyId.equals("")) {
    }
    else {
      IIdsListParameterModel idsModel = new IdsListParameterModel();
      List<String> list = new ArrayList<>();
      list.add(configDetails.getAllowedTypes()
          .get(0));
      idsModel.setIds(list);
      categoryTreeInfoModel = getKlassesTreeStrategy.execute(idsModel);
      
      List<String> typesToSet = new ArrayList<>();
      List<String> selectedTypes = dataModel.getSelectedTypes();
      List<String> availableTypes = categoryTreeInfoModel.getKlassesIds();
      for (String types : selectedTypes) {
        if (availableTypes.contains(types)) {
          typesToSet.add(types);
        }
      }
      dataModel.setSelectedTypes(typesToSet);
    }
    
    IGetFilterAndSortDataRequestModel sortAndFilterDataRequestModel = KlassInstanceUtils.prepareSortAndFilterDataRequestModel(dataModel);
    
    IGetFilterInfoModel filterInfoModel = null;
    if (configDetails.getAllowedTypes()
        .get(0) != null) {
      List<String> ids = new ArrayList<>();
      ids.add(configDetails.getAllowedTypes().get(0));
      if (dataModel.getSelectedTypes().size() > 0) {
        sortAndFilterDataRequestModel.setTypeIds(dataModel.getSelectedTypes());
      }
      else {
        sortAndFilterDataRequestModel.setTypeIds(ids);
      }
      IGetFilterInformationModel filterInformationModel = getFilterAndSortDataForKlassStrategy.execute(sortAndFilterDataRequestModel);
      filterInfoModel = getFilterInfoModel(filterInformationModel);
    }
    else if (!parentTaxonomyId.equals("")) {
      if (dataModel.getSelectedTaxonomyIds().size() == 0) {
        List<String> ids = new ArrayList<>();
        ids.add(parentTaxonomyId);
        sortAndFilterDataRequestModel.setTypeIds(ids);
      }
      else {
        sortAndFilterDataRequestModel.setTypeIds(dataModel.getSelectedTaxonomyIds());
      }
      /*IdsListParameterModel idsListParameterModel = new IdsListParameterModel();
      idsListParameterModel.getIds().addAll(dataModel.getEntities());*/
      sortAndFilterDataRequestModel.setTypeIds((List<String>) dataModel.getEntities());
      filterInfoModel = getTaxonomyFilterInfo(sortAndFilterDataRequestModel);
    }
    else {
      filterInfoModel = getFilterInfoModel(getFilterAndSortDataForKlassStrategy.execute(sortAndFilterDataRequestModel));
    }
    
    dataModel.setSearchableAttributes(filterInfoModel.getSearchableAttributes());
    List<IApplicableFilterModel> filterDataModel = filterInfoModel.getFilterData();
    List<String> tagIdsToSet = new ArrayList<>();
    for (IApplicableFilterModel tag : filterDataModel) {
      if (IStandardConfig.TagType.AllTagTypes.contains(tag.getType())) {
        tagIdsToSet.add(tag.getId());
      }
    }
    List<IPropertyInstanceFilterModel> filterTagsToSet = new ArrayList<>();
    List<IPropertyInstanceFilterModel> filterTags = (List<IPropertyInstanceFilterModel>) dataModel.getTags();
    for (IPropertyInstanceFilterModel filterTag : filterTags) {
      if (tagIdsToSet.contains(filterTag.getId()) || filterTag.getAdvancedSearchFilter()) {
        filterTagsToSet.add(filterTag);
      }
    }
    dataModel.setTags(filterTagsToSet);
    dataModel.setFilterInfo(filterInfoModel);

    List<IClassifierDTO> classifiers = this.getAllFClassifier(categoryTreeInfoModel);
    IGetKlassInstanceTreeModel responseModel = executeGetQuickListElements(dataModel, categoryTreeInfoModel, configDetails, classifiers, filterInfoModel);
    // klassInstances.setTaxonomies(getTaxonomyParentList(dataModel));
    updateHitsInfoLabels(responseModel);
    // TODO Fix Me
    if (parentTaxonomyId.equals("") && dataModel.getKlassType() != null && !dataModel.getKlassType()
        .equals(CommonConstants.TAG_TYPE)) {
      setAllowedTypesAndDefaultKlasses(responseModel, dataModel.getKlassType());
      retainDefaultTypesAccordingToSelectedTypes(dataModel, responseModel, configDetails.getAllowedTypes());
      permissionUtils.retainDefaultTypesAccordingToPermission(responseModel, configDetails.getKlassIdsHavingCP());
    }
    else {
      responseModel.setDefaultTypes(new GetDefaultKlassesModel());
    }
    
    getAndSetPostConfigData(responseModel, classifiers);
    responseModel.setXRayConfigDetails(configDetails.getXRayConfigDetails());
    
    return (R) responseModel;
  }
  
  protected IConfigDetailsForQuickListModel getConfigDetails(IGetTargetKlassesModel allowedTypesModel, P dataModel) throws Exception
  {
    return getConfigDetailsForQuicklistStrategy.execute(allowedTypesModel);
  }
  
  /**
   * @author Krish This method adds referenced klass data for all instances in
   *         'children'
   */
  protected void getAndSetPostConfigData(IGetKlassInstanceTreeModel model, List<IClassifierDTO> classifiers) throws Exception
  {
    Set<String> klassIdsSet = new HashSet<>();
    
    classifiers.forEach(targetClassifier -> {
      klassIdsSet.add(targetClassifier.getClassifierCode());
    });
    
    if (!klassIdsSet.isEmpty()) {
      List<String> klassIdsList = new ArrayList<>(klassIdsSet);
      IGetPostConfigDetailsRequestModel requestModel = new GetPostConfigDetailsRequestModel();
      requestModel.setKlassIds(klassIdsList);
      IGetPostConfigDetailsResponseModel responseModel = postConfigDetailsForRelationshipsStrategy.execute(requestModel);
      
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses = model.getReferencedKlasses();
      referencedKlasses.putAll(responseModel.getReferencedKlasses());
    }
  }
  
  protected IGetFilterInfoModel getTaxonomyFilterInfo(IGetFilterAndSortDataRequestModel idsModel) throws Exception
  {
    IGetFilterInformationModel filterInformationModel = getFilterAndSortDataForKlassStrategy.execute(idsModel);
    return getFilterInfoModel(filterInformationModel);
  }
  
  @SuppressWarnings("unchecked")
  protected IGetFilterInfoModel getFilterInfoModel(IGetFilterInformationModel filterInformationModel)
  {
    IGetFilterInfoModel filterInfoModel = new GetFilterInfoModel();
    filterInfoModel.setDefaultFilterTags(filterInformationModel.getDefaultFilterTags());
    List<IConfigEntityTreeInformationModel> tags = filterInformationModel.getFilterData()
        .getTags();
    List<IConfigEntityTreeInformationModel> attributes = filterInformationModel.getFilterData()
        .getAttributes();
    List<IConfigEntityTreeInformationModel> filterInfos = new ArrayList<>();
    filterInfos.addAll(tags);
    filterInfos.addAll(attributes);
    List<IApplicableFilterModel> applicableFilters = new ArrayList<>();
    for (IConfigEntityTreeInformationModel filterInfo : filterInfos) {
      IApplicableFilterModel filterModel = new ApplicableFilterModel();
      filterModel.setId(filterInfo.getId());
      filterModel.setLabel(filterInfo.getLabel());
      filterModel.setType(filterInfo.getType());
      filterModel.setDefaultUnit(filterInfo.getDefaultUnit());
      filterModel.setPrecision(filterInfo.getPrecision());
      filterModel.setCode(filterInfo.getCode());
      List<IApplicableFilterModel> childrensToSet = new ArrayList<>();
      List<IConfigEntityTreeInformationModel> childrens = (List<IConfigEntityTreeInformationModel>) filterInfo.getChildren();
      for (IConfigEntityTreeInformationModel children : childrens) {
        IApplicableFilterModel childrenFilterModel = new ApplicableFilterModel();
        childrenFilterModel.setId(children.getId());
        childrenFilterModel.setLabel(children.getLabel());
        childrenFilterModel.setType(children.getType());
        childrenFilterModel.setCode(children.getCode());
        childrensToSet.add(childrenFilterModel);
      }
      filterModel.setChildren(childrensToSet);
      applicableFilters.add(filterModel);
    }
    filterInfoModel.setFilterData(applicableFilters);
    filterInfoModel.setSortData(filterInformationModel.getSortData());
    filterInfoModel.setSearchableAttributes(filterInformationModel.getSearchableAttributes());
    filterInfoModel.setConfigDetails(filterInformationModel.getConfigDetails());
    filterInfoModel.setTranslatableAttributesIds(filterInformationModel.getTranslatableAttributesIds());
    return filterInfoModel;
  }
  
  protected ITaxonomyInformationModel getTaxonomyContentCountForRelationshipQuicklist(IGetTaxonomyTreeForQuicklistModel dataModelForQuickList,
      P requestDatamodel) throws Exception
  {
    // return
    // getTaxonomyContentCountForRelationshipQuicklistStrategy.execute(dataModelForQuickList);
    return null;
  }
  
  protected void setAllowedTypesAndDefaultKlasses(IGetKlassInstanceTreeModel returnModel, String klassType) throws Exception
  {
    IListModel<IGetAllowedTypesForModulesModel> listModel = new ListModel<>();
    List<IGetAllowedTypesForModulesModel> list = new ArrayList<>();
    IGetAllowedTypesForModulesModel allowedTypeModel = new GetAllowedTypesForModulesModel();
    allowedTypeModel.getStandardKlassIds()
        .add(KlassInstanceUtils.getStandardKlassIds(klassType));
    list.add(allowedTypeModel);
    listModel.setList(list);
    IGetDefaultKlassesModel defaultKlassesModel = getDefaultKlassesForModulesStrategy.execute(listModel);
    returnModel.setDefaultTypes(defaultKlassesModel);
  }
  
  private void retainDefaultTypesAccordingToSelectedTypes(P getKlassInstanceTreeStrategyModel, IGetKlassInstanceTreeModel returnModel,
      List<String> allowedTypes)
  {
    List<String> selectedTypes = new ArrayList<>();
    if (getKlassInstanceTreeStrategyModel.getSelectedTypes()
        .size() > 0) {
      selectedTypes = getKlassInstanceTreeStrategyModel.getSelectedTypes();
    }
    else {
      selectedTypes.addAll(allowedTypes);
    }
    IGetDefaultKlassesModel defaultKlassesModel = returnModel.getDefaultTypes();
    
    List<IKlassInformationModel> childrenListToRetain = new ArrayList<>();
    List<IKlassInformationModel> childrenList = defaultKlassesModel.getChildren();
    for (IKlassInformationModel child : childrenList) {
      if (selectedTypes.contains(child.getId())) {
        childrenListToRetain.add(child);
      }
    }
    defaultKlassesModel.setChildren(childrenListToRetain);
  }
  
  protected ICategoryTreeInformationModel getKlassTree(IModule module) throws Exception
  {
    IIdsListParameterModel listModel = new IdsListParameterModel();
    List<String> ids = KlassInstanceUtils.getStandardKlassIds(module.getEntities());
    listModel.setIds(ids);
    return getKlassesTreeStrategy.execute(listModel);
  }
  
  /**
   * Copied from AbstractGetInstanceTree
   *
   * @param returnModel
   * @throws Exception
   */
  private void updateHitsInfoLabels(IGetKlassInstanceTreeModel returnModel) throws Exception
  {
    List<String> attributeHitList = new ArrayList<>();
    List<String> tagHitList = new ArrayList<>();
    for (IKlassInstanceInformationModel child : returnModel.getChildren()) {
      for (ISearchHitInfoModel hitInfoModel : child.getHits()) {
        if (hitInfoModel.getType()
            .equals("attribute")) {
          attributeHitList.add(hitInfoModel.getId());
        }
        
        // TODO :: not working for tags rightnow..
        if (hitInfoModel.getType()
            .equals("tag")) {
          tagHitList.add(hitInfoModel.getId());
        }
      }
    }
    
    IIdsListParameterModel idsListModel = new IdsListParameterModel();
    idsListModel.setIds(attributeHitList);
    IListModel<IAttribute> attributes = getAttributesByIdsStrategy.execute(idsListModel);
    idsListModel.setIds(tagHitList);
    IListModel<ITag> tags = getTagsByIdsStrategy.execute(idsListModel);
    
    for (IKlassInstanceInformationModel child : returnModel.getChildren()) {
      for (ISearchHitInfoModel hitInfoModel : child.getHits()) {
        
        if (hitInfoModel.getType()
            .equals("attribute")) {
          for (IAttribute attribute : attributes.getList()) {
            if (attribute.getId()
                .equals(hitInfoModel.getId())) {
              String type = attribute.getType();
              hitInfoModel.setLabel(attribute.getLabel());
              hitInfoModel.setType(type);
              if (IUnitAttribute.class.isAssignableFrom(attribute.getClass())) {
                IUnitAttribute unitAttribute = (IUnitAttribute) attribute;
                hitInfoModel.setDefaultUnit(unitAttribute.getDefaultUnit());
              }
            }
          }
        }
        
        // TODO :: not working for tags rightnow..
        if (hitInfoModel.getType()
            .equals("tag")) {
          for (ITag tag : tags.getList()) {
            if (tag.getId()
                .equals(hitInfoModel.getId())) {
              hitInfoModel.setLabel(tag.getLabel());
              hitInfoModel.setType(tag.getTagType());
              for (ISearchHitInfoModel hitInfoChildModel : hitInfoModel.getValues()) {
                for (ITreeEntity childTag : tag.getChildren()) {
                  if (childTag.getId()
                      .equals(hitInfoChildModel.getId())) {
                    hitInfoChildModel.setLabel(((ITag) childTag).getLabel());
                  }
                }
              }
            }
          }
        }
      }
    }
  }
  
  protected IGetKlassInstanceTreeModel executeGetQuickListElements(IRelationshipInstanceQuickListModel klassInstanceQuickListModel,
      ICategoryTreeInformationModel categoryTreeInfoModel, IConfigDetailsForQuickListModel configDetails, List<IClassifierDTO> classifiers,
      IGetFilterInfoModel filterInfoModel) throws Exception
  {
    IGetKlassInstanceTreeModel response = new GetKlassInstanceTreeModel();
    
    IConfigEntityTreeInformationModel categoryInformationModel = categoryTreeInfoModel.getCategoryInfo().get(0);
    BaseType baseType = IConfigMap.getBaseType(categoryInformationModel.getType());
    
    String searchExpression = generateSearchExpression(klassInstanceQuickListModel, classifiers, baseType, configDetails.getRelationshipConfig(), filterInfoModel.getSearchableAttributes());
    List<ISortModel> sortOptionsModel = klassInstanceQuickListModel.getSortOptions();
    sortOptionsModel.removeIf(x -> x.getSortField().equals("relevance"));
    List<ISortDTO> sortOptions = sortOptionsModel.stream().map(x -> new SortDTO(x.getSortField(),
        ISortDTO.SortOrder.valueOf(x.getSortOrder().toLowerCase()),x.getIsNumeric())).collect(Collectors.toList());

    IRDBMSOrderedCursor<IBaseEntityDTO> cursor = getAllUtils.getCursor( rdbmsComponentUtils.getLocaleCatlogDAO(), sortOptions, searchExpression);

    response.setTotalContents(cursor.getCount());
    getAllUtils.fillCountsForFilter(filterInfoModel, searchExpression,true);
    getAllUtils.fillCountsForDataQualityRules(filterInfoModel, searchExpression, true);
    response.setFilterInfo(filterInfoModel);
    
    List<IBaseEntityDTO> entities = cursor.getNext(klassInstanceQuickListModel.getFrom(), klassInstanceQuickListModel.getSize());
    List<Long> relatedEntities = getRelatedEntities(klassInstanceQuickListModel, rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(klassInstanceQuickListModel.getId())),
     configDetails.getRelationshipConfig());
    response.setChildren(populateResponseList( entities, relatedEntities));
    response.setTotalContents(cursor.getCount());
    return response;
  }
  
  private List<IClassifierDTO> getAllFClassifier(ICategoryTreeInformationModel categoryTreeInfoModel) throws Exception
  {
    if (categoryTreeInfoModel == null || categoryTreeInfoModel.getCategoryInfo() == null || categoryTreeInfoModel.getCategoryInfo()
        .size() != 1)
      throw new Exception("Error in target klass");
    
    IConfigEntityTreeInformationModel categoryInformationModel = categoryTreeInfoModel.getCategoryInfo().get(0);
    // Filter for classifier
    List<IClassifierDTO> classifiers = new ArrayList<>();
    List<? extends ITreeEntity> children = categoryInformationModel.getChildren();
    this.getFilterClassifier((List<IConfigEntityTreeInformationModel>) children, classifiers);
    IClassifierDTO classifierDTO = ConfigurationDAO.instance().getClassifierByCode(categoryInformationModel.getCode());
    classifiers.add(classifierDTO);
    return classifiers;
  }
  
  private void getFilterClassifier(List<IConfigEntityTreeInformationModel> treeEntity, List<IClassifierDTO> classifiers) throws Exception
  {
    for(IConfigEntityTreeInformationModel categoryInformationModel : treeEntity) {
        this.getFilterClassifier((List<IConfigEntityTreeInformationModel>) categoryInformationModel.getChildren(), classifiers);
        IClassifierDTO classifierDTO = ConfigurationDAO.instance().getClassifierByCode(categoryInformationModel.getId());
        classifiers.add(classifierDTO);
    }
  }
  
  private List<Long> getRelatedEntities(IRelationshipInstanceQuickListModel dataModel, IBaseEntityDAO baseEntityDAO, IRelationship relationshipConfig)
      throws Exception
  {
    List<Long> relatedEntities = new ArrayList<>();
    
    IPropertyDTO relationPropertyDTO = baseEntityDAO.newPropertyDTO(relationshipConfig.getPropertyIID(), relationshipConfig.getCode(),
            relationshipConfig.getIsNature() ?PropertyType.NATURE_RELATIONSHIP: PropertyType.RELATIONSHIP);
    RelationSide relationshipSide = RelationshipRecordBuilder.getRelationshipSide(dataModel.getSideId(), relationshipConfig);
    relationPropertyDTO.setRelationSide(relationshipSide);
    
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.loadPropertyRecords(relationPropertyDTO);
    IRelationsSetDTO relationsSetDTO = (IRelationsSetDTO) baseEntityDTO.getPropertyRecord(relationPropertyDTO.getIID());
    if (relationsSetDTO != null) {
      Set<IEntityRelationDTO> relations = relationsSetDTO.getRelations();
      for (IEntityRelationDTO entityRelationDTO : relations) {
        relatedEntities.add(entityRelationDTO.getOtherSideEntityIID());
      }
    }
    return relatedEntities;
  }
  
  private List<IKlassInstanceInformationModel> populateResponseList(List<IBaseEntityDTO> listOfDTO, List<Long> relatedEntities)
      throws Exception
  {
    List<IKlassInstanceInformationModel> responseList =  new ArrayList<>();
    for (IBaseEntityDTO baseEntityDTO : listOfDTO) {
      if (relatedEntities.contains(baseEntityDTO.getBaseEntityIID())) {
        // Skip adding
        continue;
      }
      IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModel(baseEntityDTO, rdbmsComponentUtils);
      if (klassInstanceInformationModel != null) {
        responseList.add(klassInstanceInformationModel);
      }
    }
    return responseList;
  }
  
  public String generateSearchExpression(IRelationshipInstanceQuickListModel dataModel, List<IClassifierDTO> classifiers, BaseType baseType,
      IRelationship relationship, List<String> searchableAttributes) throws Exception
  {
    InstanceSearchModel dataModel2 = (InstanceSearchModel) dataModel;
    String allSearch = dataModel.getAllSearch();
    
    List<String> selectedTypes = new ArrayList<>();
    List<String> selectedTaxonomies = new ArrayList<>();
    for (IClassifierDTO classifier : classifiers) {
      if (classifier.getClassifierType()
          .equals(ClassifierType.CLASS)) {
        selectedTypes.add(classifier.getCode());
      }
      else {
        selectedTaxonomies.add(classifier.getCode());
      }
    }
    dataModel2.setSelectedTypes(selectedTypes);
    dataModel2.setSelectedTaxonomyIds(selectedTaxonomies);
    StringBuilder searchExpression = searchAssembler.getBaseQuery(baseType);
    StringBuilder generateEntityFilterExpression = searchAssembler
        .generateEntityFilterExpression(dataModel.getSelectedTypes(), dataModel.getSelectedTypes(), false, new ArrayList<>());
    String expressionForRelationship = getExpressionForRelationship(dataModel, relationship);
    if (!generateEntityFilterExpression.toString().isEmpty() && !expressionForRelationship.isEmpty()) {
      generateEntityFilterExpression.append(" and ");
      generateEntityFilterExpression.append(expressionForRelationship);
    }
    if (!generateEntityFilterExpression.toString().isEmpty()) {
      searchExpression.append(generateEntityFilterExpression);
    }
    List<? extends IPropertyInstanceFilterModel> attributes = dataModel.getAttributes();
    List<? extends IPropertyInstanceFilterModel> tags = dataModel.getTags();
    String evaluationExpression = searchAssembler.getEvaluationExpression(attributes, tags, allSearch);
    if(!allSearch.isEmpty()) {
      searchableAttributes.removeAll(Arrays.asList("createdonattribute","lastmodifiedattribute"));
      String join = Text.join(" or ", searchableAttributes, " %s contains " + Text.escapeStringWithQuotes(allSearch) );
      String searchExpresssion = String.format("(%s)", join);
      
      if(!join.isEmpty() && !evaluationExpression.isEmpty()) {
        evaluationExpression = evaluationExpression + " and "+ searchExpresssion;
      }
      else if(!join.isEmpty()) {
        evaluationExpression =  join;
      }
    }
    if (!evaluationExpression.isEmpty()) {
      searchExpression.append(" where ")
          .append(evaluationExpression);
    }
    return searchExpression.toString();
  }

  protected String getExpressionForRelationship(IRelationshipInstanceQuickListModel dataModel, IRelationship relationship) throws NumberFormatException, Exception
  {
    String id = dataModel.getId();
    IBaseEntityDTO baseEntity = rdbmsComponentUtils.getBaseEntityDTO(Long.parseLong(id));
    RelationSide relationshipSide = RelationshipRecordBuilder.getRelationshipSide(dataModel.getSideId(), relationship);
    Integer side = relationshipSide.equals(RelationSide.SIDE_1) ? 1 : 2;
    return String.format(" $entity belongsto [e>%s $iid=%d].[%s $side=%d].complement ", baseEntity.getBaseEntityID(), baseEntity.getBaseEntityIID(),
        dataModel.getRelationshipId(), side);
  }
}
