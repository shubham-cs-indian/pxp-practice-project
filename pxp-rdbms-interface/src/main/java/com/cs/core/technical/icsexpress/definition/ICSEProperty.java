package com.cs.core.technical.icsexpress.definition;

/**
 * The representation of a CSExpress classifier
 *
 * @author vallee
 */
public interface ICSEProperty extends ICSEObject {

  /**
   * @return the side from specifications or 0 when not defined
   */
  public int getSide();

  /**
   * @return the context attached to this property when defined
   */
  public ICSEObject getContext();
}
