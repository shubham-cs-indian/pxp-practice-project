package com.cs.core.config.interactor.entity.datarule;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
public interface IMandatoryProperty extends IConfigEntity {
  
  public String getType();
  
  public void setType(String type);
  
  public List<String> getKlassType();
  
  public void setKlassType(List<String> klassType);
  
  public String getLabel();
  
  public void setLabel(String label);
}
