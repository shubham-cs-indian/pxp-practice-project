package com.cs.ui.runtime.controller.usecase.supplierinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.instance.CreateInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.BulkCreateInstanceModel;
import com.cs.core.runtime.interactor.usecase.supplierinstance.IBulkCreateSupplierInstance;
import com.cs.core.runtime.interactor.usecase.supplierinstance.ICreateSupplierInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/runtime")
public class BulkCreateSupplierInstanceController extends BaseController implements IRuntimeController {
  
  @Autowired IBulkCreateSupplierInstance bulkCreateSupplierInstance;
  
  @RequestMapping(value = "/bulkcreate/suppliers", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody BulkCreateInstanceModel createModel) throws Exception
  {
    return createResponse(bulkCreateSupplierInstance.execute(createModel));
  }
}
