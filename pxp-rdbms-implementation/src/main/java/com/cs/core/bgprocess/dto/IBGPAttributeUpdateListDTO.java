package com.cs.core.bgprocess.dto;

import java.util.List;

import com.cs.core.bgprocess.idto.IAttributeCalculationUpdateDTO;
import com.cs.core.bgprocess.idto.IInitializeBGProcessDTO;

public interface IBGPAttributeUpdateListDTO extends IInitializeBGProcessDTO {
  
  public List<IAttributeCalculationUpdateDTO> getCalculationUpdatedAttributes();
  
  public void setCalculationUpdatedAttributes(List<IAttributeCalculationUpdateDTO> calculationUpdatedAttributes);
  
  public String getType();
  
  public void setType(String type);
}
