package com.cs.ui.config.controller.usecase.klass;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.klass.IGetArticleTree;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetArticleTreeController extends BaseController implements IConfigController {
  
  @Autowired
  IGetArticleTree getArticleTree;
  
  @RequestMapping(value = "/article/klasses", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody IdParameterModel bulkIdsModel) throws Exception
  {
    return createResponse(getArticleTree.execute(bulkIdsModel));
  }
}
