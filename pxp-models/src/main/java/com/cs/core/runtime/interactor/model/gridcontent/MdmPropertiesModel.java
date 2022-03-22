package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class MdmPropertiesModel implements IMdmPropertiesModel {
  
  private static final long          serialVersionUID = 1L;
  
  protected List<IAttributeInstance> attributeInstances;
  protected List<ITagInstance>       tagInstances;
  
  @Override
  public List<IAttributeInstance> getAttributeInstances()
  {
    return attributeInstances;
  }
  
  @JsonDeserialize(contentAs = AttributeInstance.class)
  @Override
  public void setAttributeInstances(List<IAttributeInstance> attributeInstances)
  {
    this.attributeInstances = attributeInstances;
  }
  
  @Override
  public List<ITagInstance> getTagInstances()
  {
    return tagInstances;
  }
  
  @JsonDeserialize(contentAs = TagInstance.class)
  @Override
  public void setTagInstances(List<ITagInstance> tagInstances)
  {
    this.tagInstances = tagInstances;
  }
}
