package com.cs.core.csexpress.scope;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.calculation.ICSECalculation;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.scope.ICSECompoundEntityFilter;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.ijosn.IJSONContent;
import java.util.*;

/**
 * @author vallee
 */
public class CSECompoundEntityFilter extends CSEEntityFilterNode
        implements ICSECompoundEntityFilter {

  private ICSECalculation.Operator operator;
  private ICSEEntityFilterNode leftNode;
  private ICSEEntityFilterNode rightNode;

  CSECompoundEntityFilter() {
    super(FilterNodeType.compound);
  }

  @Override
  public ICSECalculation.Operator getLogicalOperator() {
    return operator;
  }

  void setLogicalOperator(ICSECalculation.Operator operator) {
    this.operator = operator;
  }

  @Override
  public ICSEEntityFilterNode[] getOperands() {
    return new ICSEEntityFilterNode[]{leftNode, rightNode};
  }

  void setLeft(ICSEEntityFilterNode parsedFilter) {
    leftNode = parsedFilter;
  }

  void setRight(ICSEEntityFilterNode parsedFilter) {
    rightNode = parsedFilter;
  }

  @Override
  public void setObject(ICSECouplingSource source) throws CSFormatException {
    throw new CSFormatException("A compound filter cannot have a target object");
  }

  @Override
  public Collection<ICSEEntityFilterNode> getSimpleFilters() {
    List<ICSEEntityFilterNode> filters = new ArrayList<>();
    filters.addAll(leftNode.getSimpleFilters());
    filters.addAll(rightNode.getSimpleFilters());
    return filters;
  }

  @Override
  public Collection<String> getIncludingClassifiers() throws CSFormatException {
    Set<String> classifierCodes = new HashSet<>();
    classifierCodes.addAll(leftNode.getIncludingClassifiers());
    classifierCodes.addAll(rightNode.getIncludingClassifiers());
    return classifierCodes;
  }

  @Override
  public Collection<String> getExcludedClassifiers() throws CSFormatException {
    Set<String> classifierCodes = new HashSet<>();
    classifierCodes.addAll(leftNode.getExcludedClassifiers());
    classifierCodes.addAll(rightNode.getExcludedClassifiers());
    return classifierCodes;
  }

  @Override
  public Collection<String> getContainedProperties() throws CSFormatException {
    Set<String> propertyCodes = new HashSet<>();
    propertyCodes.addAll(leftNode.getContainedProperties());
    propertyCodes.addAll(rightNode.getContainedProperties());
    return propertyCodes;
  }

  @Override
  public IJSONContent toJSON() throws CSFormatException {
    IJSONContent json = super.toJSON();
    json.setField("left", leftNode.toJSON());
    if (rightNode != null) {
      json.setField("right", rightNode.toJSON());
    }
    json.setField("op", operator != null ? operator.toString() : "");
    return json;
  }

  @Override
  public String toString() {
    String opStr = operator != null ? String.format(" %s ", operator) : "";
    String rightHand = rightNode != null ? rightNode.toString() : "";
    return String.format("%s( %s%s%s )", hasNot() ? "not " : "", leftNode.toString(), opStr,
            rightHand);
  }
}
