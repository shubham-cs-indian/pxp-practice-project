package com.cs.ui.config.controller.usecase.datarule;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.BulkSaveDataRuleModel;
import com.cs.core.config.interactor.model.datarule.IBulkSaveDataRuleModel;
import com.cs.core.config.interactor.usecase.datarule.IBulkSaveDataRule;
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
public class BulkSaveDataRuleController extends BaseController implements IConfigController {
  
  @Autowired
  protected IBulkSaveDataRule bulkSaveDataRule;
  
  @RequestMapping(value = "/bulksave/datarules", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<BulkSaveDataRuleModel> dataRuleListModel)
      throws Exception
  {
    IListModel<IBulkSaveDataRuleModel> ruleListSaveModel = new ListModel<>();
    ruleListSaveModel.setList(dataRuleListModel);
    return createResponse(bulkSaveDataRule.execute(ruleListSaveModel));
  }
}
