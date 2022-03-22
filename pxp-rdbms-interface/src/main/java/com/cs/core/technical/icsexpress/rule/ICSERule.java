package com.cs.core.technical.icsexpress.rule;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.actions.ICSEActionList;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.scope.ICSEScope;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * Defines a KPI or a DQ rule
 *
 * @author vallee
 */
public interface ICSERule {

  /**
   * @return the type of rule
   */
  public RuleType getType();

  /**
   * @return the scope that determines what entities are concerned by the rule
   */
  public ICSEScope getScope();

  /**
   * @return the logical calculation that determines if the rule is applicable
   */
  public ICSECalculationNode getEvaluation();

  /**
   * @return the list of actions to execute when the logical calculation returns true /!\ systematically empty for KPI and search rules
   */
  public ICSEActionList getActionList();

  /**
   * @return the JSON structure of the rule
   * @throws CSFormatException
   */
  public IJSONContent toJSON() throws CSFormatException;

  public enum RuleType {
    kpi, dataquality, search;

    private static final RuleType[] values = values();

    public static RuleType valueOf(int ordinal) {
      return values[ordinal];
    }
  }
}
