package com.cs.ui.runtime.controller.usecase.textassetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.filter.KlassInstanceQuickListModel;
import com.cs.core.runtime.interactor.usecase.textassetinstance.IQuickListTextAssetInstances;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;



@RestController
@RequestMapping(value = "/runtime")
public class QuickListTextAssetInstanceController extends BaseController implements IRuntimeController {
  
  @Autowired
  IQuickListTextAssetInstances quickListTextAssetInstances;
  
  @RequestMapping(value = "/textassetinstance/quicklist", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody KlassInstanceQuickListModel quickListModel) throws Exception
  {
    return createResponse(quickListTextAssetInstances.execute(quickListModel));
  }
}
