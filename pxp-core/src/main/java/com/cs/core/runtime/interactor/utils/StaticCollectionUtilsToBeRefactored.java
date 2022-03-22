package com.cs.core.runtime.interactor.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.GetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetConfigDetailsForGetNewInstanceTreeStrategy;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;

@Component("staticCollectionUtils")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StaticCollectionUtilsToBeRefactored {
  
  @Autowired
  protected ISessionContext                                context;
  
  @Autowired
  protected IGetConfigDetailsForGetNewInstanceTreeStrategy getConfigDetailsForGetNewInstanceTreeStrategy;
  
  @Autowired
  protected ModuleMappingUtil                              moduleMappingUtil;
  
  /*@Autowired
  protected IGetFilterAndSortDataStrategy                  getFilterAndSortDataForKlassStrategy;
  
  @Autowired
  protected IGetKlassesTreeStrategy                        getKlassesTreeStrategy;
  
  @Autowired
  protected IGetTaxonomiesStrategy                         getTaxonomiesStrategy;
  
  @Autowired
  protected IGetTaxonomyContentCountStrategy            getTaxonomyContentCountStrategy;
  
  
  @Autowired
  protected IGetAllDynamicRelationshipHierarchyStrategy    getAllDynamicRelationshipHierarchyStrategy;
  
  @Autowired
  protected FilterUtils                                    filterUtils;*/
  
  public IConfigDetailsForGetNewInstanceTreeModel processFilterDataAndReturnConfigDetails(
      IGetKlassInstanceTreeStrategyModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    String moduleId = getKlassInstanceTreeStrategyModel.getModuleId();
    IModule module = getModule(moduleId);
    List<String> moduleEntities = module.getEntities();
    IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel = new GetConfigDetailsForGetNewInstanceTreeRequestModel();
    configRequestModel.setUserId(context.getUserId());
    configRequestModel.setAllowedEntities(moduleEntities);
    configRequestModel.setXRayAttributes(getKlassInstanceTreeStrategyModel.getXRayAttributes());
    configRequestModel.setXRayTags(getKlassInstanceTreeStrategyModel.getXRayTags());
    fillFilterInfoInConfigRequestModel(getKlassInstanceTreeStrategyModel, configRequestModel);
    IConfigDetailsForGetNewInstanceTreeModel configDetails = getConfigDetailsForGetNewInstanceTreeStrategy.execute(configRequestModel);
    
    /*   module.getEntities().retainAll(configDetails.getAllowedEntities());
    
    getKlassInstanceTreeStrategyModel.setCurrentUserId(context.getUserId());
    String parentTaxonomyId = getKlassInstanceTreeStrategyModel.getParentTaxonomyId();
    parentTaxonomyId = parentTaxonomyId == null ? "" : parentTaxonomyId;
    
    if (!parentTaxonomyId.equals("")) {
      // do nothing..
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
    getKlassInstanceTreeStrategyModel.setSearchableAttributes(filterInfoModel.getSearchableAttributes());
    List<IApplicableFilterModel> filterDataModel = filterInfoModel.getFilterData();
    List<String> tagIdsToSet = new ArrayList<>();
    for (IApplicableFilterModel tag : filterDataModel) {
      if (IStandardConfig.TagType.AllTagTypes.contains(tag.getType())) {
        tagIdsToSet.add(tag.getId());
      }
    }
    List<IPropertyInstanceFilterModel> filterTagsToSet = new ArrayList<>();
    List<IPropertyInstanceFilterModel> filterTags = (List<IPropertyInstanceFilterModel>) getKlassInstanceTreeStrategyModel.getTags();
    for (IPropertyInstanceFilterModel filterTag : filterTags) {
      if (tagIdsToSet.contains(filterTag.getId()) || filterTag.getAdvancedSearchFilter()) {
        filterTagsToSet.add(filterTag);
      }
    }
    getKlassInstanceTreeStrategyModel.setTags(filterTagsToSet);
    
    getKlassInstanceTreeStrategyModel.setFilterInfo(filterInfoModel);*/
    getKlassInstanceTreeStrategyModel.setModuleEntities(module.getEntities());
    return configDetails;
  }
  
  protected void fillFilterInfoInConfigRequestModel(IGetKlassInstanceTreeStrategyModel getKlassInstanceTreeStrategyModel,
      IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel)
  {
    List<String> attributeIds = new ArrayList<>();
    List<String> tagIds = new ArrayList<>();
    List<IPropertyInstanceFilterModel> attributes = (List<IPropertyInstanceFilterModel>) getKlassInstanceTreeStrategyModel.getAttributes();
    for (IPropertyInstanceFilterModel attribute : attributes) {
      attributeIds.add(attribute.getId());
    }
    List<IPropertyInstanceFilterModel> tags = (List<IPropertyInstanceFilterModel>) getKlassInstanceTreeStrategyModel.getTags();
    for (IPropertyInstanceFilterModel tag : tags) {
      tagIds.add(tag.getId());
    }
    configRequestModel.setAttributeIds(attributeIds);
    configRequestModel.setTagIds(tagIds);
    configRequestModel.setIsFilterDataRequired(true);
  }
  
  /* 
  
  protected IGetFilterInfoModel getFilterInfo(IGetFilterAndSortDataRequestModel idsModel) throws Exception
  {
    IGetFilterInformationModel filterInformationModel = getFilterAndSortDataForKlassStrategy.execute(idsModel);
    IGetFilterInfoModel filterInfoModel = new GetFilterInfoModel();
    filterInfoModel.setSortData(filterInformationModel.getSortData());
    return filterInfoModel;
  }
  
  protected ICategoryTreeInformationModel getKlassTree(IModule module) throws Exception
  {
    IIdsListParameterModel listModel = new IdsListParameterModel();
    List<String> ids = KlassInstanceUtils.getStandardKlassIds(module.getEntities());
    listModel.setIds(ids);
    return getKlassesTreeStrategy.execute(listModel);
  }
  
  public List<ICategoryInformationModel> getTaxonomyParentList(String moduleId,
      Set<String> allowedEntities, Set<String> taxonomyIdsHavingRP, Set<String> klassIdsHavingRP,
      String collectionId) throws Exception
  {
    IGetTaxonomyTreeModel getTaxonomyTreeModel = new GetTaxonomyTreeModel();
    getTaxonomyTreeModel.setCollectionId(collectionId);
    List<IConfigEntityTreeInformationModel> categoryInfo = new ArrayList<>();
    List<ICategoryInformationModel> taxonomyParentList = new ArrayList<>();
    IGetTaxonomyRequestModel getTaxonomyRequestModel = new GetTaxonomyRequestModel();
    getTaxonomyRequestModel.setId("-1");
    
    IGetAttributionTaxonomyModel taxonomyTree = null; 
        //getTaxonomiesStrategy.execute(getTaxonomyRequestModel);
        List<IMasterTaxonomy> rootLevelTaxonomies = (List<IMasterTaxonomy>) taxonomyTree.getChildren();
    
    rootLevelTaxonomies.forEach(rootLevelTaxonomy -> {
      ICategoryInformationModel categoryInformationModel = new CategoryInformationModel();
      categoryInformationModel.setId(rootLevelTaxonomy.getId());
      categoryInformationModel.setLabel(rootLevelTaxonomy.getLabel());
      taxonomyParentList.add(categoryInformationModel);
    });
    ICategoryInformationModel klassTaxonomy = new CategoryInformationModel();
    klassTaxonomy.setId("-1");
    klassTaxonomy.setChildren(taxonomyParentList);
    categoryInfo.add(klassTaxonomy);
    
    getTaxonomyTreeModel.setIsKlassTaxonomy(false);
    getTaxonomyTreeModel.setCategoryInfo(categoryInfo);
    getTaxonomyTreeModel.setModuleEntities(getModule(allowedEntities, moduleId).getEntities());
    getTaxonomyTreeModel.setCurrentUserId(context.getUserId());
    getTaxonomyTreeModel.setKlassIdsHavingRP(klassIdsHavingRP);
    getTaxonomyTreeModel.setTaxonomyIdsHavingRP(taxonomyIdsHavingRP);
    
    ICategoryInformationModel infoModel = getTaxonomyContentCountStrategy.execute(getTaxonomyTreeModel);
    List<ICategoryInformationModel> childrens = (List<ICategoryInformationModel>) infoModel.getChildren();
    
    return childrens;
    return new ArrayList<>();
  }
  
  protected IGetFilterInfoModel getTaxonomyFilterInfo(IIdsListParameterModel idsModel) throws Exception
  {
    IGetFilterAndSortDataRequestModel requestModel = new GetFilterAndSortDataRequestModel();
    requestModel.setTypeIds(idsModel.getIds());
    IGetFilterInformationModel filterInformationModel = getFilterAndSortDataForKlassStrategy.execute(requestModel);
    return filterUtils.getFilterInfoModel(filterInformationModel);
  }
  
  public List<IConfigEntityInformationModel> getDynamicRelationshipHierarchy() throws Exception
  {
    IListModel<IConfigEntityInformationModel> listModel = getAllDynamicRelationshipHierarchyStrategy.execute(new IdParameterModel());
    List<IConfigEntityInformationModel> list = (List<IConfigEntityInformationModel>) listModel.getList();
    return list;
  }
  
  protected IGetFilterInfoModel getFilterInfoModel(IGetFilterInformationModel filterInformationModel)
  {
    IGetFilterInfoModel filterInfoModel = new GetFilterInfoModel();
    filterInfoModel.setSortData(filterInformationModel.getSortData());
    return filterInfoModel;
  }*/
  
  protected IModule getModule(Set<String> allowedEntities, String moduleId) throws Exception
  {
    List<IModule> modules = moduleMappingUtil.getRuntimeModule();
    for (IModule iModule : modules) {
      if (iModule.getId().equals(moduleId)) {
        iModule.getEntities().retainAll(allowedEntities);
        return iModule;
      }
    }
    return null;
  }
  
  public IModule getModule(String moduleId) throws Exception
  {
    List<IModule> modules = moduleMappingUtil.getRuntimeModule();
    for (IModule iModule : modules) {
      if (iModule.getId().equals(moduleId)) {
        return iModule;
      }
    }
    return null;
  }
  
}
