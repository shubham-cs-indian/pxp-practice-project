package com.cs.ui.runtime.controller.usecase.articleinstance;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.klassinstance.CreateKlassInstanceSingleCloneModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.ICreateArticleInstanceSingleClone;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class CreateArticleInstanceSingleCloneController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected ICreateArticleInstanceSingleClone createArticleInstanceSingleClone;
  
  @RequestMapping(value = "/articleinstance/singleclone", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateKlassInstanceSingleCloneModel createCloneModel) throws Exception
  {
    return createResponse(createArticleInstanceSingleClone.execute(createCloneModel));
  }

}