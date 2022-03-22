package com.cs.core.config.interactor.entity.textasset;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.klass.AbstractKlass;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TextAsset extends AbstractKlass implements ITextAsset {
  
  private static final long serialVersionUID = 1L;
  
  @JsonDeserialize(as = TextAsset.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    this.parent = (TextAsset) parent;
  }
}
