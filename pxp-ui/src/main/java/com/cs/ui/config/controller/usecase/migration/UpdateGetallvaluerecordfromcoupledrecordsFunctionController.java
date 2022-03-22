package com.cs.ui.config.controller.usecase.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.interactor.usecase.migration.IUpdateGetallvaluerecordfromcoupledrecordsFunction;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping
public class UpdateGetallvaluerecordfromcoupledrecordsFunctionController extends BaseController{
  
  @Autowired
  protected IUpdateGetallvaluerecordfromcoupledrecordsFunction  updateGetallvaluerecordfromcoupledrecordsFunction;
  
  @RequestMapping(value = "/updategetallvaluerecordfromcoupledrecordsfunction", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(updateGetallvaluerecordfromcoupledrecordsFunction.execute(new VoidModel()));
  } 
}
