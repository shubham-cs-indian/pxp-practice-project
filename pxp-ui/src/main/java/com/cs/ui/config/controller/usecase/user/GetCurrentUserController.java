package com.cs.ui.config.controller.usecase.user;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.usecase.user.IGetCurrentUser;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class GetCurrentUserController extends BaseController implements IConfigController {
  
  @Autowired
  ISessionContext context;
  
  @Autowired
  IGetCurrentUser getCurrentUser;
  
  @RequestMapping(value = "/currentUser", method = RequestMethod.GET)
  public IRESTModel execute() throws Exception
  {
    IIdParameterModel getEntityModel = new IdParameterModel(context.getUserId());
    return createResponse(getCurrentUser.execute(getEntityModel));
  }
}
