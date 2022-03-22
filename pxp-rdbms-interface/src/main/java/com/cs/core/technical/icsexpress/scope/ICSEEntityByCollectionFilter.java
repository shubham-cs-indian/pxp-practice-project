package com.cs.core.technical.icsexpress.scope;

import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.definition.ICSEObject;

import java.util.Collection;

/**
 * @author vallee
 */
public interface ICSEEntityByCollectionFilter extends ICSEEntityFilterNode {

  /**
   * @return the object to be filtered
   */
  ICSECouplingSource getObject();

  /**
   * @return the concerned contexts
   */
  ICSEObject getCollection();
}
