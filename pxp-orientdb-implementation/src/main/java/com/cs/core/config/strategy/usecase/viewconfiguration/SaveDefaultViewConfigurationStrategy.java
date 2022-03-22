package com.cs.core.config.strategy.usecase.viewconfiguration;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.viewconfiguration.IViewConfigurationModel;
import com.cs.core.config.interactor.model.viewconfiguration.ViewConfigurationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class SaveDefaultViewConfigurationStrategy extends OrientDBBaseStrategy implements ISaveDefaultViewConfigurationStrategy {

	@Override
	public IViewConfigurationModel execute(IViewConfigurationModel dataModel) throws Exception {
		return execute(SAVE_DEFAULT_VIEW_CONFIGURATION , dataModel,
		        ViewConfigurationModel.class);
	}
}
