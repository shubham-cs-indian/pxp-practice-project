package com.cs.core.config.interactor.usecase.entityconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;

@Service
public class GetUserEntityConfiguration extends
    AbstractGetConfigInteractor<IGetEntityConfigurationRequestModel, IGetEntityConfigurationResponseModel>
    implements IGetEntityConfiguration {
  
	@Autowired
	protected IGetEntityConfigurationStrategy getUserEntityConfigurationStrategy;

	@Override
	public IGetEntityConfigurationResponseModel executeInternal(IGetEntityConfigurationRequestModel dataModel)
			throws Exception {
		return getUserEntityConfigurationStrategy.execute(dataModel);
	}

}

