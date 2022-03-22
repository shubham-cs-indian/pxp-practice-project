package com.cs.ui.runtime.controller.usecase.supplierinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.version.IMoveKlassInstanceVersionsSuccessModel;
import com.cs.core.runtime.interactor.model.version.MoveKlassInstanceVersionsModel;
import com.cs.pim.runtime.interactor.usecase.supplierinstance.IRestoreSupplierInstanceVersions;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class RestoreSupplierInstanceVersionsController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IRestoreSupplierInstanceVersions restoreSupplierInstanceVersions;
  
  @RequestMapping(value = "/supplierinstances/versionsrestore", method = RequestMethod.POST)
  public IMoveKlassInstanceVersionsSuccessModel execute(@RequestBody MoveKlassInstanceVersionsModel model) throws Exception
  {
    return restoreSupplierInstanceVersions.execute(model);
  }
}
