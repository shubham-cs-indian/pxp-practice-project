package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.model.instance.CreateInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateVariantModel extends CreateInstanceModel implements ICreateVariantModel {
  
  private static final long                 serialVersionUID          = 1L;
  
  protected String                          variantId;
  protected List<IContentTagInstance>       tags                      = new ArrayList<>();
  protected List<IContentTagInstance>       contextTags               = new ArrayList<>();
  protected String                          contextInstanceId;
  protected List<IContentAttributeInstance> attributes;
  protected String                          createdBy;
  protected Long                            createdOn;
  protected String                          lastModifiedBy;
  protected Long                            lastModified;
  protected String                          templateId;
  protected String                          contextId;
  protected IInstanceTimeRange              timeRange;
  protected List<IIdAndBaseType>            linkedInstances;
  protected Map<String, Object>             metadata;
  protected List<String>                    attributeIds;
  protected Boolean                         isDuplicateVariantAllowed = false;
  protected Boolean                         isFromExternalSource      = false;
  protected List<String>                    types;
  protected String                          tabId;
  protected String                          typeId;
  protected String                          baseType;
  protected List<String>                    languageCodes;
  protected String                          creationLanguage;
  protected List<String>                    contextTagIds;
  protected Boolean                         shouldAutoCreate;
  
  @Override
  public String getContextInstanceId()
  {
    return contextInstanceId;
  }
  
  @Override
  public void setContextInstanceId(String contextInstanceId)
  {
    this.contextInstanceId = contextInstanceId;
  }
  
  @Override
  public List<IContentAttributeInstance> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return attributes;
  }
  
  @Override
  public void setAttributes(List<IContentAttributeInstance> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<IContentTagInstance> getTags()
  {
    return tags;
  }
  
  @Override
  public void setTags(List<IContentTagInstance> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<IContentTagInstance> getContextTags()
  {
    return contextTags;
  }
  
  @Override
  public void setContextTags(List<IContentTagInstance> contextTags)
  {
    this.contextTags = contextTags;
  }
  
  @Override
  public String getVariantInstanceId()
  {
    return variantId;
  }
  
  @Override
  public void setVariantInstanceId(String variantId)
  {
    this.variantId = variantId;
  }
  
  @Override
  public String getCreatedBy()
  {
    return createdBy;
  }
  
  @Override
  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
  }
  
  @Override
  public Long getCreatedOn()
  {
    return createdOn;
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    this.createdOn = createdOn;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public Long getLastModified()
  {
    return lastModified;
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    this.lastModified = lastModified;
  }
  
  @Override
  public String getTemplateId()
  {
    return templateId;
  }
  
  @Override
  public void setTemplateId(String templateId)
  {
    this.templateId = templateId;
  }
  
  @Override
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public IInstanceTimeRange getTimeRange()
  {
    return timeRange;
  }
  
  @JsonDeserialize(as = InstanceTimeRange.class)
  @Override
  public void setTimeRange(IInstanceTimeRange timeRange)
  {
    this.timeRange = timeRange;
  }
  
  @Override
  public List<IIdAndBaseType> getLinkedInstances()
  {
    if (linkedInstances == null) {
      linkedInstances = new ArrayList<>();
    }
    return linkedInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndBaseType.class)
  public void setLinkedInstances(List<IIdAndBaseType> linkedInstances)
  {
    this.linkedInstances = linkedInstances;
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
  
  @Override
  public Boolean getIsDuplicateVariantAllowed()
  {
    return isDuplicateVariantAllowed;
  }
  
  @Override
  public void setIsDuplicateVariantAllowed(Boolean isDuplicateVariantAllowed)
  {
    this.isDuplicateVariantAllowed = isDuplicateVariantAllowed;
  }
  
  @Override
  public List<String> getTypes()
  {
    if (types == null) {
      types = new ArrayList<>();
    }
    return types;
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    this.types = types;
  }
  
  @Override
  public String getTabId()
  {
    return tabId;
  }
  
  @Override
  public void setTabId(String tabId)
  {
    this.tabId = tabId;
  }
  
  @Override
  public String getTypeId()
  {
    return typeId;
  }
  
  @Override
  public void setTypeId(String typeId)
  {
    this.typeId = typeId;
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
  public String getCreationLanguage()
  {
    return creationLanguage;
  }
  
  @Override
  public void setCreationLanguage(String creationLanguage)
  {
    this.creationLanguage = creationLanguage;
  }
  
  public List<String> getLanguageCodes()
  {
    if (languageCodes == null) {
      languageCodes = new ArrayList<>();
    }
    return languageCodes;
  }
  
  public void setLanguageCodes(List<String> languageCodes)
  {
    this.languageCodes = languageCodes;
  }
  
  @Override
  public List<String> getContextTagIds()
  {
    if (contextTagIds == null) {
      contextTagIds = new ArrayList<>();
    }
    return contextTagIds;
  }
  
  @Override
  public void setContextTagIds(List<String> contextTagIds)
  {
    this.contextTagIds = contextTagIds;
  }
  
  @Override
  public Boolean getShouldAutoCreate()
  {
    if (shouldAutoCreate == null) {
      shouldAutoCreate = true;
    }
    return shouldAutoCreate;
  }
  
  @Override
  public void setShouldAutoCreate(Boolean shouldAutoCreate)
  {
    this.shouldAutoCreate = shouldAutoCreate;
  }
}
