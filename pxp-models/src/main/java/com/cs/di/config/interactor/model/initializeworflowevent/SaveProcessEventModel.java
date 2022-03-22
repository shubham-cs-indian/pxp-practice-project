package com.cs.di.config.interactor.model.initializeworflowevent;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.processevent.IValidationInformationModel;
import com.cs.core.config.interactor.model.processevent.ProcessEventModel;
import com.cs.core.config.interactor.model.processevent.ValidationInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SaveProcessEventModel extends ProcessEventModel implements ISaveProcessEventModel {
  
  private static final long             serialVersionUID                = 1L;
  
  protected List<String>                addedKlassIds                   = new ArrayList<>();
  protected List<String>                deletedKlassIds                 = new ArrayList<>();
  protected List<String>                addedUsers                      = new ArrayList<>();
  protected List<String>                deletedUsers                    = new ArrayList<>();
  protected List<String>                addedOrganizations              = new ArrayList<>();
  protected List<String>                deletedOrganizations            = new ArrayList<>();
  protected List<String>                addedTasks                      = new ArrayList<>();
  protected List<String>                deletedTasks                    = new ArrayList<>();
  protected List<String>                addedAttributeIds               = new ArrayList<>();
  protected List<String>                deletedAttributeIds             = new ArrayList<>();
  protected List<String>                addedTagIds                     = new ArrayList<>();
  protected List<String>                deletedTagIds                   = new ArrayList<>();
  protected List<String>                addedContexts                   = new ArrayList<>();
  protected List<String>                deletedContexts                 = new ArrayList<>();
  protected List<String>                addedRelationships              = new ArrayList<>();
  protected List<String>                deletedRelationships            = new ArrayList<>();
  protected List<String>                addedTaxonomyIds                = new ArrayList<>();
  protected List<String>                deletedTaxonomyIds              = new ArrayList<>();
  protected List<String>                addedKlassIdsForComponent       = new ArrayList<>();
  protected List<String>                deletedKlassIdsForComponent     = new ArrayList<>();
  protected List<String>                addedTaxonomyIdsForComponent    = new ArrayList<>();
  protected List<String>                deletedTaxonomyIdsForComponent  = new ArrayList<>();
  protected List<String>                addedAttributeIdsForComponent   = new ArrayList<>();
  protected List<String>                deletedAttributeIdsForComponent = new ArrayList<>();
  protected List<String>                addedTagIdsForComponent         = new ArrayList<>();
  protected List<String>                deletedTagIdsForComponent       = new ArrayList<>();
  protected List<String>                addedEndpointIdsForComponent    = new ArrayList<>();
  protected List<String>                deletedEndpointIdsForComponent  = new ArrayList<>();
  protected List<String>                addedEndpointIdsForProcess      = new ArrayList<>();
  protected List<String>                deletedEndpointIdsForProcess    = new ArrayList<>();
  protected List<String>                addedReferences                 = new ArrayList<>();
  protected List<String>                deletedReferences               = new ArrayList<>();
  protected List<String>                addedRoles                      = new ArrayList<>();
  protected List<String>                deletedRoles                    = new ArrayList<>();
  protected List<String>                addedNonNatureKlassIds          = new ArrayList<>();
  protected List<String>                deletedNonNatureKlassIds        = new ArrayList<>();
  protected List<String>                addedDestinationOrganizations   = new ArrayList<>();
  protected List<String>                deletedDestinationOrganizations = new ArrayList<>();
  protected IValidationInformationModel validationInfo;

  @Override
  public List<String> getAddedKlassIds()
  {
    return addedKlassIds;
  }
  
  @Override
  public void setAddedKlassIds(List<String> addedKlassIds)
  {
    this.addedKlassIds = addedKlassIds;
  }
  
  @Override
  public List<String> getDeletedKlassIds()
  {
    return deletedKlassIds;
  }
  
  @Override
  public void setDeletedKlassIds(List<String> deletedKlassIds)
  {
    this.deletedKlassIds = deletedKlassIds;
  }
  
  @Override
  public List<String> getAddedUsers()
  {
    return addedUsers;
  }
  
  @Override
  public void setAddedUsers(List<String> addedUsers)
  {
    this.addedUsers = addedUsers;
  }
  
  @Override
  public List<String> getDeletedUsers()
  {
    return deletedUsers;
  }
  
  @Override
  public void setDeletedUsers(List<String> deletedUsers)
  {
    this.deletedUsers = deletedUsers;
  }
  
  @Override
  public List<String> getAddedOrganizations()
  {
    return addedOrganizations;
  }
  
  @Override
  public void setAddedOrganizations(List<String> addedOrganizations)
  {
    this.addedOrganizations = addedOrganizations;
  }
  
  @Override
  public List<String> getDeletedOrganizations()
  {
    return deletedOrganizations;
  }
  
  @Override
  public void setDeletedOrganizations(List<String> deletedOrganizations)
  {
    this.deletedOrganizations = deletedOrganizations;
  }
  
  @Override
  public List<String> getAddedTasks()
  {
    return addedTasks;
  }
  
  @Override
  public void setAddedTasks(List<String> addedTasks)
  {
    this.addedTasks = addedTasks;
  }
  
  @Override
  public List<String> getDeletedTasks()
  {
    return deletedTasks;
  }
  
  @Override
  public void setDeletedTasks(List<String> deletedTasks)
  {
    this.deletedTasks = deletedTasks;
  }
  
  @Override
  public List<String> getAddedAttributeIds()
  {
    return addedAttributeIds;
  }
  
  @Override
  public void setAddedAttributeIds(List<String> addedAttributeIds)
  {
    this.addedAttributeIds = addedAttributeIds;
  }
  
  @Override
  public List<String> getDeletedAttributeIds()
  {
    return deletedAttributeIds;
  }
  
  @Override
  public void setDeletedAttributeIds(List<String> deletedAttributeIds)
  {
    this.deletedAttributeIds = deletedAttributeIds;
  }
  
  @Override
  public List<String> getAddedTagIds()
  {
    return addedTagIds;
  }
  
  @Override
  public void setAddedTagIds(List<String> addedTagIds)
  {
    this.addedTagIds = addedTagIds;
  }
  
  @Override
  public List<String> getDeletedTagIds()
  {
    return deletedTagIds;
  }
  
  @Override
  public void setDeletedTagIds(List<String> deletedTagIds)
  {
    this.deletedTagIds = deletedTagIds;
  }
  
  @Override
  public List<String> getAddedContexts()
  {
    return addedContexts;
  }
  
  @Override
  public void setAddedContexts(List<String> addedContexts)
  {
    this.addedContexts = addedContexts;
  }
  
  @Override
  public List<String> getDeletedContexts()
  {
    return deletedContexts;
  }
  
  @Override
  public void setDeletedContexts(List<String> deletedContexts)
  {
    this.deletedContexts = deletedContexts;
  }
  
  @Override
  public List<String> getAddedRelationships()
  {
    return addedRelationships;
  }
  
  @Override
  public void setAddedRelationships(List<String> addedRelationships)
  {
    this.addedRelationships = addedRelationships;
  }
  
  @Override
  public List<String> getDeletedRelationships()
  {
    return deletedRelationships;
  }
  
  @Override
  public void setDeletedRelationships(List<String> deletedRelationships)
  {
    this.deletedRelationships = deletedRelationships;
  }
  
  @Override
  public List<String> getAddedTaxonomyIds()
  {
    return addedTaxonomyIds;
  }
  
  @Override
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds)
  {
    this.addedTaxonomyIds = addedTaxonomyIds;
  }
  
  @Override
  public List<String> getDeletedTaxonomyIds()
  {
    return deletedTaxonomyIds;
  }
  
  @Override
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds)
  {
    this.deletedTaxonomyIds = deletedTaxonomyIds;
  }
  
  @Override
  public List<String> getAddedKlassIdsForComponent()
  {
    return addedKlassIdsForComponent;
  }
  
  @Override
  public void setAddedKlassIdsForComponent(List<String> addedKlassIdsForComponent)
  {
    this.addedKlassIdsForComponent = addedKlassIdsForComponent;
  }
  
  @Override
  public List<String> getDeletedKlassIdsForComponent()
  {
    return deletedKlassIdsForComponent;
  }
  
  @Override
  public void setDeletedKlassIdsForComponent(List<String> deletedKlassIdsForComponent)
  {
    this.deletedKlassIdsForComponent = deletedKlassIdsForComponent;
  }
  
  @Override
  public List<String> getAddedTaxonomyIdsForComponent()
  {
    return addedTaxonomyIdsForComponent;
  }
  
  @Override
  public void setAddedTaxonomyIdsForComponent(List<String> addedTaxonomyIdsForComponent)
  {
    this.addedTaxonomyIdsForComponent = addedTaxonomyIdsForComponent;
  }
  
  @Override
  public List<String> getDeletedTaxonomyIdsForComponent()
  {
    return deletedTaxonomyIdsForComponent;
  }
  
  @Override
  public void setDeletedTaxonomyIdsForComponent(List<String> deletedTaxonomyIdsForComponent)
  {
    this.deletedTaxonomyIdsForComponent = deletedTaxonomyIdsForComponent;
  }
  
  @Override
  public List<String> getAddedAttributeIdsForComponent()
  {
    return addedAttributeIdsForComponent;
  }
  
  @Override
  public void setAddedAttributeIdsForComponent(List<String> addedAttributeIdsForComponent)
  {
    this.addedAttributeIdsForComponent = addedAttributeIdsForComponent;
  }
  
  @Override
  public List<String> getDeletedAttributeIdsForComponent()
  {
    return deletedAttributeIdsForComponent;
  }
  
  @Override
  public void setDeletedAttributeIdsForComponent(List<String> deletedAttributeIdsForComponent)
  {
    this.deletedAttributeIdsForComponent = deletedAttributeIdsForComponent;
  }
  
  @Override
  public List<String> getAddedTagIdsForComponent()
  {
    return addedTagIdsForComponent;
  }
  
  @Override
  public void setAddedTagIdsForComponent(List<String> addedTagIdsForComponent)
  {
    this.addedTagIdsForComponent = addedTagIdsForComponent;
  }
  
  @Override
  public List<String> getDeletedTagIdsForComponent()
  {
    return deletedTagIdsForComponent;
  }
  
  @Override
  public void setDeletedTagIdsForComponent(List<String> deletedTagIdsForComponent)
  {
    this.deletedTagIdsForComponent = deletedTagIdsForComponent;
  }
  
  @Override
  public List<String> getAddedEndpointIdsForProcess()
  {
    return addedEndpointIdsForProcess;
  }
  
  @Override
  public void setAddedEndpointIdsForProcess(List<String> addedEndpointIdsForProcess)
  {
    this.addedEndpointIdsForProcess = addedEndpointIdsForProcess;
  }
  
  @Override
  public List<String> getDeletedEndpointIdsForProcess()
  {
    return deletedEndpointIdsForProcess;
  }
  
  @Override
  public void setDeletedEndpointIdsForProcess(List<String> deletedEndpointIdsForProcess)
  {
    this.deletedEndpointIdsForProcess = deletedEndpointIdsForProcess;
  }
  
  @Override
  public List<String> getAddedEndpointIdsForComponent()
  {
    return addedEndpointIdsForComponent;
  }
  
  @Override
  public void setAddedEndpointIdsForComponent(List<String> addedEndpointIdsForComponent)
  {
    this.addedEndpointIdsForComponent = addedEndpointIdsForComponent;
  }
  
  @Override
  public List<String> getDeletedEndpointIdsForComponent()
  {
    return deletedEndpointIdsForComponent;
  }
  
  @Override
  public void setDeletedEndpointIdsForComponent(List<String> deletedEndpointIdsForComponent)
  {
    this.deletedEndpointIdsForComponent = deletedEndpointIdsForComponent;
  }
  
  @Override
  public List<String> getAddedReferences()
  {
    return addedReferences;
  }
  
  @Override
  public void setAddedReferences(List<String> addedReferences)
  {
    this.addedReferences = addedReferences;
  }
  
  @Override
  public List<String> getDeletedReferences()
  {
    return deletedReferences;
  }
  
  @Override
  public void setDeletedReferences(List<String> deletedReferences)
  {
    this.deletedReferences = deletedReferences;
  }
  
  @Override
  public List<String> getAddedRoles()
  {
    return addedRoles;
  }
  
  @Override
  public void setAddedRoles(List<String> addedRoles)
  {
    this.addedRoles = addedRoles;
  }
  
  @Override
  public List<String> getDeletedRoles()
  {
    return deletedRoles;
  }
  
  @Override
  public void setDeletedRoles(List<String> deletedRoles)
  {
    this.deletedRoles = deletedRoles;
  }
  
  @JsonDeserialize(as = ValidationInformationModel.class)
  @Override
  public void setValidationInfo(IValidationInformationModel validationInfo)
  {
    this.validationInfo = validationInfo;
  }
  
  @Override
  public IValidationInformationModel getValidationInfo()
  {
    return validationInfo;
  }

  @Override
  public List<String> getAddedNonNatureKlassIds()
  {
    return addedNonNatureKlassIds;
  }

  @Override
  public void setAddedNonNatureKlassIds(List<String> addedNonNatureKlassIds)
  { 
    this.addedNonNatureKlassIds = addedNonNatureKlassIds;
    
  }
  
  @Override
  public List<String> getDeletedNonNatureKlassIds()
  {
    return deletedNonNatureKlassIds;
  }
  
  @Override
  public void setDeletedNonNatureKlassIds(List<String> deletedNonNatureKlassIds)
  {
    this.deletedNonNatureKlassIds = deletedNonNatureKlassIds;
  }  
  
  public List<String> getAddedDestinationOrganizations()
  {
    return addedDestinationOrganizations;
  }

  
  public void setAddedDestinationOrganizations(List<String> addedDestinationOrganizations)
  {
    this.addedDestinationOrganizations = addedDestinationOrganizations;
  }

  
  public List<String> getDeletedDestinationOrganizations()
  {
    return deletedDestinationOrganizations;
  }

  
  public void setDeletedDestinationOrganizations(List<String> deletedDestinationOrganizations)
  {
    this.deletedDestinationOrganizations = deletedDestinationOrganizations;
  }
}
