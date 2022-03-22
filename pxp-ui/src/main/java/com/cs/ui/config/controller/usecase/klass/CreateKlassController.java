package com.cs.ui.config.controller.usecase.klass;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.klass.AbstractKlassModel;
import com.cs.core.config.interactor.usecase.klass.ICreateKlass;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateKlassController extends BaseController implements IConfigController {
  
  @Autowired
  ICreateKlass createKlass;
  
  @RequestMapping(value = "/klasses", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody AbstractKlassModel contentKlassModel) throws Exception
  {
    return createResponse(createKlass.execute(contentKlassModel));
  }
}
