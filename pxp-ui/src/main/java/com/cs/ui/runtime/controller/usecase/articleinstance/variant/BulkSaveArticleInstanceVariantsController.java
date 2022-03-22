package com.cs.ui.runtime.controller.usecase.articleinstance.variant;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.variants.BulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.usecase.variant.articleinstance.IBulkSaveArticleInstanceVariants;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class BulkSaveArticleInstanceVariantsController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IBulkSaveArticleInstanceVariants bulkSaveArticleInstanceVariants;
  
  @RequestMapping(value = "/articleinstance/variants/bulksave", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody BulkSaveInstanceVariantsModel klassInstanceModels)
      throws Exception
  {
    return createResponse(bulkSaveArticleInstanceVariants.execute(klassInstanceModels));
  }
}
