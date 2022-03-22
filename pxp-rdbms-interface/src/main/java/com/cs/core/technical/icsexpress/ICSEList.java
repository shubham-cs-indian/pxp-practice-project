package com.cs.core.technical.icsexpress;

import java.util.List;

/**
 * A CSExpress list representation
 *
 * @author vallee
 */
public interface ICSEList extends ICSEElement {

  /**
   * @return the sub-elements of this element (or void if it cannot be broken down)
   */
  public List<ICSEElement> getSubElements();

  /**
   * @param elt CSE element to add to the list
   * @return this list for addition operations
   */
  public default ICSEList addElement(ICSEElement elt) {
    this.getSubElements()
            .add(elt);
    return this;
  }

}
