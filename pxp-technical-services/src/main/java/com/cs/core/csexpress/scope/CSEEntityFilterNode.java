package com.cs.core.csexpress.scope;

import com.cs.core.json.JSONContent;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.scope.ICSEEntityFilterNode;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * @author vallee
 */
public abstract class CSEEntityFilterNode implements ICSEEntityFilterNode {

  private final FilterNodeType type;
  private boolean notFlag = false;

  protected CSEEntityFilterNode(FilterNodeType type) {
    this.type = type;
  }

  @Override
  public boolean hasNot() {
    return notFlag;
  }

  @Override
  public void setNot(boolean notStatus) {
    notFlag = notStatus;
  }

  @Override
  public FilterNodeType getType() {
    return type;
  }

  /**
   * Defines the object specification on which the filter applies
   *
   * @param source
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public abstract void setObject(ICSECouplingSource source) throws CSFormatException;

  @Override
  public IJSONContent toJSON() throws CSFormatException {
    JSONContent json = new JSONContent();
    json.setField("type", type);
    if (notFlag) {
      json.setField("not", true);
    }
    return json;
  }
}
