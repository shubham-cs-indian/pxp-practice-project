package com.cs.core.config.interactor.model.organization;

import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.IConfigTaxonomyHierarchyInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IGetOrganizationModel extends IConfigResponseWithAuditLogModel {
  
  public static final String ORGANIZATION          = "organization";
  public static final String REFERENCED_ROLES      = "referencedRoles"; 
  public static final String REFERENCED_TAXONOMIES = "referencedTaxonomies";
  public static final String REFERENCED_ENDPOINTS  = "referencedEndpoints";
  public static final String REFERENCED_KLASSES    = "referencedKlasses";
  public static final String REFERENCED_SYSTEMS    = "referencedSystems";
  
  public IOrganizationModel getOrganization();
  
  public void setOrganization(IOrganizationModel organization);
  
  public Map<String, IConfigEntityInformationModel> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IConfigEntityInformationModel> referencedRoles);
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
  
  public Map<String, IReferencedEndpointModel> getReferencedEndpoints();
  
  public void setReferencedEndpoints(Map<String, IReferencedEndpointModel> referencedRoles);
  
  public Map<String, IConfigTaxonomyHierarchyInformationModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IConfigTaxonomyHierarchyInformationModel> referencedKlasses);
  
  public Map<String, IReferencedSystemModel> getReferencedSystems();
  
  public void setReferencedSystems(Map<String, IReferencedSystemModel> referencedSystems);
}
