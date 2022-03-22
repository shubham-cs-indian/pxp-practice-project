package com.cs.core.config.interactor.model.attribute.standard;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.attribute.IConfigDetailsForGridAttributesModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class ConfigDetailsForGridAttributesModel implements IConfigDetailsForGridAttributesModel {
  
  private static final long                serialVersionUID = 1L;
  
  protected Map<String, IAttribute>        referencedAttributes;
  protected Map<String, ITag>              referencedTags;
  protected Map<String, IIdLabelCodeModel> referencedContext;
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  @Override
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @JsonDeserialize(contentAs = Tag.class)
  @Override
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedContexts()
  {
    return referencedContext;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setReferencedContexts(Map<String, IIdLabelCodeModel> referencedContext)
  {
    this.referencedContext = referencedContext;
  }
}
