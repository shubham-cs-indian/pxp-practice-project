package com.cs.ui.config.controller.usecase.datarule;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.interactor.usecase.datarule.ICreateDataRule;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateDataRuleController extends BaseController implements IConfigController {
  
  @Autowired
  protected ICreateDataRule createDataRule;
  
  @RequestMapping(value = "/datarule", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody DataRuleModel dataRule) throws Exception
  {
    return createResponse(createDataRule.execute(dataRule));
  }
}
