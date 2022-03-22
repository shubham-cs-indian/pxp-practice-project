package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Market extends AbstractKlass implements ITarget {
  
  private static final long serialVersionUID = 1L;
  
  @JsonDeserialize(as = Market.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    this.parent = (Market) parent;
  }
}
