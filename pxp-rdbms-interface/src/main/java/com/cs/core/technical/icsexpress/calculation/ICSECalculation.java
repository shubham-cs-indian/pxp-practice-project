 package com.cs.core.technical.icsexpress.calculation;

import static com.cs.core.technical.icsexpress.calculation.ICSECalculationNode.OperatorType.Conditional;
import static com.cs.core.technical.icsexpress.calculation.ICSECalculationNode.OperatorType.Logical;
import static com.cs.core.technical.icsexpress.calculation.ICSECalculationNode.OperatorType.Math;
import static com.cs.core.technical.icsexpress.calculation.ICSECalculationNode.OperatorType.Text;

/**
 * Definition of a calculation as a tree of executions that join an operator and operands
 *
 * @author vallee
 */
public interface ICSECalculation extends ICSECalculationNode {

  /**
   * @return the operator involved in the current calculation node
   */
  Operator getOperator();

  /**
   * @return the 2 operands by order of application
   */
  ICSECalculationNode[] getOperands();

  public enum Operator {

    // Precedence goes first to math operators, then text operators, then
    // conditional operators and then logical combinations
    Plus(Math, "+", 50), Minus(Math, "-", 50), Multiply(Math, "*", 51), Divide(Math, "/", 51),
    Modulus(Math, "%", 52), Concatenate(Text, "||", 40), Equals(Conditional, "=", 30),
    Differs(Conditional, "<>", 30), GT(Conditional, ">", 30), GTE(Conditional, ">=", 30),
    LT(Conditional, "<", 30), LTE(Conditional, "<=", 30), Like(Conditional, "like", 30),
    Regex(Conditional, "regex", 30), Contains(Conditional, "contains", 30),
    Relates(Conditional, "relates", 30), Lor(Logical, "or", 20), Land(Logical, "and", 21),
    Lxor(Logical, "xor", 22), Between(Conditional, "between", 30),Notcontains(Conditional, "notcontains", 30);

    private static final Operator[] values = values();
    private static final String[] symbols = new String[values.length];

    static {
      for (int i = 0; i < values.length; i++) {
        symbols[i] = values[i].symbol;
      }
    }

    private final OperatorType type;
    private final String symbol;
    private final int precedence;

    Operator(OperatorType type, String symbol, int precedence) {
      this.type = type;
      this.symbol = symbol;
      this.precedence = precedence;
    }

    public static Operator parseString(String symbol) {
      for (int i = 0; i < values.length; i++) {
        if (symbols[i].equals(symbol)) {
          return values[i];
        }
      }
      return null;
    }

    public OperatorType getType() {
      return this.type;
    }

    public String getSymbol() {
      return this.symbol;
    }

    public int getPrecedence() {
      return this.precedence;
    }

    @Override
    public String toString() {
      return this.symbol;
    }
  }
}
