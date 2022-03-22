package com.cs.core.technical.icsexpress.definition;

/**
 * The representation of a CSExpress tag value
 *
 * @author frva
 */
public interface ICSETagValue extends ICSEObject {

  /**
   * @return the master tag property when defined or null
   */
  public ICSEProperty getMasterProperty();

  /**
   * @return the range of the tag or 100 per default
   */
  public int getRange();

  /**
   * @return true when the range has been attributed per default
   */
  public boolean isDefaultRange();
}
