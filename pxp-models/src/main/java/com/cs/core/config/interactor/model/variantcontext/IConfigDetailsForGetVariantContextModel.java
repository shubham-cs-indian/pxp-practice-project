package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IConfigDetailsForGetVariantContextModel extends IModel {
  
  public static final String REFERENCED_TAGS = "referencedTags";
  public static final String REFERENCED_TABS = "referencedTabs";
  
  public Map<String, IIdLabelCodeModel> getReferencedTabs();
  
  public void setReferencedTabs(Map<String, IIdLabelCodeModel> referencedTabs);
  
  public Map<String, ? extends ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ? extends ITag> referencedTags);
}
