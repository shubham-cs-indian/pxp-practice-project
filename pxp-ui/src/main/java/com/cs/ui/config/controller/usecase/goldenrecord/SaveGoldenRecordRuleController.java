package com.cs.ui.config.controller.usecase.goldenrecord;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.goldenrecord.SaveGoldenRecordRuleModel;
import com.cs.core.config.interactor.usecase.goldenrecord.ISaveGoldenRecordRule;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveGoldenRecordRuleController extends BaseController implements IConfigController {
  
  @Autowired
  protected ISaveGoldenRecordRule saveGoldenRecordRule;
  
  @RequestMapping(value = "/goldenrecordrule", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody SaveGoldenRecordRuleModel ruleModel) throws Exception
  {
    return createResponse(saveGoldenRecordRule.execute(ruleModel));
  }
}
