package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffectType;
import com.cs.core.config.interactor.entity.goldenrecord.MergeEffectType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ModifiedMergeEffectModel implements IModifiedMergeEffectModel {
  
  private static final long        serialVersionUID = 1L;
  protected List<IMergeEffectType> addedEffectAttributes;
  protected List<IMergeEffectType> modifiedEffectAttributes;
  protected List<String>           deletedEffectAttributes;
  protected List<IMergeEffectType> addedEffectTags;
  protected List<IMergeEffectType> modifiedEffectTags;
  protected List<String>           deletedEffectTags;
  protected List<IMergeEffectType> addedEffectRelationships;
  protected List<IMergeEffectType> modifiedEffectRelationships;
  protected List<String>           deletedEffectRelationships;
  protected List<IMergeEffectType> addedEffectNatureRelationships;
  protected List<IMergeEffectType> modifiedEffectNatureRelationships;
  protected List<String>           deletedEffectNatureRelationships;
  
  @Override
  public List<IMergeEffectType> getAddedEffectAttributes()
  {
    if (addedEffectAttributes == null) {
      addedEffectAttributes = new ArrayList<>();
    }
    return addedEffectAttributes;
  }
  
  @JsonDeserialize(contentAs = MergeEffectType.class)
  @Override
  public void setAddedEffectAttributes(List<IMergeEffectType> addedEffectAttributes)
  {
    this.addedEffectAttributes = addedEffectAttributes;
  }
  
  @Override
  public List<IMergeEffectType> getModifiedEffectAttributes()
  {
    if (modifiedEffectAttributes == null) {
      modifiedEffectAttributes = new ArrayList<>();
    }
    return modifiedEffectAttributes;
  }
  
  @JsonDeserialize(contentAs = MergeEffectType.class)
  @Override
  public void setModifiedEffectAttributes(List<IMergeEffectType> modifiedEffectAttributes)
  {
    this.modifiedEffectAttributes = modifiedEffectAttributes;
  }
  
  @Override
  public List<String> getDeletedEffectAttributes()
  {
    if (deletedEffectAttributes == null) {
      deletedEffectAttributes = new ArrayList<>();
    }
    return deletedEffectAttributes;
  }
  
  @Override
  public void setDeletedEffectAttributes(List<String> deletedEffectAttributes)
  {
    this.deletedEffectAttributes = deletedEffectAttributes;
  }
  
  @Override
  public List<IMergeEffectType> getAddedEffectTags()
  {
    if (addedEffectTags == null) {
      addedEffectTags = new ArrayList<>();
    }
    return addedEffectTags;
  }
  
  @JsonDeserialize(contentAs = MergeEffectType.class)
  @Override
  public void setAddedEffectTags(List<IMergeEffectType> addedEffectTags)
  {
    this.addedEffectTags = addedEffectTags;
  }
  
  @Override
  public List<IMergeEffectType> getModifiedEffectTags()
  {
    if (modifiedEffectTags == null) {
      modifiedEffectTags = new ArrayList<>();
    }
    return modifiedEffectTags;
  }
  
  @JsonDeserialize(contentAs = MergeEffectType.class)
  @Override
  public void setModifiedEffectTags(List<IMergeEffectType> modifiedEffectTags)
  {
    this.modifiedEffectTags = modifiedEffectTags;
  }
  
  @Override
  public List<String> getDeletedEffectTags()
  {
    if (deletedEffectTags == null) {
      deletedEffectTags = new ArrayList<>();
    }
    return deletedEffectTags;
  }
  
  @Override
  public void setDeletedEffectTags(List<String> deletedEffectTags)
  {
    this.deletedEffectTags = deletedEffectTags;
  }
  
  @Override
  public List<IMergeEffectType> getAddedEffectRelationships()
  {
    if (addedEffectRelationships == null) {
      addedEffectRelationships = new ArrayList<>();
    }
    return addedEffectRelationships;
  }
  
  @JsonDeserialize(contentAs = MergeEffectType.class)
  @Override
  public void setAddedEffectRelationships(List<IMergeEffectType> addedEffectRelationships)
  {
    this.addedEffectRelationships = addedEffectRelationships;
  }
  
  @Override
  public List<IMergeEffectType> getModifiedEffectRelationships()
  {
    if (modifiedEffectRelationships == null) {
      modifiedEffectRelationships = new ArrayList<>();
    }
    return modifiedEffectRelationships;
  }
  
  @JsonDeserialize(contentAs = MergeEffectType.class)
  @Override
  public void setModifiedEffectRelationships(List<IMergeEffectType> modifiedEffectRelationships)
  {
    this.modifiedEffectRelationships = modifiedEffectRelationships;
  }
  
  @Override
  public List<String> getDeletedEffectRelationships()
  {
    if (deletedEffectRelationships == null) {
      deletedEffectRelationships = new ArrayList<>();
    }
    return deletedEffectRelationships;
  }
  
  @Override
  public void setDeletedEffectRelationships(List<String> deletedEffectRelationships)
  {
    this.deletedEffectRelationships = deletedEffectRelationships;
  }
  
  @Override
  public List<IMergeEffectType> getAddedEffectNatureRelationships()
  {
    if (addedEffectNatureRelationships == null) {
      addedEffectNatureRelationships = new ArrayList<>();
    }
    return addedEffectNatureRelationships;
  }
  
  @JsonDeserialize(contentAs = MergeEffectType.class)
  @Override
  public void setAddedEffectNatureRelationships(
      List<IMergeEffectType> addedEffectNatureRelationships)
  {
    this.addedEffectNatureRelationships = addedEffectNatureRelationships;
  }
  
  @Override
  public List<IMergeEffectType> getModifiedEffectNatureRelationships()
  {
    if (modifiedEffectNatureRelationships == null) {
      modifiedEffectNatureRelationships = new ArrayList<>();
    }
    return modifiedEffectNatureRelationships;
  }
  
  @JsonDeserialize(contentAs = MergeEffectType.class)
  @Override
  public void setModifiedEffectNatureRelationships(
      List<IMergeEffectType> modifiedEffectNatureRelationships)
  {
    this.modifiedEffectNatureRelationships = modifiedEffectNatureRelationships;
  }
  
  @Override
  public List<String> getDeletedEffectNatureRelationships()
  {
    if (deletedEffectNatureRelationships == null) {
      deletedEffectNatureRelationships = new ArrayList<>();
    }
    return deletedEffectNatureRelationships;
  }
  
  @Override
  public void setDeletedEffectNatureRelationships(List<String> deletedEffectNatureRelationships)
  {
    this.deletedEffectNatureRelationships = deletedEffectNatureRelationships;
  }
}
