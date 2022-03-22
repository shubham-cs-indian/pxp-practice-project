package com.cs.ui.config.controller.usecase.variantconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.variantconfiguration.VariantConfigurationModel;
import com.cs.core.config.interactor.usecase.variantconfiguration.ISaveVariantConfiguration;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;


@RestController
@RequestMapping(value = "/config")
public class SaveVariantConfigurationController extends BaseController implements IConfigController {
  
  @Autowired
  protected ISaveVariantConfiguration saveVariantConfiguration;
  
  @RequestMapping(value = "/variantconfigurations", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody VariantConfigurationModel variantConfigurationModel) throws Exception
  {
    return createResponse(saveVariantConfiguration.execute(variantConfigurationModel));
  }
  
}
