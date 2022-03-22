package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.model.role.ModifiedRoleInstanceModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceStructure;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceStructure;
import com.cs.core.runtime.interactor.entity.propertyinstance.*;
import com.cs.core.runtime.interactor.entity.role.RoleInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.model.attribute.IModifiedAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.attribute.ModifiedAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.instance.AbstractModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedRoleInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IModifiedKlassInstanceStructureModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class ModifiedKlassInstanceStructureModel implements IModifiedKlassInstanceStructureModel {
  
  private static final long                        serialVersionUID = 1L;
  
  protected IKlassInstanceStructure                entity;
  
  protected List<IAttributeInstance>               addedAttributes;
  
  protected List<String>                           deletedAttributes;
  
  protected List<IModifiedAttributeInstanceModel>  modifiedAttributes;
  
  protected List<ITagInstance>                     addedTags;
  
  protected List<String>                           deletedTags;
  
  protected List<IModifiedContentTagInstanceModel> modifiedTags;
  
  protected List<IRoleInstance>                    addedRoles;
  
  protected List<String>                           deletedRoles;
  
  protected List<IModifiedRoleInstanceModel>       modifiedRoles;
  
  public ModifiedKlassInstanceStructureModel()
  {
    this.entity = new KlassInstanceStructure();
  }
  
  public ModifiedKlassInstanceStructureModel(IKlassInstanceStructure klassInstanceStructure)
  {
    this.entity = klassInstanceStructure;
  }
  
  @Override
  public IEntity getEntity()
  {
    return this.entity;
  }
  
  @JsonIgnore
  @Override
  public List<? extends ITagInstance> getTags()
  {
    throw new RuntimeException("Do not call this on Modified Model");
  }
  
  @JsonIgnore
  @Override
  public void setTags(List<? extends ITagInstance> tags)
  {
    throw new RuntimeException("Do not call this on Modified Model");
  }
  
  @JsonIgnore
  @Override
  public List<? extends IAttributeInstance> getAttributes()
  {
    throw new RuntimeException("Do not call this on Modified Model");
  }
  
  @JsonIgnore
  @Override
  public void setAttributes(List<? extends IContentAttributeInstance> attributes)
  {
    throw new RuntimeException("Do not call this on Modified Model");
  }
  
  @JsonIgnore
  @Override
  public List<IRoleInstance> getRoles()
  {
    throw new RuntimeException("Do not call this on Modified Model");
  }
  
  @JsonIgnore
  @Override
  public void setRoles(List<IRoleInstance> roles)
  {
    throw new RuntimeException("Do not call this on Modified Model");
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public String getData()
  {
    return entity.getData();
  }
  
  @Override
  public void setData(String data)
  {
    entity.setData(data);
  }
  
  @Override
  public String getComment()
  {
    return entity.getComment();
  }
  
  @Override
  public void setComment(String comment)
  {
    entity.setComment(comment);
  }
  
  @Override
  public String getCaption()
  {
    return entity.getCaption();
  }
  
  @Override
  public void setCaption(String caption)
  {
    entity.setCaption(caption);
  }
  
  @Override
  public String getDescription()
  {
    return entity.getDescription();
  }
  
  @Override
  public void setDescription(String description)
  {
    entity.setDescription(description);
  }
  
  @Override
  public Boolean getIsValueChanged()
  {
    return entity.getIsValueChanged();
  }
  
  @Override
  public void setIsValueChanged(Boolean isValueChanged)
  {
    entity.setIsValueChanged(isValueChanged);
  }
  
  @Override
  public String getStructureId()
  {
    return entity.getStructureId();
  }
  
  @Override
  public void setStructureId(String structureId)
  {
    entity.setStructureId(structureId);
  }
  
  @Override
  public String getReferenceId()
  {
    return entity.getReferenceId();
  }
  
  @Override
  public void setReferenceId(String referenceId)
  {
    entity.setReferenceId(referenceId);
  }
  
  @Override
  public List<IAttributeInstance> getAddedAttributes()
  {
    return addedAttributes;
  }
  
  @JsonDeserialize(contentAs = AttributeInstance.class)
  @Override
  public void setAddedAttributes(List<IAttributeInstance> addedAttributes)
  {
    this.addedAttributes = addedAttributes;
  }
  
  @Override
  public List<String> getDeletedAttributes()
  {
    return deletedAttributes;
  }
  
  @Override
  public void setDeletedAttributes(List<String> deletedAttributes)
  {
    this.deletedAttributes = deletedAttributes;
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
  @Override
  public List<IModifiedAttributeInstanceModel> getModifiedAttributes()
  {
    return modifiedAttributes;
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
  @JsonDeserialize(contentAs = ModifiedAttributeInstanceModel.class)
  @Override
  public void setModifiedAttributes(List<IModifiedAttributeInstanceModel> modifiedAttributes)
  {
    this.modifiedAttributes = modifiedAttributes;
  }
  
  @Override
  public List<ITagInstance> getAddedTags()
  {
    return addedTags;
  }
  
  @JsonDeserialize(contentAs = TagInstance.class)
  @Override
  public void setAddedTags(List<ITagInstance> addedTags)
  {
    this.addedTags = addedTags;
  }
  
  @Override
  public List<String> getDeletedTags()
  {
    return deletedTags;
  }
  
  @Override
  public void setDeletedTags(List<String> deletedTags)
  {
    this.deletedTags = deletedTags;
  }
  
  @Override
  public List<IModifiedContentTagInstanceModel> getModifiedTags()
  {
    return modifiedTags;
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
  @JsonDeserialize(contentAs = AbstractModifiedContentTagInstanceModel.class)
  @Override
  public void setModifiedTags(List<IModifiedContentTagInstanceModel> modifiedTags)
  {
    this.modifiedTags = modifiedTags;
  }
  
  @Override
  public List<IRoleInstance> getAddedRoles()
  {
    return addedRoles;
  }
  
  @JsonDeserialize(contentAs = RoleInstance.class)
  @Override
  public void setAddedRoles(List<IRoleInstance> addedRoles)
  {
    this.addedRoles = addedRoles;
  }
  
  @Override
  public List<String> getDeletedRoles()
  {
    return deletedRoles;
  }
  
  @Override
  public void setDeletedRoles(List<String> deletedRoles)
  {
    this.deletedRoles = deletedRoles;
  }
  
  @Override
  public List<IModifiedRoleInstanceModel> getModifiedRoles()
  {
    return modifiedRoles;
  }
  
  @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
  @JsonDeserialize(contentAs = ModifiedRoleInstanceModel.class)
  @Override
  public void setModifiedRoles(List<IModifiedRoleInstanceModel> modifiedRoles)
  {
    this.modifiedRoles = modifiedRoles;
  }
}
