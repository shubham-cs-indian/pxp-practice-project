package com.cs.core.rdbms.process.dto;

import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.process.idto.IKpiUniquenessViolationDTOBuilder;
import com.cs.core.rdbms.process.idto.IKpiUniqunessViolationDTO;
import com.cs.core.technical.exception.CSFormatException;

public class KpiUniquenessViolationDTO extends SimpleDTO implements IKpiUniqunessViolationDTO {

  public Long ruleExpressionIID;
  public Long sourceIID;
  public Long TargetIID;
  
  
  public KpiUniquenessViolationDTO(Long ruleExpressionIID, Long sourceIID, Long targetIID) {
    this.ruleExpressionIID = ruleExpressionIID;
    this.sourceIID = sourceIID;
    this.TargetIID = targetIID;
  }
  
  private static final long serialVersionUID = 1L;

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return null;
  }

  @Override
  public Long getRuleExpressionIID()
  {
    return ruleExpressionIID;
  }

  @Override
  public Long getSourceIID()
  {
    return sourceIID;
  }

  @Override
  public Long getTargetIID()
  {
    return TargetIID;
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    // TODO Auto-generated method stub
    
  }
  
  public static class KpiUniqunessViolationDTOBuilder implements IKpiUniquenessViolationDTOBuilder{
    
    KpiUniquenessViolationDTO kpiUniquenessViolationDTO =  null;
    
    public void KpiUniquenessViolationDTOBuilder(Long ruleExpressionIID, Long sourceIID, Long targetIID) {
      
      kpiUniquenessViolationDTO = new KpiUniquenessViolationDTO(ruleExpressionIID, sourceIID, targetIID);
      
    }
    
    @Override
    public IKpiUniqunessViolationDTO build()
    {
      return kpiUniquenessViolationDTO;
    }
    
  }
  
}
