package com.cs.ui.runtime.controller.usecase.articleinstance;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instance.GetCloneWizardRequestModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IGetArticleInstancePropertiesForClone;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class GetArticleInstancePropertiesForCloneController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IGetArticleInstancePropertiesForClone getArticleInstancePropertiesForClone;
  
  @RequestMapping(value = "/getPropertiesToClone", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetCloneWizardRequestModel model) throws Exception
  {
    return createResponse(getArticleInstancePropertiesForClone.execute(model));
  }
  
}
