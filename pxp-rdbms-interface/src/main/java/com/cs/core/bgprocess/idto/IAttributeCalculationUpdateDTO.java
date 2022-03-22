package com.cs.core.bgprocess.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IAttributeCalculationUpdateDTO extends ISimpleDTO {
  
  public Long getPropertyIID();
  
  public void setPropertyIID(Long propertyIID);
  
  public String getExistingCalculation();
  
  public void setExistingCalculation(String existingCalculation);
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);  
}
