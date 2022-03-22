package com.cs.ui.config.controller.usecase.governancerule;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.governancerule.BulkSaveKpiRuleRequestModel;
import com.cs.core.config.interactor.model.governancerule.IBulkSaveKpiRuleRequestModel;
import com.cs.core.config.interactor.usecase.governancerule.IBulkSaveKpiRule;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/config")
public class BulkSaveKpiRuleController extends BaseController implements IConfigController {
  
  @Autowired
  protected IBulkSaveKpiRule bulkSaveKpiRule;
  
  @RequestMapping(value = "/bulksave/kpirules", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<BulkSaveKpiRuleRequestModel> ruleListModel)
      throws Exception
  {
    IListModel<IBulkSaveKpiRuleRequestModel> rulelistSaveModel = new ListModel<>();
    rulelistSaveModel.setList(ruleListModel);
    return createResponse(bulkSaveKpiRule.execute(rulelistSaveModel));
  }
}
