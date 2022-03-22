package com.cs.ui.config.controller.usecase.viewconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.viewconfiguration.ISaveDefaultViewConfiguration;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class SaveDefaultViewConfigurationController  extends BaseController implements IConfigController {

	@Autowired
	protected ISaveDefaultViewConfiguration saveDefaultViewConfiguration;

	@RequestMapping(value = "/savedefaultviewconfigurations", method = RequestMethod.POST)
	public IRESTModel execute() throws Exception {
		return createResponse(saveDefaultViewConfiguration.execute(new VoidModel()));
	}
}