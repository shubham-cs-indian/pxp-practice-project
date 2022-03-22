package com.cs.core.runtime.interactor.model.version;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IKlassInstanceVersionGetModel extends IModel {
  
  public static final String FROM        = "from";
  public static final String SIZE        = "size";
  public static final String ID          = "id";
  public static final String TEMPLATE_ID = "templateId";
  
  public int getSize();
  
  public void setSize(int size);
  
  public int getFrom();
  
  public void setFrom(int from);
  
  public String getId();
  
  public void setId(String id);
  
  public String getTemplateId();
  
  public void setTemplateId(String templateId);
}
