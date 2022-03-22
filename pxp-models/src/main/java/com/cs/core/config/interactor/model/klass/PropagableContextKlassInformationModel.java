package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndCouplingTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class PropagableContextKlassInformationModel
    implements IPropagableContextKlassInformationModel {
  
  private static final long                      serialVersionUID = 1L;
  
  protected String                               id;
  protected String                               type;
  protected String                               label;
  protected String                               icon;
  protected String                               code;
  protected String                               natureType;
  protected Map<String, IIdAndCouplingTypeModel> propagableAttributes;
  protected Map<String, IIdAndCouplingTypeModel> propagableTags;
  
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
  public IEntity getEntity()
  {
    return null;
  }
  
  @Override
  public Long getVersionId()
  {
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
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
  public String getNatureType()
  {
    return natureType;
  }
  
  @Override
  public void setNatureType(String natureType)
  {
    this.natureType = natureType;
  }
  
  @Override
  public Map<String, IIdAndCouplingTypeModel> getPropagableAttributes()
  {
    return propagableAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndCouplingTypeModel.class)
  public void setPropagableAttributes(Map<String, IIdAndCouplingTypeModel> propagableAttributes)
  {
    this.propagableAttributes = propagableAttributes;
  }
  
  @Override
  public Map<String, IIdAndCouplingTypeModel> getPropagableTags()
  {
    return propagableTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndCouplingTypeModel.class)
  public void setPropagableTags(Map<String, IIdAndCouplingTypeModel> propagableTags)
  {
    this.propagableTags = propagableTags;
  }
}
