package com.cs.core.runtime.interactor.usecase.taxonomy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.config.interactor.model.attributiontaxonomy.ITaxonomyInformationModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.TaxonomyInformationModel;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IGetFilterAndSortDataRequestModel;
import com.cs.core.config.interactor.model.datarule.ICategoryTreeInformationModel;
import com.cs.core.config.interactor.model.klass.IConfigDetailsForTaxonomyHierarchyModel;
import com.cs.core.config.strategy.usecase.klass.IConfigDetailsForTaxonomyHierarchyStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetFilterAndSortDataStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetKlassesTreeStrategy;
import com.cs.core.config.strategy.usecase.taxonomy.IGetTaxonomyHierarchyStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;

@Component
public abstract class AbstractGetTaxonomyHierarchy<P extends IGetTaxonomyTreeModel, R extends ICategoryInformationModel>
  extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  protected ISessionContext                            context;
  
  @Autowired
  protected IGetTaxonomyHierarchyStrategy              getTaxonomyHierarchyStrategy;
  
  @Autowired
  protected IGetKlassesTreeStrategy                    getKlassesTreeStrategy;
  
  @Autowired
  protected IConfigDetailsForTaxonomyHierarchyStrategy getAllowedEntitiesAndKlassIdsHavingRPStrategy;
  
  @Autowired
  protected IGetFilterAndSortDataStrategy              getFilterAndSortDataForKlassStrategy;
  
  @Autowired
  protected KlassInstanceUtils                         klassInstanceUtils;
  
  @Autowired
  protected ModuleMappingUtil                          moduleMappingUtil;
  
  
  @Autowired
  protected GetAllUtils getAllUtils;
  
  protected abstract ITaxonomyInformationModel getTaxonomyContentCount(P dataModel) throws Exception;
  
  @SuppressWarnings("unchecked")
  @Override
  public R executeInternal(P dataModel)
      throws Exception
  {
    IIdParameterModel idModel = new IdParameterModel(context.getUserId());
    IConfigDetailsForTaxonomyHierarchyModel permissionModel = getAllowedEntitiesAndKlassIdsHavingRPStrategy.execute(idModel);
    
    IModule module = getModule(dataModel.getModuleId(), permissionModel.getEntities());
    String clickedTaxonomyId = dataModel.getClickedTaxonomyId();
    List<IConfigEntityTreeInformationModel> categoryInfo = new ArrayList<>();
    
    if (clickedTaxonomyId.isEmpty()) {
      dataModel.setIsKlassTaxonomy(true);
      List<IConfigEntityTreeInformationModel> klassTreeChildren = getKlassTree(module).getCategoryInfo();
      ITaxonomyInformationModel klassTaxonomy = new TaxonomyInformationModel();
      klassTaxonomy.setId("-1");
      klassTaxonomy.setChildren(klassTreeChildren);
      categoryInfo.add(klassTaxonomy);
    }
    else {
      dataModel.setIsKlassTaxonomy(false);
      IIdParameterModel idParameterModel = new IdParameterModel(clickedTaxonomyId);
      ITaxonomyInformationModel execute = getTaxonomyHierarchyStrategy.execute(idParameterModel);
      categoryInfo.add(execute);
    }
    
    IGetFilterAndSortDataRequestModel filterAndSortDataRequestModel = KlassInstanceUtils
        .prepareSortAndFilterDataRequestModel(dataModel);
    
    IGetFilterInformationModel filterInformationModel = getFilterAndSortDataForKlassStrategy.execute(filterAndSortDataRequestModel);
    IGetFilterInfoModel filterInfoModel = getAllUtils.getFilterInfoModel(filterInformationModel);
    
    dataModel.setCategoryInfo(categoryInfo);
    dataModel.setModuleEntities(module.getEntities());
    dataModel.setCurrentUserId(context.getUserId());
    dataModel.setKlassIdsHavingRP(permissionModel.getKlassIdsHavingRP());
    dataModel.setTaxonomyIdsHavingRP(permissionModel.getTaxonomyIdsHavingRP());
    dataModel.setFilterInfo(filterInfoModel);

    fillAllowedKlasses(dataModel);
    
    ITaxonomyInformationModel infoModel = getTaxonomyContentCount(dataModel);
    return (R)infoModel;
  }
  
  private ICategoryTreeInformationModel getKlassTree(IModule module)
      throws Exception
  {
    IIdsListParameterModel listModel = new IdsListParameterModel();
    List<String> ids = KlassInstanceUtils.getStandardKlassIds(module.getEntities());
    listModel.setIds(ids);
    return getKlassesTreeStrategy.execute(listModel);
  }
  
  private IModule getModule(String moduleId, Set<String> allowedEntities) throws Exception
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
  
  protected void fillAllowedKlasses(P dataModel) throws Exception
  {
    // TODO Auto-generated method stub
    
  }
}
