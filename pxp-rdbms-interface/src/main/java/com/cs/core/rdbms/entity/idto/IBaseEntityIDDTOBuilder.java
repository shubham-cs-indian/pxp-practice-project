package com.cs.core.rdbms.entity.idto;

import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.technical.rdbms.idto.IRootDTOBuilder;

/**
 * The Builder interface to construct IBaseEntityIDDTO
 * 
 * @author janak
 */
@SuppressWarnings("rawtypes")
public interface IBaseEntityIDDTOBuilder<T extends IBaseEntityIDDTOBuilder> extends IRootDTOBuilder<IBaseEntityIDDTO> {
  
  /**
   * @param baseEntityIID the IID of the object or 0 for creation
   * @return IBaseEntityIDDTOBuilder or its inherited classes
   */
  public T baseEntityIID(long baseEntityIID);

  /**
   * @param contextDTO the context referred by this object or empty context object
   * @return IBaseEntityIDDTOBuilder or its inherited classes
   */
  public T contextDTO(IContextDTO contextDTO);

  /**
   * @param imageIID, set the default image iid for baseEntity
   * @return IBaseEntityIDDTOBuilder or its inherited classes
   */
  public T defaultImageIID(long imageIID);

  /**
   * @param hashCode, set hash code of the assest entity
   * @return IBaseEntityIDDTOBuilder or its inherited classes
   */
  public T hashCode(String hashCode);

  /**
   * factory method to build IBaseEntityIDDTO object
   *
   * @return IBaseEntityIDDTO
   */
  @Override
  public IBaseEntityIDDTO build();

}
