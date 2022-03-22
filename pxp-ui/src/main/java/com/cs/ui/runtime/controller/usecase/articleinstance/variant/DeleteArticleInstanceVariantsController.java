package com.cs.ui.runtime.controller.usecase.articleinstance.variant;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.variants.DeleteVariantModel;
import com.cs.core.runtime.interactor.usecase.variant.articleinstance.IDeleteArticleInstanceVariants;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/runtime")
public class DeleteArticleInstanceVariantsController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  IDeleteArticleInstanceVariants deleteArticleInstanceVariants;
  
  @RequestMapping(value = "/articlevariantinstances", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody DeleteVariantModel deleteVariantsModel) throws Exception
  {
    
    return createResponse(deleteArticleInstanceVariants.execute(deleteVariantsModel));
  }
}
