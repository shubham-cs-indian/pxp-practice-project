package com.cs.core.csexpress.calculation;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.calculation.ICSECalculation;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.calculation.ICSERecordOperand;
import java.util.HashSet;
import java.util.Set;

/**
 * @author vallee
 */
public abstract class CSECalculationNode implements ICSECalculationNode {

  private final OperandType type;
  private ICSECalculation.UnaryOperator unary = null;

  protected CSECalculationNode(OperandType type) {
    this.type = type;
  }

  @Override
  public OperandType getType() {
    return type;
  }

  @Override
  public Set<String> getRecordNodeIDs() throws CSFormatException {
    Set<String> signatures = new HashSet<>();
    for (ICSERecordOperand sourceRecord : this.getRecords()) {
      signatures.add(sourceRecord.getDependencyNodeID(false));
    }
    return signatures;
  }

  @Override
  public ICSECalculation.UnaryOperator getUnaryOperator() {
    return unary;
  }

  @Override
  public void setUnaryOperator(ICSECalculation.UnaryOperator unary) {
    this.unary = unary;
  }
}
