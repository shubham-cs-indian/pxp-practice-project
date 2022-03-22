package com.cs.core.technical.icsexpress.actions;

import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;

/**
 * An action that consists in classifying an object
 *
 * @author vallee
 */
public interface ICSEObjectClassification extends ICSEAction {

  /**
   * @return the object to be classified
   */
  ICSECouplingSource getObject();

  /**
   * @return the classification object of destination
   */
  ICSECouplingSource getDestination();
}
