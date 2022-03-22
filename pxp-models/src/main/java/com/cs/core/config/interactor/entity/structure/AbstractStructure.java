package com.cs.core.config.interactor.entity.structure;

import com.cs.core.config.interactor.entity.klass.IKlassViewSetting;
import com.cs.core.config.interactor.entity.klass.KlassViewSetting;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.javers.core.metamodel.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
public abstract class AbstractStructure implements IStructure, Cloneable {
  
  private static final long   serialVersionUID  = 1L;
  
  @Id
  protected String            id;
  
  protected String            label;
  
  protected String            icon;
  
  protected String            iconKey;
  
  @DiffIgnore
  protected List<IStructure>  structureChildren = new ArrayList<>();
  
  protected String            type              = this.getClass()
      .getName();
  
  protected Boolean           isGhost           = false;
  
  protected Integer           position;
  
  @DiffIgnore
  protected String            structureId;
  
  protected Long              versionId;
  
  protected Long              versionTimestamp;
  
  protected IKlassViewSetting viewSettings;
  
  protected Boolean           isInherited;
  
  protected String            linkPath;
  
  protected String            lastModifiedBy;
  
  protected String            code;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
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
  public String getLabel()
  {
    return this.label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getIcon()
  {
    return this.icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    return this.iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
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
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public List<IStructure> getStructureChildren()
  {
    return this.structureChildren;
  }
  
  @JsonDeserialize(contentAs = AbstractStructure.class)
  @Override
  public void setStructureChildren(List<IStructure> elements)
  {
    this.structureChildren = elements;
  }
  
  @Override
  public Boolean getIsGhost()
  {
    return this.isGhost;
  }
  
  @Override
  public void setIsGhost(Boolean isGhost)
  {
    this.isGhost = isGhost;
  }
  
  @Override
  public String getStructureId()
  {
    return this.structureId;
  }
  
  @Override
  public void setStructureId(String structureId)
  {
    this.structureId = structureId;
  }
  
  @Override
  public Integer getPosition()
  {
    return this.position;
  }
  
  @Override
  public void setPosition(Integer position)
  {
    this.position = position;
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
  public IKlassViewSetting getViewSettings()
  {
    return viewSettings;
  }
  
  @JsonDeserialize(as = KlassViewSetting.class)
  @Override
  public void setViewSettings(IKlassViewSetting viewSettings)
  {
    this.viewSettings = viewSettings;
  }
  
  @Override
  public Boolean getIsInherited()
  {
    return isInherited;
  }
  
  @Override
  public void setIsInherited(Boolean isInherited)
  {
    this.isInherited = isInherited;
  }
  
  @Override
  public String getLinkPath()
  {
    return linkPath;
  }
  
  @Override
  public void setLinkPath(String linkPath)
  {
    this.linkPath = linkPath;
  }
  
  @Override
  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }
}
