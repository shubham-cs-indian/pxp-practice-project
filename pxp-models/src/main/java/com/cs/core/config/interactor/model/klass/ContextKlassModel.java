package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndCouplingTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ContextKlassModel implements IContextKlassModel {
  
  private static final long               serialVersionUID = 1L;
  
  protected String                        contextKlassId;
  protected List<IIdAndCouplingTypeModel> attributes;
  protected List<IIdAndCouplingTypeModel> tags;
  protected String                        contextId;
  
  @Override
  public String getContextKlassId()
  {
    return contextKlassId;
  }
  
  @Override
  public void setContextKlassId(String contextKlassId)
  {
    this.contextKlassId = contextKlassId;
  }
  
  @Override
  public List<IIdAndCouplingTypeModel> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndCouplingTypeModel.class)
  public void setTags(List<IIdAndCouplingTypeModel> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<IIdAndCouplingTypeModel> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return attributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndCouplingTypeModel.class)
  public void setAttributes(List<IIdAndCouplingTypeModel> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
}
