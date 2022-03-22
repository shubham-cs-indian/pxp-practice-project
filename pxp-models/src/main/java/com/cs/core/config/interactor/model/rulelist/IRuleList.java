package com.cs.core.config.interactor.model.rulelist;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

import java.util.List;

public interface IRuleList extends IConfigEntity {
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<String> getList();
  
  public void setList(List<String> list);
}
