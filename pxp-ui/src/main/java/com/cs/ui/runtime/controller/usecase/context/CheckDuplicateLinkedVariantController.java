package com.cs.ui.runtime.controller.usecase.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.context.CheckDuplicateLinkedVariantRequestModel;
import com.cs.core.runtime.interactor.usecase.context.ICheckDuplicateLinkedVariant;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class CheckDuplicateLinkedVariantController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  ICheckDuplicateLinkedVariant checkDuplicateLinkedVariant;
  
  @RequestMapping(value = "/checkduplicatelinkedvariant", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody CheckDuplicateLinkedVariantRequestModel model)
      throws Exception
  {
    return createResponse(checkDuplicateLinkedVariant.execute(model));
  }
  
}