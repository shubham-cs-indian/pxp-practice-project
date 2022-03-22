package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;

import java.util.List;
import java.util.Map;

public interface ICreateVariantModel extends ICreateInstanceModel {
  
  String TAGS                         = "tags";
  String CONTEXT_TAGS                 = "contextTags";
  String ID                           = "id";
  String ORIGINAL_INSTANCE_ID         = "originalInstanceId";
  String VARIANT_INSTANCE_ID          = "variantInstanceId";
  String NAME                         = "name";
  String ATTRIBUTES                   = "attributes";
  String CONTEXT_INSTANCE_ID          = "contextInstanceId";
  String CREATED_BY                   = "createdBy";
  String CREATED_ON                   = "createdOn";
  String LAST_MODIFIED                = "lastModified";
  String LAST_MODIFIED_BY             = "lastModifiedBy";
  String TEMPLATE_ID                  = "templateId";
  String PARENT_ID                    = "parentId";
  String CONTEXT_ID                   = "contextId";
  String TIME_RANGE                   = "timeRange";
  String LINKED_INSTANCES             = "linkedInstances";
  String METADATA                     = "metadata";
  String ATTRIBUTE_IDS                = "attributeIds";
  String IS_DUPLICATE_VARIANT_ALLOWED = "isDuplicateVariantAllowed";
  String TYPES                        = "types";
  String TAB_ID                       = "tabId";
  String TYPE_ID                      = "typeId";
  String BASETYPE                     = "baseType";
  String LANGUAGE_CODES               = "languageCodes";
  String CREATION_LANGUAGE            = "creationLanguage";
  String CONTEXT_TAG_IDS              = "contextTagIds";
  String SHOULD_AUTO_CREATE           = "shouldAutoCreate";
  
  public List<IContentTagInstance> getTags();
  
  public void setTags(List<IContentTagInstance> tags);
  
  public List<IContentTagInstance> getContextTags();
  
  public void setContextTags(List<IContentTagInstance> contextTags);
  
  public String getVariantInstanceId();
  
  public void setVariantInstanceId(String variantInstanceId);
  
  public List<IContentAttributeInstance> getAttributes();
  
  public void setAttributes(List<IContentAttributeInstance> tags);
  
  public String getContextInstanceId();
  
  public void setContextInstanceId(String contextInstanceId);
  
  public String getCreatedBy();
  
  public void setCreatedBy(String createdBy);
  
  public Long getCreatedOn();
  
  public void setCreatedOn(Long createdOn);
  
  public Long getLastModified();
  
  public void setLastModified(Long lastModified);
  
  public String getLastModifiedBy();
  
  public void setLastModifiedBy(String lastModifiedBy);
  
  public String getTemplateId();
  
  public void setTemplateId(String templateId);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public IInstanceTimeRange getTimeRange();
  
  public void setTimeRange(IInstanceTimeRange timeRange);
  
  public List<IIdAndBaseType> getLinkedInstances();
  
  public void setLinkedInstances(List<IIdAndBaseType> linkedInstances);
  
  public Map<String, Object> getMetadata();
  
  public void setMetadata(Map<String, Object> metadata);
  
  public List<String> getAttributeIds();
  
  public void setAttributeIds(List<String> attributeIds);
  
  public Boolean getIsDuplicateVariantAllowed();
  
  public void setIsDuplicateVariantAllowed(Boolean isDuplicateVariantAllowed);
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
  
  public String getOriginalInstanceId();
  
  public void setOriginalInstanceId(String originalInstanceId);
  
  public String getTabId();
  
  public void setTabId(String tabId);
  
  public String getTypeId();
  
  public void setTypeId(String typeId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
  
  public String getCreationLanguage();
  
  public void setCreationLanguage(String creationLanguage);
  
  public List<String> getContextTagIds();
  
  public void setContextTagIds(List<String> contextTagIds);
  
  public Boolean getShouldAutoCreate();
  
  public void setShouldAutoCreate(Boolean shouldAutoCreate);
}
