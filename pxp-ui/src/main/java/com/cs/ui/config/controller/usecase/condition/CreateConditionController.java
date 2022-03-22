package com.cs.ui.config.controller.usecase.condition;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.condition.ConditionModel;
import com.cs.core.config.interactor.usecase.condition.ICreateCondition;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateConditionController extends BaseController implements IConfigController {
  
  @Autowired
  ICreateCondition createCondition;
  
  @RequestMapping(value = "/condition", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody ConditionModel condition) throws Exception
  {
    return createResponse(createCondition.execute(condition));
  }
}
