package com.cs.core.runtime.interactor.entity.relationshipinstance;

import java.util.ArrayList;
import java.util.List;

public class RelationshipInheritanceOnTypeSwitchRequestModel
    implements IRelationshipInheritanceOnTypeSwitchRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          contentId;
  protected String          baseType;
  protected List<String>    existingTypes;
  protected List<String>    existingTaxonomies;
  protected List<String>    addedTypes;
  protected List<String>    addedTaxonomies;
  protected List<String>    removedTypes;
  protected List<String>    removedTaxonomies;
  
  @Override
  public String getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public List<String> getExistingTypes()
  {
    if (existingTypes == null) {
      existingTypes = new ArrayList<>();
    }
    return existingTypes;
  }
  
  @Override
  public void setExistingTypes(List<String> existingTypes)
  {
    this.existingTypes = existingTypes;
  }
  
  @Override
  public List<String> getExistingTaxonomies()
  {
    if (existingTaxonomies == null) {
      existingTaxonomies = new ArrayList<>();
    }
    return existingTaxonomies;
  }
  
  @Override
  public void setExistingTaxonomies(List<String> existingTaxonomies)
  {
    this.existingTaxonomies = existingTaxonomies;
  }
  
  @Override
  public List<String> getAddedTypes()
  {
    if (addedTypes == null) {
      addedTypes = new ArrayList<>();
    }
    return addedTypes;
  }
  
  @Override
  public void setAddedTypes(List<String> addedTypes)
  {
    this.addedTypes = addedTypes;
  }
  
  @Override
  public List<String> getAddedTaxonomies()
  {
    if (addedTaxonomies == null) {
      addedTaxonomies = new ArrayList<>();
    }
    return addedTaxonomies;
  }
  
  @Override
  public void setAddedTaxonomies(List<String> addedTaxonomies)
  {
    this.addedTaxonomies = addedTaxonomies;
  }
  
  @Override
  public List<String> getRemovedTypes()
  {
    if (removedTypes == null) {
      return new ArrayList<>();
    }
    return removedTypes;
  }
  
  @Override
  public void setRemovedTypes(List<String> removedType)
  {
    this.removedTypes = removedType;
  }
  
  @Override
  public List<String> getRemovedTaxonomies()
  {
    if (removedTaxonomies == null) {
      return new ArrayList<>();
    }
    return removedTaxonomies;
  }
  
  @Override
  public void setRemovedTaxonomies(List<String> removedTaxonomy)
  {
    this.removedTaxonomies = removedTaxonomy;
  }
}
