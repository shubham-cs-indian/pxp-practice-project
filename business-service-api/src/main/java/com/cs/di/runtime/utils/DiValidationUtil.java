package com.cs.di.runtime.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cs.di.workflow.constants.FileType;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.WorkflowTaskModel;

/**
 * This class work as generic validation utility for all Input validates
 * depending on DataType and Required/Optional field
 * 
 * @author mayuri.wankhade
 *
 */
public class DiValidationUtil {
  
  public static final String  IPADDRESS_PATTERN    = "^(tcp://|tcps://|ssl://)?([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
      + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
  
  public static final String  EMAIL_REGEX          = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
  public static final Pattern EMAIL_PATTERN        = Pattern.compile(EMAIL_REGEX);
  public static final String  PHONE_NUMBER_REGEX   = "^[0-9@\\s\\()\\[\\]\\{}:#&\"\\+\\-\\*!^_%$]+$";
  public static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER_REGEX);
  
  /**
   * Method Validates all mandatory string Input and set error back to model
   * 
   * @param model
   * @param paramName
   * @return
   */
  public static String validateAndGetRequiredString(WorkflowTaskModel model, String paramName)
  {
    Object param = model.getInputParameters().get(paramName);
    if (param == null) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003, new String[] { paramName });
      return null;
    }
    else if (param instanceof String && ((String) param).isEmpty()) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004, new String[] { paramName });
      return null;
    }
    else if (!(param instanceof String)) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005, new String[] { paramName });
    }
    return param.toString();
  }
  
  /**
   * Validate and return Enum type
   * 
   * @param <T>
   * @param model
   * @param paramName
   * @param enumType
   * @return
   */
  public static <T extends Enum<T>> T validateAndGetRequiredEnum(WorkflowTaskModel model, String paramName, Class<T> enumType)
  {
    Object param = model.getInputParameters().get(paramName);
    if (param == null) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003, new String[] { paramName });
      return null;
    }
    return (T) Enum.valueOf(enumType, param.toString().toUpperCase());
  }
  
  /**
   * Method Validates all Optional string Input and set Warning back to model
   * 
   * @param model
   * @param paramName
   * @return
   */
  public static String validateAndGetOptionalString(WorkflowTaskModel model, String paramName)
  {
    Object param = model.getInputParameters().get(paramName);
    if (param == null) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003,
          new String[] { paramName });
      return null;
    }
    else if (param instanceof String && ((String) param).isEmpty()) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004,
          new String[] { paramName });
      return null;
    }
    else if (!(param instanceof String)) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005,
          new String[] { paramName });
    }
    return param.toString();
  }
  
  /**
   * Method Validates all mandatory Collection Input and set error back to model
   * 
   * @param model
   * @param paramName
   * @return
   */
  public static Collection<?> validateAndGetRequiredCollection(WorkflowTaskModel model, String paramName)
  {
    Object param = model.getInputParameters().get(paramName);
    if (param == null) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003, new String[] { paramName });
      return null;
    }
    else if (param instanceof Collection<?> && ((Collection<?>) param).isEmpty()) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004, new String[] { paramName });
      return null;
    }
    else if (!(param instanceof Collection<?>)) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005, new String[] { paramName });
      return null;
    }
    return (Collection<?>) param;
  }
  
  /**
   * Method Validates all mandatory Collection Input and set error back to model
   * Empty collection is allowed in this case
   * 
   * @param model
   * @param paramName
   * @return
   */
  public static Collection<?> validateAndGetRequiredCollectionAllowEmpty(WorkflowTaskModel model, String paramName)
  {
    Object param = model.getInputParameters().get(paramName);
    if (param == null) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003, new String[] { paramName });
      return null;
    }
    else if (!(param instanceof Collection<?>)) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005, new String[] { paramName });
      return null;
    }
    return (Collection<?>) param;
  }
  
  /**
   * Method Validates all mandatory Map Input and set error back to model
   * 
   * @param model
   * @param paramName
   * @return
   */
  public static Map<?, ?> validateAndGetRequiredMapAllowEmpty(WorkflowTaskModel model, String paramName)
  {
    Object param = model.getInputParameters().get(paramName);
    if (param == null) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003, new String[] { paramName });
      return null;
    }
    else if (!(param instanceof Map<?, ?>)) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005, new String[] { paramName });
      return null;
    }
    return (Map<?, ?>) param;
  }

  
  /**
   * Method Validates all mandatory Collection Input and set warning back to
   * model
   * 
   * @param model
   * @param paramName
   * @return
   */
  public static Collection<?> validateAndGetOptionalCollection(WorkflowTaskModel model, String paramName)
  {
    Object param = model.getInputParameters().get(paramName);
    if (param == null) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003,
          new String[] { paramName });
      return null;
    }
    else if (param instanceof Collection<?> && ((Collection<?>) param).isEmpty()) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004,
          new String[] { paramName });
      return null;
    }
    else if (!(param instanceof Collection<?>)) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005,
          new String[] { paramName });
      return null;
    }
    return (Collection<?>) param;
  }
  
  /**
   * Method Validates all mandatory Folder Path Input and set error back to
   * model
   * 
   * @param model
   * @param paramName
   * @return
   */
  public static Object validateAndGetRequiredFolder(WorkflowTaskModel model, String paramName)
  {
    Object param = model.getInputParameters().get(paramName);
    if (param == null) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003, new String[] { paramName });
      return null;
    }
    if (param instanceof String && param.toString().isEmpty()) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004, new String[] { paramName });
      return null;
    }
    if (!Files.exists(Paths.get(param.toString()))) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005, new String[] { paramName });
      return null;
    }
    if (!Files.isDirectory(Paths.get(param.toString()))) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM, ObjectCode.FILE_TYPE }, MessageCode.GEN021,
          new String[] { paramName, FileType.DIRECTORY.name() });
      return null;
    }
    return param;
  }
  
  /**
   * Method Validates all optional Folder Path Input and set warning back to
   * model
   * 
   * @param model
   * @param paramName
   * @return
   */
  public static Object validateAndGetOptionalFolder(WorkflowTaskModel model, String paramName)
  {
    Object param = model.getInputParameters().get(paramName);
    if (param == null) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003,
          new String[] { paramName });
      return null;
    }
    if (param instanceof String && param.toString().isEmpty()) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004,
          new String[] { paramName });
      return null;
    }
    if (!Files.exists(Paths.get(param.toString()))) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005,
          new String[] { paramName });
      return null;
    }
    if (!Files.isDirectory(Paths.get(param.toString()))) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM, ObjectCode.FILE_TYPE }, MessageCode.GEN021,
          new String[] { paramName, FileType.DIRECTORY.name() });
      return null;
    }
    return param;
  }
  
  /**
   * Method Validates all mandatory Boolean Input and set error back to model
   * 
   * @param model
   * @param paramName
   * @return
   */
  public static Boolean validateAndGetOptionalBoolean(WorkflowTaskModel model, String paramName)
  {
    Object param = model.getInputParameters().get(paramName);
    if (param == null) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003,
          new String[] { paramName });
      return null;
    }
    else if (param instanceof Boolean) {
      return (Boolean) param;
    }
    else if (param instanceof String) {
      if (param.toString().equalsIgnoreCase("true"))
        return true;
      else if (param.toString().equalsIgnoreCase("false"))
        return false;
    }
    model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005, new String[] { paramName });
    return null;
  }
  
  /**
   * Method Validates all optional Boolean Input and set error back to model
   * 
   * @param model
   * @param paramName
   * @return
   */
  public static Boolean validateAndGetRequiredBoolean(WorkflowTaskModel model, String paramName)
  {
    Object param = model.getInputParameters().get(paramName);
    if (param == null) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003, new String[] { paramName });
      return null;
    }
    else if (param instanceof Boolean) {
      return (Boolean) param;
    }
    else if (param instanceof String) {
      if (param.toString().equalsIgnoreCase("true"))
        return true;
      else if (param.toString().equalsIgnoreCase("false"))
        return false;
    }
    model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005, new String[] { paramName });
    return null;
  }
  
  /**
   * Method Validates all mandatory Map Input and set error back to model
   * 
   * @param model
   * @param paramName
   * @return
   */
  public static Map<?, ?> validateAndGetRequiredMap(WorkflowTaskModel model, String paramName)
  {
    Object param = model.getInputParameters().get(paramName);
    if (param == null) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003, new String[] { paramName });
      return null;
    }
    else if (param instanceof Map<?, ?> && ((Map<?, ?>) param).isEmpty()) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004, new String[] { paramName });
      return null;
    }
    else if (!(param instanceof Map<?, ?>)) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005, new String[] { paramName });
      return null;
    }
    return (Map<?, ?>) param;
  }
  
  /**
   * Method Validates all mandatory Map Input and set warning back to model
   * 
   * @param model
   * @param paramName
   * @return
   */
  public static Map<?, ?> validateAndGetOptionalMap(WorkflowTaskModel model, String paramName)
  {
    Object param = model.getInputParameters().get(paramName);
    if (param == null) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003,
          new String[] { paramName });
      return null;
    }
    else if (param instanceof Map<?, ?> && ((Map<?, ?>) param).isEmpty()) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004,
          new String[] { paramName });
      return null;
    }
    else if (!(param instanceof Map<?, ?>)) {
      model.getExecutionStatusTable().addWarning(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005,
          new String[] { paramName });
      return null;
    }
    return (Map<?, ?>) param;
  }
  
  /**
   * Method Validates all mandatory File path Input and set error back to model
   * 
   * @param model
   * @param paramName
   * @return
   */
  public static Object validateAndGetRequiredFilePath(WorkflowTaskModel model, String paramName)
  {
    Object param = model.getInputParameters().get(paramName);
    if (param == null) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003, new String[] { paramName });
      return null;
    }
    if (param instanceof String && param.toString().isEmpty()) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004, new String[] { paramName });
      return null;
    }
    if (!Files.exists(Paths.get(param.toString()))) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005, new String[] { paramName });
      return null;
    }
    return param;
  }
  
  /**
   * Method Validates all mandatory File path Input and set warning back to
   * model
   * 
   * @param model
   * @param paramName
   * @return
   */
  public static Object validateAndGetOptionalFilePath(WorkflowTaskModel model, String paramName)
  {
    Object param = model.getInputParameters().get(paramName);
    if (param == null) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN003, new String[] { paramName });
      return null;
    }
    if (param instanceof String && param.toString().isEmpty()) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN004, new String[] { paramName });
      return null;
    }
    if (!Files.exists(Paths.get(param.toString()))) {
      model.getExecutionStatusTable().addError(new ObjectCode[] { ObjectCode.INPUT_PARAM }, MessageCode.GEN005, new String[] { paramName });
      return null;
    }
    return param;
  }
  
  /**
   * Check runtime value
   * 
   * @param value
   * @return
   */
  public static Boolean isRuntimeValue(String value)
  {
    return value != null && value.startsWith("$");
  }
  
  /**
   * This method checks if directory path mentioned in the task input variables
   * exists or not.
   * 
   * @param directoryPath
   * @return
   */
  public static Boolean validateDirectoryPath(String directoryPath)
  {
    if (!isRuntimeValue(directoryPath)) {
      File outputFolder = new File(directoryPath);
      if (!outputFolder.exists() || !outputFolder.isDirectory()) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * Check if two file paths are same
   * 
   * @param dirPath1
   * @param dirPath2
   * @return
   */
  public static Boolean checkForEqualDirectoryPaths(String dirPath1, String dirPath2)
  {
    return Paths.get(dirPath1 + "/").toString().equals(Paths.get(dirPath2 + "/").toString());
  }
  
  /**
   * 
   * Checks if a String is whitespace, empty ("") or null.
   *
   * 
   * StringUtils.isBlank(null) = true StringUtils.isBlank("") = true
   * StringUtils.isBlank(" ") = true StringUtils.isBlank("bob") = false
   * StringUtils.isBlank(" bob ") = false
   *
   * @param str the String to check, may be null
   * @return true if the String is null, empty or whitespace
   *
   */
  public static boolean isBlank(String str)
  {
    int strLen;
    if (str == null || (strLen = str.length()) == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if ((Character.isWhitespace(str.charAt(i)) == false)) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * This method checks if file path mentioned in the task input variables
   * exists or not.
   * 
   * @param filePath
   * @return
   */
  public static Boolean validateFilePath(String filePath)
  {
    if (!isRuntimeValue(filePath)) {
      if (isBlank(filePath) || Files.notExists(Path.of(filePath))) {
        return false;
      }
    }
    return true;
  }
  
  /**
   *
   * @param ipAddress
   * @param check for valid ipAddress
   * @return
   */
  public static boolean isValidFormatOfIpAddress(String ipAddress)
  {
    if (!isRuntimeValue(ipAddress)) {
      Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
      Matcher matcher = pattern.matcher(ipAddress);
      return matcher.matches();
    }
    return false;
  }
  
  /**
   * This method checks for valid port
   *
   * @param port
   * @param check for valid port
   * @return
   */
  public static boolean isValidPort(String port)
  {
    if (!isRuntimeValue(port)) {
      final String PORT_PATTERN = "[0-9]{1,5}$";
      Pattern pattern = Pattern.compile(PORT_PATTERN);
      Matcher matcher = pattern.matcher(port);
      return (matcher.matches() && Integer.valueOf(port) < 65536) ? true : false;
    }
    return false;
  }
  
  /**
   * This method checks for valid email address
   * 
   * @param email
   * @return
   */
  public static boolean emailValidator(String email)
  {
    
    if (email == null) {
      return false;
    }
    
    Matcher matcher = EMAIL_PATTERN.matcher(email);
    return matcher.matches();
  }

  public static boolean validateRelativePath(String relativePath)
  {
    // TODO alpha numeric or _ or /\
    final String regex = "^[a-zA-Z0-9-_ / \\\\]+$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(relativePath);
    return matcher.matches();
  }
  
  public static boolean checkForEquqlRelativePath(String relativePathOne ,String relativePathTwo)
  {
    return Paths.get(relativePathOne + File.separator)
        .toString()
        .equals(Paths.get(relativePathTwo + File.separator)
            .toString());
  }
  /**
   * This method checks for valid contact number
   * 
   * @param phoneNumber
   * @return
   */
  public static boolean contactNumberValidator(String phoneNumber)
  {
    if (phoneNumber == null) {
      return true;
    }
    Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
    return matcher.matches();
  }
}