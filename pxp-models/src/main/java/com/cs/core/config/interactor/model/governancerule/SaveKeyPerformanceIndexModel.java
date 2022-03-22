package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.entity.governancerule.*;
import com.cs.core.config.interactor.model.variantcontext.AddedVariantContextTagsModel;
import com.cs.core.config.interactor.model.variantcontext.IAddedVariantContextTagsModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextModifiedTagsModel;
import com.cs.core.config.interactor.model.variantcontext.VariantContextModifiedTagsModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SaveKeyPerformanceIndexModel implements ISaveKeyPerformanceIndexModel {
  
  private static final long                        serialVersionUID = 1L;
  protected String                                 id;
  protected String                                 label;
  protected String                                 frequency;
  protected IModifiedTargetFiltersModel            modifiedTargetFilters;
  protected List<IGovernanceRuleBlock>             modifiedGovernanceRuleBlocks;
  protected List<IGovernanceRule>                  addedRules;
  protected List<String>                           deletedRules;
  protected List<IModifiedKPIRuleModel>            modifiedRules;
  protected List<IAddedKPIRoleModel>               addedRoles;
  protected List<IModifiedKPIRoleModel>            modifiedRoles;
  protected List<IAddedVariantContextTagsModel>    addedTags;
  protected List<IVariantContextModifiedTagsModel> modifiedTags;
  protected List<String>                           deletedTags;
  protected List<IDrillDown>                       addedDrillDowns;
  protected List<IModifiedDrillDownModel>          modifiedDrillDowns;
  protected List<String>                           deletedDrillDowns;
  protected String                                 addedDashboardTabId;
  protected String                                 deletedDashboardTabId;
  protected List<String>                           addedOrganizationIds;
  protected List<String>                           deletedOrganizationIds;
  protected List<String>                           physicalCatalogIds;
  protected List<String>                           addedEndpoints;
  protected List<String>                           deletedEndpoints;
  protected List<String>                           availablePhysicalCatalogIds;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getFrequency()
  {
    return frequency;
  }
  
  @Override
  public void setFrequency(String frequency)
  {
    this.frequency = frequency;
  }
  
  @Override
  public List<IGovernanceRuleBlock> getModifiedGovernanceRuleBlocks()
  {
    if (modifiedGovernanceRuleBlocks == null) {
      modifiedGovernanceRuleBlocks = new ArrayList<>();
    }
    return modifiedGovernanceRuleBlocks;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleBlock.class)
  @Override
  public void setModifiedGovernanceRuleBlocks(
      List<IGovernanceRuleBlock> modifiedGovernanceRuleBlocks)
  {
    this.modifiedGovernanceRuleBlocks = modifiedGovernanceRuleBlocks;
  }
  
  @Override
  public List<IGovernanceRule> getAddedRules()
  {
    if (addedRules == null) {
      addedRules = new ArrayList<>();
    }
    return addedRules;
  }
  
  @JsonDeserialize(contentAs = GovernanceRule.class)
  @Override
  public void setAddedRules(List<IGovernanceRule> addedRules)
  {
    this.addedRules = addedRules;
  }
  
  @Override
  public List<String> getDeletedRules()
  {
    if (deletedRules == null) {
      deletedRules = new ArrayList<>();
    }
    return deletedRules;
  }
  
  @Override
  public void setDeletedRules(List<String> deletedRules)
  {
    this.deletedRules = deletedRules;
  }
  
  @Override
  public List<IModifiedKPIRuleModel> getModifiedRules()
  {
    if (modifiedRules == null) {
      modifiedRules = new ArrayList<>();
    }
    return modifiedRules;
  }
  
  @JsonDeserialize(contentAs = ModifiedKPIRuleModel.class)
  @Override
  public void setModifiedRules(List<IModifiedKPIRuleModel> modifiedRules)
  {
    this.modifiedRules = modifiedRules;
  }
  
  @Override
  public List<IAddedKPIRoleModel> getAddedRoles()
  {
    if (addedRoles == null) {
      addedRoles = new ArrayList<>();
    }
    return addedRoles;
  }
  
  @JsonDeserialize(contentAs = AddedKPIRoleModel.class)
  @Override
  public void setAddedRoles(List<IAddedKPIRoleModel> addedRoles)
  {
    this.addedRoles = addedRoles;
  }
  
  @Override
  public List<IModifiedKPIRoleModel> getModifiedRoles()
  {
    if (modifiedRoles == null) {
      modifiedRoles = new ArrayList<>();
    }
    return modifiedRoles;
  }
  
  @JsonDeserialize(contentAs = ModifiedKPIRoleModel.class)
  @Override
  public void setModifiedRoles(List<IModifiedKPIRoleModel> modifiedRoles)
  {
    this.modifiedRoles = modifiedRoles;
  }
  
  @Override
  public List<IAddedVariantContextTagsModel> getAddedTags()
  {
    if (addedTags == null) {
      addedTags = new ArrayList<>();
    }
    return addedTags;
  }
  
  @JsonDeserialize(contentAs = AddedVariantContextTagsModel.class)
  @Override
  public void setAddedTags(List<IAddedVariantContextTagsModel> addedTags)
  {
    this.addedTags = addedTags;
  }
  
  @Override
  public List<IVariantContextModifiedTagsModel> getModifiedTags()
  {
    if (modifiedTags == null) {
      modifiedTags = new ArrayList<>();
    }
    return modifiedTags;
  }
  
  @JsonDeserialize(contentAs = VariantContextModifiedTagsModel.class)
  @Override
  public void setModifiedTags(List<IVariantContextModifiedTagsModel> modifiedTags)
  {
    this.modifiedTags = modifiedTags;
  }
  
  @Override
  public List<String> getDeletedTags()
  {
    if (deletedTags == null) {
      deletedTags = new ArrayList<>();
    }
    return deletedTags;
  }
  
  @Override
  public void setDeletedTags(List<String> deletedTags)
  {
    this.deletedTags = deletedTags;
  }
  
  @Override
  public IModifiedTargetFiltersModel getModifiedTargetFilters()
  {
    return modifiedTargetFilters;
  }
  
  @JsonDeserialize(as = ModifiedTargetFiltersModel.class)
  @Override
  public void setModifiedTargetFilters(IModifiedTargetFiltersModel modifiedTargetFilters)
  {
    this.modifiedTargetFilters = modifiedTargetFilters;
  }
  
  @Override
  public List<IDrillDown> getAddedDrillDowns()
  {
    if (addedDrillDowns == null) {
      addedDrillDowns = new ArrayList<>();
    }
    return addedDrillDowns;
  }
  
  @JsonDeserialize(contentAs = DrillDown.class)
  @Override
  public void setAddedDrillDowns(List<IDrillDown> addedDrillDowns)
  {
    this.addedDrillDowns = addedDrillDowns;
  }
  
  @Override
  public List<IModifiedDrillDownModel> getModifiedDrillDowns()
  {
    if (modifiedDrillDowns == null) {
      modifiedDrillDowns = new ArrayList<>();
    }
    return modifiedDrillDowns;
  }
  
  @JsonDeserialize(contentAs = ModifiedDrillDownModel.class)
  @Override
  public void setModifiedDrillDowns(List<IModifiedDrillDownModel> modifiedDrillDowns)
  {
    this.modifiedDrillDowns = modifiedDrillDowns;
  }
  
  @Override
  public List<String> getDeletedDrillDowns()
  {
    if (deletedDrillDowns == null) {
      deletedDrillDowns = new ArrayList<>();
    }
    return deletedDrillDowns;
  }
  
  @Override
  public void setDeletedDrillDowns(List<String> deletedDrillDowns)
  {
    this.deletedDrillDowns = deletedDrillDowns;
  }
  
  @Override
  public String getDeletedDashboardTabId()
  {
    return deletedDashboardTabId;
  }
  
  @Override
  public void setDeletedDashboardTabId(String deletedDashboardTabId)
  {
    this.deletedDashboardTabId = deletedDashboardTabId;
  }
  
  @Override
  public String getAddedDashboardTabId()
  {
    return addedDashboardTabId;
  }
  
  @Override
  public void setAddedDashboardTabId(String addedDashboardTabId)
  {
    this.addedDashboardTabId = addedDashboardTabId;
  }
  
  @Override
  public List<String> getAddedOrganizationIds()
  {
    if (addedOrganizationIds == null) {
      return new ArrayList<>();
    }
    return addedOrganizationIds;
  }
  
  @Override
  public void setAddedOrganizationIds(List<String> addedOrganizationIds)
  {
    this.addedOrganizationIds = addedOrganizationIds;
  }
  
  @Override
  public List<String> getDeletedOrganizationIds()
  {
    if (deletedOrganizationIds == null) {
      return new ArrayList<>();
    }
    return deletedOrganizationIds;
  }
  
  @Override
  public void setDeletedOrganizationIds(List<String> deletedOrganizationIds)
  {
    this.deletedOrganizationIds = deletedOrganizationIds;
  }
  
  @Override
  public List<String> getPhysicalCatalogIds()
  {
    if (physicalCatalogIds == null) {
      physicalCatalogIds = new ArrayList<>();
    }
    return physicalCatalogIds;
  }
  
  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    this.physicalCatalogIds = physicalCatalogIds;
  }
  
  @Override
  public List<String> getAddedEndpoints()
  {
    if (addedEndpoints == null) {
      addedEndpoints = new ArrayList<>();
    }
    return addedEndpoints;
  }
  
  @Override
  public void setAddedEndpoints(List<String> addedEndpoints)
  {
    this.addedEndpoints = addedEndpoints;
  }
  
  @Override
  public List<String> getDeletedEndpoints()
  {
    if (deletedEndpoints == null) {
      deletedEndpoints = new ArrayList<>();
    }
    return deletedEndpoints;
  }
  
  @Override
  public void setDeletedEndpoints(List<String> deletedEndpoints)
  {
    this.deletedEndpoints = deletedEndpoints;
  }
  
  @Override
  public List<String> getAvailablePhysicalCatalogIds()
  {
    if (availablePhysicalCatalogIds == null) {
      availablePhysicalCatalogIds = new ArrayList<>();
    }
    return availablePhysicalCatalogIds;
  }
  
  @Override
  public void setAvailablePhysicalCatalogIds(List<String> availablePhysicalCatalogIds)
  {
    this.availablePhysicalCatalogIds = availablePhysicalCatalogIds;
  }
}
