package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class ConfigDetailsForGetVariantContextModel
    implements IConfigDetailsForGetVariantContextModel {
  
  private static final long                serialVersionUID = 1L;
  protected Map<String, ? extends ITag>    referencedTags;
  protected Map<String, IIdLabelCodeModel> referencedTabs;
  
  @Override
  public Map<String, ? extends ITag> getReferencedTags()
  {
    if (referencedTags == null) {
      referencedTags = new HashMap<>();
    }
    return referencedTags;
  }
  
  @JsonDeserialize(contentAs = Tag.class)
  @Override
  public void setReferencedTags(Map<String, ? extends ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedTabs()
  {
    if (referencedTabs == null) {
      referencedTabs = new HashMap<>();
    }
    return referencedTabs;
  }
  
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  @Override
  public void setReferencedTabs(Map<String, IIdLabelCodeModel> referencedTabs)
  {
    this.referencedTabs = referencedTabs;
  }
}
