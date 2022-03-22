package com.cs.core.technical.icsexpress.calculation;

import static com.cs.core.technical.icsexpress.calculation.ICSECalculationNode.OperatorType.Logical;

import java.util.Set;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * An operand included in a calculation expression
 *
 * @author vallee
 */
public interface ICSECalculationNode {

  /**
   * @return the operand function that applies as a transform of the node evaluation
   */
  public UnaryOperator getUnaryOperator();

  /**
   * @param function overwritten unary operator or function that applies on the node evaluation
   */
  void setUnaryOperator(UnaryOperator function);

  /**
   * Fetch recursively all terminal record operands involved in this operand
   *
   * @return the record identities
   */
  public Set<ICSERecordOperand> getRecords();

  /**
   * @return the record signatures of all records involved
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public Set<String> getRecordNodeIDs() throws CSFormatException;

  /**
   * @return the type of operand
   */
  OperandType getType();

  /**
   * @return true if this operand doesn't require more parsing
   */
  public default boolean isTerminal() {
    return (getType() == OperandType.Record || getType() == OperandType.Literal);
  }

  /**
   * @return the JSON compilation result of calculation
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public IJSONContent toJSON() throws CSFormatException;

  public enum OperandType {
    Literal, Record, Function, Calculation;
  }

  public enum OperatorType {
    Conditional, Logical, Math, Text;
  }

  public enum UnaryOperator {

    // unary operator or function that is transforming the evaluation of the
    // operand
    not(Logical, "not"),
    uppercase(OperatorType.Text, "uC"),
    lowercase(OperatorType.Text, "lC"),
    trim(OperatorType.Text, "trim"),
    propercase(OperatorType.Text, "propercase");

    private static final UnaryOperator[] values = values();
    private static final String[] symbols = new String[values.length];

    static {
      for (int i = 0; i < values.length; i++) {
        symbols[i] = values[i].symbol;
      }
    }

    private final OperatorType type;
    private final String symbol;

    UnaryOperator(OperatorType type, String symbol) {
      this.symbol = symbol;
      this.type = type;
    }

    public static UnaryOperator parseString(String symbol) {
      for (int i = 0; i < values.length; i++) {
        if (symbols[i].equals(symbol)) {
          return values[i];
        }
      }
      return null;
    }
  }

  /**
   * Check the consistency of the calculation form and raises an exception in case of violation
   *
   * @throws CSFormatException
   */
  public void checkConsistency() throws CSFormatException;
}
