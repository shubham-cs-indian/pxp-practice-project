package com.cs.ui.runtime.controller.usecase.login;

import com.cs.core.rdbms.tracking.idto.IUserSessionDTO.LogoutType;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.usersession.IUserSessionModel;
import com.cs.core.runtime.interactor.model.usersession.UserSessionModel;
import com.cs.core.runtime.interactor.usecase.logout.ILogOutUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class LogoutController {
  
  @Autowired
  private ISessionContext context;
  
  @Autowired
  ILogOutUser             logOutUser;
  
  @RequestMapping(value = "/logout")
  public void redirect(HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    HttpSession session = request.getSession(false);
    UUID requestId = (UUID) request.getAttribute("requestId");
    System.out.println("[" + requestId + "] " + "Attempt to logout");
    
    context.setUserName(null);
    context.setUserId(null);
    session.setAttribute(
        "org.springframework.web.context.request.ServletRequestAttributes.DESTRUCTION_CALLBACK.scopedTarget.context",
        null);
    session.setAttribute("logoutType", LogoutType.NORMAL);
    updateTableOnLogout(session);
    session.invalidate();
  }
  
  public void updateTableOnLogout(HttpSession session) throws Exception {
		IUserSessionModel model = new UserSessionModel();
		model.setSessionId(session.getId());
		LogoutType logoutType = (LogoutType) session.getAttribute("logoutType");
		if (logoutType == null) {
			logoutType = LogoutType.TIMEOUT;
		}
		model.setLogoutType(logoutType);
		logOutUser.execute(model);
  }
}
