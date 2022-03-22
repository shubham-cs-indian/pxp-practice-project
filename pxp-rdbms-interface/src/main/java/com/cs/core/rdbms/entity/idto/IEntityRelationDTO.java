package com.cs.core.rdbms.entity.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

/**
 * DTO representation of a relation between 2 entities across a relationship An entity relation is made of: - a reference to a relationship
 * (read only) - a side base entity IID in both sides - a side contextual object in both sides - an extension entity in case of extended
 * relation
 *
 * @author vallee
 */
public interface IEntityRelationDTO extends ISimpleDTO, Comparable {

  /**
   * @return the entity IIDs involved on the remote side of this relation
   */
  public long getOtherSideEntityIID();

  /**
   * @param entityIID overwritten entity IID involved on the remote side of this relation
   */
  public void setOtherSideEntityIID(long entityIID);

  /**
   * @return the contextual object on the other side of this relation
   */
  public IContextualDataDTO getContextualObject();

  /**
   * @return other side  contextual object on the other side of this relation
   */
  public IContextualDataDTO getOtherSideContextualObject();

  /**
   * @return other side  contextual object on the other side of this relation
   */
  public void setOtherSideContextCode(String contextCode);
  /**
   * @param contextCode overwritten context on the other side of this relation
   */
  public void setContextCode(String contextCode);

  /**
   * @param entityID overwritten entity ID involved on the remote side of this relation for import is is added
   */
  public String getOtherSideEntityID();
  
  public long getSideBaseEntityIID();
 
  public String getSideBaseEntityID();

  public void setOtherSideEntityID(String baseEntityID);



  }
