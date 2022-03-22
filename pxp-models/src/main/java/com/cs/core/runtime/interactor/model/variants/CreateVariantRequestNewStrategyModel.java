package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.config.interactor.entity.klass.ProjectKlass;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateVariantRequestNewStrategyModel implements ICreateVariantRequestNewStrategyModel {
  
  private static final long                    serialVersionUID = 1L;
  protected IKlassInstance                     klassInstance;
  protected IGetConfigDetailsForCustomTabModel configDetails;
  protected List<IContentTagInstance>          contextTags;
  protected Boolean                            isDuplicateVariantAllowed;
  protected String                             baseType;
  protected Map<String, Object>                metadata;
  protected List<String>                       attributeIds;
  
  @Override
  public IKlassInstance getKlassInstance()
  {
    return klassInstance;
  }
  
  @Override
  public void setKlassInstance(IKlassInstance klassInstance)
  {
    this.klassInstance = klassInstance;
  }
  
  @Override
  public IGetConfigDetailsForCustomTabModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = ProjectKlass.class)
  public void setConfigDetails(IGetConfigDetailsForCustomTabModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public List<IContentTagInstance> getContextTags()
  {
    if (contextTags == null) {
      contextTags = new ArrayList<IContentTagInstance>();
    }
    return contextTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = TagInstance.class)
  public void setContextTags(List<IContentTagInstance> contextTags)
  {
    this.contextTags = contextTags;
  }
  
  @Override
  public Boolean getIsDuplicateVariantAllowed()
  {
    if (isDuplicateVariantAllowed == null) {
      isDuplicateVariantAllowed = false;
    }
    return isDuplicateVariantAllowed;
  }
  
  @Override
  public void setIsDuplicateVariantAllowed(Boolean isDuplicateVariantAllowed)
  {
    this.isDuplicateVariantAllowed = isDuplicateVariantAllowed;
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
  public Map<String, Object> getMetadata()
  {
    return metadata;
  }
  
  @JsonDeserialize(as = HashMap.class)
  @Override
  public void setMetadata(Map<String, Object> metadata)
  {
    this.metadata = metadata;
  }
  
  public List<String> getAttributeIds()
  {
    if (attributeIds == null) {
      attributeIds = new ArrayList<>();
    }
    return attributeIds;
  }
  
  public void setAttributeIds(List<String> attributeIds)
  {
    this.attributeIds = attributeIds;
  }
}
