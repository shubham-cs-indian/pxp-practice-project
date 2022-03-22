package com.cs.ui.config.controller.usecase.rulelist;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.rulelist.RuleListModel;
import com.cs.core.config.interactor.usecase.rulelist.ISaveRuleList;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveRuleListController extends BaseController implements IConfigController {
  
  @Autowired
  ISaveRuleList saveRuleList;
  
  @RequestMapping(value = "/ruleList", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody RuleListModel ruleListModel) throws Exception
  {
    return createResponse(saveRuleList.execute(ruleListModel));
  }
}
