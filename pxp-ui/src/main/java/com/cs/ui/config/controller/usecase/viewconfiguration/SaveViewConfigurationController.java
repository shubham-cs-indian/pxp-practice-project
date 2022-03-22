package com.cs.ui.config.controller.usecase.viewconfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.viewconfiguration.ViewConfigurationModel;
import com.cs.core.config.interactor.usecase.viewconfiguration.ISaveViewConfiguration;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;


@RestController
@RequestMapping(value = "/config")
public class SaveViewConfigurationController extends BaseController implements IConfigController {
	  
	  @Autowired
	  protected ISaveViewConfiguration saveViewConfiguration;
	  
	  @RequestMapping(value = "/viewconfigurations", method = RequestMethod.POST)
	  public IRESTModel execute(@RequestBody ViewConfigurationModel viewConfigurationModel)
	      throws Exception
	  {
	    return createResponse(saveViewConfiguration.execute(viewConfigurationModel));
	  }
	}
