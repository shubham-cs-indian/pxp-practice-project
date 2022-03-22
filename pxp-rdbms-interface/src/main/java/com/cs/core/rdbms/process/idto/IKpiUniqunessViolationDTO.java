package com.cs.core.rdbms.process.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IKpiUniqunessViolationDTO extends ISimpleDTO{
  
  public static final String RULE_EXPRESSION_IID = "ruleExpressionIID";
  public static final String SOURCE_IID = "sourceIID";
  public static final String TARGET_IID = "targetIID";
  
  public Long getRuleExpressionIID();
  
  public Long getSourceIID();
  
  public Long getTargetIID();
  
}
