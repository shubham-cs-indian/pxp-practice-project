package com.cs.core.config.interactor.entity.smartdocument.preset;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;
import com.cs.core.config.interactor.entity.datarule.IDataRuleTagValues;

import java.util.List;

public interface ISmartDocumentPresetTagRule extends IConfigEntity {
  
  public static final String TYPE       = "type";
  public static final String TAG_VALUES = "tagValues";
  
  public String getType();
  
  public void setType(String type);
  
  public List<IDataRuleTagValues> getTagValues();
  
  public void setTagValues(List<IDataRuleTagValues> tagValues);
}
