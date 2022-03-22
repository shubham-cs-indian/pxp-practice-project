package com.cs.core.runtime.interactor.model.filter;

import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("rawtypes")
public interface IKlassInstanceFilterModel extends IModel {
  
  public static final String ATTRIBUTES            = "attributes";
  public static final String TAGS                  = "tags";
  public static final String ALL_SEARCH            = "allSearch";
  public static final String ID                    = "id";
  public static final String ROLES                 = "roles";
  public static final String CURRENT_USER_ID       = "currentUserId";
  public static final String DIMENSIONAL_TAG_IDS   = "dimensionalTagIds";
  public static final String FROM                  = "from";
  public static final String SIZE                  = "size";
  public static final String GET_FOLDERS           = "getFolders";
  public static final String KLASS_IDS             = "klassIds";
  public static final String SELECTED_ROLES        = "selectedRoles";
  public static final String SELECTED_TYPES        = "selectedTypes";
  public static final String IS_RED                = "isRed";
  public static final String IS_YELLOW             = "isYellow";
  public static final String IS_GREEN              = "isGreen";
  public static final String IS_ORANGE             = "isOrange";
  public static final String SELECTED_TAXONOMY_IDS = "selectedTaxonomyIds";
  public static final String PARENT_TAXONOMY_ID    = "parentTaxonomyId";
  public static final String SORT_OPTIONS          = "sortOptions";
  public static final String VARIANT_INSTANCE_ID   = "variantInstanceId";
  public static final String CONTEXT_ID            = "contextId";
  public static final String VARIANT_INSTANCE_IDS  = "variantInstanceIds";
  
  public List<IPropertyInstanceFilterModel> getAttributes();
  
  public void setAttributes(List<IPropertyInstanceFilterModel> attributes);
  
  public List<IPropertyInstanceFilterModel> getTags();
  
  public void setTags(List<IPropertyInstanceFilterModel> tags);
  
  public String getAllSearch();
  
  public void setAllSearch(String allSearch);
  
  public String getId();
  
  public void setId(String id);
  
  public List<IRoleModel> getRoles();
  
  public void setRoles(List<IRoleModel> roles);
  
  public String getCurrentUserId();
  
  public void setCurrentUserId(String currentUserId);
  
  public Collection<String> getDimensionalTagIds();
  
  public void setDimensionalTagIds(Collection<String> dimensionalTagIds);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getSize();
  
  public void setSize(Integer size);
  
  public Boolean getGetFolders();
  
  public void setGetFolders(Boolean getFolders);
  
  public Boolean getGetLeaves();
  
  public void setGetLeaves(Boolean getLeaves);
  
  public List<IFilterValueModel> getSelectedRoles();
  
  public void setSelectedRoles(List<IFilterValueModel> selectedRoles);
  
  public List<String> getSelectedTypes();
  
  public void setSelectedTypes(List<String> selectedTypes);
  
  public Boolean getIsRed();
  
  public void setIsRed(Boolean isRed);
  
  public Boolean getIsOrange();
  
  public void setIsOrange(Boolean isOrange);
  
  public Boolean getIsYellow();
  
  public void setIsYellow(Boolean isYellow);
  
  public Boolean getIsGreen();
  
  public void setIsGreen(Boolean isGreen);
  
  public List<String> getSelectedTaxonomyIds();
  
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds);
  
  public String getParentTaxonomyId();
  
  public void setParentTaxonomyId(String parentTaxonomyId);
  
  public List<ISortModel> getSortOptions();
  
  public void setSortOptions(List<ISortModel> sortOptions);
  
  public String getVariantInstanceId();
  
  public void setVariantInstanceId(String variantInstanceId);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public List<String> getVariantInstanceIds();
  
  public void setVariantInstanceIds(List<String> variantInstanceIds);
}
