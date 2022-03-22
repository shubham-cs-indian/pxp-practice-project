package com.cs.ui.config.controller.usecase.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.interactor.usecase.migration.IUpdateCreateCouplingFunction;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping
public class UpdateCreateCouplingFucntionController extends BaseController{
  
  @Autowired
  protected IUpdateCreateCouplingFunction  updateCreateCouplingFunction;
  
  @RequestMapping(value = "/updateCreateCouplingFunction", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(updateCreateCouplingFunction.execute(new VoidModel()));
  }
  
}
