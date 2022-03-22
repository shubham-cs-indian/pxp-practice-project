package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class ReferencedContextPropertiesModel implements IReferencedContextPropertiesModel {
  
  private static final long              serialVersionUID = 1L;
  protected Map<String, IIdAndTypeModel> attributes;
  protected Map<String, IIdAndTypeModel> tags;
  protected String                       contextId;
  protected String                       contextKlassId;
  
  @Override
  public Map<String, IIdAndTypeModel> getAttributes()
  {
    return attributes;
  }
  
  @JsonDeserialize(contentAs = IdAndTypeModel.class)
  @Override
  public void setAttributes(Map<String, IIdAndTypeModel> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public Map<String, IIdAndTypeModel> getTags()
  {
    return tags;
  }
  
  @JsonDeserialize(contentAs = IdAndTypeModel.class)
  @Override
  public void setTags(Map<String, IIdAndTypeModel> tags)
  {
    this.tags = tags;
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
}
