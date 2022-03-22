package com.cs.ui.runtime.controller.usecase.supplierinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instance.GetInstanceRequestModel;
import com.cs.pim.runtime.interactor.usecase.supplierinstance.IGetArchivedSupplierInstanceForVersionTab;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetArchivedSupplierInstanceForVersionTabController extends BaseController
  implements IRuntimeController {
  
  @Autowired
  protected IGetArchivedSupplierInstanceForVersionTab getArchivedSupplierInstanceForVersionTab;
  
  @RequestMapping(value = "/archivedsupplierinstances/timelinetab/{id}", method = RequestMethod.POST)
  public IRESTModel execute(@PathVariable String id, @RequestBody GetInstanceRequestModel  model) throws Exception
  {
    model.setId(id);
    
    return createResponse(getArchivedSupplierInstanceForVersionTab.execute(model));
  }
}
