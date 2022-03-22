package com.cs.core.technical.icsexpress.scope;

import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;

import java.util.Collection;

/**
 * @author vallee
 */
public interface ICSEEntityByQualityFilter extends ICSEEntityFilterNode {

  /**
   * @return the object to be filtered
   */
  ICSECouplingSource getObject();

  /**
   * @return the concerned quality levels
   */
  Collection<ICSEPropertyQualityFlag.QualityFlag> getFlags();
}
