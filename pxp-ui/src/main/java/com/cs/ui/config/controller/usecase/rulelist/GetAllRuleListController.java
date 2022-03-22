package com.cs.ui.config.controller.usecase.rulelist;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;
import com.cs.core.config.interactor.usecase.rulelist.IGetAllRuleList;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetAllRuleListController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetAllRuleList getAllRuleList;
  
  @RequestMapping(value = "/ruleList/getall", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ConfigGetAllRequestModel model) throws Exception
  {
    return createResponse(getAllRuleList.execute(model));
  }
}
