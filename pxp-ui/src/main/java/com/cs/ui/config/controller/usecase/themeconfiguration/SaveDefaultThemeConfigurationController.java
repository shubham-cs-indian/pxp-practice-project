package com.cs.ui.config.controller.usecase.themeconfiguration;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.themeconfiguration.ISaveDefaultThemeConfiguration;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveDefaultThemeConfigurationController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected ISaveDefaultThemeConfiguration saveDefaultThemeConfiguration;
  
  @RequestMapping(value = "/savedefaultthemeconfigurations", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody VoidModel voidModel) throws Exception
  {
    return createResponse(saveDefaultThemeConfiguration.execute(voidModel));
  }
}
