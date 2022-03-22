package com.cs.core.config.strategy.usecase.user;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.user.User;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.runtime.interactor.exception.configuration.InvalidCredentialsException;
import com.cs.core.runtime.strategy.utils.AuthUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("authenticateUserStrategy")
public class OrientDBAuthenticateUserStrategy extends OrientDBBaseStrategy
    implements IAuthenticateUserStrategy {
  
  public static final String useCase = "AuthenticateUser";
  
  @Resource(name = "cookies")
  protected List<String>     cookies;
  
  @Value("${system.mode}")
  protected String                mode;
  
  @Override
  public IUserModel execute(IUserModel model) throws Exception
  {
    if (model.getUserName() == null || model.getPassword() == null) {
      log("BLANK USERNAME OR PASSWORD");
      throw new Exception("authentication failed");
    }
    
    log("Authenticating User.......");
    String userName = model.getUserName();
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("username", userName);
    User foundUser = execute(useCase, requestMap, User.class);
    if (foundUser != null) {
      String uiEncodedPasswordToken = model.getPassword();
      String osName = System.getProperty("os.name");
      String uiDecodedString = "";
      if (osName.toLowerCase()
          .contains("windows")) {
        uiDecodedString = new String(Base64.getDecoder()
            .decode(uiEncodedPasswordToken));
      }
      else {
        uiDecodedString = new String(Base64.getDecoder()
            .decode(uiEncodedPasswordToken), "UTF-8");
      }
      
      try {
        String password = uiDecodedString.split("::")[1];
        if (AuthUtils.check(password, foundUser.getPassword())) {
          UserModel userModel = new UserModel(foundUser);
          
          return userModel;
        }
        
      }
      catch (ArrayIndexOutOfBoundsException e) {
        // Will throw exception "InvalidCredentialsException" at the end of the
        // function,
        // one of the reasons can be password field left empty.
      }
    }
    else {
      log("USER NOT FOUND! of USERNAME "+ userName);
    }
    
    throw new InvalidCredentialsException();
  }


  private void log(String message)
  {
    RDBMSLogger logger = RDBMSLogger.instance();
    if (mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_PRODUCTION) || mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_QA)) {
      logger.info(message);
    }
    else if (mode.equalsIgnoreCase(CommonConstants.SYSTEM_MODE_DEVELOPMENT)) {
      System.out.println(message);
    }
  }
}
