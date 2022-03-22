package com.cs.core.config.interactor.model.governancerule;

import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleBlock;
import com.cs.core.config.interactor.entity.governancerule.IKeyPerformanceIndicator;
import com.cs.core.config.interactor.entity.organization.IOrganization;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.model.organization.IReferencedEndpointModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IGetKeyPerformanceIndexModel extends IConfigResponseWithAuditLogModel {
  
  public static final String KEY_PERFORMANCE_INDEX     = "keyPerformanceIndex";
  public static final String REFERENCED_RULES          = "referencedRules";
  public static final String REFERENCED_ATTRIBUTES     = "referencedAttributes";
  public static final String REFERENCED_TAGS           = "referencedTags";
  public static final String REFERENCED_ROLES          = "referencedRoles";
  public static final String REFERENCED_TASK           = "referencedTask";
  public static final String REFERENCED_KLASSES        = "referencedKlasses";
  public static final String REFERENCED_TAXONOMIES     = "referencedTaxonomies";
  public static final String REFERENCED_DASHBOARD_TABS = "referencedDashboardTabs";
  public static final String REFERENCED_ORANIZATIONS   = "referencedOraganizations";
  public static final String REFERENCED_ENDPOINTS      = "referencedEndpoints";
  
  public IKeyPerformanceIndicator getKeyPerformanceIndex();
  
  public void setKeyPerformanceIndex(IKeyPerformanceIndicator keyPerformanceIndex);
  
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Map<String, IRole> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IRole> referencedRoles);
  
  public Map<String, IConfigEntityInformationModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IConfigEntityInformationModel> referencedKlasses);
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
  
  public Map<String, IGovernanceRuleBlock> getReferencedRules();
  
  public void setReferencedRules(Map<String, IGovernanceRuleBlock> referencedRules);
  
  public Map<String, ITask> getReferencedTask();
  
  public void setReferencedTask(Map<String, ITask> referencedTask);
  
  public Map<String, IConfigEntityInformationModel> getReferencedDashboardTabs();
  
  public void setReferencedDashboardTabs(
      Map<String, IConfigEntityInformationModel> referencedDashboardTabs);
  
  public Map<String, IOrganization> getReferencedOraganizations();
  
  public void setReferencedOraganizations(Map<String, IOrganization> referencedOraganizations);
  
  public Map<String, IReferencedEndpointModel> getReferencedEndpoints();
  
  public void setReferencedEndpoints(Map<String, IReferencedEndpointModel> referencedEndpoints);
}
