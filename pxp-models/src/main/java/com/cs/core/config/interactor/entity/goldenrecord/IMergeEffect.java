package com.cs.core.config.interactor.entity.goldenrecord;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

import java.util.List;

public interface IMergeEffect extends IConfigEntity {
  
  public static final String ATTRIBUTES           = "attributes";
  public static final String TAGS                 = "tags";
  public static final String RELATIONSHIPS        = "relationships";
  public static final String NATURE_RELATIONSHIPS = "natureRelationships";
  
  public List<IMergeEffectType> getAttributes();
  
  public void setAttributes(List<IMergeEffectType> attributes);
  
  public List<IMergeEffectType> getTags();
  
  public void setTags(List<IMergeEffectType> tags);
  
  public List<IMergeEffectType> getRelationships();
  
  public void setRelationships(List<IMergeEffectType> relationships);
  
  public List<IMergeEffectType> getNatureRelationships();
  
  public void setNatureRelationships(List<IMergeEffectType> relationships);
}
