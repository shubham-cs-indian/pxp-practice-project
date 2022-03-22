package com.cs.ui.config.controller.usecase.template;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.customtemplate.SaveCustomTemplateModel;
import com.cs.core.config.interactor.usecase.template.ISaveTemplate;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveTemplateController extends BaseController implements IConfigController {
  
  @Autowired
  protected ISaveTemplate saveTemplate;
  
  @RequestMapping(value = "/templates", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveCustomTemplateModel saveTemplateModel) throws Exception
  {
    return createResponse(saveTemplate.execute(saveTemplateModel));
  }
}
