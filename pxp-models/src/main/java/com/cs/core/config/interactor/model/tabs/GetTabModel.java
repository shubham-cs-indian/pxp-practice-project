package com.cs.core.config.interactor.model.tabs;

import java.util.Map;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetTabModel extends ConfigResponseWithAuditLogModel implements IGetTabModel {
  
  private static final long                serialVersionUID = 1L;
  protected IGetTabEntityModel             tab;
  protected Map<String, IIdLabelTypeModel> referencedProperties;
  
  @Override
  public IGetTabEntityModel getTab()
  {
    return tab;
  }
  
  @Override
  @JsonDeserialize(as = GetTabEntityModel.class)
  public void setTab(IGetTabEntityModel tab)
  {
    this.tab = tab;
  }
  
  @Override
  public Map<String, IIdLabelTypeModel> getReferencedProperties()
  {
    return referencedProperties;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelTypeModel.class)
  public void setReferencedProperties(Map<String, IIdLabelTypeModel> referencedProperties)
  {
    this.referencedProperties = referencedProperties;
  }
}
