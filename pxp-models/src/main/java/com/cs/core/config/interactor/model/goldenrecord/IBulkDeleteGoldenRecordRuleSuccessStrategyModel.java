package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IBulkDeleteGoldenRecordRuleSuccessStrategyModel extends IModel {
  
  public static final String DELETED_GOLDEN_RECORD_RULE_IDS = "deletedGoldenRecordRuleIds";
  public static final String LINKED_KLASS_IDS               = "linkedKlassIds";
  public static final String LINKED_TAXONOMY_IDS            = "linkedTaxonomyIds";
  public static final String LINKED_ORGANIZATIONS           = "linkedOrganizations";
  public static final String LINKED_ENDPOINTIDS             = "linkedEndpointids";
  public static final String LINKED_PHYSICALCATALOG_IDS     = "linkedPhysicalcatalogIds";
  
  public List<String> getDeletedGoldenRecordRuleIds();
  
  public void setDeletedGoldenRecordRuleIds(List<String> deletedGoldenRecordRuleIds);
  
  public List<String> getLinkedKlassIds();
  
  public void setLinkedKlassIds(List<String> linkedKlassIds);
  
  public List<String> getLinkedTaxonomyIds();
  
  public void setLinkedTaxonomyIds(List<String> linkedTaxonomyIds);
  
  public List<String> getLinkedOrganizations();
  
  public void setLinkedOrganizations(List<String> linkedOrganizations);
  
  public List<String> getLinkedEndpointids();
  
  public void setLinkedEndpointids(List<String> linkedEndpointids);
  
  public List<String> getLinkedPhysicalcatalogIds();
  
  public void setLinkedPhysicalcatalogIds(List<String> linkedPhysicalcatalogIds);
}
