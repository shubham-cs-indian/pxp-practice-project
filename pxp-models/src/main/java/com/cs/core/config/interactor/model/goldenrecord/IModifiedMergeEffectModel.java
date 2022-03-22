package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.config.interactor.entity.goldenrecord.IMergeEffectType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IModifiedMergeEffectModel extends IModel {
  
  public static final String ADDED_EFFECT_ATTRIBUTES              = "addedEffectAttributes";
  public static final String MODIFIED_EFFECT_ATTRIBUTES           = "modifiedEffectAttributes";
  public static final String DELETED_EFFECT_ATTRIBUTES            = "deletedEffectAttributes";
  public static final String ADDED_EFFECT_TAGS                    = "addedEffectTags";
  public static final String MODIFIED_EFFECT_TAGS                 = "modifiedEffectTags";
  public static final String DELETED_EFFECT_TAGS                  = "deletedEffectTags";
  public static final String ADDED_EFFECT_RELATIONSHIPS           = "addedEffectRelationships";
  public static final String MODIFIED_EFFECT_RELATIONSHIPS        = "modifiedEffectRelationships";
  public static final String DELETED_EFFECT_RELATIONSHIPS         = "deletedEffectRelationships";
  public static final String ADDED_EFFECT_NATURE_RELATIONSHIPS    = "addedEffectNatureRelationships";
  public static final String MODIFIED_EFFECT_NATURE_RELATIONSHIPS = "modifiedEffectNatureRelationships";
  public static final String DELETED_EFFECT_NATURE_RELATIONSHIPS  = "deletedEffectNatureRelationships";
  
  public List<IMergeEffectType> getAddedEffectAttributes();
  
  public void setAddedEffectAttributes(List<IMergeEffectType> addedEffectAttributes);
  
  public List<IMergeEffectType> getModifiedEffectAttributes();
  
  public void setModifiedEffectAttributes(List<IMergeEffectType> modifiedEffectAttributes);
  
  public List<String> getDeletedEffectAttributes();
  
  public void setDeletedEffectAttributes(List<String> deletedEffectAttributes);
  
  public List<IMergeEffectType> getAddedEffectTags();
  
  public void setAddedEffectTags(List<IMergeEffectType> addedEffectTags);
  
  public List<IMergeEffectType> getModifiedEffectTags();
  
  public void setModifiedEffectTags(List<IMergeEffectType> modifiedEffectTags);
  
  public List<String> getDeletedEffectTags();
  
  public void setDeletedEffectTags(List<String> deletedEffectTags);
  
  public List<IMergeEffectType> getAddedEffectRelationships();
  
  public void setAddedEffectRelationships(List<IMergeEffectType> addedEffectRelationships);
  
  public List<IMergeEffectType> getModifiedEffectRelationships();
  
  public void setModifiedEffectRelationships(List<IMergeEffectType> modifiedEffectRelationships);
  
  public List<String> getDeletedEffectRelationships();
  
  public void setDeletedEffectRelationships(List<String> deletedEffectRelationships);
  
  public List<IMergeEffectType> getAddedEffectNatureRelationships();
  
  public void setAddedEffectNatureRelationships(
      List<IMergeEffectType> addedEffectNatureRelationships);
  
  public List<IMergeEffectType> getModifiedEffectNatureRelationships();
  
  public void setModifiedEffectNatureRelationships(
      List<IMergeEffectType> modifiedEffectNatureRelationships);
  
  public List<String> getDeletedEffectNatureRelationships();
  
  public void setDeletedEffectNatureRelationships(List<String> deletedEffectNatureRelationships);
}
