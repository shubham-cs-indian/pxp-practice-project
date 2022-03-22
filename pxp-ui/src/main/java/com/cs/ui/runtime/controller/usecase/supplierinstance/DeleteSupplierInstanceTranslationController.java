package com.cs.ui.runtime.controller.usecase.supplierinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.languageinstance.DeleteTranslationRequestModel;
import com.cs.pim.runtime.interactor.usecase.supplierinstance.IDeleteSupplierInstanceTranslation;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class DeleteSupplierInstanceTranslationController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected IDeleteSupplierInstanceTranslation deleteTranslation;
  
  @RequestMapping(value = "supplier/translations", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody DeleteTranslationRequestModel model) throws Exception
  {
    return createResponse(deleteTranslation.execute(model));
  }
}
