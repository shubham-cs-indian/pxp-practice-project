package com.cs.core.config.interactor.model.tabs;

import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;

public interface IGetTabModel extends IConfigResponseWithAuditLogModel {
  
  public static final String TAB                   = "tab";
  public static final String REFERENCED_PROPERTIES = "referencedProperties";
  
  public IGetTabEntityModel getTab();
  
  public void setTab(IGetTabEntityModel tab);
  
  public Map<String, IIdLabelTypeModel> getReferencedProperties();
  
  public void setReferencedProperties(Map<String, IIdLabelTypeModel> referencedProperties);
}
