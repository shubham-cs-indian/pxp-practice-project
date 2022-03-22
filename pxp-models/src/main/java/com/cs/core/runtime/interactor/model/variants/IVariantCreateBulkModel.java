package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IVariantCreateBulkModel extends IModel {
  
  public static final String CONTEXT_TAGS            = "contextTags";
  public static final String VARIANT_ID              = "variantId";
  public static final String NAME                    = "name";
  public static final String ATTRIBUTES              = "attributes";
  public static final String TAGS                    = "tags";
  public static final String STATUS_TAG_INSTANCES    = "statusTagInstances";
  public static final String CREATED_BY              = "createdBy";
  public static final String CREATED_ON              = "createdOn";
  public static final String LAST_MODIFIED           = "lastModified";
  public static final String LAST_MODIFIED_BY        = "lastModifiedBy";
  public static final String PARENT_ID               = "parentId";
  public static final String TIME_RANGE              = "timeRange";
  public static final String LINKED_INSTANCES        = "linkedInstances";
  public static final String IS_FROM_EXTERNAL_SOURCE = "isFromExternalSource";
  
  public List<IContentTagInstance> getContextTags();
  
  public void setContextTags(List<IContentTagInstance> tags);
  
  public String getName();
  
  public void setName(String name);
  
  public String getVariantId();
  
  public void setVariantId(String variantId);
  
  public List<IContentAttributeInstance> getAttributes();
  
  public void setAttributes(List<IContentAttributeInstance> tags);
  
  public List<IContentTagInstance> getTags();
  
  public void setTags(List<IContentTagInstance> tags);
  
  public List<ITagInstance> getStatusTagInstances();
  
  public void setStatusTagInstances(List<ITagInstance> statusTagInstances);
  
  public String getCreatedBy();
  
  public void setCreatedBy(String createdBy);
  
  public Long getCreatedOn();
  
  public void setCreatedOn(Long createdOn);
  
  public Long getLastModified();
  
  public void setLastModified(Long lastModified);
  
  public String getLastModifiedBy();
  
  public void setLastModifiedBy(String lastModifiedBy);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public IInstanceTimeRange getTimeRange();
  
  public void setTimeRange(IInstanceTimeRange timeRange);
  
  public List<String> getLinkedInstances();
  
  public void setLinkedInstances(List<String> linkedInstances);
  
  public Boolean getIsFromExternalSource();
  
  public void setIsFromExternalSource(Boolean isFromExternalSource);
}
