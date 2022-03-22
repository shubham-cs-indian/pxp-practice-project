package com.cs.core.config.strategy.usecase.viewconfiguration;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.viewconfiguration.IViewConfigurationModel;
import com.cs.core.config.interactor.model.viewconfiguration.ViewConfigurationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

@Component
public class GetViewConfigurationStrategy extends OrientDBBaseStrategy implements IGetViewConfigurationStrategy {

	@Override
	public IViewConfigurationModel execute(IVoidModel model) throws Exception {
		return execute(GET_VIEW_CONFIGURATION, model, ViewConfigurationModel.class);
	}

	@Override
	public String getUsecase() {
		return "Get View Configuration Strategy";
	}

}
