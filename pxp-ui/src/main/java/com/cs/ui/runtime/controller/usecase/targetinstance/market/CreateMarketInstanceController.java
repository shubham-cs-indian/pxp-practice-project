package com.cs.ui.runtime.controller.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.interactor.model.instance.CreateInstanceModel;
import com.cs.pim.runtime.interactor.usecase.targetinstance.market.ICreateMarketInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class CreateMarketInstanceController extends BaseController implements IRuntimeController {
  
  @Autowired
  ICreateMarketInstance createMarketInstance;
  
  @RequestMapping(value = "/marketinstances", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody CreateInstanceModel klassInstanceModel) throws Exception
  {
    return createResponse(createMarketInstance.execute(klassInstanceModel));
  }
}
