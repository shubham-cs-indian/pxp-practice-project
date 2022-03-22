package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigEntityTreeInformationModel implements IConfigEntityTreeInformationModel {
  
  private static final long             serialVersionUID = 1L;
  
  protected String                      id;
  protected String                      label;
  protected String                      type;
  protected List<? extends ITreeEntity> children;
  protected String                      defaultUnit;
  protected Integer                     precision;
  protected String                      icon;
  protected String                      iconKey;
  protected String                      code;
  protected Long                        iid;
  
  public ConfigEntityTreeInformationModel()
  {
  }
  
  public ConfigEntityTreeInformationModel( IConfigEntityTreeInformationModel model)
  {
    super();
    this.id = model.getId();
    this.label = model.getLabel();
    this.type = model.getType();
    this.children = model.getChildren();
    this.defaultUnit = model.getDefaultUnit();
    this.precision = model.getPrecision();
    this.icon = model.getIcon();
    this.iconKey = model.getIconKey();
    this.code = model.getId();
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
  public List<? extends ITreeEntity> getChildren()
  {
    if (children == null) {
      children = new ArrayList<>();
    }
    return children;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityTreeInformationModel.class)
  @Override
  public void setChildren(List<? extends ITreeEntity> children)
  {
    this.children = children;
  }
  
  @JsonIgnore
  @Override
  public ITreeEntity getParent()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setParent(ITreeEntity parent)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
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
  
  @JsonIgnore
  @Override
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
  
  @JsonIgnore
  @Override
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
  
  @Override
  public String getDefaultUnit()
  {
    return defaultUnit;
  }
  
  @Override
  public void setDefaultUnit(String defaultUnit)
  {
    this.defaultUnit = defaultUnit;
  }
  
  @Override
  public Integer getPrecision()
  {
    return precision;
  }
  
  @Override
  public void setPrecision(Integer precision)
  {
    this.precision = precision;
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
  public Long getIid()
  {
    return iid;
  }

  @Override
  public void setIid(Long iid)
  {
    this.iid = iid;
  }
}
