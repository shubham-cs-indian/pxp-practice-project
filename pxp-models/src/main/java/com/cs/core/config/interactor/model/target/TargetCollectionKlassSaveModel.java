package com.cs.core.config.interactor.model.target;

import com.cs.core.config.interactor.entity.klass.ITarget;
import com.cs.core.config.interactor.entity.klass.TargetCollectionKlass;

public class TargetCollectionKlassSaveModel extends TargetSaveModel {
  
  private static final long serialVersionUID = 1L;
  
  public TargetCollectionKlassSaveModel()
  {
    super(new TargetCollectionKlass());
  }
  
  public TargetCollectionKlassSaveModel(ITarget klass)
  {
    super(klass);
  }
}
