package com.cs.core.csexpress.actions;

import com.cs.core.csexpress.calculation.CSECalculationNode;
import com.cs.core.csexpress.coupling.CSECouplingSource;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.csexpress.definition.CSEProperty;
import com.cs.core.json.JSONContent;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyAssignment;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource.Predefined;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * @author vallee
 */
public class CSEPropertyAssignment extends CSEAction implements ICSEPropertyAssignment {

  private CSECouplingSource owner = new CSECouplingSource(Predefined.$entity);
  private CSEProperty property = null;
  private ICSECalculationNode assignedValue = null;

  public CSEPropertyAssignment() {
    super(ActionType.Assignment);
  }

  @Override
  public ICSECouplingSource getOwner() {
    return owner;
  }

  @Override
  public ICSEProperty getProperty() {
    return property;
  }

  void setProperty(CSEProperty property) {
    this.property = property;
  }

  @Override
  public ICSECalculationNode getValue() {
    return assignedValue;
  }

  void setAssignment(CSECalculationNode evaluation) {
    assignedValue = evaluation;
  }

  @Override
  public void setObject(CSEObject cseObject) {
    owner.setSource(cseObject);
  }

  @Override
  public void setPredefinedObject(String predefined) {
    owner = new CSECouplingSource(Predefined.valueOf(predefined));
  }

  @Override
  public String toString() {
    return String.format("%s.%s := %s", owner.toString(), property.toString(),
            assignedValue.toString());
  }

  @Override
  public IJSONContent toJSON() throws CSFormatException {
    JSONContent json = new JSONContent();
    json.setField("target", owner != null ? owner.toString() : "");
    json.setField("property", property != null ? property.toString() : "");
    json.setField("assignment", assignedValue != null ? assignedValue.toJSON() : "");
    return json;
  }
}
