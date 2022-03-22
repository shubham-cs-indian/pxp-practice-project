package com.cs.core.config.strategy.model.attribute;

import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigDetailsForSaveConcatenatedAttributeModel implements IConfigDetailsForSaveConcatenatedAttributeModel {
  
  protected Map<String, IReferencedSectionElementModel> referencedElements;
  protected Map<String, IAttribute>                     referencedAttributes;
  protected Map<String, ITag>                           referencedTags;
  protected Map<Long, IAttribute>                     referencedAttributesForCalculated;
  
  @Override
  public Map<String, IReferencedSectionElementModel> getReferencedElements()
  {
    return referencedElements;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractReferencedSectionElementModel.class)
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    this.referencedElements = referencedElements;
  }
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }

  @Override
  public Map<Long, IAttribute> getReferencedAttributesForCalculated()
  {
    return referencedAttributesForCalculated;
  }

  @Override
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  public void setReferencedAttributesForCalculated(Map<Long, IAttribute> referencedAttributesForCalculated)
  {
    this.referencedAttributesForCalculated = referencedAttributesForCalculated;
  }
  
}
