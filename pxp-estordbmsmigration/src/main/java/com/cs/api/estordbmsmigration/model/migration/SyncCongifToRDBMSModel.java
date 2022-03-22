package com.cs.api.estordbmsmigration.model.migration;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SyncCongifToRDBMSModel implements ISyncCongifToRDBMSModel {
  
  private String                          code             = "";
  private String                          type             = "";
  private List<ISyncCongifToRDBMSModel> childrens        = new ArrayList<>();
  
  private String                          userName         = "";
  
  private static final long               serialVersionUID = 1L;
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  @JsonDeserialize(contentAs = SyncCongifToRDBMSModel.class)
  public void setChildrens(List<ISyncCongifToRDBMSModel> childrens)
  {
    this.childrens = childrens;
  }
  
  @Override
  public List<ISyncCongifToRDBMSModel> getChildrens()
  {
    return childrens;
  }
  
  @Override
  public void setUserName(String userName)
  {
    this.userName = userName;
  }
  
  @Override
  public String getUserName()
  {
    return userName;
  }
  
}
