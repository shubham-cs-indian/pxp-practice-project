package com.cs.core.listener;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO.LogoutType;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.usersession.IUserSessionModel;
import com.cs.core.runtime.interactor.model.usersession.UserSessionModel;
import com.cs.core.runtime.interactor.usecase.login.ILoginUser;
import com.cs.core.runtime.interactor.usecase.logout.ILogOutUser;
import com.cs.core.runtime.interactor.usecase.userSessionTermination.IUserSessionTermination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class ApplicationHttpSessionListner implements HttpSessionListener, ServletContextListener {
  
  @Autowired
  ILogOutUser             logOutUser;
  
  @Autowired
  IUserSessionTermination userSessionTermination;
  
  @Autowired
  ISessionContext         context;
  
  @Autowired
  ILoginUser              loginUser;
  
  @Override
  public void sessionCreated(HttpSessionEvent se)
  {
  }
  
  @Override
  public void sessionDestroyed(HttpSessionEvent se)
  {
    try {
      HttpSession session = se.getSession();
      String userId = (String) session.getAttribute(ISessionContext.USER_ID);
      if (userId != null) {
        IUserSessionModel model = new UserSessionModel();
        model.setSessionId(session.getId());
        model.setUserID(userId);
        LogoutType logoutType = (LogoutType) session.getAttribute("logoutType");
        if (logoutType == null) {
          logoutType = LogoutType.TIMEOUT;
        }
        model.setLogoutType(logoutType);
        logOutUser.execute(model);
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
  }
  
  @Override
  public void contextInitialized(ServletContextEvent sce)
  {
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
  }
  
  @Override
  public void contextDestroyed(ServletContextEvent sce)
  {
    try {
      userSessionTermination.execute(null);
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
  }
}
