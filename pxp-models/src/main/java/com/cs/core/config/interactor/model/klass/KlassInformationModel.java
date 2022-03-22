package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassBasic;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

public class KlassInformationModel implements IKlassInformationModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          type;
  protected String          label;
  protected String          icon;
  protected String          code;
  protected String          natureType;
  
  public KlassInformationModel()
  {
  }
  
  public KlassInformationModel(IKlassBasic klassBasic)
  {
    this.id = klassBasic.getId();
    this.type = klassBasic.getType();
    this.label = klassBasic.getLabel();
    this.icon = klassBasic.getIcon();
    this.code = klassBasic.getCode();
  }
  
  public KlassInformationModel(IKlass klass)
  {
    this.id = klass.getId();
    this.type = klass.getType();
    this.label = klass.getLabel();
    this.icon = klass.getIcon();
    this.code = klass.getCode();
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
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
    ;
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
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public IEntity getEntity()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
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
  public String getNatureType()
  {
    return natureType;
  }
  
  @Override
  public void setNatureType(String natureType)
  {
    this.natureType = natureType;
  }
}
