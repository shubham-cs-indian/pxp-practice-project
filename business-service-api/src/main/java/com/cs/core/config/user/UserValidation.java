package com.cs.core.config.user;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.Validations;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.exception.InvalidEmailException;
import com.cs.core.exception.InvalidFirstNameException;
import com.cs.core.exception.InvalidLastNameException;
import com.cs.core.exception.InvalidMobileNumberException;
import com.cs.core.exception.InvalidUserNameException;

@Service
public class UserValidation extends Validations{
  
  public static final String MOBILE_NUMBER_REGEX = "^\\+?\\d*(\\s?\\((\\d+[\\s\\-]*\\d+)\\)\\s?)*(\\d+[\\s\\-]\\d+)*$";
  
  public static final String EMAIL_REGEX = "^([a-zA-Z0-9_\\u00c4\\u00d6\\u00dc\\u00df\\u00e4\\u00f6\\u00fc\\-\\.]+)@([a-zA-Z0-9_\\u00c4\\u00d6\\u00dc\\u00df\\u00e4\\u00f6\\u00fc\\-\\.]+)\\.([a-zA-Z\\u00c4\\u00d6\\u00dc\\u00df\\u00e4\\u00f6\\u00fc]+)$"; 
  
  public static enum Gender
  {
    male, female, other;
  }
  
  public static final List<String> validGenders = Arrays.asList(Gender.male.toString(), Gender.female.toString(), Gender.other.toString());
  
  public static void validateContactNumber(String contactNumber) throws InvalidMobileNumberException
  {
    // throw the number format exception
    if (StringUtils.isNotEmpty(contactNumber) && !contactNumber.matches(MOBILE_NUMBER_REGEX)) {
      throw new InvalidMobileNumberException();
    }
  }
  
  public static String validateGender(String gender)
  {
    if (StringUtils.isNotEmpty(gender) && validGenders.contains(gender)) {
      return gender;
    }
    return Gender.male.toString();
  }
  
  public static void validateUser(IUserModel userModel) throws Exception
  {
    validateFirstName(userModel.getFirstName());
    
    validateLastName(userModel.getLastName());
    
    validateEmail(userModel.getEmail());

    validateUserName(userModel.getUserName());
  }
  
  public static void validateLastName(String lastName) throws InvalidLastNameException
  {
    if (isEmpty(lastName)) {
      throw new InvalidLastNameException();
    }
  }
  
  public static void validateFirstName(String firstName) throws InvalidFirstNameException
  {
    if (isEmpty(firstName)) {
      throw new InvalidFirstNameException();
    }
  }
  
  public static void validateUserName(String username) throws InvalidUserNameException
  {
    if (isEmpty(username)) {
      throw new InvalidUserNameException();
    }
  }
  
  public static void validateEmail(String email) throws InvalidEmailException
  {
    if (isEmpty(email) || !Pattern.compile(EMAIL_REGEX).matcher(email).matches()) {
      throw new InvalidEmailException();
    }
  }
}
