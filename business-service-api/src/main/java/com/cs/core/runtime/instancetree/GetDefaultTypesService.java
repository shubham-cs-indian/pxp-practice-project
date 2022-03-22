package com.cs.core.runtime.instancetree;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.instancetree.IGetDefaultTypesStrategy;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.instancetree.IDefaultTypesRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetDefaultTypesResponseModel;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;

@Service
public class GetDefaultTypesService extends AbstractGetConfigService<IDefaultTypesRequestModel, IListModel<IGetDefaultTypesResponseModel>>
    implements IGetDefaultTypesService {
  
  @Autowired
  protected ISessionContext          context;
  
  @Autowired
  protected ModuleMappingUtil        moduleMappingUtil;
  
  @Autowired
  protected IGetDefaultTypesStrategy getDefaultTypesGetStrategy;
  
  @Override
  public IListModel<IGetDefaultTypesResponseModel> executeInternal(IDefaultTypesRequestModel model) throws Exception
  {
    if (model.getEntityType() != null && !model.getEntityType().isEmpty()) {
      model.setKlassIds(KlassInstanceUtils.getStandardKlassIds(Arrays.asList(model.getEntityType())));
    }
    else if (model.getModuleId() != null && !model.getModuleId().isEmpty()) {
      IModule module = moduleMappingUtil.getModule(model.getModuleId());
      model.setKlassIds(KlassInstanceUtils.getStandardKlassIds(module.getEntities()));
    }
    
    model.setUserId(context.getUserId());
    return getDefaultTypesGetStrategy.execute(model);
  }
}
