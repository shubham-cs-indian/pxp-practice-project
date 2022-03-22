package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface ITechnicalImageVariantWithAutoCreateEnableModel extends IModel {
  
  public static final String ID                 = "id";
  public static final String TYPE               = "type";
  public static final String ATTRIBUTE_IDS      = "attributeIds";
  public static final String CONTEXTUAL_TAGS    = "contextualTags";
  public static final String UNIQUE_SELECTOR    = "uniqueSelectors";
  public static final String DEFAULT_TIME_RANGE = "defaultTimeRange";
  public static final String TAG_VALUE_MAP      = "tagValueMap";
  
  public String getId();
  
  public void setId(String id);
  
  public String getType();
  
  public void setType(String type);
  
  public List<IReferencedVariantContextTagsModel> getContextualTags();
  
  public void setContextualTags(List<IReferencedVariantContextTagsModel> contextualTags);
  
  public List<String> getAttributeIds();
  
  public void setAttributeIds(List<String> attributeIds);
  
  public List<IReferencedUniqueSelectorModel> getUniqueSelectors();
  
  public void setUniqueSelectors(List<IReferencedUniqueSelectorModel> uniqueSelector);
  
  public IDefaultTimeRange getDefaultTimeRange();
  
  public void setDefaultTimeRange(IDefaultTimeRange defaultTimeRange);
  
  public Map<String, ITag> getTagValueMap();
  
  public void setTagValueMap(Map<String, ITag> tagValueMap);
}
