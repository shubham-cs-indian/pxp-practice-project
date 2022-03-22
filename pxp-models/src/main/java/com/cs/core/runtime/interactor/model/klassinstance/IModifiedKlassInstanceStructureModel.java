package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceStructure;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.attribute.IModifiedAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.configdetails.IRuntimeModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedRoleInstanceModel;

import java.util.List;

public interface IModifiedKlassInstanceStructureModel
    extends IKlassInstanceStructure, IRuntimeModel {
  
  public static final String ADDED_ATTRIBUTES    = "addedAttributes";
  public static final String DELETED_ATTRIBUTES  = "deletedAttributes";
  public static final String MODIFIED_ATTRIBUTES = "modifiedAttributes";
  public static final String ADDED_TAGS          = "addedTags";
  public static final String DELETED_TAGS        = "deletedTags";
  public static final String MODIFIED_TAGS       = "modifiedTags";
  public static final String ADDED_ROLES         = "addedRoles";
  public static final String DELETED_ROLES       = "deletedRoles";
  public static final String MODIFIED_ROLES      = "modifiedRoles";
  
  public List<IAttributeInstance> getAddedAttributes();
  
  public void setAddedAttributes(List<IAttributeInstance> addedAttributes);
  
  public List<String> getDeletedAttributes();
  
  public void setDeletedAttributes(List<String> deletedAttributes);
  
  public List<IModifiedAttributeInstanceModel> getModifiedAttributes();
  
  public void setModifiedAttributes(List<IModifiedAttributeInstanceModel> modifiedAttributes);
  
  public List<ITagInstance> getAddedTags();
  
  public void setAddedTags(List<ITagInstance> addedTags);
  
  public List<String> getDeletedTags();
  
  public void setDeletedTags(List<String> deletedTags);
  
  public List<IModifiedContentTagInstanceModel> getModifiedTags();
  
  public void setModifiedTags(List<IModifiedContentTagInstanceModel> modifiedTags);
  
  public List<IRoleInstance> getAddedRoles();
  
  public void setAddedRoles(List<IRoleInstance> addedRoles);
  
  public List<String> getDeletedRoles();
  
  public void setDeletedRoles(List<String> deletedRoles);
  
  public List<IModifiedRoleInstanceModel> getModifiedRoles();
  
  public void setModifiedRoles(List<IModifiedRoleInstanceModel> modifiedRoles);
}
