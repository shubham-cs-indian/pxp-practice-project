package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.klass.CollectionKlass;
import com.cs.core.config.interactor.entity.klass.ProjectKlass;

public class SetKlassSaveModel extends ProjectKlassSaveModel {
  
  private static final long serialVersionUID = 1L;
  
  public SetKlassSaveModel()
  {
    super(new CollectionKlass());
  }
  
  public SetKlassSaveModel(ProjectKlass project)
  {
    super(project);
  }
}
