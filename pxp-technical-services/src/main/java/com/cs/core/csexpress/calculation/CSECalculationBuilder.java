package com.cs.core.csexpress.calculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.cs.core.csexpress.CSEBuilder;
import com.cs.core.csexpress.CSEList;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.csexpress.definition.CSEProperty;
import com.cs.core.csexpress.definition.CSETagValue;
import com.cs.core.data.Text;
import com.cs.core.parser.csexpress.csexpressParser;
import com.cs.core.parser.csexpress.csexpressParser.RangeLiteralContext;
import com.cs.core.rdbms.config.idto.IPropertyDTO.LiteralType;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.calculation.ICSECalculation.Operator;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode.UnaryOperator;
import com.cs.core.technical.icsexpress.calculation.ICSEFunctionOperand;
import com.cs.core.technical.icsexpress.calculation.ICSERecordOperand.PropertyField;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource.Predefined;

/**
 * @author vallee
 */
public class CSECalculationBuilder extends CSEBuilder<ICSECalculationNode> {

  private final Map<Integer, Operator> operatorsMap = new HashMap<>();
  private final Map<Integer, UnaryOperator> unaryOperatorsMap = new HashMap<>();
  private final Map<Integer, csexpressParser.OperandContext> operandsMap = new HashMap<>();
  private CSECalculationNode currentNode = null;
  private UnaryOperator forwardUnaryOperator = null;

  CSECalculationBuilder(CSEParser rootParser) {
    super(rootParser);
  }

  @Override
  public ICSECalculationNode visitCalculation(csexpressParser.CalculationContext ctx) {
    visitChildren(ctx);
    return currentNode;
  }

  /**
   * Split the current parser context into operators and operands
   *
   * @param ctx
   * @return the left most position of the operator with lowest precedence
   */
  private int splitIntoOperandsAndOperators(csexpressParser.Evaluation_expressionContext ctx) {
    int position = 0;
    int minPrecedenceOp = Integer.MAX_VALUE;
    int minPrecedencePostion = -1;
    //String debug = String.format("%s (%d)", contextToString(ctx), ctx.getChildCount());
    String debug = String.format("%s (%d)", ctx.getText(), ctx.getChildCount());
    for (int i = 0; i < ctx.getChildCount(); i++) {
      ParseTree child = ctx.getChild(i);
      if (child instanceof ErrorNode) {
        raisedExceptions.add("Parser error in: " + child.getText());
        return minPrecedencePostion;
      }
      if (child instanceof TerminalNode) {
        continue;
      }
      if (child instanceof csexpressParser.OperandContext) {
        operandsMap.put(position++, (csexpressParser.OperandContext) child);
      } else if (child instanceof csexpressParser.OperatorContext) {
        Operator operator = Operator.parseString(child.getText());
        if (operator.getPrecedence() < minPrecedenceOp) {
          minPrecedenceOp = operator.getPrecedence();
          minPrecedencePostion = position;
        }
        operatorsMap.put(position++, operator);
      } else if (child instanceof csexpressParser.Unary_operatorContext) {
        UnaryOperator function = UnaryOperator.parseString(child.getText());
        unaryOperatorsMap.put(position++, function);
      }
    }
    return minPrecedencePostion;
  }

  @Override
  public ICSECalculationNode visitEvaluation_expression( csexpressParser.Evaluation_expressionContext ctx) {
    // Collect operands and operators by position
    int splitOperatorPosition = splitIntoOperandsAndOperators(ctx);
    if (!raisedExceptions.isEmpty()) {
      return new CSELiteralOperand();
    }
    if (operatorsMap.isEmpty()) { // Then the calculation is reduced to a single operand
      visitChildren(ctx);
      if (forwardUnaryOperator != null && currentNode != null) { // assign unary operator if detected
        currentNode.setUnaryOperator(forwardUnaryOperator);
        forwardUnaryOperator = null;
      }
      return currentNode;
    } // else
    currentNode = new CSECalculation(operatorsMap.get(splitOperatorPosition));
    // left operand is all element below splitOperatorPosition
    StringBuffer leftExpression = new StringBuffer().append("=");
    for (int posLeft = 0; posLeft < splitOperatorPosition; posLeft++) {
      leftExpression.append(
              operandsMap.containsKey(posLeft) ? contextToString(operandsMap.get(posLeft)) + " " : "");
      leftExpression.append(operatorsMap.containsKey(posLeft) ? operatorsMap.get(posLeft)
              .toString() + " " : "");
      leftExpression.append(unaryOperatorsMap.containsKey(posLeft) ? unaryOperatorsMap.get(posLeft).toString() + " " : "");
    }
    // right operand is all element above splitOperatorPosition
    int lastRightPosition = operandsMap.size() + operatorsMap.size() + unaryOperatorsMap.size() + 1;
    StringBuffer rightExpression = new StringBuffer().append("=");
    for (int posRight = splitOperatorPosition + 1; posRight < lastRightPosition; posRight++) {
      rightExpression.append(
              operandsMap.containsKey(posRight) ? contextToString(operandsMap.get(posRight)) + " " : "");
      rightExpression.append(operatorsMap.containsKey(posRight) ? operatorsMap.get(posRight).toString() + " " : "");
      rightExpression
              .append(unaryOperatorsMap.containsKey(posRight) ? unaryOperatorsMap.get(posRight).toString() + " " : "");
    }
    try {
      ((CSECalculation) currentNode).setLeft(rootParser.newSubParser().parseCalculation(leftExpression.toString()));
      ((CSECalculation) currentNode).setRight(rootParser.newSubParser().parseCalculation(rightExpression.toString()));
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentNode;
  }

  @Override
  public ICSECalculationNode visitOperand(csexpressParser.OperandContext ctx) {
    // Check parenthesis balance
    if (ctx.GROUP_IN() != null && ctx.GROUP_OUT() == null) {
      raisedExceptions.add("Unbalanced parentheses: missing ')' in " + contextToString(ctx));
    } else if (ctx.GROUP_IN() == null && ctx.GROUP_OUT() != null) {
      raisedExceptions.add("Unbalanced parentheses: missing '(' in " + contextToString(ctx));
    }
    visitChildren(ctx);
    return currentNode;
  }

  @Override
  public ICSECalculationNode visitUnary_operator(csexpressParser.Unary_operatorContext ctx) {
    forwardUnaryOperator = UnaryOperator.parseString(ctx.getText());
    return currentNode;
  }

  @Override
  public ICSECalculationNode visitEvaluated_operand(csexpressParser.Evaluated_operandContext ctx) {
    try {
      String calculatedExpression = "=" + contextToString(ctx);
      currentNode = (CSECalculationNode) rootParser.newSubParser().parseCalculation(calculatedExpression);
      return currentNode;
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return null;
  }

  @Override
  public ICSECalculationNode visitLiteral(csexpressParser.LiteralContext ctx) {
    if (ctx.DOUBLE() != null) {
      double value = Double.parseDouble(ctx.DOUBLE()
              .getText());
      currentNode = new CSELiteralOperand(LiteralType.Number, value);
    } else if (ctx.INTEGER() != null) {
      long value = Long.parseLong(ctx.INTEGER()
              .getText());
      currentNode = new CSELiteralOperand(LiteralType.Number, (double) value);
    } else if (ctx.STRING() != null) {
      String value = ctx.STRING()
              .getText();
      value = Text.unescapeQuotedString(value);
      currentNode = new CSELiteralOperand(LiteralType.Text, value);
    } else if (ctx.TRUE() != null) {
      currentNode = new CSELiteralOperand(LiteralType.Boolean, true);
    } else if (ctx.FALSE() != null) {
      currentNode = new CSELiteralOperand(LiteralType.Boolean, false);
    } else if (ctx.KNULL() != null) {
      currentNode = new CSELiteralOperand();
    }
    return currentNode; // Terminal node
  }

  @Override
  public ICSECalculationNode visitTag_literal(csexpressParser.Tag_literalContext ctx) {
    currentNode = new CSELiteralOperand(LiteralType.List);
    visitChildren(ctx);
    return currentNode;
  }

  @Override
  public ICSECalculationNode visitTag_code(csexpressParser.Tag_codeContext ctx) {
    if (ctx.CODE() != null) {
      ((CSELiteralOperand) currentNode).addTag(ctx.CODE().getText());
    }
    return currentNode;
  }

  @Override
  public ICSECalculationNode visitTag_value(csexpressParser.Tag_valueContext ctx) {
    try {
      CSETagValue tagValue = (CSETagValue) rootParser.newSubParser().parseDefinition(contextToString(ctx));
      ((CSELiteralOperand) currentNode).addTag(tagValue);
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentNode;
  }

  @Override
  public ICSECalculationNode visitList(csexpressParser.ListContext ctx) {
    try {
      String debug = contextToString(ctx);
      CSEList tagValueList = (CSEList) rootParser.newSubParser().parseDefinition(contextToString(ctx));
      ((CSELiteralOperand) currentNode).addTagList(tagValueList);
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentNode;
  }

  @Override
  public ICSECalculationNode visitProperty_operand(csexpressParser.Property_operandContext ctx) {
    currentNode = new CSERecordOperand();
    visitChildren(ctx);
    return currentNode;
  }

  @Override
  public ICSECalculationNode visitFunction_operator(csexpressParser.Function_operatorContext ctx) {
    if (ctx.FUNIQUE() != null) {
      currentNode = new CSEFunctionOperand(ICSEFunctionOperand.Function.unique);
    } else if (ctx.FUPPER() != null) {
      currentNode = new CSEFunctionOperand(ICSEFunctionOperand.Function.upper);
    } else if (ctx.FLOWER() != null) {
      currentNode = new CSEFunctionOperand(ICSEFunctionOperand.Function.lower);
    } else if (ctx.FREPLACE() != null) {
      currentNode = new CSEFunctionOperand(ICSEFunctionOperand.Function.replace);
    } else if (ctx.FADD() != null) {
      currentNode = new CSEFunctionOperand(ICSEFunctionOperand.Function.add);
    } else if (ctx.FREMOVE() != null) {
      currentNode = new CSEFunctionOperand(ICSEFunctionOperand.Function.remove);
    } // later introduce here else if for other functions...
    else {
      raisedExceptions.add("Unexpected function found in expression: " + ctx.getText());
    }
    visitChildren(ctx);
    return currentNode;
  }

  @Override
  public ICSECalculationNode visitFunction_parameter(csexpressParser.Function_parameterContext ctx) {
    String paramStr = "= " + contextToString(ctx);
    try {
      ((CSEFunctionOperand) currentNode).addParameter(rootParser.newSubParser().parseCalculation(paramStr));
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentNode;
  }

  @Override
  public ICSECalculationNode visitProperty(csexpressParser.PropertyContext ctx) {
    try {
      ICSEElement property = rootParser.newSubParser().parseDefinition(contextToString(ctx));
      ((CSERecordOperand) currentNode).setProperty((CSEProperty) property);
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentNode; // Terminal node
  }

  @Override
  public ICSECalculationNode visitObject(csexpressParser.ObjectContext ctx) {
    try {
      ICSEElement entity = rootParser.newSubParser().parseDefinition(contextToString(ctx));
      ((CSERecordOperand) currentNode).setSourceEntity((CSEObject) entity);
    } catch (CSFormatException ex) {
      raisedExceptions.add(ex.getMessage());
    }
    return currentNode; // Terminal node
  }

  @Override
  public ICSECalculationNode visitPredefinedobject(csexpressParser.PredefinedobjectContext ctx) {
    Predefined entity = Predefined.valueOf(contextToString(ctx));
    ((CSERecordOperand) currentNode).setPredefinedSource(entity);
    return currentNode; // Terminal node
  }

  @Override
  public ICSECalculationNode visitProperty_field(csexpressParser.Property_fieldContext ctx) {
    PropertyField field = PropertyField.valueOf(contextToString(ctx));
    ((CSERecordOperand) currentNode).setPropertyField(field);
    return currentNode; // Terminal node
  }
  
  @Override
  public ICSECalculationNode visitRange_literal_operand(csexpressParser.Range_literal_operandContext ctx)
  {
    List<Double> values = new ArrayList<>();
    
    for (int i = 0; i < ctx.getChildCount(); i++) {
      ParseTree child = ctx.getChild(i);
      if (child instanceof ErrorNode) {
        raisedExceptions.add("Parser error in: " + child.getText());
      }
      if (child instanceof TerminalNode) {
        continue;
      }
      if (child instanceof csexpressParser.RangeLiteralContext) {
        RangeLiteralContext childContext = (csexpressParser.RangeLiteralContext) child;
        if (childContext.DOUBLE() != null) {
          values.add(Double.parseDouble(childContext.DOUBLE().getText()));
        }
        else if (childContext.INTEGER() != null) {
          values.add(Double.parseDouble(childContext.INTEGER().getText()));
        }
      }
    }
    if (values.get(0) > values.get(1)) {
      currentNode = new CSERangeLiteralOperand(values.get(1), values.get(0));
    }
    else {
      currentNode = new CSERangeLiteralOperand(values.get(0), values.get(1));
    }
    visitChildren(ctx);
    return currentNode;
  }
}
