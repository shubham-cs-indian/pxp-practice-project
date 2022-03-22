package com.cs.ui.config.controller.usecase.causeeffectrule;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.causeeffectrule.CauseEffectRulesModel;
import com.cs.core.config.interactor.usecase.causeeffectrule.ISaveCauseEffectRule;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveCauseEffectRuleController extends BaseController implements IConfigController {
  
  @Autowired
  ISaveCauseEffectRule saveCauseEffectRule;
  
  @RequestMapping(value = "/causeeffectrule", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody CauseEffectRulesModel causeEffectRuleModel)
      throws Exception
  {
    return createResponse(saveCauseEffectRule.execute(causeEffectRuleModel));
  }
}
