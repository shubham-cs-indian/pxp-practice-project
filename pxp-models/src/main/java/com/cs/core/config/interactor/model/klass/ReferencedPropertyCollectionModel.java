package com.cs.core.config.interactor.model.klass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ReferencedPropertyCollectionModel implements IReferencedPropertyCollectionModel {
  
  private static final long                                 serialVersionUID = 1L;
  protected Boolean                                         isCollapsed      = false;
  protected Boolean                                         isHidden         = false;
  protected Integer                                         rows;
  protected Integer                                         columns;
  protected Integer                                         sequence;
  protected String                                          propertyCollectionId;
  protected String                                          id;
  protected String                                          label;
  protected String                                          icon;
  protected String                                          iconKey;
  protected String                                          type;
  protected List<IReferencedPropertyCollectionElementModel> elements;
  protected String                                          code;
  
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
  public Boolean getIsCollapsed()
  {
    return isCollapsed;
  }
  
  @Override
  public void setIsCollapsed(Boolean isCollapsed)
  {
    this.isCollapsed = isCollapsed;
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
  public Integer getSequence()
  {
    return sequence;
  }
  
  @Override
  public void setSequence(Integer sequence)
  {
    this.sequence = sequence;
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
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getIcon()
  {
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    return iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
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
  public List<IReferencedPropertyCollectionElementModel> getElements()
  {
    if (elements == null) {
      elements = new ArrayList<>();
    }
    return elements;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedPropertyCollectionElementModel.class)
  public void setElements(List<IReferencedPropertyCollectionElementModel> elements)
  {
    this.elements = elements;
  }
  
  /**
   * ****************** ignored properties ***************
   */
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  @JsonIgnore
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
