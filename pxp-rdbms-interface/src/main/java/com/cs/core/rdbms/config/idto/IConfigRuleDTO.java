package com.cs.core.rdbms.config.idto;

import com.cs.core.technical.icsexpress.rule.ICSERule;

import java.util.Collection;

/**
 * @author vallee
 */
public interface IConfigRuleDTO extends IRootConfigDTO {

  /**
   * @return the rule expression as text
   */
  public Collection<IRuleExpressionDTO> getRuleExpressions();

  /**
   * @param ruleExpressions : expressions that are to be in the current rule dto.
   */
  public void setRuleExpressions(Collection<IRuleExpressionDTO> ruleExpressions);

  /**
   * @return The type of the rule.(KPI OR RULE)
   */
  public ICSERule.RuleType getType();

  /**
   * @return When was it last refreshed by background process.
   */
  public long getLastRefreshed();
}
