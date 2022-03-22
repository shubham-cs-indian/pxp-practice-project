package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IConfigDetailsForFilterInfoModel extends IModel {
  
  public static final String REFERENCED_ATTRIBUTES = "referencedAttributes";
  public static final String REFERENCED_TAGS       = "referencedTags";
  public static final String REFERENCED_ROLES      = "referencedRoles";
  
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Map<String, IRole> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IRole> referencedRoles);
}
