package com.cs.di.config.interactor.model.initializeworflowevent;

import java.util.List;

import com.cs.core.config.interactor.model.processevent.IGetValidationInformationModel;
import com.cs.core.config.interactor.model.processevent.IProcessEventModel;

public interface ISaveProcessEventModel extends IGetValidationInformationModel, IProcessEventModel {
  
  public static final String ADDED_KLASS_IDS                     = "addedKlassIds";
  public static final String ADDED_NON_NATURE_KLASS_IDS          = "addedNonNatureKlassIds";
  public static final String DELETED_KLASS_IDS                   = "deletedKlassIds";
  public static final String DELETED_NON_NATURE_KLASS_IDS        = "deletedNonNatureKlassIds";
  public static final String ADDED_USERS                         = "addedUsers";
  public static final String DELETED_USERS                       = "deletedUsers";
  public static final String ADDED_ORGANIZATIONS                 = "addedOrganizations";
  public static final String DELETED_ORGANIZATIONS               = "deletedOrganizations";
  public static final String ADDED_TASKS                         = "addedTasks";
  public static final String DELETED_TASKS                       = "deletedTasks";
  public static final String ADDED_ATTRIBUTE_IDS                 = "addedAttributeIds";
  public static final String DELETED_ATTRIBUTE_IDS               = "deletedAttributeIds";
  public static final String ADDED_TAG_IDS                       = "addedTagIds";
  public static final String DELETED_TAG_IDS                     = "deletedTagIds";
  public static final String ADDED_CONTEXTS                      = "addedContexts";
  public static final String DELETED_CONTEXTS                    = "deletedContexts";
  public static final String ADDED_RELATIONSHIPS                 = "addedRelationships";
  public static final String DELETED_RELATIONSHIPS               = "deletedRelationships";
  public static final String ADDED_TAXONOMY_IDS                  = "addedTaxonomyIds";
  public static final String DELETED_TAXONOMY_IDS                = "deletedTaxonomyIds";
  public static final String ADDED_KLASS_IDS_FOR_COMPONENT       = "addedKlassIdsForComponent";
  public static final String DELETED_KLASS_IDS_FOR_COMPONENT     = "deletedKlassIdsForComponent";
  public static final String ADDED_TAXONOMY_IDS_FOR_COMPONENT    = "addedTaxonomyIdsForComponent";
  public static final String DELETED_TAXONOMY_IDS_FOR_COMPONENT  = "deletedTaxonomyIdsForComponent";
  public static final String ADDED_ATTRIBUTE_IDS_FOR_COMPONENT   = "addedAttributeIdsForComponent";
  public static final String DELETED_ATTRIBUTE_IDS_FOR_COMPONENT = "deletedAttributeIdsForComponent";
  public static final String ADDED_TAG_IDS_FOR_COMPONENT         = "addedTagIdsForComponent";
  public static final String DELETED_TAG_IDS_FOR_COMPONENT       = "deletedTagIdsForComponent";
  public static final String ADDED_ENDPOINT_IDS_FOR_COMPONENT    = "addedEndpointIdsForComponent";
  public static final String DELETED_ENDPOINT_IDS_FOR_COMPONENT  = "deletedEndpointIdsForComponent";
  public static final String ADDED_ENDPOINT_IDS_FOR_PROCESS      = "addedEndpointIdsForProcess";
  public static final String DELETED_ENDPOINT_IDS_FOR_PROCESS    = "deletedEndpointIdsForProcess";
  public static final String ADDED_REFERENCES                    = "addedReferences";
  public static final String DELETED_REFERENCES                  = "deletedReferences";
  public static final String ADDED_AUTHORIZATION_MAPPINGS        = "addedAuthorizationMappings";
  public static final String DELETED_AUTHORIZATION_MAPPINGS      = "deletedAuthorizationMappings";
  public static final String ADDED_ROLES                         = "addedRoles";
  public static final String DELETED_ROLES                       = "deletedRoles";
  public static final String ADDED_DESTINATION_ORGANIZATIONS     = "addedDestinationOrganizations";
  public static final String DELETED_DESTINATION_ORGANIZATIONS   = "deletedDestinationOrganizations";
  
  public List<String> getAddedKlassIds();
  
  public void setAddedKlassIds(List<String> addedKlassIds);
  
  public List<String> getDeletedKlassIds();
  
  public void setDeletedKlassIds(List<String> deletedKlassIds);
  
  public List<String> getAddedUsers();
  
  public void setAddedUsers(List<String> addedUsers);
  
  public List<String> getDeletedUsers();
  
  public void setDeletedUsers(List<String> deletedUsers);
  
  public List<String> getAddedOrganizations();
  
  public void setAddedOrganizations(List<String> addedOrganizations);
  
  public List<String> getDeletedOrganizations();
  
  public void setDeletedOrganizations(List<String> deletedOrganizations);
  
  public List<String> getAddedTasks();
  
  public void setAddedTasks(List<String> addedTasks);
  
  public List<String> getDeletedTasks();
  
  public void setDeletedTasks(List<String> deletedTasks);
  
  public List<String> getAddedAttributeIds();
  
  public void setAddedAttributeIds(List<String> addedAttributeIds);
  
  public List<String> getDeletedAttributeIds();
  
  public void setDeletedAttributeIds(List<String> deletedAttributeIdIds);
  
  public List<String> getAddedTagIds();
  
  public void setAddedTagIds(List<String> addedTagIds);
  
  public List<String> getDeletedTagIds();
  
  public void setDeletedTagIds(List<String> deletedTagIds);
  
  public List<String> getAddedContexts();
  
  public void setAddedContexts(List<String> addedContexts);
  
  public List<String> getDeletedContexts();
  
  public void setDeletedContexts(List<String> deletedContexts);
  
  public List<String> getAddedRelationships();
  
  public void setAddedRelationships(List<String> addedRelationships);
  
  public List<String> getDeletedRelationships();
  
  public void setDeletedRelationships(List<String> deletedRelationships);
  
  public List<String> getAddedTaxonomyIds();
  
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds);
  
  public List<String> getDeletedTaxonomyIds();
  
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds);
  
  public List<String> getAddedKlassIdsForComponent();
  
  public void setAddedKlassIdsForComponent(List<String> addedKlassIdsForComponent);
  
  public List<String> getDeletedKlassIdsForComponent();
  
  public void setDeletedKlassIdsForComponent(List<String> deletedKlassIdsForComponent);
  
  public List<String> getAddedTaxonomyIdsForComponent();
  
  public void setAddedTaxonomyIdsForComponent(List<String> addedTaxonomyIdsForComponent);
  
  public List<String> getDeletedTaxonomyIdsForComponent();
  
  public void setDeletedTaxonomyIdsForComponent(List<String> deletedTaxonomyIdsForComponent);
  
  public List<String> getAddedAttributeIdsForComponent();
  
  public void setAddedAttributeIdsForComponent(List<String> addedAttributeIds);
  
  public List<String> getDeletedAttributeIdsForComponent();
  
  public void setDeletedAttributeIdsForComponent(List<String> deletedAttributeIdsForComponent);
  
  public List<String> getAddedTagIdsForComponent();
  
  public void setAddedTagIdsForComponent(List<String> addedTagIdsForComponent);
  
  public List<String> getDeletedTagIdsForComponent();
  
  public void setDeletedTagIdsForComponent(List<String> deletedTagIdsForComponent);
  
  public List<String> getAddedEndpointIdsForComponent();
  
  public void setAddedEndpointIdsForComponent(List<String> addedEndpointIdsForComponent);
  
  public List<String> getDeletedEndpointIdsForComponent();
  
  public void setDeletedEndpointIdsForComponent(List<String> deletedEndpointIdsForComponent);
  
  public List<String> getAddedEndpointIdsForProcess();
  
  public void setAddedEndpointIdsForProcess(List<String> addedEndpointIdsForProcess);
  
  public List<String> getDeletedEndpointIdsForProcess();
  
  public void setDeletedEndpointIdsForProcess(List<String> deletedEndpointIdsForProcess);
  
  public List<String> getAddedReferences();
  
  public void setAddedReferences(List<String> addedReferences);
  
  public List<String> getDeletedReferences();
  
  public void setDeletedReferences(List<String> deletedReferences);
  
  public List<String> getAddedRoles();
  
  public void setAddedRoles(List<String> addedRoles);
  
  public List<String> getDeletedRoles();
  
  public void setDeletedRoles(List<String> deletedRoles);

  public List<String> getAddedNonNatureKlassIds();

  public void setAddedNonNatureKlassIds(List<String> addedNonNatureKlassIds);

  public List<String> getDeletedNonNatureKlassIds();
  
  public void setDeletedNonNatureKlassIds(List<String> deletedNonNatureKlassIds);
  
  public List<String> getAddedDestinationOrganizations();
  
  public void setAddedDestinationOrganizations(List<String> addedDestinationOrganizations);
  
  public List<String> getDeletedDestinationOrganizations();
  
  public void setDeletedDestinationOrganizations(List<String> deletedDestinationOrganizations);
 
}
