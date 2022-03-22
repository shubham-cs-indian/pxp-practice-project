package com.cs.base.interactor.interceptor;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.ldap.AuthenticationException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.search.LdapUserSearch;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.role.UserDoesntExistInAnyRoleException;
import com.cs.core.config.interactor.model.user.IGetUserValidateModel;
import com.cs.core.config.interactor.model.user.ILDAPValidationModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.interactor.model.user.IValidateUserRequestModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.interactor.model.user.ValidateUserRequestModel;
import com.cs.core.config.interactor.usecase.user.validation.IGetUserForValidation;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.usecase.login.IAuthenticateUser;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
  
  @Autowired
  ISessionContext       context;
  
  @Autowired
  IAuthenticateUser     authenticateUser;
  
  @Autowired
  IGetUserForValidation getUserForValidation;
  
  @Value("${system.mode}")
  protected String      mode;
  
  /*
   * @Authour: Abhaypratap Singh
   * @Description: This method is called within spring security for user authentication.
   * If user is invalid than it will throw an exception i.e. BadCredentialsException 
   * @Exception: BadCredentialsException
   * */
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException
  {
    String userName = authentication.getName();
    String password = authentication.getCredentials().toString();
    
    try {
      IGetUserValidateModel authInfo = getUserValidationModel(userName);
      
      String ssoType = authInfo.getType();
      if(ssoType != null && ssoType.equals("LDAP")) {
        String decodedPassword = passwordDecoder(password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), decodedPassword);
        getLdapAuthProvider(((ILDAPValidationModel)authInfo)).authenticate(authenticationToken);
      }
      else {
        setContext(userName, password);
      }
      context.setUserId(authInfo.getUserId());
      context.setUserName(userName);
    }
    catch (UserDoesntExistInAnyRoleException e) {
      throw new AuthenticationServiceException("User without role");
    }
    catch (IncorrectResultSizeDataAccessException e) {
      throw new InternalAuthenticationServiceException("Incorrect Size");
    }
    catch (Exception e) {
      throw new BadCredentialsException(
          "Invalid Credential. Please Enter Correct UserName and Password");
    }
    String requestId = UUID.randomUUID().toString();
    String message = "[" + requestId + "] " + "New User Login ->" + userName;
    if (mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_PRODUCTION) || mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_QA)) {
      RDBMSLogger.instance().info(message);
      
    }
    else if (mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_DEVELOPMENT)) {
      System.out.println(message);
    }
    
    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        userName, password, grantedAuthorities);
    return usernamePasswordAuthenticationToken;
  }

  private String passwordDecoder(String password) throws UnsupportedEncodingException
  {
    String osName = System.getProperty("os.name");
    String uiDecodedString = "";
    if(osName.toLowerCase().contains("windows")) {
      uiDecodedString = new String(Base64.getDecoder().decode(password));
    } else {
      uiDecodedString = new String(Base64.getDecoder().decode(password), "UTF-8");
    }
      String decodedPassword = uiDecodedString.split("::")[1];
    return decodedPassword;
  }

  private IGetUserValidateModel getUserValidationModel(String userName) throws Exception
  {
    IValidateUserRequestModel userInfo = new ValidateUserRequestModel();
    Map<String, String> validationMap = new HashMap<String, String>();
    validationMap.put(IUserModel.USER_NAME, userName);
    userInfo.setValidationMap(validationMap);
    IGetUserValidateModel returnModel =  getUserForValidation.execute(userInfo);
    return returnModel;
  }
  
  @Override
  public boolean supports(Class<?> authentication)
  {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
  
  protected void setContext(String userName, String password) throws Exception
  {
    IUserModel userModel = new UserModel();
    userModel.setUserName(userName);
    userModel.setPassword(password);
    userModel = authenticateUser.execute(userModel);
  }
  
  private DefaultSpringSecurityContextSource getContextSource(ILDAPValidationModel ldapInfo)
  {
    String url = ldapInfo.getUrl();
    String rootDn = ldapInfo.getRootDn();
    String managerDn = ldapInfo.getManagerDn();
    String managerPassword = ldapInfo.getManagerPassword();
    
    DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(url + "/" + rootDn);
    contextSource.setUserDn(managerDn);
    contextSource.setPassword(managerPassword);
    contextSource.afterPropertiesSet();
    return contextSource;
  }
  
  private LdapAuthenticationProvider getLdapAuthProvider(ILDAPValidationModel ldapInfo)
  {
    String baseDn = ldapInfo.getBaseDn();
    String searchFilter = ldapInfo.getUserIdField() + "={0}";
    DefaultSpringSecurityContextSource contextSource = getContextSource(ldapInfo);
    LdapUserSearch ldapSearch = new FilterBasedLdapUserSearch(baseDn,searchFilter,contextSource) ;
    BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource);
    bindAuthenticator.setUserSearch(ldapSearch);
    LdapAuthenticationProvider ldapAuthProvider = new LdapAuthenticationProvider(bindAuthenticator);
    return ldapAuthProvider;
  }
  
}
