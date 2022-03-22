package com.cs.core.technical.icsexpress.actions;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * An action declaration
 *
 * @author vallee
 */
public interface ICSEAction {

  /**
   * @return the type of action
   */
  public ActionType getType();

  /**
   * @return the JSON compilation result of the action
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public IJSONContent toJSON() throws CSFormatException;

  public enum ActionType {
    Assignment, Classification, QualityFlag;
  }

}
