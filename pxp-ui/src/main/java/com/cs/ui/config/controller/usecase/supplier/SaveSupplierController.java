package com.cs.ui.config.controller.usecase.supplier;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.klass.AbstractKlassSaveModel;
import com.cs.core.config.interactor.model.supplier.ISupplierKlassSaveModel;
import com.cs.core.config.interactor.usecase.supplier.ISaveSupplier;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveSupplierController extends BaseController implements IConfigController {
  
  @Autowired
  ISaveSupplier saveSupplier;
  
  @RequestMapping(value = "/suppliers", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody AbstractKlassSaveModel targetModel) throws Exception
  {
    return createResponse(saveSupplier.execute((ISupplierKlassSaveModel) targetModel));
  }
}
