package com.cs.core.technical.icsexpress.definition;

import com.cs.core.technical.icsexpress.ICSEElement;

/**
 * Abstract representation of a object of CS Expression (DSL CSExpress) i.e. entity, property, context, classifier or record
 *
 * @author vallee
 */
public interface ICSEObject extends ICSEElement {

  /**
   * @return the type of object
   */
  public CSEObjectType getObjectType();

  /**
   * @return the code value of this object
   */
  public String getCode();

  /**
   * @return the iid of this object or 0 when not defined
   */
  public long getIID();
  
  /**
   * @return true if this is a null object
   */
  public boolean isNull();
}
