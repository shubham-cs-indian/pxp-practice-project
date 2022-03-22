package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.tag.IIdRelevance;

import java.util.List;

public interface ITagDefaultValueCouplingTypeModel extends IDefaultValueChangeModel {
  
  public static final String VALUE = "value";
  
  public List<IIdRelevance> getValue();
  
  public void setValue(List<IIdRelevance> value);
}
