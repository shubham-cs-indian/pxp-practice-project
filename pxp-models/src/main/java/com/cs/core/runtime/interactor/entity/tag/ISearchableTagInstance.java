package com.cs.core.runtime.interactor.entity.tag;

import com.cs.core.runtime.interactor.entity.configuration.base.IRuntimeEntity;
import java.util.List;

public interface ISearchableTagInstance extends IRuntimeEntity {
  
  public static final String IS_SHOULD_VIOLATED    = "isShouldViolated";
  public static final String IS_MANDATORY_VIOLATED = "isMandatoryViolated";
  public static final String TAG_VALUES            = "tagValues";
  public static final String TAG_ID                = "tagId";
  public static final String KLASS_INSTANCE_ID     = "klassInstanceId";
  public static final String CODE                  = "code";
  public static final String VARIANT_INSTANCE_ID   = "variantInstanceId";
  public static final String CONTEXT_INSTANCE_ID   = "contextInstanceId";
  
  public Boolean getIsShouldViolated();
  
  public void setIsShouldViolated(Boolean isShouldViolated);
  
  public Boolean getIsMandatoryViolated();
  
  public void setIsMandatoryViolated(Boolean isMandatoryViolated);
  
  public List<ISearchableTagValue> getTagValues();
  
  public void setTagValues(List<ISearchableTagValue> tagValues);
  
  public void setTagId(String tagId);
  
  public String getTagId();
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getVariantInstanceId();
  
  public void setVariantInstanceId(String variantInstanceId);
  
  public void setContextInstanceId(String contextInstanceId);
  
  public String getContextInstanceId();
}
