package com.cs.core.technical.icsexpress.scope;

import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;

import java.util.Collection;

/**
 * @author vallee
 */
public interface ICSEEntityByClassifierFilter extends ICSEEntityFilterNode {

  public enum FilterOperator {
    in, under, is;
  }

  /**
   * @return the object to be filtered
   */
  ICSECouplingSource getObject();

  /**
   * @return the filter operator that applies on the object
   */
  FilterOperator getOperator();

  /**
   * @return the concerned classifiers
   */
  Collection<ICSECouplingSource> getClassifiers();

  /**
   * @return the status of nature classifier
   */
  boolean containsNatureClass();
}
