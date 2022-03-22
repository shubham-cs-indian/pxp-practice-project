package com.cs.core.rdbms.entity.idto;

import com.cs.core.technical.rdbms.idto.IRootDTOBuilder;

/**
 * The Builder interface to construct IEntityRelationDTO
 * 
 * @author janak
 */

public interface IEntityRelationDTOBuilder extends IRootDTOBuilder<IEntityRelationDTO> {
  
  /**
   * @param otherSideBaseEntityIID overwritten entity IID involved on the remote side of this relation
   * @return IEntityRelationDTOBuilder
   *
   */
  public IEntityRelationDTOBuilder otherSideEntityIID(long otherSideBaseEntityIID);
  
  /**
   * @param otherSideBaseEntityID overwritten entity ID involved on the remote side of this relation
   * @return IEntityRelationDTOBuilder
   *
   */
  public IEntityRelationDTOBuilder OtherSideEntityID(String OtherSideEntityID);
  
  /**
   * @param contextCode overwritten context on the other side of this relation
   * @return IEntityRelationDTOBuilder
   * 
   */
  public IEntityRelationDTOBuilder contextCode(String contextCode);
  
}
