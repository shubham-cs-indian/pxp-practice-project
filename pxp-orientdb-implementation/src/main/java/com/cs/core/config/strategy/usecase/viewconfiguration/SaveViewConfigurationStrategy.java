package com.cs.core.config.strategy.usecase.viewconfiguration;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.viewconfiguration.IViewConfigurationModel;
import com.cs.core.config.interactor.model.viewconfiguration.ViewConfigurationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class SaveViewConfigurationStrategy extends OrientDBBaseStrategy implements ISaveViewConfigurationStrategy {

	@Override
	public IViewConfigurationModel execute(IViewConfigurationModel model) throws Exception {
		return execute(SAVE_VIEW_CONFIGURATION, model.getEntity(),
		        ViewConfigurationModel.class);
	}

}
