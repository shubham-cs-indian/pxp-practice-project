package com.cs.core.config.interactor.model.rulelist;

import java.util.List;

public class BulkDeleteSuccessRuleListModel implements IBulkDeleteSuccessRuleListModel {
  
  private static final long serialVersionUID = 1L;
  List<String>              ids;
  List<String>              klassIds;
  
  @Override
  public List<String> getIds()
  {
    return ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
}
