package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IReferencedContextPropertiesModel extends IModel {
  
  public static final String ATTRIBUTES       = "attributes";
  public static final String TAGS             = "tags";
  public static final String CONTEXT_ID       = "contextId";
  public static final String CONTEXT_KLASS_ID = "contextKlassId";
  
  // key:attributeId
  public Map<String, IIdAndTypeModel> getAttributes();
  
  public void setAttributes(Map<String, IIdAndTypeModel> attributes);
  
  // key:tagId
  public Map<String, IIdAndTypeModel> getTags();
  
  public void setTags(Map<String, IIdAndTypeModel> tags);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public String getContextKlassId();
  
  public void setContextKlassId(String contextKlassId);
}
