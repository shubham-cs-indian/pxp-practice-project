package com.cs.core.technical.icsexpress.scope;

import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;

/**
 * @author vallee
 */
public interface ICSEEntityByExpiryFilter extends ICSEEntityFilterNode {

  /**
   * @return the object to be filtered
   */
  ICSECouplingSource getObject();

  /**
   *
   * @return is the value to be filtered true or false
   *
   */
  Boolean getIsExpired();
}
