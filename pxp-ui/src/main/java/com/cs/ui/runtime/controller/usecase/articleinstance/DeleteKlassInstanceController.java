package com.cs.ui.runtime.controller.usecase.articleinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.config.interactor.model.klass.DeleteKlassInstanceRequestModel;
import com.cs.core.runtime.interactor.usecase.klass.IDeleteKlassInstance;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class DeleteKlassInstanceController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected IDeleteKlassInstance deleteKlassInstance;
  
  @RequestMapping(value = "/instances", method = RequestMethod.DELETE)
  public IRESTModel execute(@RequestBody DeleteKlassInstanceRequestModel model) throws Exception
  {
    return createResponse(deleteKlassInstance.execute(model));
  }
}
