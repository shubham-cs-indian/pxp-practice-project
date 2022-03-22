package com.cs.core.runtime.interactor.entity.propertyinstance;

import com.cs.core.runtime.interactor.entity.datarule.IAttributeConflictingValue;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
    property = "baseType", visible = true)
public interface IContentAttributeInstance extends IPropertyInstance {
  
  public static final String TAGS               = "tags";
  public static final String ATTRIBUTE_ID       = "attributeId";
  public static final String CONFLICTING_VALUES = "conflictingValues";
  public static final String DUPLICATE_STATUS   = "duplicateStatus";
  public static final String IS_UNIQUE          = "isUnique";
  
  public List<? extends ITagInstance> getTags();
  
  public void setTags(List<? extends ITagInstance> tags);
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
  
  public List<IAttributeConflictingValue> getConflictingValues();
  
  public void setConflictingValues(List<IAttributeConflictingValue> conflictingValues);
  
  public List<IDuplicateTypeAndInstanceIds> getDuplicateStatus();
  
  public void setDuplicateStatus(List<IDuplicateTypeAndInstanceIds> duplicateStatus);
  
  public Integer getIsUnique();
  
  public void setIsUnique(Integer isUnique);
}
