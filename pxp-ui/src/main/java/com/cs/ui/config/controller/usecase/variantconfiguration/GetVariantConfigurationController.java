package com.cs.ui.config.controller.usecase.variantconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.variantconfiguration.IGetVariantConfiguration;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;


@RestController
@RequestMapping(value = "/config")
public class GetVariantConfigurationController extends BaseController implements IConfigController {

	@Autowired
	protected IGetVariantConfiguration getVariantConfiguration;

	@RequestMapping(value = "/variantconfigurations", method = RequestMethod.GET)
	public IRESTModel execute() throws Exception {
		return createResponse(getVariantConfiguration.execute(null));
	}

}