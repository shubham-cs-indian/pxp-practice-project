
package com.cs.ui.runtime.controller.usecase.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.usersession.IUserSessionModel;
import com.cs.core.runtime.interactor.model.usersession.UserSessionModel;
import com.cs.core.runtime.interactor.usecase.login.ILoginUser;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;
/*
 * @Authour: Abhaypratap Singh
 * @Description: After successful authentication from IDP, it redirects to this url.
 * */
@Controller
public class SSOAuthenticationVerificationController extends BaseController {
  
  @Autowired
  protected ISessionContext context;
  
  @Autowired
  ILoginUser        loginUser;
  
  @RequestMapping(value = Constants.SSO_VERIFICATION, method = RequestMethod.GET)
  public String testGet(HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String sessionId = request.getSession().getId();
    context.setSessionId(sessionId);
    setContext(context.getUserName(), context.getUserId(), sessionId, context.getClientIPAddress());
    return "redirect:/";
  }
  
  protected void setContext(String userName, String userId, String sessionId, String clientIPAddress) throws Exception
  {
    IUserSessionModel userSessionModel = new UserSessionModel();
    userSessionModel.setUserID(userId);
    userSessionModel.setUserName(userName);
    userSessionModel.setSessionId(sessionId);
    loginUser.execute(userSessionModel);
  }
}
