package com.cs.core.technical.icsexpress.scope;

import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;

import java.util.Collection;

/**
 * @author vallee
 */
public interface ICSEEntityByPropertyFilter extends ICSEEntityFilterNode {

  public enum PropertyFilter {
    contains, involves;
  }

  /**
   * @return the object to be filtered
   */
  ICSECouplingSource getObject();

  /**
   * @return the properties that must be owned by the object
   */
  Collection<ICSEProperty> getProperties();

  /**
   * @return the operator used to filter by property
   */
  PropertyFilter getPropertyFilter();
}
