package com.cs.core.bgprocess.idto;

import java.util.Collection;
import java.util.Set;

/**
 * Nature relationship information 
 *
 */
public interface IBaseEntityPlanForBulkCloneDTO extends IBaseEntityPlanDTO {

  /**
   * @return the collection of nature relationship
   */
  public Set<String> getNatureRelationshipIds();

  /**
   * @param sets the collection of nature relationship
   */
  public void setNatureRelationshipIds(Collection<String> natureRelationshipIds);

}
