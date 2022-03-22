package com.cs.ui.runtime.controller.usecase.targetinstance.market;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.config.interactor.usecase.target.IGetAllMasterMarkets;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/runtime")
public class GetAllMasterMarketsController extends BaseController implements IRuntimeController {
  
  @Autowired
  IGetAllMasterMarkets getAllMasterTargets;
  
  @RequestMapping(value = "/markets", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(getAllMasterTargets.execute(null));
  }
}
