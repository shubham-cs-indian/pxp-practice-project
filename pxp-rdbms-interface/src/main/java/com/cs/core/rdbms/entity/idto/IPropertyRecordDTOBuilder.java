package com.cs.core.rdbms.entity.idto;

import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.idto.IRootDTOBuilder;

/**
 * The Builder interface to construct IPropertyRecordDTO
 * 
 * @author janak
 */
@SuppressWarnings("rawtypes")
public interface IPropertyRecordDTOBuilder<T extends IPropertyRecordDTOBuilder> extends IRootDTOBuilder<IPropertyRecordDTO>{
  
  /**
   * @param recordStatus status of record
   * @return IPropertyRecordDTOBuilder inherited class
   */
  public T recordStatus(RecordStatus recordStatus);
  
  /**
   * @param couplingType of property
   * @return IPropertyRecordDTOBuilder inherited class
   */
  public T couplingType(CouplingType couplingType);
  
  /**
   * @param isVersionable, attribute is versionable or not
   * @return IValueRecordDTOBuilder
   */
  public T isVersionable(boolean isVersionable);
  
}
