package com.cs.core.rdbms.entity.idto;

import com.cs.core.technical.rdbms.idto.IRootDTO;


/**
 * Represents a Taxonomy Inheritance information
 *
 * @author Kushal
 */

public interface ITaxonomyInheritanceDTO extends IRootDTO {

  /**
   * @return entityIID 
   */
  public long getEntityIID();
  
  /**
   * @return sourceEntityIID, (IID of Base Article instance)
   */
  public long getSourceEntityIID();
  
  /**
   * @return propertyIID 
   */
  public long getPropertyIID();
  
  /**
   * @return isResloved, whether the Taxonomy Inheritance conflict is resolved or not 
   */
  public Boolean getIsResolved();
  
  /**
   * @param isResolved
   */
  public void setIsResolved(Boolean isResolved);
  
}
