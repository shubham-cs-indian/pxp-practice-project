package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.entity.variantcontext.DefaultTimeRange;
import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TechnicalImageVariantWithAutoCreateEnableModel
    implements ITechnicalImageVariantWithAutoCreateEnableModel {
  
  private static final long                          serialVersionUID = 1L;
  
  protected String                                   id;
  protected List<IReferencedVariantContextTagsModel> contextualTags;
  protected List<String>                             attributeIds;
  protected String                                   type;
  protected List<IReferencedUniqueSelectorModel>     uniqueSelector   = new ArrayList<>();
  protected IDefaultTimeRange                        defaultTimeRange;
  protected Map<String, ITag>                        tagValueMap;
  
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
  public List<IReferencedVariantContextTagsModel> getContextualTags()
  {
    
    return contextualTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedVariantContextTagsModel.class)
  public void setContextualTags(List<IReferencedVariantContextTagsModel> contextualTags)
  {
    this.contextualTags = contextualTags;
  }
  
  @Override
  public List<String> getAttributeIds()
  {
    if (attributeIds == null) {
      return new ArrayList<String>();
    }
    return attributeIds;
  }
  
  @Override
  public void setAttributeIds(List<String> attributeIds)
  {
    this.attributeIds = attributeIds;
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
  public List<IReferencedUniqueSelectorModel> getUniqueSelectors()
  {
    return uniqueSelector;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedUniqueSelectorModel.class)
  public void setUniqueSelectors(List<IReferencedUniqueSelectorModel> uniqueSelector)
  {
    this.uniqueSelector = uniqueSelector;
  }
  
  @Override
  public IDefaultTimeRange getDefaultTimeRange()
  {
    return defaultTimeRange;
  }
  
  @JsonDeserialize(as = DefaultTimeRange.class)
  @Override
  public void setDefaultTimeRange(IDefaultTimeRange defaultTimeRange)
  {
    this.defaultTimeRange = defaultTimeRange;
  }
  
  @Override
  public Map<String, ITag> getTagValueMap()
  {
    return tagValueMap;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setTagValueMap(Map<String, ITag> tagValueMap)
  {
    this.tagValueMap = tagValueMap;
  }
}
