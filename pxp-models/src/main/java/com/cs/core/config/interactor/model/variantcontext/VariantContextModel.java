package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.variantcontext.DefaultTimeRange;
import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.entity.variantcontext.VariantContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class VariantContextModel extends VariantContext implements IVariantContextModel {
  
  private static final long serialVersionUID = 1L;
  
  protected IVariantContext entity;
  
  public VariantContextModel()
  {
    this.entity = new VariantContext();
  }
  
  public VariantContextModel(IVariantContext entity)
  {
    this.entity = entity;
  }
  
  @Override
  public IEntity getEntity()
  {
    return entity;
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public String getLabel()
  {
    return entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    entity.setLabel(label);
  }
  
  @Override
  public String getType()
  {
    
    return entity.getType();
  }
  
  @Override
  public void setType(String type)
  {
    entity.setType(type);
  }
  
  @Override
  public View getDefaultView()
  {
    return entity.getDefaultView();
  }
  
  @Override
  public void setDefaultView(View defaultView)
  {
    entity.setDefaultView(defaultView);
  }
  
  public Boolean getIsAutoCreate()
  {
    
    return entity.getIsAutoCreate();
  }
  
  @Override
  public void setIsAutoCreate(Boolean isAutoCreate)
  {
    entity.setIsAutoCreate(isAutoCreate);
  }
  
  @Override
  public IDefaultTimeRange getDefaultTimeRange()
  {
    return entity.getDefaultTimeRange();
  }
  
  @JsonDeserialize(as = DefaultTimeRange.class)
  @Override
  public void setDefaultTimeRange(IDefaultTimeRange defaultTimeRange)
  {
    entity.setDefaultTimeRange(defaultTimeRange);
  }
  
  @Override
  public long getContextIID()
  {
    return entity.getContextIID();
  }
  
  @Override
  public void setContextIID(long contextIID)
  {
    entity.setContextIID(contextIID);
  }
}
