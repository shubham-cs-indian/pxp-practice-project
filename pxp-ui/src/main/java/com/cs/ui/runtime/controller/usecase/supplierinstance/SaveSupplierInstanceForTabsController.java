package com.cs.ui.runtime.controller.usecase.supplierinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.supplierinstance.SupplierInstanceSaveModel;
import com.cs.pim.runtime.interactor.usecase.supplierinstance.ISaveSupplierInstanceForTabs;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class SaveSupplierInstanceForTabsController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  protected ISaveSupplierInstanceForTabs saveSupplierInstanceForTabs;
  
  @RequestMapping(value = "/supplierinstances/tabs", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SupplierInstanceSaveModel klassInstanceModels,
      @RequestParam(required = false) String versionClassId) throws Exception
  {
    return createResponse(saveSupplierInstanceForTabs.execute(klassInstanceModels));
  }
}
