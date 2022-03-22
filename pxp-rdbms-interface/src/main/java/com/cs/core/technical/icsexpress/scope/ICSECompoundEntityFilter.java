package com.cs.core.technical.icsexpress.scope;

import com.cs.core.technical.icsexpress.calculation.ICSECalculation.Operator;

/**
 * @author vallee
 */
public interface ICSECompoundEntityFilter extends ICSEEntityFilterNode {

  /**
   * @return the operator involved in the compound expression
   */
  public Operator getLogicalOperator();

  /**
   * @return the left(0)/right(1) operands of the compound formula
   */
  public ICSEEntityFilterNode[] getOperands();
}
