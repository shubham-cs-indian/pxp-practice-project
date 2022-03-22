package com.cs.core.technical.icsexpress.scope;

import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;

/**
 * @author jamil.ahmad
 *
 */
public interface ICSEEntityByDuplicateFilter extends ICSEEntityFilterNode {

  /**
   * @return the object to be filtered
   */
  ICSECouplingSource getObject();

  /**
   *
   * @return is the value to be filtered true or false
   *
   */
  Boolean getIsDuplicate();
}
