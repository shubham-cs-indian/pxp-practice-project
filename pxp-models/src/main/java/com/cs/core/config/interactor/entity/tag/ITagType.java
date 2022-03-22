package com.cs.core.config.interactor.entity.tag;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

import java.util.List;

public interface ITagType extends IConfigEntity {
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public Boolean getIsRange();
  
  public void setIsRange(Boolean isRange);
  
  public List<ITagValue> getTagValues();
  
  public void setTagValues(List<ITagValue> tagValues);
}
