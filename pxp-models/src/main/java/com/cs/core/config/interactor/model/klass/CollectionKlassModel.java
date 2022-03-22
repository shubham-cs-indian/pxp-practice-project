package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.klass.CollectionKlass;
import com.cs.core.config.interactor.entity.klass.IProjectKlass;

public class CollectionKlassModel extends ProjectKlassModel {
  
  private static final long serialVersionUID = 1L;
  
  public CollectionKlassModel()
  {
    super(new CollectionKlass());
  }
  
  public CollectionKlassModel(IProjectKlass klass)
  {
    super(klass);
  }
}
