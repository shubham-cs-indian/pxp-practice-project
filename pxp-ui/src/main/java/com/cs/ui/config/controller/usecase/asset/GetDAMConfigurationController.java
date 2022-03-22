package com.cs.ui.config.controller.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.asset.IGetDAMConfigurationDetails;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

/**
 * This controller is to get DAM related configuration.
 * @author pranav.huchche
 *
 */
@RestController
@RequestMapping(value = "/config")
public class GetDAMConfigurationController extends BaseController implements IConfigController{
  
  @Autowired
  IGetDAMConfigurationDetails getDAMConfigurationDetails;
  
  @RequestMapping(value = "/getdamconfiguration", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(getDAMConfigurationDetails.execute(null));
  }
}
