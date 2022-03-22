package com.cs.ui.config.controller.usecase.causeeffectrule;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.causeeffectrule.IDeleteCauseEffectRule;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class DeleteCauseEffectRuleController extends BaseController implements IConfigController {
  
  @Autowired
  IDeleteCauseEffectRule deleteCauseEffectRule;
  
  @RequestMapping(value = "/causeeffectrule", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody IdsListParameterModel bulkIdsModel) throws Exception
  {
    return createResponse(deleteCauseEffectRule.execute(bulkIdsModel));
  }
}
