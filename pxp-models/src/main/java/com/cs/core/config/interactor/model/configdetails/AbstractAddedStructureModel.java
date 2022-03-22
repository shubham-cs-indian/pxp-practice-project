package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.config.interactor.entity.klass.IKlassViewSetting;
import com.cs.core.config.interactor.entity.klass.KlassViewSetting;
import com.cs.core.config.interactor.entity.structure.AbstractStructure;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.entity.structure.IStructureValidator;
import com.cs.core.config.interactor.model.attribute.AddedClassFrameStructureModel;
import com.cs.core.config.interactor.model.attribute.AddedContainerFrameStructureModel;
import com.cs.core.config.interactor.model.attribute.AddedHTMLFrameStructureModel;
import com.cs.core.config.interactor.model.attribute.AddedHTMLVisualAttributeFrameStructureModel;
import com.cs.core.config.interactor.model.attribute.AddedImageFrameStructureModel;
import com.cs.core.config.interactor.model.attribute.AddedImageVisualAttributeFrameStructureModel;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonTypeInfo(use = Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @Type(value = AddedHTMLFrameStructureModel.class, name = "com.cs.core.config.interactor.entity.visualattribute.HTMLFrameStructure"),
    @Type(value = AddedImageFrameStructureModel.class, name = "com.cs.core.config.interactor.entity.visualattribute.ImageFrameStructure"),
    @Type(value = AddedClassFrameStructureModel.class, name = "com.cs.core.config.interactor.entity.visualattribute.ClassFrameStructure"),
    @Type(value = AddedContainerFrameStructureModel.class,
        name = "com.cs.core.config.interactor.entity.visualattribute.ContainerFrameStructure"),
    @Type(value = AddedHTMLVisualAttributeFrameStructureModel.class,
        name = "com.cs.core.config.interactor.entity.visualattribute.HTMLVisualAttributeFrameStructure"),
    @Type(value = AddedImageVisualAttributeFrameStructureModel.class,
        name = "com.cs.core.config.interactor.entity.visualattribute.ImageVisualAttributeFrameStructure") })
public abstract class AbstractAddedStructureModel implements IAddedStructureModel {
  
  private static final long serialVersionUID = 1L;
  
  protected IStructure      entity;
  
  protected String          parentId;
  
  public AbstractAddedStructureModel(IStructure entity)
  {
    this.entity = entity;
  }
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
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
  public String getLabel()
  {
    return entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    entity.setLabel(label);
  }
  
  @Override
  public String getIcon()
  {
    return entity.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    entity.setIcon(icon);
  }
  
  @Override
  public String getIconKey()
  {
    return entity.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    entity.setIconKey(iconKey);
  }
  
  @Override
  public List<IStructure> getStructureChildren()
  {
    return entity.getStructureChildren();
  }
  
  @JsonDeserialize(contentAs = AbstractStructure.class)
  @Override
  public void setStructureChildren(List<IStructure> elements)
  {
    entity.setStructureChildren(elements);
  }
  
  @Override
  public IStructureValidator getValidator()
  {
    return entity.getValidator();
  }
  
  @Override
  public void setValidator(IStructureValidator validator)
  {
    entity.setValidator(validator);
  }
  
  @Override
  public String getType()
  {
    return this.entity.getType();
  }
  
  @Override
  public void setType(String type)
  {
    this.entity.setType(type);
  }
  
  @Override
  public IStructure getEntity()
  {
    return this.entity;
  }
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  @Override
  public String getStructureId()
  {
    return this.entity.getStructureId();
  }
  
  @Override
  public void setStructureId(String structureId)
  {
    this.entity.setStructureId(structureId);
  }
  
  @Override
  public Integer getPosition()
  {
    return this.entity.getPosition();
  }
  
  @Override
  public void setPosition(Integer position)
  {
    this.entity.setPosition(position);
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
  public Boolean getIsGhost()
  {
    return entity.getIsGhost();
  }
  
  @Override
  public void setIsGhost(Boolean isGhost)
  {
    entity.setIsGhost(isGhost);
  }
  
  @JsonDeserialize(as = KlassViewSetting.class)
  @Override
  public IKlassViewSetting getViewSettings()
  {
    return entity.getViewSettings();
  }
  
  @Override
  public void setViewSettings(IKlassViewSetting viewSettings)
  {
    entity.setViewSettings(viewSettings);
  }
  
  @Override
  public Boolean getIsInherited()
  {
    return entity.getIsInherited();
  }
  
  @Override
  public void setIsInherited(Boolean isInherited)
  {
    entity.setIsInherited(isInherited);
  }
  
  @Override
  public String getLinkPath()
  {
    return entity.getLinkPath();
  }
  
  @Override
  public void setLinkPath(String linkPath)
  {
    entity.setLinkPath(linkPath);
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
  public Object clone() throws CloneNotSupportedException
  {
    // TODO Auto-generated method stub
    return super.clone();
  }
}
