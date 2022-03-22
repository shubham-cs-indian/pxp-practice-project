package com.cs.ui.runtime.controller.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeSwitchModel;
import com.cs.pim.runtime.interactor.usecase.targetinstance.market.ISwitchMarketInstanceType;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetSwitchedTypeMarketInstanceController extends BaseController
    implements IRuntimeController {
  
  @Autowired
  ISwitchMarketInstanceType switchMarketInstanceType;
  
  @RequestMapping(value = "/marketinstances/typeswitch", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody KlassInstanceTypeSwitchModel typeSwitchModel)
      throws Exception
  {
    return createResponse(switchMarketInstanceType.execute(typeSwitchModel));
  }
}
