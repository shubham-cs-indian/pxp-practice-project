package com.cs.core.config.strategy.usecase.adminconfiguration;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.themeandviewconfiguration.IAdminConfigurationModel;
import com.cs.core.config.interactor.model.themeandviewconfiguration.AdminConfigurationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Component
public class GetAdminConfigurationStrategy extends OrientDBBaseStrategy implements IGetAdminConfigurationStrategy {

	@Override
	public IAdminConfigurationModel execute(IModel model) throws Exception {
		return execute(GET_ADMIN_CONFIGURATION, model, AdminConfigurationModel.class);
	}
}
