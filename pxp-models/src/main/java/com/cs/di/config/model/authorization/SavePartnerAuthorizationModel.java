package com.cs.di.config.model.authorization;

import java.util.List;

public class SavePartnerAuthorizationModel implements ISavePartnerAuthorizationModel {
  
  private static final long serialVersionUID                     = 1L;
  
  protected String          id;
  protected String          label;
  protected String          code;
  protected String          icon;
  protected String          iconKey;
  protected String          type                                 = this.getClass()
      .getName();
  protected Long            versionId;
  protected Long            versionTimestamp;
  protected String          lastModifiedBy;
  
  protected Boolean         isAllAttributesSelected              = false;
  protected Boolean         isAllTagsSelected                    = false;
  protected Boolean         isAllClassesSelected                 = false;
  protected Boolean         isAllContextsSelected                = false;
  protected Boolean         isAllTaxonomiesSelected              = false;
  protected Boolean         isAllRelationshipsSelected           = false;
  
  protected Boolean         isBlankValueAcceptedForAttributes    = false;
  protected Boolean         isBlankValueAcceptedForTags          = false;
  protected Boolean         isBlankValueAcceptedForClasses       = false;
  protected Boolean         isBlankValueAcceptedForContexts      = false;
  protected Boolean         isBlankValueAcceptedForTaxonomies    = false;
  protected Boolean         isBlankValueAcceptedForRelationships = false;
  
  protected List<String>    addedAttributeMappings;
  protected List<String>    deletedAttributeMappings;
  protected List<String>    addedTagMappings;
  protected List<String>    deletedTagMappings;
  protected List<String>    addedClassMappings;
  protected List<String>    deletedClassMappings;
  protected List<String>    addedTaxonomyMappings;
  protected List<String>    deletedTaxonomyMappings;
  protected List<String>    addedContextMappings;
  protected List<String>    deletedContextMappings;
  protected List<String>    addedRelationshipMappings;
  protected List<String>    deletedRelationshipMappings;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public String getType()
  {
    return this.type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getLabel()
  {
    return this.label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getIcon()
  {
    return this.icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    return this.iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public Boolean getIsAllAttributesSelected()
  {
    return isAllAttributesSelected;
  }
  
  @Override
  public void setIsAllAttributesSelected(Boolean isAllAttributesSelected)
  {
    this.isAllAttributesSelected = isAllAttributesSelected;
  }
  
  @Override
  public Boolean getIsAllTagsSelected()
  {
    return isAllTagsSelected;
  }
  
  @Override
  public void setIsAllTagsSelected(Boolean isAllTagsSelected)
  {
    this.isAllTagsSelected = isAllTagsSelected;
  }
  
  @Override
  public Boolean getIsAllClassesSelected()
  {
    return isAllClassesSelected;
  }
  
  @Override
  public void setIsAllClassesSelected(Boolean isAllClassesSelected)
  {
    this.isAllClassesSelected = isAllClassesSelected;
  }
  
  @Override
  public Boolean getIsAllContextsSelected()
  {
    return isAllContextsSelected;
  }
  
  @Override
  public void setIsAllContextsSelected(Boolean isAllContextsSelected)
  {
    this.isAllContextsSelected = isAllContextsSelected;
  }
  
  @Override
  public Boolean getIsAllTaxonomiesSelected()
  {
    return isAllTaxonomiesSelected;
  }
  
  @Override
  public void setIsAllTaxonomiesSelected(Boolean isAllTaxonomiesSelected)
  {
    this.isAllTaxonomiesSelected = isAllTaxonomiesSelected;
  }
  
  @Override
  public Boolean getIsAllRelationshipsSelected()
  {
    return isAllRelationshipsSelected;
  }
  
  @Override
  public void setIsAllRelationshipsSelected(Boolean isAllRelationshipsSelected)
  {
    this.isAllRelationshipsSelected = isAllRelationshipsSelected;
  }
  
  @Override
  public Boolean getIsBlankValueAcceptedForAttributes()
  {
    return isBlankValueAcceptedForAttributes;
  }
  
  @Override
  public void setIsBlankValueAcceptedForAttributes(Boolean isBlankValueAcceptedForAttributes)
  {
    this.isBlankValueAcceptedForAttributes = isBlankValueAcceptedForAttributes;
  }
  
  @Override
  public Boolean getIsBlankValueAcceptedForTags()
  {
    return isBlankValueAcceptedForTags;
  }
  
  @Override
  public void setIsBlankValueAcceptedForTags(Boolean isBlankValueAcceptedForTags)
  {
    this.isBlankValueAcceptedForTags = isBlankValueAcceptedForTags;
  }
  
  @Override
  public Boolean getIsBlankValueAcceptedForClasses()
  {
    return isBlankValueAcceptedForClasses;
  }
  
  @Override
  public void setIsBlankValueAcceptedForClasses(Boolean isBlankValueAcceptedForClasses)
  {
    this.isBlankValueAcceptedForClasses = isBlankValueAcceptedForClasses;
  }
  
  @Override
  public Boolean getIsBlankValueAcceptedForContexts()
  {
    return isBlankValueAcceptedForContexts;
  }
  
  @Override
  public void setIsBlankValueAcceptedForContexts(Boolean isBlankValueAcceptedForContexts)
  {
    this.isBlankValueAcceptedForContexts = isBlankValueAcceptedForContexts;
  }
  
  @Override
  public Boolean getIsBlankValueAcceptedForTaxonomies()
  {
    return isBlankValueAcceptedForTaxonomies;
  }
  
  @Override
  public void setIsBlankValueAcceptedForTaxonomies(Boolean isBlankValueAcceptedForTaxonomies)
  {
    this.isBlankValueAcceptedForTaxonomies = isBlankValueAcceptedForTaxonomies;
  }
  
  @Override
  public Boolean getIsBlankValueAcceptedForRelationships()
  {
    return isBlankValueAcceptedForRelationships;
  }
  
  @Override
  public void setIsBlankValueAcceptedForRelationships(Boolean isBlankValueAcceptedForRelationships)
  {
    this.isBlankValueAcceptedForRelationships = isBlankValueAcceptedForRelationships;
  }
  
  public List<String> getAddedAttributeMappings()
  {
    return addedAttributeMappings;
  }
  
  public void setAddedAttributeMappings(List<String> addedAttributeMappings)
  {
    this.addedAttributeMappings = addedAttributeMappings;
  }
  
  public List<String> getDeletedAttributeMappings()
  {
    return deletedAttributeMappings;
  }
  
  public void setDeletedAttributeMappings(List<String> deletedAttributeMappings)
  {
    this.deletedAttributeMappings = deletedAttributeMappings;
  }
  
  public List<String> getAddedTagMappings()
  {
    return addedTagMappings;
  }
  
  public void setAddedTagMappings(List<String> addedTagMappings)
  {
    this.addedTagMappings = addedTagMappings;
  }
  
  public List<String> getDeletedTagMappings()
  {
    return deletedTagMappings;
  }
  
  public void setDeletedTagMappings(List<String> deletedTagMappings)
  {
    this.deletedTagMappings = deletedTagMappings;
  }
  
  public List<String> getAddedClassMappings()
  {
    return addedClassMappings;
  }
  
  public void setAddedClassMappings(List<String> addedClassMappings)
  {
    this.addedClassMappings = addedClassMappings;
  }
  
  public List<String> getDeletedClassMappings()
  {
    return deletedClassMappings;
  }
  
  public void setDeletedClassMappings(List<String> deletedClassMappings)
  {
    this.deletedClassMappings = deletedClassMappings;
  }
  
  public List<String> getAddedTaxonomyMappings()
  {
    return addedTaxonomyMappings;
  }
  
  public void setAddedTaxonomyMappings(List<String> addedTaxonomyMappings)
  {
    this.addedTaxonomyMappings = addedTaxonomyMappings;
  }
  
  public List<String> getDeletedTaxonomyMappings()
  {
    return deletedTaxonomyMappings;
  }
  
  public void setDeletedTaxonomyMappings(List<String> deletedTaxonomyMappings)
  {
    this.deletedTaxonomyMappings = deletedTaxonomyMappings;
  }
  
  public List<String> getAddedContextMappings()
  {
    return addedContextMappings;
  }
  
  public void setAddedContextMappings(List<String> addedContextMappings)
  {
    this.addedContextMappings = addedContextMappings;
  }
  
  public List<String> getDeletedContextMappings()
  {
    return deletedContextMappings;
  }
  
  public void setDeletedContextMappings(List<String> deletedContextMappings)
  {
    this.deletedContextMappings = deletedContextMappings;
  }
  
  @Override
  public List<String> getAddedRelationshipMappings()
  {
    return addedRelationshipMappings;
  }
  
  @Override
  public void setAddedRelationshipMappings(List<String> addedRelationshipMappings)
  {
    this.addedRelationshipMappings = addedRelationshipMappings;
  }
  
  @Override
  public List<String> getDeletedRelationshipMappings()
  {
    return deletedRelationshipMappings;
  }
  
  @Override
  public void setDeletedRelationshipMappings(List<String> deletedRelationshipMappings)
  {
    this.deletedRelationshipMappings = deletedRelationshipMappings;
  }
}
