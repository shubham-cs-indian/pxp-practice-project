package com.cs.core.config.interactor.entity.matchandmerge;

import com.cs.core.config.interactor.entity.propertycollection.AbstractSectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.Section;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true, defaultImpl = Section.class)
public abstract class AbstractSection implements ISection {
  
  private static final long       serialVersionUID = 1L;
  
  protected String                id;
  protected String                label;
  protected String                icon             = "";
  protected String                iconKey          = "";          
  protected Integer               rows;
  protected Integer               columns;
  protected List<ISectionElement> elements         = new ArrayList<>();
  protected Boolean               isCollapsed      = false;
  protected Long                  versionId;
  protected Long                  versionTimestamp;
  protected String                lastModifiedBy;
  protected Integer               sequence         = 0;
  protected String                type             = this.getClass()
      .getName();
  protected String                propertyCollectionId;
  protected Boolean               isHidden         = false;
  protected Boolean               isSkipped        = false;
  protected Boolean               isInherited      = false;
  protected String                code;
  
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
  public List<ISectionElement> getElements()
  {
    return this.elements;
  }
  
  @JsonDeserialize(contentAs = AbstractSectionElement.class)
  @Override
  public void setElements(List<ISectionElement> elements)
  {
    this.elements = elements;
  }
  
  @Override
  public Integer getRows()
  {
    return rows;
  }
  
  @Override
  public void setRows(Integer rows)
  {
    this.rows = rows;
  }
  
  @Override
  public Integer getColumns()
  {
    return columns;
  }
  
  @Override
  public void setColumns(Integer columns)
  {
    this.columns = columns;
  }
  
  @Override
  public Boolean getIsCollapsed()
  {
    return this.isCollapsed;
  }
  
  @Override
  public void setIsCollapsed(Boolean isCollapsed)
  {
    this.isCollapsed = isCollapsed;
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
  public Integer getSequence()
  {
    return this.sequence;
  }
  
  @Override
  public void setSequence(Integer sequence)
  {
    this.sequence = sequence;
  }
  
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof AbstractSection))
      return false;
    AbstractSection other = (AbstractSection) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    }
    else if (!id.equals(other.id))
      return false;
    return true;
  }
  
  @Override
  public String getPropertyCollectionId()
  {
    return propertyCollectionId;
  }
  
  @Override
  public void setPropertyCollectionId(String propertyCollectionId)
  {
    this.propertyCollectionId = propertyCollectionId;
  }
  
  @Override
  public Boolean getIsHidden()
  {
    return isHidden;
  }
  
  @Override
  public void setIsHidden(Boolean isHidden)
  {
    this.isHidden = isHidden;
  }
  
  @Override
  public Boolean getIsSkipped()
  {
    if (isSkipped == null) {
      isSkipped = false;
    }
    return isSkipped;
  }
  
  @Override
  public void setIsSkipped(Boolean isSkipped)
  {
    this.isSkipped = isSkipped;
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
}
