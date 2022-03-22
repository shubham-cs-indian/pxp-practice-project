package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.klass.CollectionKlass;
import com.cs.core.config.interactor.entity.klass.ProjectKlass;

public class CollectionKlassSaveModel extends ProjectKlassSaveModel {
  
  private static final long serialVersionUID = 1L;
  
  public CollectionKlassSaveModel()
  {
    super(new CollectionKlass());
  }
  
  public CollectionKlassSaveModel(ProjectKlass project)
  {
    super(project);
  }
}
