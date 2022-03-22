package com.cs.core.runtime.interactor.entity.klassinstance;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import java.util.List;

public interface IKlassInstanceStructure extends IEntity {
  
  public static final String TAGS             = "tags";
  public static final String ATTRIBUTES       = "attributes";
  public static final String ROLES            = "roles";
  public static final String DATA             = "data";
  public static final String COMMENT          = "comment";
  public static final String CAPTION          = "caption";
  public static final String DESCRIPTION      = "description";
  public static final String IS_VALUE_CHANGED = "isValueChanged";
  public static final String STRUCTURE_ID     = "structureId";
  public static final String REFERENCE_ID     = "referenceId";
  
  public List<? extends ITagInstance> getTags();
  
  public void setTags(List<? extends ITagInstance> tags);
  
  public List<? extends IContentAttributeInstance> getAttributes();
  
  public void setAttributes(List<? extends IContentAttributeInstance> attributes);
  
  public List<IRoleInstance> getRoles();
  
  public void setRoles(List<IRoleInstance> roles);
  
  public String getData();
  
  public void setData(String data);
  
  public String getComment();
  
  public void setComment(String comment);
  
  public String getCaption();
  
  public void setCaption(String caption);
  
  public String getDescription();
  
  public void setDescription(String description);
  
  public Boolean getIsValueChanged();
  
  public void setIsValueChanged(Boolean isValueChanged);
  
  public String getStructureId();
  
  public void setStructureId(String structureId);
  
  public String getReferenceId();
  
  public void setReferenceId(String referenceId);
}
