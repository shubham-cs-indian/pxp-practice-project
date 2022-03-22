package com.cs.core.runtime.interactor.entity.propertyinstance;

import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import java.util.List;

public interface IAssetAttributeInstance extends IPropertyInstance {
  
  public static final String TYPE              = "type";
  public static final String TAGS              = "tags";
  public static final String ASSET_INSTANCE_ID = "assetInstanceId";
  public static final String ATTRIBUTE_ID      = "attributeId";
  public static final String IS_DEFAULT        = "isDefault";
  public static final String FILENAME          = "fileName";
  public static final String DESCRIPTION       = "description";
  public static final String SHOULD_MERGE      = "shouldMerge";
  public static final String CONTEXT           = "context";
  
  public String getType();
  
  public void setType(String type);
  
  public List<? extends ITagInstance> getTags();
  
  public void setTags(List<? extends ITagInstance> tags);
  
  public String getAssetInstanceId();
  
  public void setAssetInstanceId(String assetInstanceId);
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
  
  public Boolean getIsDefault();
  
  public void setIsDefault(Boolean isDefault);
  
  public String getFileName();
  
  public void setFileName(String fileName);
  
  public String getDescription();
  
  public void setDescription(String description);
  
  public Boolean getShouldMerge();
  
  public Boolean setShouldMerge(Boolean shouldMerge);
  
  public IContextInstance getContext();
  
  public void setContext(IContextInstance context);
}
