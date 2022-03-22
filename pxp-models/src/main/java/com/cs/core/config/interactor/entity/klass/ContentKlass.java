package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ContentKlass extends AbstractKlass implements IContentKlass {
  
  private static final long serialVersionUID = 1L;
  
  @JsonDeserialize(as = ContentKlass.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    this.parent = (ContentKlass) parent;
  }
}
