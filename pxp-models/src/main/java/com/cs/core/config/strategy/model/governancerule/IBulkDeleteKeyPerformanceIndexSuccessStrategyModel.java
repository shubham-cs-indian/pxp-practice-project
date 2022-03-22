package com.cs.core.config.strategy.model.governancerule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IBulkDeleteKeyPerformanceIndexSuccessStrategyModel extends IModel {
  
  public static final String DELETED_KEY_PERFORMANCE_INDEX_IDS = "deletedKeyPerformanceIndexIds";
  public static final String DELETED_RULE_CODES                 = "deletedRuleCodes";
  
  public List<String> getDeletedKeyPerformanceIndexIds();
  
  public void setDeletedKeyPerformanceIndexIds(List<String> deletedKeyPerformanceIndexIds);
  
  public List<String> getDeletedRuleCodes();
  
  public void setDeletedRuleCodes(List<String> deletedRuleCodes);
}
