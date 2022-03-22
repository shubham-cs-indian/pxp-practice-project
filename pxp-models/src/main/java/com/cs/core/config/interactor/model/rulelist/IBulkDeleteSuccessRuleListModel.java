package com.cs.core.config.interactor.model.rulelist;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IBulkDeleteSuccessRuleListModel extends IModel {
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
}
