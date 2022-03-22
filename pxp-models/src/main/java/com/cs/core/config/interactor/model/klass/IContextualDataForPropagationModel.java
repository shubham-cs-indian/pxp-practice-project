package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.entity.datarule.IAttributeConflictingValue;
import com.cs.core.runtime.interactor.entity.tag.ITagConflictingValue;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IContextualDataForPropagationModel extends IModel {
  
  public static final String SOURCE_CONTENT_ID = "sourceContentId";
  public static final String CONTEXT_ID        = "contextId";
  public static final String ATTRIBUTES        = "attributes";
  public static final String TAGS              = "tags";
  
  public String getSourceContentId();
  
  public void setSourceContentId(String sourceContentId);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public Map<String, IAttributeConflictingValue> getAttributes();
  
  public void setAttributes(Map<String, IAttributeConflictingValue> attributes);
  
  public Map<String, ITagConflictingValue> getTags();
  
  public void setTags(Map<String, ITagConflictingValue> tags);
}
