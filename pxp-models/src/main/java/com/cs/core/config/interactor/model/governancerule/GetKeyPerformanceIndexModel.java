package com.cs.core.config.interactor.model.governancerule;

import java.util.Map;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.governancerule.GovernanceRuleBlock;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleBlock;
import com.cs.core.config.interactor.entity.governancerule.IKeyPerformanceIndicator;
import com.cs.core.config.interactor.entity.governancerule.KeyPerformanceIndicator;
import com.cs.core.config.interactor.entity.organization.IOrganization;
import com.cs.core.config.interactor.entity.organization.Organization;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.entity.task.Task;
import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.organization.IReferencedEndpointModel;
import com.cs.core.config.interactor.model.organization.ReferencedEndpointModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetKeyPerformanceIndexModel extends ConfigResponseWithAuditLogModel implements IGetKeyPerformanceIndexModel {
  
  private static final long                              serialVersionUID = 1L;
  protected IKeyPerformanceIndicator                     keyPerformanceIndex;
  protected Map<String, IAttribute>                      referencedAttributes;
  protected Map<String, ITag>                            referencedTags;
  protected Map<String, IRole>                           referencedRoles;
  protected Map<String, IConfigEntityInformationModel>   referencedKlasses;
  protected Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies;
  protected Map<String, IGovernanceRuleBlock>            referencedRules;
  protected Map<String, ITask>                           referencedTask;
  protected Map<String, IConfigEntityInformationModel>   referencedDashboardTabs;
  protected Map<String, IOrganization>                   referencedOraganizations;
  protected Map<String, IReferencedEndpointModel>        referencedEndpoints;
  
  @Override
  public IKeyPerformanceIndicator getKeyPerformanceIndex()
  {
    return keyPerformanceIndex;
  }
  
  @JsonDeserialize(as = KeyPerformanceIndicator.class)
  @Override
  public void setKeyPerformanceIndex(IKeyPerformanceIndicator keyPerformanceIndex)
  {
    this.keyPerformanceIndex = keyPerformanceIndex;
  }
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, IRole> getReferencedRoles()
  {
    return referencedRoles;
  }
  
  @Override
  public void setReferencedRoles(Map<String, IRole> referencedRoles)
  {
    this.referencedRoles = referencedRoles;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedKlasses(Map<String, IConfigEntityInformationModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  @Override
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @JsonDeserialize(contentAs = ReferencedArticleTaxonomyModel.class)
  @Override
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
  @Override
  public Map<String, IGovernanceRuleBlock> getReferencedRules()
  {
    return referencedRules;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleBlock.class)
  @Override
  public void setReferencedRules(Map<String, IGovernanceRuleBlock> referencedRules)
  {
    this.referencedRules = referencedRules;
  }
  
  @Override
  public Map<String, ITask> getReferencedTask()
  {
    return referencedTask;
  }
  
  @JsonDeserialize(contentAs = Task.class)
  @Override
  public void setReferencedTask(Map<String, ITask> referencedTask)
  {
    this.referencedTask = referencedTask;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedDashboardTabs()
  {
    return referencedDashboardTabs;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedDashboardTabs(
      Map<String, IConfigEntityInformationModel> referencedDashboardTabs)
  {
    this.referencedDashboardTabs = referencedDashboardTabs;
  }
  
  @Override
  public Map<String, IOrganization> getReferencedOraganizations()
  {
    return referencedOraganizations;
  }
  
  @JsonDeserialize(contentAs = Organization.class)
  @Override
  public void setReferencedOraganizations(Map<String, IOrganization> referencedOraganizations)
  {
    this.referencedOraganizations = referencedOraganizations;
  }
  
  @Override
  public Map<String, IReferencedEndpointModel> getReferencedEndpoints()
  {
    return referencedEndpoints;
  }
  
  @JsonDeserialize(contentAs = ReferencedEndpointModel.class)
  @Override
  public void setReferencedEndpoints(Map<String, IReferencedEndpointModel> referencedEndpoints)
  {
    this.referencedEndpoints = referencedEndpoints;
  }
}
