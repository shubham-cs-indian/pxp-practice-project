package com.cs.core.config.strategy.model.governancerule;

import java.util.ArrayList;
import java.util.List;

public class BulkDeleteKeyPerformanceIndexSuccessStrategyModel
    implements IBulkDeleteKeyPerformanceIndexSuccessStrategyModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    deletedKeyPerformanceIndexIds;
  protected List<String>    linkedKlassIds;
  protected List<String>    linkedTaxonomyIds;
  protected List<String>    deletedRuleCodes;
  
  @Override
  public List<String> getDeletedKeyPerformanceIndexIds()
  {
    if (deletedKeyPerformanceIndexIds == null) {
      deletedKeyPerformanceIndexIds = new ArrayList<>();
    }
    return deletedKeyPerformanceIndexIds;
  }
  
  @Override
  public void setDeletedKeyPerformanceIndexIds(List<String> deletedGovernanceRuleIds)
  {
    this.deletedKeyPerformanceIndexIds = deletedGovernanceRuleIds;
  }
  
  @Override
  public List<String> getDeletedRuleCodes()
  {
    if (deletedRuleCodes == null) {
      deletedRuleCodes = new ArrayList<String>();
    }
    return deletedRuleCodes;
  }

  @Override
  public void setDeletedRuleCodes(List<String> deletedRuleCodes)
  {
    this.deletedRuleCodes = deletedRuleCodes;
  }
}
