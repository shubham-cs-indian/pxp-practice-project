package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.entity.governancerule.IDrillDown;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRule;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleBlock;
import com.cs.core.config.interactor.model.variantcontext.IAddedVariantContextTagsModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextModifiedTagsModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveKeyPerformanceIndexModel extends IModel {
  
  public static final String ID                              = "id";
  public static final String LABEL                           = "label";
  public static final String FREQUENCY                       = "frequency";
  public static final String MODIFIED_TARGET_FILTERS         = "modifiedTargetFilters";
  public static final String MODIFIED_GOVERNANCE_RULE_BLOCKS = "modifiedGovernanceRuleBlocks";
  public static final String ADDED_RULES                     = "addedRules";
  public static final String DELETED_RULES                   = "deletedRules";
  public static final String MODIFIED_RULES                  = "modifiedRules";
  public static final String ADDED_ROLES                     = "addedRoles";
  public static final String MODIFIED_ROLES                  = "modifiedRoles";
  public static final String ADDED_TAGS                      = "addedTags";
  public static final String DELETED_TAGS                    = "deletedTags";
  public static final String MODIFIED_TAGS                   = "modifiedTags";
  public static final String ADDED_DRILL_DOWNS               = "addedDrillDowns";
  public static final String MODIFIED_DRILL_DOWNS            = "modifiedDrillDowns";
  public static final String DELETED_DRILL_DOWNS             = "deletedDrillDowns";
  public static final String ADDED_DASHBOARD_TAB_ID          = "addedDashboardTabId";
  public static final String DELETED_DASHBOARD_TAB_ID        = "deletedDashboardTabId";
  public static final String ADDED_ORGANIZATION_IDS          = "addedOrganizationIds";
  public static final String DELETED_ORGANIZATION_IDS        = "deletedOrganizationIds";
  public static final String PHYSICAL_CATALOG_IDS            = "physicalCatalogIds";
  public static final String ADDED_ENDPOINTS                 = "addedEndpoints";
  public static final String DELETED_ENDPOINTS               = "deletedEndpoints";
  public static final String AVAILABLE_PHYSICAL_CATALOG_LIST = "availablePhysicalCatalogIds";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getFrequency();
  
  public void setFrequency(String frequency);
  
  public IModifiedTargetFiltersModel getModifiedTargetFilters();
  
  public void setModifiedTargetFilters(IModifiedTargetFiltersModel modifiedTargetFilters);
  
  public List<IGovernanceRuleBlock> getModifiedGovernanceRuleBlocks();
  
  public void setModifiedGovernanceRuleBlocks(
      List<IGovernanceRuleBlock> modifiedGovernanceRuleBlocks);
  
  public List<IGovernanceRule> getAddedRules();
  
  public void setAddedRules(List<IGovernanceRule> addedRules);
  
  public List<String> getDeletedRules();
  
  public void setDeletedRules(List<String> deletedRules);
  
  public List<IModifiedKPIRuleModel> getModifiedRules();
  
  public void setModifiedRules(List<IModifiedKPIRuleModel> modifiedRules);
  
  public List<IAddedKPIRoleModel> getAddedRoles();
  
  public void setAddedRoles(List<IAddedKPIRoleModel> addedRoles);
  
  public List<IModifiedKPIRoleModel> getModifiedRoles();
  
  public void setModifiedRoles(List<IModifiedKPIRoleModel> modifiedRoles);
  
  public List<IAddedVariantContextTagsModel> getAddedTags();
  
  public void setAddedTags(List<IAddedVariantContextTagsModel> addedTags);
  
  public List<IVariantContextModifiedTagsModel> getModifiedTags();
  
  public void setModifiedTags(List<IVariantContextModifiedTagsModel> modifiedTags);
  
  public List<String> getDeletedTags();
  
  public void setDeletedTags(List<String> deletedTags);
  
  public List<IDrillDown> getAddedDrillDowns();
  
  public void setAddedDrillDowns(List<IDrillDown> addedDrillDowns);
  
  public List<IModifiedDrillDownModel> getModifiedDrillDowns();
  
  public void setModifiedDrillDowns(List<IModifiedDrillDownModel> modifiedDrillDowns);
  
  public List<String> getDeletedDrillDowns();
  
  public void setDeletedDrillDowns(List<String> deletedDrillDowns);
  
  public String getAddedDashboardTabId();
  
  public void setAddedDashboardTabId(String addedDashboardTabId);
  
  public String getDeletedDashboardTabId();
  
  public void setDeletedDashboardTabId(String deletedDashboardTabId);
  
  public List<String> getAddedOrganizationIds();
  
  public void setAddedOrganizationIds(List<String> addedOrganizationIds);
  
  public List<String> getDeletedOrganizationIds();
  
  public void setDeletedOrganizationIds(List<String> deletedOrganizationIds);
  
  public List<String> getPhysicalCatalogIds();
  
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
  
  public List<String> getAddedEndpoints();
  
  public void setAddedEndpoints(List<String> addedEndpoints);
  
  public List<String> getDeletedEndpoints();
  
  public void setDeletedEndpoints(List<String> deletedEndpoints);
  
  public List<String> getAvailablePhysicalCatalogIds();
  
  public void setAvailablePhysicalCatalogIds(List<String> availablePhysicalCatalogIds);
}
