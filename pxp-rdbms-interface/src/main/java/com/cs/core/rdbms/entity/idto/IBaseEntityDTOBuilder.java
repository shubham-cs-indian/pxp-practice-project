package com.cs.core.rdbms.entity.idto;

import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.technical.exception.CSFormatException;

/**
 * The Builder interface to construct IBaseEntityDTO
 * 
 * @author janak
 */
public interface IBaseEntityDTOBuilder extends IBaseEntityIDDTOBuilder<IBaseEntityDTOBuilder> {
  
  /**
   * Set parent ID incase of contextual base entity
   *
   * @param parentID the parent identifier
   * @return the BaseEntityDTOBuilder
   */
  public IBaseEntityDTOBuilder parentID(String parentID);


  /**
   * Set parentIID in case of contextual base entity
   *
   * @param parentIID    the parent identifier
   * @param embeddedType the type of embedded relation
   * @return the BaseEntityDTOBuilder
   */
  public IBaseEntityDTOBuilder parentIID(long parentIID, EmbeddedType embeddedType);

  /**
   * Set parent information
   *
   * @param topParentID the overriden top parent ID
   * @return the BaseEntityDTOBuilder
   */
  public IBaseEntityDTOBuilder topParentID(String topParentID);

  /**
   * Set top parent information
   *
   * @param topParentIID the overwritten top parent
   * @return IBaseEntityDTOBuilder
   */
  public IBaseEntityDTOBuilder topParentIID(long topParentIID);

  /**
   * @param originBaseEntityIID overwritten source BaseEntity IID
   * @return IBaseEntityDTOBuilder
   */
  public IBaseEntityDTOBuilder originBaseEntityIID(long originBaseEntityIID);

  /**
   * source catalog code
   *
   * @param catalogCode overwritten source catalog
   * @return IBaseEntityDTOBuilder
   */
  public IBaseEntityDTOBuilder sourceCatalogCode(String catalogCode);

  /**
   * @param jsonContent overwritten extension part
   * @return IBaseEntityDTOBuilder
   * @throws CSFormatException in case of format error
   */
  public IBaseEntityDTOBuilder entityExtension(String jsonContent) throws CSFormatException;

  /**
   * @param records overwritten value records
   * @return IBaseEntityDTOBuilder
   */
  public IBaseEntityDTOBuilder propertyRecords(IPropertyRecordDTO... records);

  /**
   * add property record in existing records
   *
   * @param record overwritten record
   * @return IBaseEntityDTOBuilder
   */
  public IBaseEntityDTOBuilder propertyRecord(IPropertyRecordDTO record);

  /**
   * @param isCLoned weather enitity is cloned or normally created
   * @return IBaseEntityDTOBuilder
   */
  public IBaseEntityDTOBuilder isClone(boolean isCloned);
  
  /**
   * endpoint code
   *
   * @param endpoint overwritten endpoint code
   * @return IBaseEntityDTOBuilder
   */
  public IBaseEntityDTOBuilder endpointCode(String endpointCode);
  
  /**
  * factory method to build IBaseEntityDTO object
  *
  * @return IBaseEntityDTO
  */
 @Override
 public IBaseEntityDTO build();


}
