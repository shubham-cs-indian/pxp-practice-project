package com.cs.core.config.interactor.entity.supplier;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.klass.AbstractKlass;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Supplier extends AbstractKlass implements ISupplier {
  
  private static final long serialVersionUID = 1L;
  
  @JsonDeserialize(as = Supplier.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    this.parent = (Supplier) parent;
  }
}
