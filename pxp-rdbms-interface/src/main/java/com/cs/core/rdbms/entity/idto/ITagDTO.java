package com.cs.core.rdbms.entity.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

/**
 * DTO representation of records of recorded PXP tag
 *
 * @author Farooq Kadri
 */
public interface ITagDTO extends ISimpleDTO, Comparable {

  /**
   * @return the tag value code attached to this record
   */
  public String getTagValueCode();

  /**
   * @return the relevance linked to that tag value
   */
  public int getRange();

  /**
   * @param relevance overwritten relevance linked to that tag value
   */
  public void setRange(int relevance);
}
