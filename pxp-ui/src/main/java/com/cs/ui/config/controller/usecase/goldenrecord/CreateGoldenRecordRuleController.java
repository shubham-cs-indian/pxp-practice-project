package com.cs.ui.config.controller.usecase.goldenrecord;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.goldenrecord.GoldenRecordRuleModel;
import com.cs.core.config.interactor.usecase.goldenrecord.ICreateGoldenRecordRule;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateGoldenRecordRuleController extends BaseController implements IConfigController {
  
  @Autowired
  protected ICreateGoldenRecordRule createGoldenRecordRule;
  
  @RequestMapping(value = "/goldenrecordrule", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody GoldenRecordRuleModel ruleModel) throws Exception
  {
    return createResponse(createGoldenRecordRule.execute(ruleModel));
  }
}
