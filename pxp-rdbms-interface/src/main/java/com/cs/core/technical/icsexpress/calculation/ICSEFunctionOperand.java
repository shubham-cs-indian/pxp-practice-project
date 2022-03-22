package com.cs.core.technical.icsexpress.calculation;

import static com.cs.core.technical.icsexpress.calculation.ICSECalculationNode.OperatorType.Logical;
import static com.cs.core.technical.icsexpress.calculation.ICSECalculationNode.OperatorType.Text;

/**
 * @author vallee
 */
public interface ICSEFunctionOperand extends ICSECalculationNode {

  /**
   * @return the function
   */
  Function getFunction();

  /**
   * @return the list of parameters by order of application
   */
  ICSECalculationNode[] getParameters();

  public enum Function {
    // function that evalates a series of operand parameters
    unique(Logical, "unique", -1), upper(Text, "upper", 1),
    lower(Text, "lower", 1), replace(Text, "replace", 3),
    trim(Text, "trim", 1), propercase(Text, "propercase", 1),
    substring(Text, "substring", 3),
    add(OperatorType.Math, "add", 2), remove(OperatorType.Math, "remove", 2);

    private static final Function[] values = values();
    private static final String[] symbols = new String[values.length];

    static {
      for (int i = 0; i < values.length; i++) {
        symbols[i] = values[i].symbol;
      }
    }

    private final OperatorType type;
    private final String symbol;
    private final int nbArguments;

    Function(OperatorType type, String symbol, int nbArguments) {
      this.symbol = symbol;
      this.type = type;
      this.nbArguments = nbArguments;
    }

    public static Function parseString(String symbol) {
      for (int i = 0; i < values.length; i++) {
        if (symbols[i].equals(symbol)) {
          return values[i];
        }
      }
      return null;
    }

    public OperatorType getType() {
      return type;
    }

    public int getNbArguments() {
      return nbArguments;
    }
  }

}
