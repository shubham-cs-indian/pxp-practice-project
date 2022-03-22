package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IValueIdTagModel extends IModel {
  
  public static final String VALUE = "value";
  
  public List<IIdRelevance> getValue();
  
  public void setValue(List<IIdRelevance> value);
}
