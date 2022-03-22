package com.cs.core.config.strategy.usecase.adminconfiguration;

import com.cs.core.config.interactor.model.themeandviewconfiguration.IAdminConfigurationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetAdminConfigurationStrategy  extends IConfigStrategy<IModel, IAdminConfigurationModel> {
	  
}
