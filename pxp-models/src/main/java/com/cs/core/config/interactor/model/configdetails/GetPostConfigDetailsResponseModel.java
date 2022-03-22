package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.ReferencedKlassDetailsModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetPostConfigDetailsResponseModel implements IGetPostConfigDetailsResponseModel {
  
  private static final long                                  serialVersionUID = 1L;
  
  protected List<IAttribute>                                 attributes;
  protected List<ITag>                                       tags;
  protected Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses;
  
  @Override
  public List<IAttribute> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return attributes;
  }
  
  @Override
  public void setAttributes(List<IAttribute> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<ITag> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }
  
  @Override
  public void setTags(List<ITag> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    if (referencedKlasses == null) {
      referencedKlasses = new HashMap<>();
    }
    return referencedKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedKlassDetailsModel.class)
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
}
