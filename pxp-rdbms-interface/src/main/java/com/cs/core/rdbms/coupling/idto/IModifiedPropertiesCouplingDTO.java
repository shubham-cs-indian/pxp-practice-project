package com.cs.core.rdbms.coupling.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IModifiedPropertiesCouplingDTO extends ISimpleDTO {
  
  public static final String PROPERTY_IID       = "propertyIID";
  public static final String COUPLINGTYPE       = "couplingType";
  public static final String COUPLINGSOURCETYPE = "couplingSourceType";
  
  public long getPropertyIID();
  public void setPropertyIID(long propertyIID);
  
  public int getCouplingType();
  public void setCouplingType(int couplingType);
  
  public int getCouplingSourceType();
  public void setCouplingSourceType(int couplingSourceType);
  
}
