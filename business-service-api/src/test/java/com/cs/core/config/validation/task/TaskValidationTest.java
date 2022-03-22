package com.cs.core.config.validation.task;

import com.cs.core.config.businessapi.task.TaskValidations;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TaskValidationTest {


  @Test
  public void testCode()
  {
    String code1 = "code1";
    String code2 = "code_2-1";
    String code3 = "x^&_1";
    String code4 = "";
    String code5 = "  ";

    assert(TaskValidations.isCodeValid(code1));
    assert(TaskValidations.isCodeValid(code2));
    assert(!TaskValidations.isCodeValid(code3));
    assert(!TaskValidations.isCodeValid(code4));
    assert(!TaskValidations.isCodeValid(code5));
  }

  @Test
  public void testType()
  {
    String code1 = "personal";
    String code2 = "#@!#!@#@!";
    String code3 = "";
    String code4 = "   ";

    assert(TaskValidations.isTypeValid(code1));
    assert(!TaskValidations.isTypeValid(code2));
    assert(!TaskValidations.isTypeValid(code3));
    assert(!TaskValidations.isTypeValid(code4));
  }

  @Test
  public void testColor()
  {
    String code1 = "red";
    String code2 = "#603D20";
    String code3 = "";
    String code4 = "   ";

    assert(!TaskValidations.isColorValid(code1));
    assert(TaskValidations.isColorValid(code2));
    assertFalse(!TaskValidations.isColorValid(code3));
    assertFalse(!TaskValidations.isColorValid(code4));
  }
}
