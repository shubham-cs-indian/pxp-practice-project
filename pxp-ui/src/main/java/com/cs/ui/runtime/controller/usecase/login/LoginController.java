package com.cs.ui.runtime.controller.usecase.login;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cs.constants.CommonConstants;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.usersession.IUserSessionModel;
import com.cs.core.runtime.interactor.model.usersession.UserSessionModel;
import com.cs.core.runtime.interactor.usecase.login.IAuthenticateUser;
import com.cs.core.runtime.interactor.usecase.login.ILoginUser;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@Controller
public class LoginController extends BaseController {
  
  @Autowired
  ISessionContext   context;
  
  @Autowired
  IAuthenticateUser authenticateUser;
  
  @Autowired
  ILoginUser        loginUser;
  
  @Value("${system.mode}")
  protected String  mode;
  
  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public void redirectGet(HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    Map<String, String> data = new HashMap<String, String>();
    String theme = context.getTheme();
    HttpSession session = request.getSession(false);
    String sessionId = session.getId();
    String clientIpAddress = getClientIpAddress(request);
    context.setSessionId(sessionId);
    context.setClientIPAddress(clientIpAddress);
    setContext(context.getUserName(), context.getUserId(), sessionId, clientIpAddress);
    String url = request.getContextPath() + "/?login=" + sessionId;
    url += theme != null ? ("&theme=" + theme) : "";
    data.put("url", url);
    String json = ObjectMapperUtil.writeValueAsString(data);
    response.getWriter()
        .write(json);
    response.setStatus(response.SC_OK);
  }
  
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public String redirectPost(HttpServletRequest request, HttpServletResponse response)
      throws Exception
  {
    // Destroy the session because user is not authenticated yet.
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    
    String requestId = request.getParameter("requestId");
    String userName = request.getParameter("username");
    String password = request.getParameter("password");
    String sessionId = request.getSession()
        .getId();
    String clientIpAddress = getClientIpAddress(request);
    setContext(userName, password, sessionId, clientIpAddress);
    System.out.println("[" + requestId + "] " + "New User Login ->" + userName);
    return "redirect:/login";
  }
  
  private String getClientIpAddress(HttpServletRequest request) throws UnknownHostException
  {
    String remoteAddress = "";
    if (request != null) {
      remoteAddress = request.getHeader("X-FORWARDED-FOR");
      if (remoteAddress == null || "".equals(remoteAddress)) {
        remoteAddress = request.getRemoteAddr();
      }
    }
    
    if (remoteAddress.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
      InetAddress inetAddress = InetAddress.getLocalHost();
      String ipAddress = inetAddress.getHostAddress();
      remoteAddress = ipAddress;
    }
    
    return remoteAddress;
  }
  
  @RequestMapping(value = "/loginImport", method = RequestMethod.POST)
  public @ResponseBody String redirectPostImport(HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {
    UUID requestId = (UUID) request.getAttribute("requestId");
    String userName = request.getParameter("username");
    String password = request.getParameter("password");
    String sessionId = request.getSession()
        .getId();
    String clientIPAddress = getClientIpAddress(request);
    setContext(userName, password, sessionId, clientIPAddress);
    
    System.out.println();
    String message = "[" + requestId + "] " + "New User Login ->" + userName;
    if (mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_PRODUCTION) || mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_QA)) {
      RDBMSLogger.instance().info(message);
      
    }
    else if (mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_DEVELOPMENT)) {
      System.out.println(message);
    }
    Cookie myCookie = new Cookie("jSessionId", request.getSession()
        .getId());
    myCookie.setSecure(false); // determines whether the cookie should only be
                               // sent using a secure protocol, such
    // as HTTPS or SSL
    
    myCookie.setMaxAge(999999); // A negative value means that the cookie is not
                                // stored persistently and will be
    // deleted when the Web browser exits. A zero value causes the cookie to be
    // deleted.
    
    myCookie.setPath("/"); // The cookie is visible to all the pages in the
                           // directory you specify, and all the
    // pages in that directory's subdirectories
    
    response.setHeader("Set-Cookie", "JSESSIONID=" + request.getSession()
        .getId());
    
    // CookieStore cookieStore = new BasicCookieStore();
    // BasicClientCookie cookie = new BasicClientCookie("JSESSIONID",
    // getSessionId());
    
    // cookie.setDomain("your domain");
    // cookie.setPath("/");
    
    // cookieStore.addCookie(cookie);
    
    return "TEST";
  }
  
  @RequestMapping(value = "/loginPage", method = RequestMethod.GET)
  public String redirectLoginPage(HttpServletRequest request, HttpServletResponse response)
      throws Exception
  {
    // Boolean debugMode = Boolean.valueOf(request.getParameter("debugMode"));
    // System.out.println("debugMode : " + debugMode);

    return "/login/build/index.html";
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
