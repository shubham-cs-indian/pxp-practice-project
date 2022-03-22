package com.cs.ui.config.controller.usecase.themeconfiguration;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.themeconfiguration.ThemeConfigurationModel;
import com.cs.core.config.interactor.usecase.themeconfiguration.ISaveThemeConfiguration;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveThemeConfigurationController extends BaseController implements IConfigController {
  
  @Autowired
  protected ISaveThemeConfiguration saveThemeConfiguration;
  
  @RequestMapping(value = "/themeconfigurations", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ThemeConfigurationModel themeConfigurationModel)
      throws Exception
  {
    return createResponse(saveThemeConfiguration.execute(themeConfigurationModel));
  }
}
