package com.cs.ui.runtime.controller.usecase.supplierinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.filter.KlassInstanceQuickListModel;
import com.cs.pim.runtime.interactor.usecase.supplierinstance.IQuickListSupplierInstances;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class QuickListSupplierInstanceController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  IQuickListSupplierInstances quickListSupplierInstances;
  
  @RequestMapping(value = "/supplierinstance/quicklist", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody KlassInstanceQuickListModel quickListModel)
      throws Exception
  {
    return createResponse(quickListSupplierInstances.execute(quickListModel));
  }
}
