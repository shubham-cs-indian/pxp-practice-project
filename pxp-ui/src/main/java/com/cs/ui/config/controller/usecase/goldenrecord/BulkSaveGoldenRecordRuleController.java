package com.cs.ui.config.controller.usecase.goldenrecord;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.usecase.goldenrecord.IBulkSaveGoldenRecordRule;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
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
public class BulkSaveGoldenRecordRuleController extends BaseController
    implements IConfigController {
  
  @Autowired
  protected IBulkSaveGoldenRecordRule bulkSaveGoldenRecordRule;
  
  @RequestMapping(value = "/bulksave/goldenrecordrules", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody ArrayList<IdLabelCodeModel> ruleListModel) throws Exception
  {
    IListModel<IIdLabelCodeModel> rulelistSaveModel = new ListModel<>();
    rulelistSaveModel.setList(ruleListModel);
    return createResponse(bulkSaveGoldenRecordRule.execute(rulelistSaveModel));
  }
}
