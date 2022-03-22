package com.cs.core.technical.icsexpress.scope;

import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;

/**
 * @author vallee
 */
public interface ICSEEntityByRelationshipFilter extends ICSEEntityFilterNode {

  /**
   * @return the object to be filtered
   */
  ICSECouplingSource getObject();

  /**
   * @return the owner of the relationship
   */
  ICSECouplingSource getPropertyOwner();

  /**
   * @return the relationship property
   */
  ICSEProperty getProperty();

  /**
   * @return true whenever the list complement has to be considered
   */
  boolean getComplement();
}
