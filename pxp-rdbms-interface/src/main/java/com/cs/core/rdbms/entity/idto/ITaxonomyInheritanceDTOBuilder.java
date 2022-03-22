package com.cs.core.rdbms.entity.idto;

import com.cs.core.technical.rdbms.idto.IRootDTOBuilder;

/**
 * The Builder interface to construct ITaxonomyInheritanceDTO * 
 * @author Kushal
 */
public interface ITaxonomyInheritanceDTOBuilder extends IRootDTOBuilder<ITaxonomyInheritanceDTO>{
  
  /**
   * @param isResolved,  whether conflict  is resolved or not
   * @return ITaxonomyInheritanceDTOBuilder
   */
  public ITaxonomyInheritanceDTOBuilder isResolved(Boolean isResolved);

  /**
   * factory method to construct ICollectionDTO
   * @return ITaxonomyInheritanceDTO
   */
  @Override
  public ITaxonomyInheritanceDTO build();
}
