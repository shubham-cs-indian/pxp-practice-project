package com.cs.core.technical.icsexpress;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.actions.ICSEActionList;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;
import com.cs.core.technical.icsexpress.rule.ICSERule;
import com.cs.core.technical.icsexpress.rule.ICSESearch;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.icsexpress.scope.ICSEScope;

/**
 * parser methods for CSExpression
 *
 * @author vallee
 */
public interface ICSEParser {

  /**
   * @param expression CSExpress string expression to be parsed
   * @return the result of parsing a CSExpress definition
   * @throws CSFormatException
   */
  public ICSEElement parseDefinition(String expression) throws CSFormatException;

  /**
   * return the result of parsing a coupling expression
   *
   * @param expression CSExpress string expression to be parsed
   * @return the result of parsing a CSExpress coupling expression
   * @throws CSFormatException
   */
  public ICSECoupling parseCoupling(String expression) throws CSFormatException;

  /**
   * return the result of parsing a calculation expression
   *
   * @param expression CSExpress string expression to be parsed
   * @return the result of parsing a CSExpress calculation expression
   * @throws CSFormatException
   */
  public ICSECalculationNode parseCalculation(String expression) throws CSFormatException;

  /**
   * return the result of parsing a list of actions
   *
   * @param expression CSExpress string expression to be parsed
   * @return the result of parsing a CSExpress action list expression
   * @throws CSFormatException
   */
  public ICSEActionList parseActionList(String expression) throws CSFormatException;

  /**
   * return the result of parsing separately an entity filter expression (part of scopes)
   *
   * @param expression the entity filter
   * @return the result of parsing a CSExpress entity filter expression
   * @throws CSFormatException
   */
  public ICSEEntityFilterNode parseEntityFilter(String expression) throws CSFormatException;

  /**
   * return the result of parsing a scope expression
   *
   * @param expression CSExpress string expression to be parsed
   * @return the result of parsing a CSExpress scope expression
   * @throws CSFormatException
   */
  public ICSEScope parseScope(String expression) throws CSFormatException;

  /**
   * return the result of parsing a rule expression
   *
   * @param expression CSExpress string expression to be parsed
   * @return the result of parsing a CSExpress rule expression
   * @throws CSFormatException
   */
  public ICSERule parseRule(String expression) throws CSFormatException;

  /**
   * return the result of parsing a search expression
   *
   * @param expression CSExpress string expression to be parsed
   * @return the result of parsing a CSExpress search expression
   * @throws CSFormatException
   */
  public ICSESearch parseSearch(String expression) throws CSFormatException;
}
