package com.cs.core.config.strategy.model.governancerule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IBulkDeleteGovernanceRuleSuccessStrategyModel extends IModel {
  
  public static final String DELETED_GOVERNANCE_RULE_IDS = "deletedGovernanceRuleIds";
  public static final String LINKED_KLASS_IDS            = "linkedKlassIds";
  public static final String LINKED_TAXONOMY_IDS         = "linkedTaxonomyIds";
  
  public List<String> getDeletedGovernanceRuleIds();
  
  public void setDeletedGovernanceRuleIds(List<String> deletedGovernanceRuleIds);
  
  public List<String> getLinkedKlassIds();
  
  public void setLinkedKlassIds(List<String> linkedKlassIds);
  
  public List<String> getLinkedTaxonomyIds();
  
  public void setLinkedTaxonomyIds(List<String> linkedtaxonomyIds);
}
