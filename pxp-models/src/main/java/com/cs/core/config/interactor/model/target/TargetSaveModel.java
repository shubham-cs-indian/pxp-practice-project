package com.cs.core.config.interactor.model.target;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.klass.ITarget;
import com.cs.core.config.interactor.entity.klass.Market;
import com.cs.core.config.interactor.model.klass.AbstractKlassSaveModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TargetSaveModel extends AbstractKlassSaveModel implements ITargetKlassSaveModel {
  
  private static final long serialVersionUID = 1L;
  
  public TargetSaveModel()
  {
    super(new Market());
  }
  
  public TargetSaveModel(ITarget klass)
  {
    super(klass);
  }
  
  @JsonDeserialize(as = Market.class)
  @Override
  public ITreeEntity getParent()
  {
    return this.entity.getParent();
  }
}
