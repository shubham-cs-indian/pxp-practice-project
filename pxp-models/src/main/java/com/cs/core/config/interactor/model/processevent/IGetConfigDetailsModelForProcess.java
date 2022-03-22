package com.cs.core.config.interactor.model.processevent;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.ISearchComponentAttributeInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public interface IGetConfigDetailsModelForProcess extends IModel {
  
  public static final String REFERENCED_TAXONOMIES             = "referencedTaxonomies";
  public static final String REFERENCED_KLASSES                = "referencedKlasses";
  public static final String REFERENCED_USERS                  = "referencedUsers";
  public static final String REFERENCED_ORGANIZATIONS          = "referencedOrganizations";
  public static final String REFERENCED_TASKS                  = "referencedTasks";
  public static final String REFERENCED_ATTRIBUTES             = "referencedAttributes";
  public static final String REFERENCED_TAGS                   = "referencedTags";
  public static final String REFERENCED_CONTEXTS               = "referencedContexts";
  public static final String REFERENCED_RELATIONSHIPS          = "referencedRelationships";
  public static final String REFERENCED_USERS_IDS              = "referencedUsersIds";
  public static final String REFERENCED_ORGANIZATIONS_IDS      = "referencedOrganizationsIds";
  public static final String REFERENCED_TASKS_IDS              = "referencedTasksIds";
  public static final String REFERENCED_CONTEXTS_IDS           = "referencedContextsIds";
  public static final String REFERENCED_RELATIONSHIPS_IDS      = "referencedRelationshipsIds";
  public static final String REFERENCED_ENDPOINTS              = "referencedEndpoints";
  public static final String REFERENCED_LANGUAGES              = "referencedLanguages";
  public static final String REFERENCED_REFERENCES             = "referencedReferences";
  public static final String REFERENCED_AUTHORIZATION_MAPPINGS = "referencedAuthorizationMappings";
  public static final String REFERENCED_ROLES                  = "referencedRoles";
  public static final String REFERENCED_ROLES_IDS              = "referencedRolesIds";
  public static final String REFERENCED_NON_NATURE_KLASSES     = "referencedNonNatureKlasses";
  public static final String REFERENCED_MAPPINGS               = "referencedMappings";
  public static final String REFERENCED_DESTINATION_ORGANIZATIONS = "referencedDestinationOrganizations";
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
  
  public Map<String, IConfigEntityInformationModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IConfigEntityInformationModel> referencedKlasses);
  
  public Map<String, IConfigEntityInformationModel> getReferencedUsers();
  
  public void setReferencedUsers(Map<String, IConfigEntityInformationModel> referencedUsers);
  
  public Map<String, IConfigEntityInformationModel> getReferencedOrganizations();
  
  public void setReferencedOrganizations(
      Map<String, IConfigEntityInformationModel> referencedOrganizations);
  
  public Map<String, IConfigEntityInformationModel> getReferencedTasks();
  
  public void setReferencedTasks(Map<String, IConfigEntityInformationModel> referencedTasks);
  
  public Map<String, ISearchComponentAttributeInfoModel> getReferencedAttributes();
  
  public void setReferencedAttributes(
      Map<String, ISearchComponentAttributeInfoModel> referencedAttributes);
  
  public Map<String, IConfigTagInformationModel> getReferencedTags();
  
  public void setReferencedTags(Map<String, IConfigTagInformationModel> referencedTags);
  
  public Map<String, IConfigEntityInformationModel> getReferencedContexts();
  
  public void setReferencedContexts(Map<String, IConfigEntityInformationModel> referencedContexts);
  
  public Map<String, IConfigEntityInformationModel> getReferencedRelationships();
  
  public void setReferencedRelationships(
      Map<String, IConfigEntityInformationModel> referencedRelationships);
  
  public List<String> getReferencedUsersIds();
  
  public void setReferencedUsersIds(List<String> referencedUsersIds);
  
  public List<String> getReferencedOrganizationsIds();
  
  public void setReferencedOrganizationsIds(List<String> referencedOrganizationsIds);
  
  public List<String> getReferencedTasksIds();
  
  public void setReferencedTasksIds(List<String> referencedTasksIds);
  
  public List<String> getReferencedContextsIds();
  
  public void setReferencedContextsIds(List<String> referencedContextsIds);
  
  public List<String> getReferencedRelationshipsIds();
  
  public void setReferencedRelationshipsIds(List<String> referencedRelationshipsIds);
  
  public Map<String, IConfigEntityInformationModel> getReferencedEndpoints();
  
  public void setReferencedEndpoints(
      Map<String, IConfigEntityInformationModel> referencedEndpoints);
  
  public Map<String, IConfigEntityInformationModel> getReferencedLanguages();
  
  public void setReferencedLanguages(
      Map<String, IConfigEntityInformationModel> referencedLanguages);
  
  public Map<String, IConfigEntityInformationModel> getReferencedReferences();
  
  public void setReferencedReferences(
      Map<String, IConfigEntityInformationModel> referencedReferences);
  
  public Map<String, IConfigEntityInformationModel> getReferencedAuthorizationMappings();
  
  public void setReferencedAuthorizationMappings(
      Map<String, IConfigEntityInformationModel> referencedAuthorizationMappings);
  
  public Map<String, IConfigEntityInformationModel> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IConfigEntityInformationModel> referencedRoles);
  
  public List<String> getReferencedRolesIds();
  
  public void setReferencedRolesIds(List<String> referencedRolesIds);

  public Map<String, IConfigEntityInformationModel> getReferencedNonNatureKlasses();

  public void setReferencedNonNatureKlasses(Map<String, IConfigEntityInformationModel> referencedNonNatureKlasses);

  public Map<String, IConfigEntityInformationModel> getReferencedMappings();
  
  public void setReferencedMappings(Map<String, IConfigEntityInformationModel> referencedMappings);
  
  public Map<String, IConfigEntityInformationModel> getReferencedDestinationOrganizations();
  
  public void setReferencedDestinationOrganizations(
      Map<String, IConfigEntityInformationModel> referencedDestinationOrganizations);
}
