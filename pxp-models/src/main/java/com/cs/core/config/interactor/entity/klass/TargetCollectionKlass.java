package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TargetCollectionKlass extends AbstractKlass implements ITarget {
  
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  
  @JsonDeserialize(as = TargetCollectionKlass.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    this.parent = (TargetCollectionKlass) parent;
  }
}
