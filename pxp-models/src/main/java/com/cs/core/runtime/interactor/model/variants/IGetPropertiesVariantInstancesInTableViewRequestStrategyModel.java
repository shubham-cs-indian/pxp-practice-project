package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGetPropertiesVariantInstancesInTableViewRequestStrategyModel extends IModel {
  
  public static final String CONTEXT_ID                  = "contextId";
  public static final String KLASS_INSTANCE_ID           = "klassInstanceId";
  public static final String VARANT_INSTANCE_ID          = "variantInstanceId";
  public static final String ATTRIBUTES                  = "attributes";
  public static final String TAGS                        = "tags";
  public static final String ALL_SEARCH                  = "allSearch";
  public static final String SORT_OPTIONS                = "sortOptions";
  public static final String FROM                        = "from";
  public static final String SIZE                        = "size";
  public static final String REFERENCED_TAGS             = "referencedTags";
  public static final String REFERENCED_VARIANT_CONTEXTS = "referencedVariantContexts";
  public static final String REFERENCED_ELEMENTS         = "referencedElements";
  public static final String CURRENT_USER_ID             = "currentUserId";
  public static final String FILTER_INFO                 = "filterInfo";
  public static final String COLUMN_IDS                  = "columnIds";
  public static final String ATTRIBUTE_ID                = "attributeId";
  public static final String TIME_RANGE                  = "timeRange";
  public static final String ENTITIES                    = "entities";
  public static final String KLASSIDS_HAVING_RP          = "klassIdsHavingRP";
  public static final String TAXONOMY_IDS_HAVING_RP      = "taxonomyIdsHavingRP";
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP);
  
  public List<String> getColumnIds();
  
  public void setColumnIds(List<String> columnIds);
  
  public String getCurrentUserId();
  
  public void setCurrentUserId(String currentUserId);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String contentId);
  
  public String getVariantInstanceId();
  
  public void setVariantInstanceId(String variantInstanceId);
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getSize();
  
  public void setSize(Integer size);
  
  public List<? extends IPropertyInstanceFilterModel> getAttributes();
  
  public void setAttributes(List<? extends IPropertyInstanceFilterModel> attributes);
  
  public List<? extends IPropertyInstanceFilterModel> getTags();
  
  public void setTags(List<? extends IPropertyInstanceFilterModel> tags);
  
  public List<ISortModel> getSortOptions();
  
  public void setSortOptions(List<ISortModel> sortOptions);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public IReferencedContextModel getReferencedVariantContexts();
  
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts);
  
  // key:propertyId[attributeId, tagId]
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  public IGetFilterInfoModel getFilterInfo();
  
  public void setFilterInfo(IGetFilterInfoModel filterInfo);
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
  
  public IInstanceTimeRange getTimeRange();
  
  public void setTimeRange(IInstanceTimeRange timeRange);
  
  public List<String> getEntities();
  
  public void setEntities(List<String> entities);
  
  public String getAllSearch();
  
  public void setAllSearch(String allSearch);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
}
