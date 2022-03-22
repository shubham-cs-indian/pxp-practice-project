package com.cs.core.csexpress.actions;

import com.cs.core.csexpress.coupling.CSECouplingSource;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.json.JSONContent;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.actions.ICSEObjectClassification;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource.Predefined;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * @author vallee
 */
public class CSEObjectClassification extends CSEAction implements ICSEObjectClassification {

  private CSECouplingSource object = new CSECouplingSource(Predefined.$entity);
  private CSECouplingSource destination = null;

  public CSEObjectClassification() {
    super(ActionType.Classification);
  }

  @Override
  public ICSECouplingSource getObject() {
    return object;
  }

  @Override
  public void setObject(CSEObject cseObject) {
    if (object == null) {
      object = new CSECouplingSource(cseObject);
    } else {
      destination = new CSECouplingSource(cseObject);
    }
  }

  @Override
  public ICSECouplingSource getDestination() {
    return destination;
  }

  @Override
  public void setPredefinedObject(String text) {
    object = new CSECouplingSource(Predefined.valueOf(text));
  }

  void setPredefinedClassification(String text) {
    destination = new CSECouplingSource(Predefined.valueOf(text));
  }

  @Override
  public IJSONContent toJSON() throws CSFormatException {
    JSONContent json = new JSONContent();
    json.setField("object", object.toString());
    json.setField("destination", destination != null ? destination.toString() : "");
    return json;
  }

  @Override
  public String toString() {
    return String.format("%s => %s", object.toString(), destination.toString());
  }
}
