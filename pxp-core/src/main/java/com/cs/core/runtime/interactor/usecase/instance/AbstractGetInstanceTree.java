package com.cs.core.runtime.interactor.usecase.instance;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.module.IModule;
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
import com.cs.core.config.interactor.model.klass.IGetKlassModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.taxonomy.IApplicableFilterModel;
import com.cs.core.config.strategy.usecase.klass.IGetDefaultKlassesForModulesStrategy;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.configdetails.ConfigDetailsForInstanceTreeGetRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.searchable.ISearchHitInfoModel;
import com.cs.core.runtime.interactor.model.typeswitch.GetAllowedTypesForModulesModel;
import com.cs.core.runtime.interactor.model.typeswitch.IGetAllowedTypesForModulesModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.utils.FilterUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetPostConfigDetailsStrategy;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractGetInstanceTree<P extends IGetKlassInstanceTreeStrategyModel, R extends IGetKlassInstanceTreeModel>
    extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  protected ISessionContext                      context;
  
  @Autowired
  protected IGetDefaultKlassesForModulesStrategy getDefaultKlassesForModulesStrategy;
  
  @Autowired
  protected IGetPostConfigDetailsStrategy        postConfigDetailsForGetInstanceTreeStrategy;
  
  @Autowired
  protected ModuleMappingUtil                    moduleMappingUtil;
  
  @Autowired
  protected TransactionThreadData                transactionThread;
  
  @Autowired
  protected FilterUtils                          filterUtils;
  
  @Autowired
  protected PermissionUtils                      permissionUtils;
  
  protected abstract IGetKlassInstanceTreeModel executeGetKlassInstanceTree(
      P getKlassInstanceTreeStrategyModel) throws Exception;
  
  protected abstract IGetKlassModel getTypeKlass(String type) throws Exception;
  
  protected abstract IIdParameterModel getParentTypeKlassId(String parentId) throws Exception;
  
  protected abstract IGetFilterInfoModel getFilterInfo(IGetFilterAndSortDataRequestModel idsModel)
      throws Exception;
  
  protected abstract List<IConfigEntityTreeInformationModel> getTaxonomyTree(
      IIdParameterModel idsModel) throws Exception;
  
  protected abstract ICategoryTreeInformationModel getKlassTree(IModule module) throws Exception;
  
  protected abstract List<IConfigEntityInformationModel> getDynamicRelationshipHierarchy()
      throws Exception;
  
  protected abstract IConfigDetailsForInstanceTreeGetModel getConfigDetails(
      IConfigDetailsForInstanceTreeGetRequestModel model) throws Exception;
  
  // TODO - Cosmetic : Change the method name to getEntityCategory()
  /**
   * This method returns the category of entity inside module (i.e. article /
   * set / collection / asset / assetcollection / market... and so on)
   *
   * @return category
   */
  protected abstract List<ITaxonomyInformationModel> getTaxonomyParentList(P requestModel,
      Set<String> allowedEntities, Set<String> taxonomyIdsHavingRP, Set<String> klassIdsHavingRP)
      throws Exception;
  
  protected abstract IGetFilterInfoModel getTaxonomyFilterInfo(
      IGetFilterAndSortDataRequestModel idsModel) throws Exception;
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  protected R executeInternal(P getKlassInstanceTreeStrategyModel) throws Exception
  {
    String loginUserId = context.getUserId();
    String moduleId = getKlassInstanceTreeStrategyModel.getModuleId();
    IModule module = getModule(moduleId);
    List<String> moduleEntities = module.getEntities();
    List<String> xRayAttributes = getKlassInstanceTreeStrategyModel.getXRayAttributes();
    List<String> xRayTags = getKlassInstanceTreeStrategyModel.getXRayTags();
    IConfigDetailsForInstanceTreeGetRequestModel model = new ConfigDetailsForInstanceTreeGetRequestModel();
    model.setUserId(loginUserId);
    model.setAllowedEntities(moduleEntities);
    model.setIsCalendarView(getKlassInstanceTreeStrategyModel.getIsCalendarView());
    model.setXRayAttributes(xRayAttributes);
    model.setXRayTags(xRayTags);
    model.setKpiId(getKlassInstanceTreeStrategyModel.getKpiId());
    
    IConfigDetailsForInstanceTreeGetModel configDetails = getConfigDetails(model);
    
    module.getEntities().retainAll(configDetails.getAllowedEntities());
    
    getKlassInstanceTreeStrategyModel.setCurrentUserId(loginUserId);
    String parentTaxonomyId = getKlassInstanceTreeStrategyModel.getParentTaxonomyId();
    parentTaxonomyId = parentTaxonomyId == null ? "" : parentTaxonomyId;
    
    if (!parentTaxonomyId.equals("")) {
    }
    else {
      ICategoryTreeInformationModel categoryTreeInfoModel = getKlassTree(module);
      List<String> typesToSet = new ArrayList<>();
      List<String> selectedTypes = getKlassInstanceTreeStrategyModel.getSelectedTypes();
      List<String> availableTypes = categoryTreeInfoModel.getKlassesIds();
      for (String types : selectedTypes) {
        if (availableTypes.contains(types)) {
          typesToSet.add(types);
        }
      }
      getKlassInstanceTreeStrategyModel.setSelectedTypes(typesToSet);
    }
    
    IGetFilterAndSortDataRequestModel filterAndSortDataRequestModel = KlassInstanceUtils
        .prepareSortAndFilterDataRequestModel(getKlassInstanceTreeStrategyModel);
    
    IGetFilterInfoModel filterInfoModel = getFilterInfo(filterAndSortDataRequestModel);
    getKlassInstanceTreeStrategyModel
        .setSearchableAttributes(filterInfoModel.getSearchableAttributes());
    List<IApplicableFilterModel> filterDataModel = filterInfoModel.getFilterData();
    List<String> tagIdsToSet = new ArrayList<>();
    for (IApplicableFilterModel tag : filterDataModel) {
      if (IStandardConfig.TagType.AllTagTypes.contains(tag.getType())) {
        tagIdsToSet.add(tag.getId());
      }
    }
    
    String kpiId = getKlassInstanceTreeStrategyModel.getKpiId();
    
    List<IPropertyInstanceFilterModel> filterTagsToSet = new ArrayList<>();
    List<IPropertyInstanceFilterModel> filterTags = (List<IPropertyInstanceFilterModel>) getKlassInstanceTreeStrategyModel.getTags();
    for (IPropertyInstanceFilterModel filterTag : filterTags) {
      if (tagIdsToSet.contains(filterTag.getId()) || (kpiId != null && !kpiId.isEmpty()) 
          || filterTag.getAdvancedSearchFilter()) {
        filterTagsToSet.add(filterTag);
      }
    }
    getKlassInstanceTreeStrategyModel.setTags(filterTagsToSet);
    
    getKlassInstanceTreeStrategyModel.setFilterInfo(filterInfoModel);
    getKlassInstanceTreeStrategyModel.setDimensionalTagIds(configDetails.getDimensionalTagIds());
    Set<String> klassIdsHavingRP = configDetails.getKlassIdsHavingRP();
    getKlassInstanceTreeStrategyModel.setKlassIdsHavingRP(klassIdsHavingRP);
    getKlassInstanceTreeStrategyModel.setTaskIdsForRolesHavingReadPermission(
        configDetails.getTaskIdsForRolesHavingReadPermission());
    getKlassInstanceTreeStrategyModel
        .setTaskIdsHavingReadPermissions(configDetails.getTaskIdsHavingReadPermissions());
    getKlassInstanceTreeStrategyModel.setPersonalTaskIds(configDetails.getPersonalTaskIds());
    getKlassInstanceTreeStrategyModel
        .setTaxonomyIdsHavingRP(configDetails.getTaxonomyIdsHavingRP());
    // setContentRelationshipInfo(getKlassInstanceTreeStrategyModel);
    
    getKlassInstanceTreeStrategyModel.setModuleEntities(module.getEntities());
    
    // KPI
    List<String> selectedTaxonomyIds = getKlassInstanceTreeStrategyModel.getSelectedTaxonomyIds();
    List<String> selectedTypes = getKlassInstanceTreeStrategyModel.getSelectedTypes();
    
    if (kpiId != null && !kpiId.isEmpty()) {
      // means it is KPI usecase(click on KPI of dashboard)
      if (selectedTaxonomyIds.isEmpty()) {
        // means it is not drill down layer where selectedTaxonomyIds is came
        // from UI
        selectedTaxonomyIds.addAll(configDetails.getTaxonomyIdsForKPI());
      }
      if (selectedTypes.isEmpty()) {
        selectedTypes.addAll(configDetails.getKlassIdsForKPI());
      }
    }
    
    IGetKlassInstanceTreeModel returnModel = executeGetKlassInstanceTree(
        getKlassInstanceTreeStrategyModel);
    returnModel.setTaxonomies(
        getTaxonomyParentList(getKlassInstanceTreeStrategyModel, configDetails.getAllowedEntities(),
            configDetails.getTaxonomyIdsHavingRP(), klassIdsHavingRP));
    
    returnModel.setHierarchies(getDynamicRelationshipHierarchy());
    IKlassInstance klassInstance = returnModel.getKlassInstance();
    
    List<String> attributeHitList = new ArrayList<>();
    List<String> tagHitList = new ArrayList<>();
    Set<String> typeIdsSet = new HashSet<>();
    List<IKlassInstanceInformationModel> children = returnModel.getChildren();
    for (IKlassInstanceInformationModel child : children) {
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
      
      for (String type : child.getTypes()) {
        typeIdsSet.add(type);
      }
    }
    
    List<String> typeIdsList = new ArrayList<>(typeIdsSet);
    IGetPostConfigDetailsRequestModel requestModel = new GetPostConfigDetailsRequestModel();
    requestModel.setAttributeIds(attributeHitList);
    requestModel.setTagIds(tagHitList);
    requestModel.setKlassIds(typeIdsList);
    IGetPostConfigDetailsResponseModel responseModel = postConfigDetailsForGetInstanceTreeStrategy
        .execute(requestModel);
    List<IAttribute> attributes = responseModel.getAttributes();
    List<ITag> tags = responseModel.getTags();
    returnModel.setReferencedKlasses(responseModel.getReferencedKlasses());
    filterUtils.updateHitsInfoLabels(attributes, tags, children);
    
    // do not send default types in archival port
    Boolean isArchivePortal = getKlassInstanceTreeStrategyModel.getIsArchivePortal();
    if (isArchivePortal == null || !isArchivePortal) {
      setAllowedTypesAndDefaultKlasses(returnModel, klassInstance, module);
      retainDefaultTypesAccordingToSelectedTypes(getKlassInstanceTreeStrategyModel, returnModel);
      permissionUtils.retainDefaultTypesAccordingToPermission(returnModel,
          configDetails.getKlassIdsHavingCP());
    }
    
    IXRayConfigDetailsModel xRayConfigDetails = configDetails.getXRayConfigDetails();
    returnModel.setXRayConfigDetails(xRayConfigDetails);
    return (R) returnModel;
  }
  
  protected IModule getModule(Set<String> allowedEntities, String moduleId) throws Exception
  {
    
    List<IModule> modules = moduleMappingUtil.getRuntimeModule();
    
    for (IModule iModule : modules) {
      if (iModule.getId()
          .equals(moduleId)) {
        iModule.getEntities()
            .retainAll(allowedEntities);
        return iModule;
      }
    }
    
    return null;
  }
  
  // default types should be only those which are selected taxonomies..
  private void retainDefaultTypesAccordingToSelectedTypes(P getKlassInstanceTreeStrategyModel,
      IGetKlassInstanceTreeModel returnModel)
  {
    // TODO: Filter default klasses depending on the taxonomy
    if (getKlassInstanceTreeStrategyModel.getSelectedTypes().size() > 0) {
      List<String> selectedTypes = getKlassInstanceTreeStrategyModel.getSelectedTypes();
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
  }
  
  protected void setAllowedTypesAndDefaultKlasses(IGetKlassInstanceTreeModel returnModel,
      IKlassInstance klassInstance, IModule module) throws Exception
  {
    TransactionData transactionData = transactionThread.getTransactionData();
    String endpointType = transactionData.getEndpointType();
    if (module.getId()
        .equals(Constants.ALL_MODE)
        || (endpointType != null && endpointType.equals(CommonConstants.OFFBOARDING_ENDPOINT))) {
      returnModel.setDefaultTypes(new GetDefaultKlassesModel());
      return;
    }
    IListModel<IGetAllowedTypesForModulesModel> listModel = new ListModel<>();
    List<IGetAllowedTypesForModulesModel> list = new ArrayList<>();
    
    IGetAllowedTypesForModulesModel allowedTypeModel = new GetAllowedTypesForModulesModel();
    allowedTypeModel
        .setStandardKlassIds(KlassInstanceUtils.getStandardKlassIds(module.getEntities()));
    list.add(allowedTypeModel);
    listModel.setList(list);
    
    IGetDefaultKlassesModel defaultKlassesModel = getDefaultKlassesForModulesStrategy
        .execute(listModel);
    returnModel.setDefaultTypes(defaultKlassesModel);
  }
  
  public IModule getModule(String moduleId) throws Exception
  {
    
    List<IModule> modules = moduleMappingUtil.getRuntimeModule();
    
    for (IModule iModule : modules) {
      if (iModule.getId()
          .equals(moduleId)) {
        // iModule.getEntities().retainAll(allowedEntities);
        return iModule;
      }
    }
    
    return null;
  }
}
