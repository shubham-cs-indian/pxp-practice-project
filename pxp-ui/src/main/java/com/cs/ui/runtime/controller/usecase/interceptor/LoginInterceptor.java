package com.cs.ui.runtime.controller.usecase.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.jsoup.Connection.Method;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.NameID;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.impl.XSAnyImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.providers.ExpiringUsernameAuthenticationToken;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.exception.role.UserDoesntExistInAnyRoleException;
import com.cs.core.config.interactor.model.user.ISAMLValidationModel;
import com.cs.core.config.interactor.model.user.IValidateUserRequestModel;
import com.cs.core.config.interactor.model.user.ValidateUserRequestModel;
import com.cs.core.config.interactor.usecase.user.validation.IGetUserForValidation;
import com.cs.core.exception.SSOVerificationFailureException;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO.LogoutType;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.exception.configuration.InvalidCredentialsException;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.usersession.IUserSessionModel;
import com.cs.core.runtime.interactor.model.usersession.UserSessionModel;
import com.cs.core.runtime.interactor.usecase.logout.ILogOutUser;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
  
  @Autowired
  protected ISessionContext       context;
  
  @Autowired
  protected TransactionThreadData transactionThread;
  
  @Value("${system.mode}")
  protected String                mode;
  
  @Autowired
  IGetUserForValidation           getUserForValidation;
  
  @Resource
  Map<String, String>             ssoUserAuthenticationMap;
  
  @Autowired
  SessionRegistry                 sessionRegistry;
  
  @Autowired
  ILogOutUser                     logOutUser;
  
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
  {
    processAuthenticationRequest(request);
    
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP
    // 1.1.
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
    response.setHeader("Expires", "0");
    String requestId = request.getParameter("requestId");
    if (requestId == null) {
      requestId = UUID.randomUUID().toString();
    }
    
    // For SSO Validation Process
    try {
      getPathOfRequest(request);
    }
    catch (SSOVerificationFailureException e) {
      return unauthAccessHandler(request, response, requestId, "SSOVerificationFailureException");
    }
    catch (Exception e) {
      return unauthAccessHandler(request, response, requestId, e.getClass().getSimpleName());
    }
    
    HttpSession session = request.getSession(false);
    if (checkforExpiredSessionAndInvalidate(response, session)) {
      return false;
    }
    
    String userName = request.getParameter("userName");
    if (session == null || context == null || context.getUserId() == null) {
      // login request
      if (userName != null) {
        return true;
      }
      else {
        return unauthAccessHandler(request, response, requestId);
      }
    }
    else // when session exists
    {
      fillTransactionData(request, requestId);
      return true;
    }
  }

  private void fillTransactionData(HttpServletRequest request, String requestId)
  {
    TransactionData transactionData = transactionThread.getTransactionData();
    transactionData.setRequestId(requestId.toString());
    String id = UUID.randomUUID().toString();
    transactionData.setId(id);
    if (context.getUserSessionDTO() != null) {
      context.getUserSessionDTO().setTransactionId(id);
    }
    transactionData.setUserId(context.getUserId());
    transactionData.setUserName(context.getUserName());
    transactionData.setUseCase(request.getScheme() + "://" + // "http" + "://
        request.getServerName() + // "myhost"
        ":" + // ":"
        request.getServerPort() + // "8080"
        request.getRequestURI() + // "/people"
        "?" + // "?"
        request.getQueryString());
    transactionData.setRequestMethod(request.getMethod());
    transactionData.setStartTime(System.currentTimeMillis());
    transactionData.setExecutionStatus("pending");
    transactionData.setUiLanguage(request.getParameter("lang"));
    transactionData.setDataLanguage(request.getParameter(CommonConstants.DATA_LANGUAGE));
    transactionData.setEndpointId(request.getParameter(CommonConstants.ENDPOINT_ID));
    transactionData.setOrganizationId(request.getParameter(CommonConstants.ORGANIZATION_ID));
    transactionData.setPhysicalCatalogId(request.getParameter(CommonConstants.PHYSICAL_CATALOG_ID));
    transactionData.setPortalId(request.getParameter(CommonConstants.PORTAL_ID));
    transactionData.setEndpointType(request.getParameter(IEndpoint.ENDPOINT_TYPE));
    transactionData.setUrlForTalend(request.getScheme() + "://" + // "http" +
                                                                  // "://
        request.getServerName() + // "myhost"
        ":" + // ":"
        request.getServerPort() + // "8080"
        request.getContextPath() + "/");
  }
  
  private Boolean checkforExpiredSessionAndInvalidate(HttpServletResponse response, HttpSession session) throws RDBMSException, Exception
  {
    Boolean flag = false;
    if (session != null) {
      SessionInformation sessionInfo = sessionRegistry.getSessionInformation(session.getId());
      if (sessionInfo != null && sessionInfo.isExpired()) {
        IUserSessionModel model = new UserSessionModel();
        model.setSessionId(session.getId());
        model.setLogoutType(LogoutType.KILLED);
        logOutUser.execute(model);
        sessionRegistry.removeSessionInformation(session.getId());
        SecurityContextHolder.clearContext();
        session.invalidate();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("roleException", "roleException");
        flag = true;
      }
    }
    
    return flag;
  }
  
  private boolean unauthAccessHandler(HttpServletRequest request, HttpServletResponse response, String requestId)
      throws ServletException, IOException
  {
    return unauthAccessHandler(request, response, requestId, "");
  }
  
  private boolean unauthAccessHandler(HttpServletRequest request, HttpServletResponse response, String requestId, String exception)
      throws ServletException, IOException
  {
    String clientIP = getClientIp(request);
    String message = "################### Unauthorized Requestor : " + clientIP;
    if (mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_PRODUCTION) || mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_QA)) {
      RDBMSLogger.instance().info(message);
      
    }
    else if (mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_DEVELOPMENT)) {
      System.out.println(message);
    }
    
    boolean isAjaxRequest = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    if (!isAjaxRequest) {
      String theme = request.getParameter("theme");
      if (theme != null) {
        context.setTheme(theme);
      }
      
      if (request.getRequestURI().contains("/asset/Document/")) {
        // only for file download request
        response.sendRedirect(request.getContextPath() + "/getlogin");
      }
      else {
        redirectToLoginPage(response, exception);
      }
    }
    else {
      response.sendError(401);
    }
    return false;
  }
  
  private void processAuthenticationRequest(HttpServletRequest request)
      throws InvalidCredentialsException, UserDoesntExistInAnyRoleException
  {
    String authFailureParameter = request.getParameter(CommonConstants.AUTH_FAILURE);
    String userWithoutRoleParameter = request.getParameter(CommonConstants.USER_WITHOUT_ROLE);
    String incorrectDataSize = request.getParameter(CommonConstants.INCORRECT_DATA_SIZE);
    if (authFailureParameter != null && authFailureParameter.equals(Boolean.TRUE.toString())) {
      throw new InvalidCredentialsException();
    }
    if (userWithoutRoleParameter != null && userWithoutRoleParameter.equals(Boolean.TRUE.toString())) {
      throw new UserDoesntExistInAnyRoleException();
    }
    if (incorrectDataSize != null && incorrectDataSize.equals(Boolean.TRUE.toString())) {
      throw new IncorrectResultSizeDataAccessException("IncorrectDataSize", 1);
    }
  }
  
  private void getPathOfRequest(HttpServletRequest request) throws Exception
  {
    String requestURI = request.getRequestURI().trim();
    String methodType = request.getMethod();
    
    String[] split = requestURI.split("/");
    if (split.length > 2) {
      if (split[2].equals(Constants.SSO_VERIFICATION_PATH) && methodType.equals(Method.GET.toString())) {
        ExpiringUsernameAuthenticationToken principal = (ExpiringUsernameAuthenticationToken) request.getUserPrincipal();
        SAMLCredential credentials = (SAMLCredential) principal.getCredentials();
        String remoteEntityID = credentials.getRemoteEntityID();
        
        Boolean isValid = true;
        String clientIp = getClientIp(request);
        Map<String, String> validationMap = new HashMap<>();
        
        for (Entry<String, String> entry : ssoUserAuthenticationMap.entrySet()) {
          switch (entry.getKey()) {
            case NameID.DEFAULT_ELEMENT_LOCAL_NAME:
              String nameID = credentials.getNameID().getValue();
              validationMap.put(entry.getValue(), nameID);
              break;
            
            default:
            {
              Attribute attribute = credentials.getAttribute(entry.getKey());
              if (!attribute.isNil()) {
                List<XMLObject> attributeValues = attribute.getAttributeValues();
                for (XMLObject xmlObject : attributeValues) {
                  if (xmlObject instanceof XSAnyImpl) {
                    String value = ((XSAnyImpl) xmlObject).getTextContent();
                    validationMap.put(entry.getValue(), value);
                    break;
                  }
                }
              }
            }
          }
          
          isValid = validationProcess(remoteEntityID, validationMap, clientIp);
          if (!isValid) {
            throw new SSOVerificationFailureException();
          }
        }
      }
    }
  }
  
  private Boolean validationProcess(String remoteEntityID, Map<String, String> validationMap, String clientIp)
      throws Exception, URISyntaxException
  {
    IValidateUserRequestModel validationBySSOUser = new ValidateUserRequestModel();
    validationBySSOUser.setValidationMap(validationMap);
    
    ISAMLValidationModel userValidationModel;
    try {
      userValidationModel = (ISAMLValidationModel) getUserForValidation.execute(validationBySSOUser);
    }
    catch (Exception e)// e-> UserNotFound, ClassCast etc(Both can occur due to
                       // inappropriate setting of claim policy)
    {
      return false;
    }
    
    String ssoUrl = userValidationModel.getIpAddress();
    if (validateRemoteRequest(remoteEntityID, ssoUrl)) {
      context.setUserId(userValidationModel.getUserId());
      context.setUserName(userValidationModel.getUserName());
      context.setClientIPAddress(clientIp);
      return true;
    }
    return false;
  }
  
  private boolean validateRemoteRequest(String remoteEntityID, String ssoUrl) throws URISyntaxException
  {
    URI uriRemote = new URI(remoteEntityID.toLowerCase());
    URI uriSso = new URI(ssoUrl.toLowerCase());
    if ((uriRemote.getHost() != null && uriRemote.getHost().equals(uriSso.getHost()))) {
      return true;
    }
    return false;
  }
  
  private void redirectToLoginPage(HttpServletResponse response, String exception) throws IOException, ServletException
  {
    PrintWriter out = response.getWriter();
    response.setContentType("text/html");
    
    VelocityEngine ve = new VelocityEngine();
    ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
    ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
    
    ve.init();
    
    Template template = null;
    template = ve.getTemplate("login-start.html");
    
    VelocityContext context = new VelocityContext();
    
    context.put("exp", exception);
    
    Writer writer = new StringWriter();
    template.merge(context, writer);
    
    out.print(writer.toString());
  }
  
  private static String getClientIp(HttpServletRequest request) throws UnknownHostException
  {
    
    String remoteAddr = "";
    
    if (request != null) {
      remoteAddr = request.getHeader("X-FORWARDED-FOR");
      if (remoteAddr == null || "".equals(remoteAddr)) {
        remoteAddr = request.getRemoteAddr();
      }
    }
    if (remoteAddr.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
      InetAddress inetAddress = InetAddress.getLocalHost();
      String ipAddress = inetAddress.getHostAddress();
      remoteAddr = ipAddress;
    }
    
    return remoteAddr;
  }
  
}
