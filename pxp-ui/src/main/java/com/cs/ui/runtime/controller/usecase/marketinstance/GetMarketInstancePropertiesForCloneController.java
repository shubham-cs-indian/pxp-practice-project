package com.cs.ui.runtime.controller.usecase.marketinstance;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instance.GetCloneWizardRequestModel;
import com.cs.pim.runtime.interactor.usecase.targetinstance.market.IGetMarketInstancePropertiesForClone;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping("/runtime")
public class GetMarketInstancePropertiesForCloneController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IGetMarketInstancePropertiesForClone getMarketInstancePropertiesForClone;
  
  @RequestMapping(value = "/getMarketInstancePropertiesToClone", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody GetCloneWizardRequestModel model) throws Exception
  {
    return createResponse(getMarketInstancePropertiesForClone.execute(model));
  }
  
}
