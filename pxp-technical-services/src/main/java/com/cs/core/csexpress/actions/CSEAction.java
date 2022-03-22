package com.cs.core.csexpress.actions;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.actions.ICSEAction;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * @author vallee
 */
public abstract class CSEAction implements ICSEAction {

  private final ActionType type;

  protected CSEAction(ActionType type) {
    this.type = type;
  }

  @Override
  public ActionType getType() {
    return type;
  }

  @Override
  public abstract IJSONContent toJSON() throws CSFormatException;

  /**
   * add an object definition to the current action
   *
   * @param cseObject
   */
  public abstract void setObject(CSEObject cseObject);

  /**
   * add a predefined object definition to the current action
   *
   * @param text
   */
  public abstract void setPredefinedObject(String text);
}
