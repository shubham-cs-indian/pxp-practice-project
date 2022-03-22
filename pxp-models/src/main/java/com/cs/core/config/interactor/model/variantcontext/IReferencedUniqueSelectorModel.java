package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IReferencedUniqueSelectorModel extends IModel {
  
  public static final String TAGS = "tags";
  
  public List<IReferencedVariantContextTagsModel> getTags();
  
  public void setTags(List<IReferencedVariantContextTagsModel> tags);
}
