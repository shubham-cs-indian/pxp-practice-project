package com.cs.core.bgprocess.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IDeleteGoldenRecordBucketDTO extends ISimpleDTO {
  
  public void setRuleId(String ruleId);
  
  public String getRuleId();
}
