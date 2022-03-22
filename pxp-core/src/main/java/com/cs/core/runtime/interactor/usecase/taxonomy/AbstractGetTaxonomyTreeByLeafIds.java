package com.cs.core.runtime.interactor.usecase.taxonomy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IGetFilterAndSortDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.ICategoryTreeInformationModel;
import com.cs.core.config.interactor.model.klass.IConfigDetailsForTaxonomyHierarchyModel;
import com.cs.core.config.strategy.usecase.klass.IConfigDetailsForTaxonomyHierarchyStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetFilterAndSortDataStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetKlassesTreeStrategy;
import com.cs.core.config.strategy.usecase.module.IGetAllowedModuleEntitiesStrategy;
import com.cs.core.config.strategy.usecase.taxonomy.IGetTaxonomyTreeByLeafIdsStrategy;
import com.cs.core.runtime.interactor.model.configdetails.CategoryInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;
import com.cs.core.runtime.interactor.model.taxonomy.GetTaxonomyTreeLeafIdsStrategyModel;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeLeafIdsStrategyModel;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;

public abstract class AbstractGetTaxonomyTreeByLeafIds<P extends IGetTaxonomyTreeModel, R extends ICategoryInformationModel>
    extends AbstractRuntimeInteractor<P, R> {
  @Autowired
  protected ISessionContext                            context;
  
  @Autowired
  protected IGetAllowedModuleEntitiesStrategy          getAllowedModuleEntitiesStrategy;
  
  @Autowired
  protected IGetTaxonomyTreeByLeafIdsStrategy          getTaxonomyTreeByLeafIdsStrategy;
  
  @Autowired
  protected IGetKlassesTreeStrategy                    getKlassesTreeStrategy;
  
  @Autowired
  protected IConfigDetailsForTaxonomyHierarchyStrategy getAllowedEntitiesAndKlassIdsHavingRPStrategy;
  
  @Autowired
  protected ModuleMappingUtil                          moduleMappingUtil;
  
  @Autowired
  protected IGetFilterAndSortDataStrategy              getFilterAndSortDataForKlassStrategy;
  
  @Autowired
  protected KlassInstanceUtils                         klassInstanceUtils;
  
  @Autowired
  GetAllUtils                                           getAllUtils;
  
  protected abstract ICategoryInformationModel getTaxonomyContentCount(P dataModel) throws Exception;
  
  @SuppressWarnings("unchecked")
  @Override
  public R executeInternal(P dataModel) throws Exception
  {
    IModule module = getModule(dataModel.getModuleId());
    List<IConfigEntityTreeInformationModel> categoryInfo = new ArrayList<>();
    List<String> leafIds = dataModel.getLeafIds();
    String parentTaxonomyId = dataModel.getParentTaxonomyId();
    if (parentTaxonomyId.isEmpty() && leafIds.isEmpty()) {
      dataModel.setIsKlassTaxonomy(true);
      List<IConfigEntityTreeInformationModel> klassTreeChildren = getKlassTree(module)
          .getCategoryInfo();
      ICategoryInformationModel klassTaxonomy = new CategoryInformationModel();
      klassTaxonomy.setId("-1");
      klassTaxonomy.setChildren(klassTreeChildren);
      categoryInfo.add(klassTaxonomy);
    }
    else {
      dataModel.setIsKlassTaxonomy(false);
      IGetTaxonomyTreeLeafIdsStrategyModel strategyModel = new GetTaxonomyTreeLeafIdsStrategyModel();
      strategyModel.setParentTaxonomyId(parentTaxonomyId);
      strategyModel.setLeafIds(leafIds);
      ICategoryInformationModel categoryInfoModel = getTaxonomyTreeByLeafIdsStrategy.execute(strategyModel);
      if (categoryInfoModel != null) {
        categoryInfo.add(categoryInfoModel);
      }
    }
    
    if (categoryInfo.isEmpty()) { 
      return (R)new CategoryInformationModel();
    }
    dataModel.setIsKlassTaxonomy(false);
    IGetFilterAndSortDataRequestModel filterAndSortDataRequestModel = KlassInstanceUtils
        .prepareSortAndFilterDataRequestModel(dataModel);
    
    IGetFilterInformationModel filterInformationModel = getFilterAndSortDataForKlassStrategy.execute(filterAndSortDataRequestModel);
    IGetFilterInfoModel filterInfoModel = getAllUtils.getFilterInfoModel(filterInformationModel);
    
    IIdParameterModel idModel = new IdParameterModel(context.getUserId());
    IConfigDetailsForTaxonomyHierarchyModel permissionModel = getAllowedEntitiesAndKlassIdsHavingRPStrategy.execute(idModel);
    
    dataModel.setCategoryInfo(categoryInfo);
    dataModel.setModuleEntities(module.getEntities());
    dataModel.setCurrentUserId(context.getUserId());
    dataModel.setKlassIdsHavingRP(permissionModel.getKlassIdsHavingRP());
    dataModel.setTaxonomyIdsHavingRP(permissionModel.getTaxonomyIdsHavingRP());
    dataModel.setFilterInfo(filterInfoModel);
    
    fillAllowedKlasses(dataModel);
    
    ICategoryInformationModel infoModel = getTaxonomyContentCount(dataModel);
    return (R)infoModel;
  }
  
  private IModule getModule(String moduleId) throws Exception
  {
    IIdParameterModel idParameterModel = new IdParameterModel(context.getUserId());
    IListModel<String> allowedEntitiesModel = getAllowedModuleEntitiesStrategy
        .execute(idParameterModel);
    
    List<IModule> modules = moduleMappingUtil.getRuntimeModule();
    
    for (IModule iModule : modules) {
      if (iModule.getId()
          .equals(moduleId)) {
        iModule.getEntities()
            .retainAll(allowedEntitiesModel.getList());
        return iModule;
      }
    }
    
    return null;
  }
  
  private ICategoryTreeInformationModel getKlassTree(IModule module) throws Exception
  {
    IIdsListParameterModel listModel = new IdsListParameterModel();
    List<String> ids = KlassInstanceUtils.getStandardKlassIds(module.getEntities());
    listModel.setIds(ids);
    return getKlassesTreeStrategy.execute(listModel);
  }
  
  protected void fillAllowedKlasses(P dataModel) throws Exception
  {
    // TODO Auto-generated method stub
    
  }
}
