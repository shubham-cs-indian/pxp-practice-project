package com.cs.ui.config.controller.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.asset.SaveDAMConfigurationRequestModel;
import com.cs.core.config.interactor.usecase.asset.ISaveDAMConfigurationDetails;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * This controller is to save DAM related configuration.
 * @author pranav.huchche
 *
 */

@RestController
@RequestMapping(value = "/config")
public class SaveDAMConfigurationController extends BaseController implements IConfigController{
  
  @Autowired
  ISaveDAMConfigurationDetails saveDAMConfigurationDetails;
  
  @RequestMapping(value = "/savedamconfiguration", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveDAMConfigurationRequestModel model) throws Exception
  {
    return createResponse(saveDAMConfigurationDetails.execute(model));
  }
}
