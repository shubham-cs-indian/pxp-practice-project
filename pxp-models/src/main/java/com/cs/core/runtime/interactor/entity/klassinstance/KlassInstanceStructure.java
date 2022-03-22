package com.cs.core.runtime.interactor.entity.klassinstance;

import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.role.RoleInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KlassInstanceStructure implements IKlassInstanceStructure {
  
  private static final long                           serialVersionUID = 1L;
  
  protected String                                    id;
  
  protected String                                    data;
  
  protected List<IRoleInstance>                       roles            = new ArrayList<>();
  
  protected List<? extends IContentAttributeInstance> attributes       = new ArrayList<>();
  
  protected List<TagInstance>                         tags             = new ArrayList<>();
  
  protected String                                    structureId;
  
  protected String                                    comment;
  
  protected String                                    caption;
  
  protected String                                    description;
  
  protected Long                                      versionId        = 0l;
  
  protected Long                                      versionTimestamp;
  
  protected String                                    lastModifiedBy;
  
  protected Boolean                                   isValueChanged;
  
  protected String                                    referenceId;
  
  @Override
  public void setIsValueChanged(Boolean isValueChanged)
  {
    this.isValueChanged = isValueChanged;
  }
  
  @Override
  public Boolean getIsValueChanged()
  {
    return isValueChanged;
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
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public List<? extends ITagInstance> getTags()
  {
    return this.tags;
  }
  
  @JsonDeserialize(contentAs = TagInstance.class)
  @SuppressWarnings("unchecked")
  @Override
  public void setTags(List<? extends ITagInstance> tags)
  {
    this.tags = (List<TagInstance>) tags;
  }
  
  @Override
  public List<? extends IContentAttributeInstance> getAttributes()
  {
    return this.attributes;
  }
  
  @JsonDeserialize(contentAs = AttributeInstance.class)
  @SuppressWarnings("unchecked")
  @Override
  public void setAttributes(List<? extends IContentAttributeInstance> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<IRoleInstance> getRoles()
  {
    return this.roles;
  }
  
  @JsonDeserialize(contentAs = RoleInstance.class)
  @Override
  public void setRoles(List<IRoleInstance> roles)
  {
    this.roles = roles;
  }
  
  @Override
  public String getData()
  {
    return this.data;
  }
  
  @Override
  public void setData(String data)
  {
    this.data = data;
  }
  
  @Override
  public String getComment()
  {
    return comment;
  }
  
  @Override
  public void setComment(String comment)
  {
    this.comment = comment;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getCaption()
  {
    return caption;
  }
  
  @Override
  public void setCaption(String caption)
  {
    this.caption = caption;
  }
  
  @Override
  public String getDescription()
  {
    return description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  @Override
  public String getStructureId()
  {
    return structureId;
  }
  
  @Override
  public void setStructureId(String structureId)
  {
    this.structureId = structureId;
  }
  
  @Override
  public String getReferenceId()
  {
    return referenceId;
  }
  
  @Override
  public void setReferenceId(String referenceId)
  {
    this.referenceId = referenceId;
  }
}
