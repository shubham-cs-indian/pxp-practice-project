package com.cs.ui.config.controller.usecase.template;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.template.IDeleteTemplate;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class DeleteTemplateController extends BaseController implements IConfigController {
  
  @Autowired
  protected IDeleteTemplate deleteTemplate;
  
  @RequestMapping(value = "/templates", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody IdsListParameterModel listModel) throws Exception
  {
    return createResponse(deleteTemplate.execute(listModel));
  }
}
