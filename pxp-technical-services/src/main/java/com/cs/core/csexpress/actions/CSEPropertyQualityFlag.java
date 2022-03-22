package com.cs.core.csexpress.actions;

import com.cs.core.csexpress.coupling.CSECouplingSource;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.csexpress.definition.CSEProperty;
import com.cs.core.data.Text;
import com.cs.core.json.JSONContent;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource.Predefined;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * @author vallee
 */
public class CSEPropertyQualityFlag extends CSEAction implements ICSEPropertyQualityFlag {

  private CSECouplingSource owner = new CSECouplingSource(Predefined.$entity);
  private CSEProperty property = null;
  private ICSEPropertyQualityFlag.QualityFlag flag = ICSEPropertyQualityFlag.QualityFlag.$green;
  private String message = "";

  public CSEPropertyQualityFlag() {
    super(ActionType.QualityFlag);
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
  public QualityFlag getFlag() {
    return flag;
  }

  /**
   * @param flag overwritten quality flag
   */
  public void setFlag(String flag) {
    this.flag = ICSEPropertyQualityFlag.QualityFlag.valueOf(flag);
  }

  @Override
  public String getMessage() {
    return message;
  }

  /**
   * @param msg overwritten user message
   */
  public void setMessage(String msg) {
    this.message = msg;
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
  public IJSONContent toJSON() throws CSFormatException {
    JSONContent json = new JSONContent();
    json.setField("target", owner.toString());
    json.setField("property", property != null ? property.toString() : "");
    json.setField("flag", flag.toString());
    json.setField("message", message);
    return json;
  }

  @Override
  public String toString() {
    return String.format("%s.%s >> %s%s", owner.toString(), property.toString(), flag.toString(),
            !message.isEmpty() ? "(" + Text.escapeStringWithQuotes(message) + ")" : "");
  }
}
