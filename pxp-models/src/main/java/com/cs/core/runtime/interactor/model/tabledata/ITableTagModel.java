package com.cs.core.runtime.interactor.model.tabledata;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.tag.ITagMatchValueModel;

import java.util.List;
import java.util.Map;

public interface ITableTagModel extends IModel {
  
  public static final String ID                    = "id";
  public static final String TAG_ID                = "tagId";
  public static final String KLASS_INSTANCE_VALUES = "klassInstanceValues";
  public static final String TAG_VALUES            = "tagValues";
  
  public String getId();
  
  public void setId(String id);
  
  public String getTagId();
  
  public void setTagId(String tagId);
  
  public Map<String, Map<String, Object>> getKlassInstanceValues();
  
  public void setKlassInstanceValues(Map<String, Map<String, Object>> klassInstances);
  
  public List<ITagMatchValueModel> getTagValues();
  
  public void setTagValues(List<ITagMatchValueModel> tagValues);
}
