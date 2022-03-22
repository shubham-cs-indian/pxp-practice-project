package com.cs.ui.config.controller.usecase.target;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.klass.AbstractKlassSaveModel;
import com.cs.core.config.interactor.model.target.ITargetKlassSaveModel;
import com.cs.core.config.interactor.usecase.target.ISaveTarget;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class SaveTargetController extends BaseController implements IConfigController {
  
  @Autowired
  ISaveTarget saveTarget;
  
  @RequestMapping(value = "/targets", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody AbstractKlassSaveModel targetModel) throws Exception
  {
    return createResponse(saveTarget.execute((ITargetKlassSaveModel) targetModel));
  }
}
