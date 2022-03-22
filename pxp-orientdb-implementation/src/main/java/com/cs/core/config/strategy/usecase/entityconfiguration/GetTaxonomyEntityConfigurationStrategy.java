package com.cs.core.config.strategy.usecase.entityconfiguration;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class GetTaxonomyEntityConfigurationStrategy extends OrientDBBaseStrategy
implements IGetEntityConfigurationStrategy {

	@Override
	public IGetEntityConfigurationResponseModel execute(IGetEntityConfigurationRequestModel model) throws Exception {
		return execute(GET_TAXONOMY_ENTITY_CONFIGURATION, model, GetEntityConfigurationResponseModel.class);
	}

}
 