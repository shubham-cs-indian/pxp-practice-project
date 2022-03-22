package com.cs.ui.config.controller.usecase.user;

import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.interactor.usecase.user.IGetUserByUserName;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/config")
public class ValidateUserNameController extends BaseController implements IConfigController {
  
  @Autowired
  IGetUserByUserName validateUserName;
  
  @RequestMapping(value = "/checkUsers", method = RequestMethod.POST)
  public IRESTModel execute(@RequestBody UserModel userModel) throws Exception
  {
    
    return createResponse(validateUserName.execute(userModel));
  }
  
  // @RequestMapping(value = "/getUserByUserName", method = RequestMethod.POST)
  public IRESTModel executeGetUserByUserName(@RequestBody UserModel userModel) throws Exception
  {
    
    return createResponse(validateUserName.execute(userModel));
  }
}
