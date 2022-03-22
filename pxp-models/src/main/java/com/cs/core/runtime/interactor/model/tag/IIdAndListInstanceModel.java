package com.cs.core.runtime.interactor.model.tag;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Set;

public interface IIdAndListInstanceModel extends IModel {
  
  public static final String TAG_INSTANCE_IDS = "tagInstanceIds";
  public static final String KLASSINSTANCE_ID = "klassinstanceId";
  
  public Set<String> getTagInstanceIds();
  
  public void setTagInstanceIds(Set<String> tagInstanceIds);
  
  public String getKlassinstanceId();
  
  public void setKlassinstanceId(String klassinstanceId);
}
