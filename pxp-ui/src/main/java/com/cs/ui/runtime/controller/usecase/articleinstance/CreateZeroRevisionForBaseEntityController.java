package com.cs.ui.runtime.controller.usecase.articleinstance;

import com.cs.core.config.controller.usecase.config.IRuntimeController;
import com.cs.core.runtime.imprt.revisioncreation.ICreateZeroRevisionForBaseEntity;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/runtime")
public class CreateZeroRevisionForBaseEntityController extends BaseController implements IRuntimeController {
  
  @Autowired
  protected ICreateZeroRevisionForBaseEntity createZeroRevisionForBaseEntity;
  
  @RequestMapping(value = "/revision_creation", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    return createResponse(createZeroRevisionForBaseEntity.execute(new VoidModel()));
  }
}
