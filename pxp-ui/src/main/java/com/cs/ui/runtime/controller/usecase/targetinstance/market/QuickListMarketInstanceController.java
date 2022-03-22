package com.cs.ui.runtime.controller.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.filter.KlassInstanceQuickListModel;
import com.cs.pim.runtime.interactor.usecase.targetinstance.market.IQuickListMarketInstances;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class QuickListMarketInstanceController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  IQuickListMarketInstances quickListMarketInstances;
  
  @RequestMapping(value = "/marketinstance/quicklist", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody KlassInstanceQuickListModel quickListModel)
      throws Exception
  {
    return createResponse(quickListMarketInstances.execute(quickListModel));
  }
}
