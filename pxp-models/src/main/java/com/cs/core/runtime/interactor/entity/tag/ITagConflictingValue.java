package com.cs.core.runtime.interactor.entity.tag;

import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.runtime.interactor.entity.datarule.IConflictingValue;
import java.util.List;

public interface ITagConflictingValue extends IConflictingValue {
  
  public static final String TAG_VALUES = "tagValues";
  
  public List<IIdRelevance> getTagValues();
  
  public void setTagValues(List<IIdRelevance> defaultValue);
}
