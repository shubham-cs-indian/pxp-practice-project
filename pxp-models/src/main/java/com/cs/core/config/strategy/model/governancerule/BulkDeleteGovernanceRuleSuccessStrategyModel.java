package com.cs.core.config.strategy.model.governancerule;

import java.util.ArrayList;
import java.util.List;

public class BulkDeleteGovernanceRuleSuccessStrategyModel
    implements IBulkDeleteGovernanceRuleSuccessStrategyModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    deleteGovernanceRuleIds;
  protected List<String>    linkedKlassIds;
  protected List<String>    linkedTaxonomyIds;
  
  @Override
  public List<String> getDeletedGovernanceRuleIds()
  {
    if (deleteGovernanceRuleIds == null) {
      deleteGovernanceRuleIds = new ArrayList<>();
    }
    return deleteGovernanceRuleIds;
  }
  
  @Override
  public void setDeletedGovernanceRuleIds(List<String> deletedGovernanceRuleIds)
  {
    this.deleteGovernanceRuleIds = deletedGovernanceRuleIds;
  }
  
  @Override
  public List<String> getLinkedKlassIds()
  {
    if (linkedKlassIds == null) {
      linkedKlassIds = new ArrayList<>();
    }
    return linkedKlassIds;
  }
  
  @Override
  public void setLinkedKlassIds(List<String> linkedKlassIds)
  {
    this.linkedKlassIds = linkedKlassIds;
  }
  
  @Override
  public List<String> getLinkedTaxonomyIds()
  {
    if (linkedTaxonomyIds == null) {
      linkedTaxonomyIds = new ArrayList<>();
    }
    return linkedTaxonomyIds;
  }
  
  @Override
  public void setLinkedTaxonomyIds(List<String> linkedtaxonomyIds)
  {
    this.linkedTaxonomyIds = linkedtaxonomyIds;
  }
}
