package com.cs.core.rdbms.coupling.idto;

import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.idto.IRootDTO;

public interface ICouplingDTO extends IRootDTO{
  
  /**
   * 
   * @return targetEntityIID
   */
  public long getTargetEntityIID();
  
  /**
   * 
   * @param targetEntityIID
   */
  public void setTargetEntityIID(long targetEntityIID);
  
  /**
   * 
   * @return sourceEntityIID
   */
  public long getSourceEntityIID();
  
  /**
   * 
   * @param sourceEntityIID
   */
  public void setSourceEntityIID(long sourceEntityIID);
  
  /**
   * 
   * @return propertyiid
   */
  public long getPropertyIID();
  
  /**
   * 
   * @param propertyIID
   */
  public void setPropertyIID(long propertyIID);
  
  /**
   * 
   * @return recordStatus
   */
  public RecordStatus getRecordStatus();
  
  /**
   * 
   * @param recordStatus
   */
  public void setRecordStatus(RecordStatus recordStatus);
  
  /**
   * 
   * @return couplingType
   */
  public CouplingBehavior getCouplingType();
  
  /**
   * 
   * @param couplingType
   */
  public void setCouplingType(CouplingBehavior couplingType);
  
  /**
   * 
   * @return couplingSourceIID
   */
  public long getCouplingSourceIID();
  
  /**
   * 
   * @param couplingSourceIID
   */
  public void setCouplingSourceIID(long couplingSourceIID);
  
  /**
   * 
   * @return couplingSourceType
    */
  public CouplingType getCouplingSourceType();
  
  /**
   * 
   * @param couplingSourceType
   */
  public void setCouplingSourceType(CouplingType couplingSourceType);
  
  /**
   * 
   * @return localeIID
   */
  public long getLocaleIID();
  
  /**
   * 
   * @param localeIID
   */
  public void setLocaleIID(long localeIID);

}
