package com.cs.ui.config.controller.usecase.user.validation;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cs.constants.CommonConstants;
import com.cs.core.config.controller.usecase.config.IConfigController;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.model.user.IGetUserValidateModel;
import com.cs.core.config.interactor.model.user.IValidateUserRequestModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.interactor.model.user.ValidateUserRequestModel;
import com.cs.core.config.interactor.usecase.user.validation.IGetUserForValidation;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping(value = "/config")
public class ValidateUserExistenceController extends BaseController implements IConfigController {
  
  @Autowired
  protected IGetUserForValidation validateUserName;
  
  @RequestMapping(value = "/validateUser", method = RequestMethod.POST)
  public IRESTModel execute(HttpServletRequest request, @RequestBody UserModel userModel)
      throws Exception
  {
    Map<String, String> validationMap = new HashMap<>();
    validationMap.put(IUser.USER_NAME, userModel.getUserName()); 
    IValidateUserRequestModel model = new ValidateUserRequestModel();
    model.setValidationMap(validationMap);
    IGetUserValidateModel responseModel = validateUserName.execute(model);
    
    if (responseModel.getUrl() != null && responseModel.getType().equals("SAML")) {
      responseModel.setUrl(
          request.getContextPath() + responseModel.getUrl() + "&" + CommonConstants.USER_NAME_PROPERTY + "=" + responseModel.getUserName());
    }
    else {
      responseModel.setUrl(null);   
    }
    return createResponse(responseModel);
  }
}
