package com.cs.core.rdbms.process.idto;

import java.util.Set;

import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag;
import com.cs.core.technical.icsexpress.rule.ICSERule;

/**
 * Dashboard information for data quality or KPI results
 *
 * @author vallee
 */
public interface IRuleResultDTO {

  /**
   * @return the type of results
   */
  public ICSERule.RuleType getType();

  /**
   * @return the rule code to which this result corresponds
   */
  public String getRuleCode();

  /**
   * @return the total number of entities involved in the rule
   */
  public long getTotalNbOfEntities();

  /**
   * @param flag a data quality flag from red to green
   * @return the number of entities involved for a data quality flag Notice: return 0 for a KPI result
   */
  public long getNbOfFlaggedEntities(ICSEPropertyQualityFlag.QualityFlag flag);

  /**
   * @param flag a data quality flag from red to green
   * @return the set of base entity IIDs that respond to a data quality level Notice: return an empty set for KPI rule
   */
  public Set<Long> getEntityIIDs(ICSEPropertyQualityFlag.QualityFlag flag);

  /**
   * @return the number of entities involved in that fails the evaluation of a rule Notice: return the total of non-green entities for a
   * data quality rule
   */
  public long getNbOfFailedEntities();

  /**
   * @return return the non-green entity IIDs for a data quality rule
   */
  public Set<Long> getEntityIIDs(boolean kpiStatus);

  public Double getKPIResult();
}
