package com.cs.config.strategy.plugin.usecase.util;

import java.util.Map;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.user.UserValidation;
import com.tinkerpop.blueprints.Vertex;

public class ValidationUtils {
  
  // TODO : validation to be implemented properly in the future
  public static boolean validateUserInfo(Map user) throws Exception
  {
    /* boolean isValid = false;
    String userId = user.get("id").toString();
    String userName = user.get("userName").toString();
    String email = user.get("email").toString();
    String currentDate = new Date(System.currentTimeMillis()).toString();
    List<String> logMessages = new ArrayList<>();
    
    
    ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
    engine.eval(new InputStreamReader(
        ValidationUtils.class.getClassLoader().getResourceAsStream("validation.js")));
    
    engine.eval("function checkUserName(data) { return isUsernameValid(data);}");
    boolean isValidUsername = (boolean) ((Invocable) engine).invokeFunction("checkUserName",
        userName);
    
    if (isValidUsername) {
      engine.eval("function checkEmail(data) { return isEmailValid(data);}");
      boolean isValidEmail = (boolean) ((Invocable) engine).invokeFunction("checkEmail", email);
    
      if (isValidEmail) {
        isValid = true;
      }
      else {
        logMessages.add(currentDate + " User id \"" + userId + "\" invalid email : " + email);
      }
    }
    else {
      logMessages.add(currentDate + " User id \"" + userId + "\" invalid userName : " + userName);
    }
    
    if (!isValid) {
      Path path = Paths.get("logs/user/validationLog.txt");
      Files.createDirectories(path.getParent());
      File file = path.toFile();
      if (!file.exists()) {
        file.createNewFile();
      }
      Files.write(path, logMessages, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
    
      throw new InvalidDataException("Field validation failed");
    }
    
    return isValid;*/
    UserValidation.validateContactNumber((String)user.get(IUserModel.CONTACT));
    String gender = UserValidation.validateGender((String)user.get(IUserModel.GENDER));
    user.put(IUserModel.GENDER, gender);
    return true;
  }
  
  public static boolean validateTagInfo(Map tag) throws Exception
  {
    
    return true;
  }
  
  public static boolean validateUserGroupInfo(Map userGroup) throws Exception
  {
    
    return true;
  }
  
  public static boolean validateAttributeInfo(Map attribute) throws Exception
  {
    
    return true;
  }
  
  public static boolean validateKlassInfo(Map klass) throws Exception
  {
    
    return true;
  }
  
  public static boolean validateSupplierInfo(Map supplier) throws Exception
  {
    
    return true;
  }
  
  public static boolean vaildateIfStandardEntity(Vertex vertex) {
    boolean isStandard = vertex.getProperty(CommonConstants.IS_STANDARD) == null ? false : 
      vertex.getProperty(CommonConstants.IS_STANDARD);
    return isStandard;
  }
}
