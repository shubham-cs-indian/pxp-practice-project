package com.cs.core.config.portal;

import com.cs.base.interactor.model.portal.IGetPortalModel;
import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetPortalsService extends IGetConfigService<IModel, IGetPortalModel> {
  
}
