package com.cs.core.csexpress.calculation;

import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContent;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.calculation.ICSECalculation;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.calculation.ICSERecordOperand;
import com.cs.core.technical.ijosn.IJSONContent;
import java.util.Set;

/**
 * @author vallee
 */
public class CSECalculation extends CSECalculationNode implements ICSECalculation {

  private final Operator operator;
  private CSECalculationNode leftOperand = null;
  private CSECalculationNode rightOperand = null;

  /**
   * Value constructor
   *
   * @param operator
   */
  public CSECalculation(Operator operator) {
    super(OperandType.Calculation);
    this.operator = operator;
  }

  @Override
  public Operator getOperator() {
    return operator;
  }

  @Override
  public ICSECalculationNode[] getOperands() {
    return new ICSECalculationNode[]{leftOperand, rightOperand};
  }

  @Override
  public Set<ICSERecordOperand> getRecords() {
    Set<ICSERecordOperand> records = leftOperand.getRecords();
    records.addAll(rightOperand.getRecords());
    return records;
  }

  @Override
  public String toString() {
    return String.format("( %s %s %s )", leftOperand.toString(), operator.getSymbol(),
            rightOperand.toString());
  }

  /**
   * @param leftOperandPart overwritten left operand
   */
  void setLeft(ICSECalculationNode leftOperandPart) {
    leftOperand = (CSECalculationNode) leftOperandPart;
  }

  /**
   * @param righOperandPart overwritten right operand
   */
  void setRight(ICSECalculationNode righOperandPart) {
    rightOperand = (CSECalculationNode) righOperandPart;
  }

  /**
   * @return true when the calculation is accepted as a search condition
   */
  public boolean isSearchCondition() {
    // The two operands must be present
    if (rightOperand == null) {
      return false;
    }
    // The operator must be conditional or logical
    if ( getOperator().getType() != OperatorType.Conditional && getOperator().getType() != OperatorType.Logical )
      return false;
    // If the operator is Logical, then the two operands must be of calcuation type
    if (getOperator().getType() == OperatorType.Logical) {
      return (leftOperand.getType() == OperandType.Calculation
              && rightOperand.getType() == OperandType.Calculation);
    }
    // If the operator is conditional, then operands must be of type literral or record
    if (leftOperand.getType() != OperandType.Literal
            && leftOperand.getType() != OperandType.Record) {
      return false;
    }
    // The two operands cannot be literal
    if (leftOperand.getType() == OperandType.Literal
            && rightOperand.getType() == OperandType.Literal) {
      return false;
    }
    if (rightOperand.getType() != OperandType.Literal && rightOperand.getType() != OperandType.Record
        //this check is currently for range conditions but can also be useful for complex condition 
        && !(rightOperand.getType() == OperandType.Calculation && getOperator().equals(Operator.Equals))) {
      return false;
    }
    return true;
  }

  @Override
  public IJSONContent toJSON() throws CSFormatException {
    String json = JSONBuilder.assembleJSON(
            getUnaryOperator() != null
                    ? JSONBuilder.newJSONField("unary", getUnaryOperator().toString())
                    : JSONBuilder.VOID_FIELD,
            JSONBuilder.newJSONField("op", operator.toString()),
            JSONBuilder.newJSONField("left", leftOperand.toJSON()),
            JSONBuilder.newJSONField("right", rightOperand.toJSON()));
    return new JSONContent(json);
  }

  @Override
  public void checkConsistency() throws CSFormatException {
    leftOperand.checkConsistency();
    if (rightOperand != null) {
      rightOperand.checkConsistency();
    }
  }
}
