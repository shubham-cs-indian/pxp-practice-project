package com.cs.core.config.interactor.model.target;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.klass.ITarget;
import com.cs.core.config.interactor.entity.klass.TargetCollectionKlass;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class TargetCollectionKlassModel extends TargetModel {
  
  private static final long serialVersionUID = 1L;
  
  public TargetCollectionKlassModel()
  {
    super(new TargetCollectionKlass());
  }
  
  public TargetCollectionKlassModel(ITarget klass)
  {
    super(klass);
  }
  
  @JsonDeserialize(as = TargetCollectionKlass.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    super.setParent(parent);
  }
  
  @JsonDeserialize(contentAs = TargetCollectionKlass.class)
  @Override
  public void setChildren(List<? extends ITreeEntity> children)
  {
    super.setChildren(children);
  }
}
