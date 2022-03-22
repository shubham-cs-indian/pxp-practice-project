package com.cs.ui.config.controller.usecase.state;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.state.StateModel;
import com.cs.core.config.interactor.usecase.state.ISaveState;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveStateController extends BaseController implements IConfigController {
  
  @Autowired
  ISaveState saveState;
  
  @RequestMapping(value = "/state", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody StateModel conditionModel) throws Exception
  {
    return createResponse(saveState.execute(conditionModel));
  }
}
