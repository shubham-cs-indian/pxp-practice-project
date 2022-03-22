package com.cs.ui.config.controller.usecase.target;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.klass.AbstractKlassModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.config.interactor.usecase.target.ICreateTarget;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateTargetController extends BaseController implements IConfigController {
  
  @Autowired
  ICreateTarget createTarget;
  
  @RequestMapping(value = "/targets", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody AbstractKlassModel marketModel) throws Exception
  {
    return createResponse(createTarget.execute((ITargetModel) marketModel));
  }
}
