package com.cs.core.config.interactor.entity.governancerule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface IDrillDown extends IEntity {
  
  public static final String ID       = "id";
  public static final String TYPE     = "type";
  public static final String TYPE_IDS = "typeIds";
  public static final String CODE     = "code";
  
  public String getCode();
  
  public void setCode(String code);
  
  @Override
  public String getId();
  
  @Override
  public void setId(String id);
  
  public String getType();
  
  public void setType(String type);
  
  public List<String> getTypeIds();
  
  public void setTypeIds(List<String> typeIds);
}
