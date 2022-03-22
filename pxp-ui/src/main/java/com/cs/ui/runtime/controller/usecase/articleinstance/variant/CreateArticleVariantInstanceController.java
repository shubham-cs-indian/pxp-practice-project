package com.cs.ui.runtime.controller.usecase.articleinstance.variant;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.variants.CreateVariantModel;
import com.cs.core.runtime.interactor.usecase.variant.articleinstance.ICreateArticleVariantInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class CreateArticleVariantInstanceController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected ICreateArticleVariantInstance createArticleVariantInstance;
  
  @RequestMapping(value = "/variants/article", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateVariantModel model) throws Exception
  {
    return createResponse(createArticleVariantInstance.execute(model));
  }
}
