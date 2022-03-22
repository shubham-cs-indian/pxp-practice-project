package com.cs.core.csexpress.calculation;

import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContent;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.calculation.ICSEFunctionOperand;
import com.cs.core.technical.icsexpress.calculation.ICSERecordOperand;
import com.cs.core.technical.ijosn.IJSONContent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author vallee
 */
public class CSEFunctionOperand extends CSECalculationNode implements ICSEFunctionOperand {

  private final Function function;
  private final List<ICSECalculationNode> parameters = new ArrayList<>();

  public CSEFunctionOperand(Function function) {
    super(OperandType.Function);
    this.function = function;
  }

  @Override
  public Function getFunction() {
    return function;
  }

  @Override
  public ICSECalculationNode[] getParameters() {
    return parameters.toArray(new ICSECalculationNode[0]);
  }

  public void addParameter(ICSECalculationNode operand) {
    parameters.add(operand);
  }

  @Override
  public Set<ICSERecordOperand> getRecords() {
    Set<ICSERecordOperand> operands = new HashSet<>();
    parameters.forEach((ICSECalculationNode param) -> {
      operands.addAll(param.getRecords());
    });
    return operands;
  }

  @Override
  public IJSONContent toJSON() throws CSFormatException {
    StringBuffer paramStr = new StringBuffer("\"parameters\":[");
    for (ICSECalculationNode param : parameters) {
      paramStr.append(param.toJSON()
              .toString())
              .append(",");
    }
    if (parameters.size() > 0) {
      paramStr.setLength(paramStr.length() - 1);
    }
    paramStr.append("]");
    String json = JSONBuilder.assembleJSON(JSONBuilder.newJSONField("function", getFunction()),
            getUnaryOperator() != null
                    ? JSONBuilder.newJSONField("unary", getUnaryOperator().toString())
                    : JSONBuilder.VOID_FIELD,
            paramStr);
    return new JSONContent(json);
  }

  @Override
  public String toString() {
    StringBuffer paramStr = new StringBuffer();
    for (ICSECalculationNode param : parameters) {
      paramStr.append(param.toString())
              .append(",");
    }
    if (parameters.size() > 0) {
      paramStr.setLength(paramStr.length() - 1);
    }
    paramStr.append(")");
    return String.format("%s%s(%s)",
            getUnaryOperator() != null ? getUnaryOperator().toString() + " " : "", function.toString(),
            paramStr);
  }

  @Override
  public void checkConsistency() throws CSFormatException {
    if (function.getNbArguments() > 0
            && function.getNbArguments() != parameters.size()) {
      throw new CSFormatException("Incorrect number of parameters for function: " + function);
    }
  }
}
