package com.cs.ui.runtime.controller.usecase.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.klassinstance.ArticleInstanceSaveModel;
import com.cs.core.runtime.interactor.usecase.articleinstance.ICreateTranslatableArticleInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class CreateTranslatableArticleInstanceController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected ICreateTranslatableArticleInstance createTranslatableArticleInstance;
  
  @RequestMapping(value = "/createtranslatablearticleinstance", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArticleInstanceSaveModel klassInstanceSaveModel) throws Exception
  {
    return createResponse(createTranslatableArticleInstance.execute(klassInstanceSaveModel));
  }
}
