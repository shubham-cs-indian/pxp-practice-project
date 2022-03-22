package com.cs.core.config.interactor.entity.configuration.base;

import java.util.List;

public interface ITreeEntity extends IEntity {
  
  public static final String PARENT   = "parent";
  public static final String CHILDREN = "children";
  
  public ITreeEntity getParent();
  
  public void setParent(ITreeEntity parent);
  
  public List<? extends ITreeEntity> getChildren();
  
  public void setChildren(List<? extends ITreeEntity> children);
}
