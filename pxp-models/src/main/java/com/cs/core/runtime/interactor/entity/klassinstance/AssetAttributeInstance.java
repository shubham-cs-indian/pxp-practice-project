package com.cs.core.runtime.interactor.entity.klassinstance;

import com.cs.core.runtime.interactor.entity.propertyinstance.IAssetAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.PropertyInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.variants.ContextInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssetAttributeInstance extends PropertyInstance implements IAssetAttributeInstance {
  
  private static final long     serialVersionUID = 1L;
  
  protected String              attributeId;
  protected String              assetInstanceId;
  protected Boolean             isDefault        = false;
  protected String              fileName;
  protected String              description;
  protected String              variantOf;
  protected Map<String, Object> notification;
  protected String              baseType         = this.getClass()
      .getName();
  protected List<ITagInstance>  tags             = new ArrayList<>();
  protected String              type;
  protected Boolean             shouldMerge      = true;
  protected IContextInstance    context;
  
  public String getType()
  {
    return type;
  }
  
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getAssetInstanceId()
  {
    return assetInstanceId;
  }
  
  @Override
  public void setAssetInstanceId(String assetInstanceId)
  {
    this.assetInstanceId = assetInstanceId;
  }
  
  @Override
  public Boolean getIsDefault()
  {
    return isDefault;
  }
  
  @Override
  public void setIsDefault(Boolean isDefault)
  {
    this.isDefault = isDefault;
  }
  
  @Override
  public String getFileName()
  {
    return fileName;
  }
  
  @Override
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
  
  @Override
  public String getDescription()
  {
    return description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  @Override
  public List<? extends ITagInstance> getTags()
  {
    return this.tags;
  }
  
  @SuppressWarnings("unchecked")
  @JsonDeserialize(contentAs = TagInstance.class)
  @Override
  public void setTags(List<? extends ITagInstance> tags)
  {
    this.tags = (List<ITagInstance>) tags;
  }
  
  @Override
  public String getAttributeId()
  {
    return this.attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public Map<String, Object> getNotification()
  {
    return notification;
  }
  
  @Override
  public void setNotification(Map<String, Object> notification)
  {
    this.notification = notification;
  }
  
  @Override
  public Boolean getShouldMerge()
  {
    return shouldMerge;
  }
  
  @Override
  public Boolean setShouldMerge(Boolean shouldMerge)
  {
    return this.shouldMerge = shouldMerge;
  }
  
  @Override
  public IContextInstance getContext()
  {
    return context;
  }
  
  @Override
  @JsonDeserialize(as = ContextInstance.class)
  public void setContext(IContextInstance context)
  {
    this.context = context;
  }
}
