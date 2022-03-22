package com.cs.core.config.interactor.model.attribute;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IConfigDetailsForGridAttributesModel extends IModel {
  
  public static final String REFERENCED_ATTRIBUTES = "referencedAttributes";
  public static final String REFERENCED_TAGS       = "referencedTags";
  public static final String REFERENCED_CONTEXTS   = "referencedContexts";
  
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Map<String, IIdLabelCodeModel> getReferencedContexts();
  
  public void setReferencedContexts(Map<String, IIdLabelCodeModel> referencedContext);
}
