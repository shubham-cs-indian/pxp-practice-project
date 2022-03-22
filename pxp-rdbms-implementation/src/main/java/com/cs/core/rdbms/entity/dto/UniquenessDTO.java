package com.cs.core.rdbms.entity.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.entity.idto.IKpiUniqunessDTO;
import com.cs.core.rdbms.entity.idto.IUniquenessDTO;
import com.cs.core.rdbms.entity.idto.IUniquenessViolationDTO;
import com.cs.core.technical.exception.CSFormatException;

public class UniquenessDTO extends SimpleDTO implements IUniquenessDTO {
  
  private static final long            serialVersionUID        = 1L;
  public List<IUniquenessViolationDTO> uniquenessViolationDTOs = new ArrayList<>();
  public IKpiUniqunessDTO  kpiUniquenessDTOs = new KpiUniqunessDTO();
  
  @Override
  @SuppressWarnings("unchecked")
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    
    json.getJSONArray(UNIQUENESS_VIOLATION).forEach((iid) -> {
      IUniquenessViolationDTO uniquenessViolationDTO = new UniquenessViolationDTO();
      try {
        uniquenessViolationDTO.fromJSON(iid.toString());
        uniquenessViolationDTOs.add(uniquenessViolationDTO);
      }
      catch (CSFormatException e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    });
    
    kpiUniquenessDTOs.fromJSON(json.getJSONParser(KPI_UNIQUENESS).toString());

  }
  
  @Override
  public void setUniquenessViolationDTOs(List<IUniquenessViolationDTO> uniquenessViolationDTOs)
  {
    this.uniquenessViolationDTOs = uniquenessViolationDTOs;
  }
  
  @Override
  public List<IUniquenessViolationDTO> getUniquenessViolationDTOs()
  {
    return uniquenessViolationDTOs;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONArray(UNIQUENESS_VIOLATION, uniquenessViolationDTOs), 
        JSONBuilder.newJSONField(KPI_UNIQUENESS, kpiUniquenessDTOs.toJSONBuffer()));
  }

  @Override
  public void setKpiUniqunessDTOs(IKpiUniqunessDTO kpiUniqunessDTO)
  {
    this.kpiUniquenessDTOs = kpiUniqunessDTO;
  }

  @Override
  public IKpiUniqunessDTO getKpiUniqunessDTOs()
  {
    return kpiUniquenessDTOs;
  }
  
}
