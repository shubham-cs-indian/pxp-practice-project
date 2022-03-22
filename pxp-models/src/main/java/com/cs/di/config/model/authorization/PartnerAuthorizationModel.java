package com.cs.di.config.model.authorization;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.ArrayList;
import java.util.List;

public class PartnerAuthorizationModel implements IPartnerAuthorizationModel {
  
  private static final long serialVersionUID                     = 1L;
  protected String          id;
  protected String          label;
  protected String          code;
  protected String          description;
  protected String          tooltip;
  protected String          placeholder;
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
  
  protected List<String>    attributeMappings                    = new ArrayList<>();
  protected List<String>    tagMappings                          = new ArrayList<>();
  protected List<String>    classMappings                        = new ArrayList<>();
  protected List<String>    taxonomyMappings                     = new ArrayList<>();
  protected List<String>    contextMappings                      = new ArrayList<>();
  protected List<String>    relationshipMappings                 = new ArrayList<>();
  
  public static long getSerialversionuid()
  {
    return serialVersionUID;
  }
  
  public List<String> getAttributeMappings()
  {
    return attributeMappings;
  }
  
  public void setAttributeMappings(List<String> attributeMappings)
  {
    this.attributeMappings = attributeMappings;
  }
  
  public List<String> getTagMappings()
  {
    return tagMappings;
  }
  
  public void setTagMappings(List<String> tagMappings)
  {
    this.tagMappings = tagMappings;
  }
  
  public List<String> getClassMappings()
  {
    return classMappings;
  }
  
  public void setClassMappings(List<String> classMappings)
  {
    this.classMappings = classMappings;
  }
  
  public List<String> getTaxonomyMappings()
  {
    return taxonomyMappings;
  }
  
  public void setTaxonomyMappings(List<String> taxonomyMappings)
  {
    this.taxonomyMappings = taxonomyMappings;
  }
  
  public List<String> getContextMappings()
  {
    return contextMappings;
  }
  
  public void setContextMappings(List<String> contextMappings)
  {
    this.contextMappings = contextMappings;
  }
  
  public List<String> getRelationshipMappings()
  {
    return relationshipMappings;
  }
  
  public void setRelationshipMappings(List<String> relationshipMappings)
  {
    this.relationshipMappings = relationshipMappings;
  }
  
  public String getId()
  {
    return id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  public String getLabel()
  {
    return label;
  }
  
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  public String getCode()
  {
    return code;
  }
  
  public void setCode(String code)
  {
    this.code = code;
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  public String getTooltip()
  {
    return tooltip;
  }
  
  public void setTooltip(String tooltip)
  {
    this.tooltip = tooltip;
  }
  
  public String getPlaceholder()
  {
    return placeholder;
  }
  
  public void setPlaceholder(String placeholder)
  {
    this.placeholder = placeholder;
  }
  
  public String getIcon()
  {
    return icon;
  }
  
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
  
  public String getType()
  {
    return type;
  }
  
  public void setType(String type)
  {
    this.type = type;
  }
  
  public Long getVersionId()
  {
    return versionId;
  }
  
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  public Boolean getIsAllAttributesSelected()
  {
    return isAllAttributesSelected;
  }
  
  public void setIsAllAttributesSelected(Boolean isAllAttributesSelected)
  {
    this.isAllAttributesSelected = isAllAttributesSelected;
  }
  
  public Boolean getIsAllTagsSelected()
  {
    return isAllTagsSelected;
  }
  
  public void setIsAllTagsSelected(Boolean isAllTagsSelected)
  {
    this.isAllTagsSelected = isAllTagsSelected;
  }
  
  public Boolean getIsAllClassesSelected()
  {
    return isAllClassesSelected;
  }
  
  public void setIsAllClassesSelected(Boolean isAllClassesSelected)
  {
    this.isAllClassesSelected = isAllClassesSelected;
  }
  
  public Boolean getIsAllContextsSelected()
  {
    return isAllContextsSelected;
  }
  
  public void setIsAllContextsSelected(Boolean isAllContextsSelected)
  {
    this.isAllContextsSelected = isAllContextsSelected;
  }
  
  public Boolean getIsAllTaxonomiesSelected()
  {
    return isAllTaxonomiesSelected;
  }
  
  public void setIsAllTaxonomiesSelected(Boolean isAllTaxonomiesSelected)
  {
    this.isAllTaxonomiesSelected = isAllTaxonomiesSelected;
  }
  
  public Boolean getIsAllRelationshipsSelected()
  {
    return isAllRelationshipsSelected;
  }
  
  public void setIsAllRelationshipsSelected(Boolean isAllRelationshipsSelected)
  {
    this.isAllRelationshipsSelected = isAllRelationshipsSelected;
  }
  
  public Boolean getIsBlankValueAcceptedForAttributes()
  {
    return isBlankValueAcceptedForAttributes;
  }
  
  public void setIsBlankValueAcceptedForAttributes(Boolean isBlankValueAcceptedForAttributes)
  {
    this.isBlankValueAcceptedForAttributes = isBlankValueAcceptedForAttributes;
  }
  
  public Boolean getIsBlankValueAcceptedForTags()
  {
    return isBlankValueAcceptedForTags;
  }
  
  public void setIsBlankValueAcceptedForTags(Boolean isBlankValueAcceptedForTags)
  {
    this.isBlankValueAcceptedForTags = isBlankValueAcceptedForTags;
  }
  
  public Boolean getIsBlankValueAcceptedForClasses()
  {
    return isBlankValueAcceptedForClasses;
  }
  
  public void setIsBlankValueAcceptedForClasses(Boolean isBlankValueAcceptedForClasses)
  {
    this.isBlankValueAcceptedForClasses = isBlankValueAcceptedForClasses;
  }
  
  public Boolean getIsBlankValueAcceptedForContexts()
  {
    return isBlankValueAcceptedForContexts;
  }
  
  public void setIsBlankValueAcceptedForContexts(Boolean isBlankValueAcceptedForContexts)
  {
    this.isBlankValueAcceptedForContexts = isBlankValueAcceptedForContexts;
  }
  
  public Boolean getIsBlankValueAcceptedForTaxonomies()
  {
    return isBlankValueAcceptedForTaxonomies;
  }
  
  public void setIsBlankValueAcceptedForTaxonomies(Boolean isBlankValueAcceptedForTaxonomies)
  {
    this.isBlankValueAcceptedForTaxonomies = isBlankValueAcceptedForTaxonomies;
  }
  
  public Boolean getIsBlankValueAcceptedForRelationships()
  {
    return isBlankValueAcceptedForRelationships;
  }
  
  public void setIsBlankValueAcceptedForRelationships(Boolean isBlankValueAcceptedForRelationships)
  {
    this.isBlankValueAcceptedForRelationships = isBlankValueAcceptedForRelationships;
  }
  
  @Override
  public IEntity getEntity()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
