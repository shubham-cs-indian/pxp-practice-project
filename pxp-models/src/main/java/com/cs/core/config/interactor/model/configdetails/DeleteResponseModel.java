package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;

public class DeleteResponseModel implements IDeleteResponseModel {
  
  private static final long serialVersionUID = 1L;
  
  List<String>              ids              = new ArrayList<String>();
  
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
}
