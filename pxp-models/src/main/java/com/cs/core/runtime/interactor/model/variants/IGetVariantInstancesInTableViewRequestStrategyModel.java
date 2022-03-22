package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGetVariantInstancesInTableViewRequestStrategyModel extends IModel {
  
  public static final String CONTEXT_ID                      = "contextId";
  public static final String KLASS_INSTANCE_ID               = "klassInstanceId";
  public static final String PARENT_ID                       = "parentId";
  public static final String ATTRIBUTES                      = "attributes";
  public static final String TAGS                            = "tags";
  public static final String ALL_SEARCH                      = "allSearch";
  public static final String SORT_OPTIONS                    = "sortOptions";
  public static final String FROM                            = "from";
  public static final String SIZE                            = "size";
  public static final String REFERENCED_TAGS                 = "referencedTags";
  public static final String REFERENCED_VARIANT_CONTEXTS     = "referencedVariantContexts";
  public static final String REFERENCED_ELEMENTS             = "referencedElements";
  public static final String CURRENT_USER_ID                 = "currentUserId";
  public static final String COLUMN_IDS                      = "columnIds";
  public static final String FILTER_INFO                     = "filterInfo";
  public static final String TIME_RANGE                      = "timeRange";
  public static final String CONTEXT_KLASSIDS_HAVING_RP      = "contextKlassIdsHavingRP";
  public static final String KLASSIDS_HAVING_RP              = "klassIdsHavingRP";
  public static final String ENTITIES                        = "entities";
  public static final String REFERENCED_PROPERTY_COLLECTIONS = "referencedPropertyCollections";
  public static final String TAXONOMY_IDS_HAVING_RP          = "taxonomyIdsHavingRP";
  
  public List<String> getContextKlassIdsHavingRP();
  
  public void setContextKlassIdsHavingRP(List<String> contextKlassIdsHavingRP);
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP);
  
  public IGetFilterInfoModel getFilterInfo();
  
  public void setFilterInfo(IGetFilterInfoModel filterInfo);
  
  public List<String> getColumnIds();
  
  public void setColumnIds(List<String> columnIds);
  
  public String getCurrentUserId();
  
  public void setCurrentUserId(String currentUserId);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String contentId);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
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
  
  public Map<String, IReferencedVariantContextModel> getReferencedVariantContexts();
  
  public void setReferencedVariantContexts(
      Map<String, IReferencedVariantContextModel> referencedVariantContexts);
  
  // key:propertyId[attributeId, tagId]
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  public IInstanceTimeRange getTimeRange();
  
  public void setTimeRange(IInstanceTimeRange timeRange);
  
  public String getAllSearch();
  
  public void setAllSearch(String allSearch);
  
  public List<String> getEntities();
  
  public void setEntities(List<String> entities);
  
  // key:propertyCollectionId
  public Map<String, IReferencedPropertyCollectionModel> getReferencedPropertyCollections();
  
  public void setReferencedPropertyCollections(
      Map<String, IReferencedPropertyCollectionModel> referencedSections);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
}
