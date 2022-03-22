package com.cs.ui.config.controller.usecase.rulelist;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.rulelist.RuleListModel;
import com.cs.core.config.interactor.usecase.rulelist.ICreateRuleList;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateRuleListController extends BaseController implements IConfigController {
  
  @Autowired
  ICreateRuleList createRuleList;
  
  @RequestMapping(value = "/ruleList", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody RuleListModel ruleList) throws Exception
  {
    return createResponse(createRuleList.execute(ruleList));
  }
}
