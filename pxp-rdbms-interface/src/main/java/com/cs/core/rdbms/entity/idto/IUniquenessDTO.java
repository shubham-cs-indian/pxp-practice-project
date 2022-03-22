package com.cs.core.rdbms.entity.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IUniquenessDTO extends ISimpleDTO{
  
  public static String UNIQUENESS_VIOLATION = "uniquenessViolationDTOs";
  public static String KPI_UNIQUENESS = "kpiUniqunessDTOs";
  
  public void setUniquenessViolationDTOs(List<IUniquenessViolationDTO> uniquenessViolationDTO);
  public List<IUniquenessViolationDTO> getUniquenessViolationDTOs();
  
  public void setKpiUniqunessDTOs(IKpiUniqunessDTO kpiUniqunessDTO);
  public IKpiUniqunessDTO getKpiUniqunessDTOs();
  
}