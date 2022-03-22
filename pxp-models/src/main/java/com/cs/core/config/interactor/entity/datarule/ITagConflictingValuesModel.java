package com.cs.core.config.interactor.entity.datarule;

import com.cs.core.config.interactor.entity.tag.IIdRelevance;

import java.util.List;

public interface ITagConflictingValuesModel extends IElementConflictingValuesModel {
  
  public static final String VALUE         = "value";
  public static final String COUPLING_TYPE = "couplingType";
  
  public List<IIdRelevance> getValue();
  
  public void setValue(List<IIdRelevance> value);
  
  public String getCouplingType();
  
  public void setCouplingType(String couplingType);
}
