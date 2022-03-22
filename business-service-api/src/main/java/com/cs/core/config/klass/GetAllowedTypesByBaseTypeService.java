package com.cs.core.config.klass;

import com.cs.core.asset.services.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.config.interactor.model.taxonomyhierarchy.ITaxonomyHierarchyModel;
import com.cs.core.config.strategy.usecase.klass.IGetAllowedTypesByBaseTypeStrategy;
import com.cs.core.config.strategy.usecase.taxonomy.IGetAllTaxonomiesForAllowedTypesStrategy;
import com.cs.core.config.strategy.usecase.taxonomy.IGetNonRootTaxonomiesForAllowedTypesStrategy;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO: Write seperate calls for klasses & taxonomies

@Service
public class GetAllowedTypesByBaseTypeService
    extends AbstractRuntimeService<IAllowedTypesRequestModel, ITaxonomyHierarchyModel>
    implements IGetAllowedTypesByBaseTypeService {
  
  @Autowired
  protected IGetAllowedTypesByBaseTypeStrategy           getAllowedByBaseTypeStrategy;
  
  @Autowired
  protected IGetNonRootTaxonomiesForAllowedTypesStrategy getNonRootTaxonomiesForAllowedTypesStrategy;
  
  @Autowired
  protected IGetAllTaxonomiesForAllowedTypesStrategy     getAllTaxonomiesForAllowedTypesStrategy;
  
  @Autowired
  protected ISessionContext                              context;
  
  @Autowired
  protected ModuleMappingUtil                            moduleMappingUtil;
  
  @Override
  public ITaxonomyHierarchyModel executeInternal(IAllowedTypesRequestModel dataModel) throws Exception
  {
    dataModel.setUserId(context.getUserId());
    if ((dataModel.getSelectionType().equals(CommonConstants.MAJOR_TAXONOMY)
        || dataModel.getSelectionType().equals(CommonConstants.TAXONOMY))
        && dataModel.getId() == null) {
      return getAllTaxonomiesForAllowedTypesStrategy.execute(dataModel);
    }
    else if ((dataModel.getSelectionType()
        .equals(CommonConstants.MAJOR_TAXONOMY)
        || dataModel.getSelectionType()
            .equals(CommonConstants.MINOR_TAXONOMY))
        && !dataModel.getId()
            .equals("-1")) {
      return getNonRootTaxonomiesForAllowedTypesStrategy.execute(dataModel);
    }
    else {
      String moduleId = dataModel.getModuleId();
      if(moduleId != null && !moduleId.isEmpty()) {
        IModule module = getModule(moduleId);
        List<String> moduleEntities = module.getEntities();
        dataModel.setAllowedEntities(moduleEntities);
      }
      return getAllowedByBaseTypeStrategy.execute(dataModel);
    }
  }
  
  public IModule getModule(String moduleId)throws Exception 
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
