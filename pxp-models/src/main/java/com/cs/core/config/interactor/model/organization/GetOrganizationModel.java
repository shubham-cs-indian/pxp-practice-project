package com.cs.core.config.interactor.model.organization;

import java.util.Map;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.ConfigTaxonomyHierarchyInformationModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.IConfigTaxonomyHierarchyInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetOrganizationModel extends ConfigResponseWithAuditLogModel implements IGetOrganizationModel {
  
  private static final long                              serialVersionUID = 1L;
  protected IOrganizationModel                           organization;
  protected Map<String, IConfigEntityInformationModel>   referencedRoles;
  protected Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies;
  protected Map<String, IReferencedEndpointModel>        referencedEndpoints;
  protected Map<String, IConfigTaxonomyHierarchyInformationModel>   referencedKlasses;
  protected Map<String, IReferencedSystemModel>          referencedSystems;
  
  @Override
  public IOrganizationModel getOrganization()
  {
    return organization;
  }
  
  @Override
  @JsonDeserialize(as = OrganizationModel.class)
  public void setOrganization(IOrganizationModel organization)
  {
    this.organization = organization;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedRoles()
  {
    return referencedRoles;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedRoles(Map<String, IConfigEntityInformationModel> referencedRoles)
  {
    this.referencedRoles = referencedRoles;
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
  public Map<String, IReferencedEndpointModel> getReferencedEndpoints()
  {
    return referencedEndpoints;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedEndpointModel.class)
  public void setReferencedEndpoints(Map<String, IReferencedEndpointModel> referencedEndpoints)
  {
    this.referencedEndpoints = referencedEndpoints;
  }
  
  @Override
  public Map<String, IConfigTaxonomyHierarchyInformationModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @JsonDeserialize(contentAs = ConfigTaxonomyHierarchyInformationModel.class)
  @Override
  public void setReferencedKlasses(Map<String, IConfigTaxonomyHierarchyInformationModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  @Override
  public Map<String, IReferencedSystemModel> getReferencedSystems()
  {
    return referencedSystems;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedSystemModel.class)
  public void setReferencedSystems(Map<String, IReferencedSystemModel> referencedSystems)
  {
    this.referencedSystems = referencedSystems;
  }
}
