package com.cs.ui.runtime.controller.usecase.articleinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instance.CreateInstanceModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.ICreateArticleInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class CreateArticleInstanceController extends BaseController implements IRuntimeController {
  
  @Autowired
  ICreateArticleInstance createArticleInstance;
  
  @RequestMapping(value = "/klassinstances", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateInstanceModel klassInstanceModel) throws Exception
  {
    return createResponse(createArticleInstance.execute(klassInstanceModel));
  }
}
