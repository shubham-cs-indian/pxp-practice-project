package com.cs.core.config.validation.language;

import com.cs.core.config.language.LanguageValidations;
import com.cs.core.config.strategy.usecase.language.GetLanguageByLocaleIdStrategy;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Assert;
import org.junit.Test;

public class LanguageValidationTests {

  LanguageValidations validations;

  public LanguageValidationTests() {
    validations = new LanguageValidations();
    validations.getLanguageByLocaleIdStrategy =  new GetLanguageByLocaleIdStrategy();
  }

  @Test
  public void validateDateFormat(){
    Assert.assertFalse(validations.isValidDateFormat(""));
    Assert.assertFalse(validations.isValidDateFormat(null));
    Assert.assertTrue(validations.isValidDateFormat("YYYY-MM-DD HH:mm:ss"));
    Assert.assertFalse(validations.isValidDateFormat("   "));
    Assert.assertFalse(validations.isValidDateFormat("asdqwewq"));
  }

  @Test
  public void validateNumberFormat(){
    Assert.assertFalse(validations.isValidNumberFormat(""));
    Assert.assertFalse(validations.isValidNumberFormat(null));
    Assert.assertTrue(validations.isValidNumberFormat("### ### ###,##"));
    Assert.assertFalse(validations.isValidNumberFormat("   "));
    Assert.assertFalse(validations.isValidNumberFormat("asdqwewq"));
  }

  @Test
  public void validateParent() throws RDBMSException
  {
    Assert.assertFalse(validations.isParentValid(""));
    Assert.assertFalse(validations.isParentValid(null));
    Assert.assertTrue(validations.isParentValid("-1"));
/*    Assert.assertTrue(validations.isParentValid("en_US"));*/
    Assert.assertFalse(validations.isParentValid("asds"));
  }

  @Test
  public void validateLocaleId(){
    Assert.assertFalse(validations.isLocaleIdValid(""));
    Assert.assertFalse(validations.isLocaleIdValid(null));
    Assert.assertFalse(validations.isLocaleIdValid("### ### ###,##"));
    Assert.assertTrue(validations.isLocaleIdValid("1078"));
    Assert.assertFalse(validations.isLocaleIdValid("asdqwewq"));
  }
}
