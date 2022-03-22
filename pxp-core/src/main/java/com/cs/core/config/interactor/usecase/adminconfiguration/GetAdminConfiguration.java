package com.cs.core.config.interactor.usecase.adminconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.themeandviewconfiguration.IAdminConfigurationModel;
import com.cs.core.config.strategy.usecase.adminconfiguration.IGetAdminConfigurationStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Service
public class GetAdminConfiguration
    extends AbstractGetConfigInteractor<IModel, IAdminConfigurationModel>
    implements IGetAdminConfiguration {

	@Autowired
	protected IGetAdminConfigurationStrategy getAdminConfigurationStrategy;

	@Override
	public IAdminConfigurationModel executeInternal(IModel dataModel) throws Exception {
		return getAdminConfigurationStrategy.execute(dataModel);
	}

}
