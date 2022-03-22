package com.cs.ui.config.controller.usecase.datarule;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.datarule.IGetDataRule;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetDataRuleController extends BaseController implements IConfigController {
  
  @Autowired
  IGetDataRule getDataRule;
  
  @RequestMapping(value = "/datarule/{id}", method = RequestMethod.GET)
  public IRESTModel execute(@PathVariable String id) throws Exception
  {
    IIdParameterModel getEntityModel = new IdParameterModel(id);
    
    return createResponse(getDataRule.execute(getEntityModel));
  }
}
