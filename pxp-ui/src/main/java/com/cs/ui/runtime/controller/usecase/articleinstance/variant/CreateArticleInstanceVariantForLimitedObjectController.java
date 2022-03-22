package com.cs.ui.runtime.controller.usecase.articleinstance.variant;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.variants.CreateVariantForLimitedObjectRequestModel;
import com.cs.core.runtime.interactor.usecase.variant.articleinstance.ICreateArticleInstanceVariantForLimitedObject;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/runtime")
public class CreateArticleInstanceVariantForLimitedObjectController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected ICreateArticleInstanceVariantForLimitedObject createArticleInstanceVariantForLimitedObject;
  
  @RequestMapping(value = "/article/variant/limitedobject", method = RequestMethod.PUT)
  public IRESTModel execute(
      @RequestBody CreateVariantForLimitedObjectRequestModel createVariantModel) throws Exception
  {
    return createResponse(createArticleInstanceVariantForLimitedObject.execute(createVariantModel));
  }
}
