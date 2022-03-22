package com.cs.core.rdbms.process.dto;

import com.cs.core.rdbms.process.idto.IRuleResultDTO;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag.QualityFlag;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author vallee
 */
public class RuleResultDTO implements IRuleResultDTO {

  private RuleType                    ruleType;
  private String                      ruleCode;
  private long                        totalNbOfEntities  = 0l;
  private Map<QualityFlag, Set<Long>> flaggedEntityIds   = new HashMap<>();
  private Set<Long>                   kpiEntityIds       = new HashSet<>();
  private Map<QualityFlag, Long>      flaggedEntityCount = new HashMap<>();
  private Double                         kpiResult          = .0;
  
  public RuleResultDTO(RuleType ruleType, String ruleCode) {
    super();
    this.ruleType = ruleType;
    this.ruleCode = ruleCode;
  }

  public RuleResultDTO(RuleType ruleType) {
    super();
    this.ruleType = ruleType;
  }
  @Override
  public RuleType getType() {
    return ruleType;
  }

  @Override
  public String getRuleCode() {
    return ruleCode;
  }

  @Override
  public long getTotalNbOfEntities() {
    return totalNbOfEntities;
  }

  @Override
  public long getNbOfFlaggedEntities(QualityFlag flag) {
    if (ruleType.equals(RuleType.dataquality)) {
      return flaggedEntityIds.get(flag) == null ? 0 : flaggedEntityIds.get(flag).size();
    }
    return 0l;
  }

  @Override
  public Set<Long> getEntityIIDs(QualityFlag flag) {

    if (ruleType.equals(RuleType.dataquality)) {
      return flaggedEntityIds.get(flag);
    }
    return new HashSet<>();
  }

  @Override
  public long getNbOfFailedEntities() {
    if (ruleType.equals(RuleType.dataquality)) {
			return flaggedEntityIds.values()
					.stream()
					.flatMap(x -> x.stream())
					.collect(Collectors.toSet()).size();
    }
    return 0l;
  }

  @Override
  public Set<Long> getEntityIIDs(boolean kpiStatus) {

    if (ruleType.equals(RuleType.dataquality)) {
      Predicate<Entry<QualityFlag, Set<Long>>> qualityFlagFilter =
          kpiStatus
              ? x -> x.getKey().equals(QualityFlag.$green)
              : x -> !x.getKey().equals(QualityFlag.$green);

      return flaggedEntityIds
          .entrySet()
          .stream()
          .filter(x -> qualityFlagFilter.test(x))
          .flatMap(x -> x.getValue().stream())
          .collect(Collectors.toSet());
    }
    return new HashSet<>();
  } 
  
  public void fillDataQualityViolations(QualityFlag qualityFlag, Set<Long> baseentityiids, long count)
  {
    flaggedEntityIds.put(qualityFlag, baseentityiids);
    flaggedEntityCount.put(qualityFlag, count);
  }
  
  public void setTotalNumberOfEntities(long count)
  {
    totalNbOfEntities = count;
  }
  
  @Override
  public Double getKPIResult()
  {
    return kpiResult;
  }

  public void fillKPI(Double kpi, long totalNbOfEntities)
  {
    kpiResult = kpi;
    this.totalNbOfEntities = totalNbOfEntities;
  }
}
