package com.cs.core.config.interactor.model.processevent;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.configdetails.SearchComponentAttributeInfoModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.ISearchComponentAttributeInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetConfigDetailsModelForProcess implements IGetConfigDetailsModelForProcess {
  
  private static final long                                 serialVersionUID                = 1L;
  
  protected Map<String, IReferencedArticleTaxonomyModel>    referencedTaxonomies            = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel>      referencedKlasses               = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel>      referencedUsers                 = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel>      referencedOrganizations         = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel>      referencedTasks                 = new HashMap<>();
  protected Map<String, ISearchComponentAttributeInfoModel> referencedAttributes            = new HashMap<>();
  protected Map<String, IConfigTagInformationModel>         referencedTags                  = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel>      referencedContexts              = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel>      referencedRelationships         = new HashMap<>();
  protected List<String>                                    referencedUsersIds              = new ArrayList<>();
  protected List<String>                                    referencedOrganizationsIds      = new ArrayList<>();
  protected List<String>                                    referencedTasksIds              = new ArrayList<>();
  protected List<String>                                    referencedContextsIds           = new ArrayList<>();
  protected List<String>                                    referencedRelationshipsIds      = new ArrayList<>();
  protected Map<String, IConfigEntityInformationModel>      referencedEndpoints             = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel>      referencedLanguages             = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel>      referencedReferences            = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel>      referencedAuthorizationMappings = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel>      referencedRoles                 = new HashMap<>();
  protected List<String>                                    referencedRolesIds              = new ArrayList<>();
  protected Map<String, IConfigEntityInformationModel>      referencedNonNatureKlasses      = new HashMap<>();
  protected Map<String, IConfigEntityInformationModel>      referencedMappings              = new HashMap<>(); 
  protected Map<String, IConfigEntityInformationModel>      referencedDestinationOrganizations = new HashMap<>();  
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedNonNatureKlasses()
  {
    return referencedNonNatureKlasses;
  }

  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedNonNatureKlasses(Map<String, IConfigEntityInformationModel> referencedNonNatureKlasses)
  {
    this.referencedNonNatureKlasses = referencedNonNatureKlasses;
  }

  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedKlasses(Map<String, IConfigEntityInformationModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedUsers()
  {
    return referencedUsers;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedUsers(Map<String, IConfigEntityInformationModel> referencedUsers)
  {
    this.referencedUsers = referencedUsers;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedOrganizations()
  {
    return referencedOrganizations;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedOrganizations(
      Map<String, IConfigEntityInformationModel> referencedOrganizations)
  {
    this.referencedOrganizations = referencedOrganizations;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedTasks()
  {
    return referencedTasks;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedTasks(Map<String, IConfigEntityInformationModel> referencedTasks)
  {
    this.referencedTasks = referencedTasks;
  }
  
  @Override
  public Map<String, ISearchComponentAttributeInfoModel> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = SearchComponentAttributeInfoModel.class)
  public void setReferencedAttributes(
      Map<String, ISearchComponentAttributeInfoModel> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, IConfigTagInformationModel> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigTagInformationModel.class)
  public void setReferencedTags(Map<String, IConfigTagInformationModel> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public List<String> getReferencedUsersIds()
  {
    return referencedUsersIds;
  }
  
  @Override
  public void setReferencedUsersIds(List<String> referencedUsersIds)
  {
    this.referencedUsersIds = referencedUsersIds;
  }
  
  @Override
  public List<String> getReferencedOrganizationsIds()
  {
    return referencedOrganizationsIds;
  }
  
  @Override
  public void setReferencedOrganizationsIds(List<String> referencedOrganizationsIds)
  {
    this.referencedOrganizationsIds = referencedOrganizationsIds;
  }
  
  @Override
  public List<String> getReferencedTasksIds()
  {
    return referencedTasksIds;
  }
  
  @Override
  public void setReferencedTasksIds(List<String> referencedTasksIds)
  {
    this.referencedTasksIds = referencedTasksIds;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedContexts()
  {
    return referencedContexts;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedContexts(Map<String, IConfigEntityInformationModel> referencedContexts)
  {
    this.referencedContexts = referencedContexts;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedRelationships()
  {
    return referencedRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedRelationships(
      Map<String, IConfigEntityInformationModel> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }
  
  @Override
  public List<String> getReferencedContextsIds()
  {
    return referencedContextsIds;
  }
  
  @Override
  public void setReferencedContextsIds(List<String> referencedContextsIds)
  {
    this.referencedContextsIds = referencedContextsIds;
  }
  
  @Override
  public List<String> getReferencedRelationshipsIds()
  {
    return referencedRelationshipsIds;
  }
  
  @Override
  public void setReferencedRelationshipsIds(List<String> referencedRelationshipsIds)
  {
    this.referencedRelationshipsIds = referencedRelationshipsIds;
  }
  
  @Override
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedArticleTaxonomyModel.class)
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedEndpoints()
  {
    return referencedEndpoints;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedEndpoints(Map<String, IConfigEntityInformationModel> referencedEndpoints)
  {
    this.referencedEndpoints = referencedEndpoints;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedLanguages()
  {
    return referencedLanguages;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedLanguages(Map<String, IConfigEntityInformationModel> referencedLanguages)
  {
    this.referencedLanguages = referencedLanguages;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedReferences()
  {
    return referencedReferences;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedReferences(
      Map<String, IConfigEntityInformationModel> referencedReferences)
  {
    this.referencedReferences = referencedReferences;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedAuthorizationMappings()
  {
    return referencedAuthorizationMappings;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedAuthorizationMappings(
      Map<String, IConfigEntityInformationModel> referencedAuthorizationMappings)
  {
    this.referencedAuthorizationMappings = referencedAuthorizationMappings;
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
  public List<String> getReferencedRolesIds()
  {
    return referencedRolesIds;
  }
  
  @Override
  public void setReferencedRolesIds(List<String> referencedRolesIds)
  {
    this.referencedRolesIds = referencedRolesIds;
  }

  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedMappings()
  {
    return referencedMappings;
  }

  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedMappings(Map<String, IConfigEntityInformationModel> referencedMappings)
  {
    this.referencedMappings = referencedMappings;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedDestinationOrganizations()
  {
    return referencedDestinationOrganizations;
  }

  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedDestinationOrganizations(
      Map<String, IConfigEntityInformationModel> referencedDestinationOrganizations)
  {
    this.referencedDestinationOrganizations = referencedDestinationOrganizations;
  }
}
