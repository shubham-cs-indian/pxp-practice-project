package com.cs.core.rdbms.entity.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IKpiUniqunessDTO extends ISimpleDTO{
  
  public static final String RESULT = "result";
  public static final String RULE_EXPRESSION_IID = "ruleExpressionIID";
  public static final String BASE_ENTITY_IID = "baseEntityIID";
  public static final String SOURCE_IID = "sourceIID";
  
  /**
   * 
   * @return the result of RuleExpression in Kpi
   */
  public double getResult();
  
  /**
   * 
   * @param result of kpi ruleExpression
   */
  public void setResult(double result);
  
  /**
   * 
   * @return ruleExpressionIID of kpi
   */
  public Long getRuleExpressionIID();
  
  /**
   * 
   * @param ruleExpressionIID
   */
  public void setRuleExpressionIID(Long ruleExpressionIID);
  
  /**
   * 
   * @return baseEntityIID which is linked with ruleExpression
   */
  public List<Long> getBaseEntityIID();
  
  /**
   * 
   * @param baseEntityIID
   */
  public void setBaseEntityIID(List<Long> baseEntityIID);
  
  /**
   * 
   * @return sourceIID
   */
  public Long getSourceIID();
  
  /**
   * 
   * @param sourceIID
   */
  public void setSourceIID(long sourceIID);
  
}
