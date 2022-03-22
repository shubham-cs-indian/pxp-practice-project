package com.cs.core.technical.rdbms.idto;

/**
 * root builder for all DTO builder
 * @author Janak.Gurme
 *
 * @param <T> target DTO class
 */
public interface IRootDTOBuilder<T extends ISimpleDTO> {
  
  /**
   * @return the final built DTO
   */

  public T build();
}
