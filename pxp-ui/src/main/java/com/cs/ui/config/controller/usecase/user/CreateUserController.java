package com.cs.ui.config.controller.usecase.user;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.interactor.usecase.user.ICreateUser;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class CreateUserController extends BaseController implements IConfigController {
  
  @Autowired
  ICreateUser createUser;
  
  @RequestMapping(value = "/users", method = RequestMethod.PUT)
  public IRESTModel execute(@RequestBody UserModel userModel) throws Exception
  {
    
    return createResponse(createUser.execute(userModel));
  }
}
