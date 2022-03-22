package com.cs.ui.config.controller.usecase.template;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.customtemplate.CustomTemplateModel;
import com.cs.core.config.interactor.usecase.template.ICreateCustomTemplate;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateCustomTemplateController extends BaseController implements IConfigController {
  
  @Autowired
  protected ICreateCustomTemplate createCustomTemplate;
  
  @RequestMapping(value = "/templates", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CustomTemplateModel model) throws Exception
  {
    return createResponse(createCustomTemplate.execute(model));
  }
}
