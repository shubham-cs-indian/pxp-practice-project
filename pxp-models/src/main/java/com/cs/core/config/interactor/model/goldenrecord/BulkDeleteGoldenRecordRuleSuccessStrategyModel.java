package com.cs.core.config.interactor.model.goldenrecord;

import java.util.ArrayList;
import java.util.List;

public class BulkDeleteGoldenRecordRuleSuccessStrategyModel
    implements IBulkDeleteGoldenRecordRuleSuccessStrategyModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    deletedGoldenRecordRuleIds;
  protected List<String>    linkedKlassIds;
  protected List<String>    linkedTaxonomyIds;
  protected List<String>    linkedOrganizations;
  protected List<String>    linkedPhysicalcatalogIds;
  protected List<String>    linkedEndpointids;
  
  @Override
  public List<String> getDeletedGoldenRecordRuleIds()
  {
    if (deletedGoldenRecordRuleIds == null) {
      deletedGoldenRecordRuleIds = new ArrayList<>();
    }
    return deletedGoldenRecordRuleIds;
  }
  
  @Override
  public void setDeletedGoldenRecordRuleIds(List<String> deletedGoldenRecordRuleIds)
  {
    this.deletedGoldenRecordRuleIds = deletedGoldenRecordRuleIds;
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
  public void setLinkedTaxonomyIds(List<String> linkedTaxonomyIds)
  {
    this.linkedTaxonomyIds = linkedTaxonomyIds;
  }
  
  @Override
  public List<String> getLinkedOrganizations()
  {
    if (linkedOrganizations == null) {
      linkedOrganizations = new ArrayList<>();
    }
    return linkedOrganizations;
  }
  
  @Override
  public void setLinkedOrganizations(List<String> linkedOrganizations)
  {
    this.linkedOrganizations = linkedOrganizations;
  }
  
  @Override
  public List<String> getLinkedEndpointids()
  {
    if (linkedEndpointids == null) {
      linkedEndpointids = new ArrayList<>();
    }
    return linkedEndpointids;
  }
  
  @Override
  public void setLinkedEndpointids(List<String> linkedEndpointids)
  {
    this.linkedEndpointids = linkedEndpointids;
  }
  
  @Override
  public List<String> getLinkedPhysicalcatalogIds()
  {
    if (linkedPhysicalcatalogIds == null) {
      linkedPhysicalcatalogIds = new ArrayList<>();
    }
    return linkedPhysicalcatalogIds;
  }
  
  @Override
  public void setLinkedPhysicalcatalogIds(List<String> linkedPhysicalcatalogIds)
  {
    this.linkedPhysicalcatalogIds = linkedPhysicalcatalogIds;
  }
}
