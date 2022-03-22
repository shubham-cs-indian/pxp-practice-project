package com.cs.core.technical.icsexpress;

/**
 * The representation of a CSExpress meta element
 *
 * @author vallee
 */
public interface ICSEMeta extends ICSEElement {

  /**
   * @return the key code of this meta
   */
  public String getKey();

  /**
   * @return the attached value of this meta
   */
  public String getValue();
}
