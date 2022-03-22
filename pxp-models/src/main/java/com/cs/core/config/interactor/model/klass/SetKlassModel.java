package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.klass.IProjectKlass;
import com.cs.core.config.interactor.entity.klass.SetKlass;

public class SetKlassModel extends ProjectKlassModel {
  
  private static final long serialVersionUID = 1L;
  
  public SetKlassModel()
  {
    super(new SetKlass());
  }
  
  public SetKlassModel(IProjectKlass klass)
  {
    super(klass);
  }
}
