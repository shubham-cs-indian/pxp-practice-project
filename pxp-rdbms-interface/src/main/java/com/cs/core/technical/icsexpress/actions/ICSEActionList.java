package com.cs.core.technical.icsexpress.actions;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;

import java.util.List;

/**
 * formal implementation of an action list
 *
 * @author vallee
 */
public interface ICSEActionList extends List<ICSEAction> {

  /**
   * @return the JSON compilation result of the action
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public IJSONContent toJSON() throws CSFormatException;
}
