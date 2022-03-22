package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.model.searchable.IInstanceSearchStrategyModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGetKlassInstanceTreeStrategyModel extends IInstanceSearchStrategyModel {
  
  public static final String DIMENSIONAL_TAG_IDS                       = "dimensionalTagIds";
  public static final String CURRENT_USER_ID                           = "currentUserId";
  public static final String IS_GET_CHILDREN                           = "isGetChildren";
  public static final String CONTENT_RELATIONSHIPS                     = "contentRelationships";
  public static final String FLAT_PROPERTIES                           = "flatProperties";
  
  public static final String SHOULD_GET_MASTER_PROMOTIONS_ONLY         = "shouldGetMasterPromotionsOnly";
  public static final String REFERENCED_RELATIONSHIPS                  = "referencedRelationships";
  public static final String MODULE_ENTITIES                           = "moduleEntities";
  public static final String VARIANT_INSTANCE_ID                       = "variantInstanceId";
  public static final String CONTEXT_ID                                = "contextId";
  public static final String REFERENCED_CONTEXTS                       = "referencedContexts";
  public static final String REFERENCED_NATURE_RELATIONSHIPS           = "referencedNatureRelationships";
  public static final String VARIANT_INSTANCE_IDS                      = "variantInstanceIds";
  public static final String REFERENCED_EVENT_IDS                      = "referencedEventIds";
  public static final String IS_LINKED                                 = "isLinked";
  public static final String REFERENCED_KLASSES                        = "referencedKlasses";
  public static final String CHILDREN_IDS                              = "childrenIds";
  public static final String IS_SEARCH_ON_SELECTED_TAXONOMY            = "isSearchOnSelectedTaxonomy";
  public static final String SHOULD_GET_ONLY_MASTER                    = "shouldGetOnlyMaster";
  public static final String TASK_IDS_FOR_ROLES_HAVING_READ_PERMISSION = "taskIdsForRolesHavingReadPermission";
  public static final String TASK_IDS_HAVING_READ_PERMISSION           = "taskIdsHavingReadPermissions";
  public static final String PERSONAL_TASK_IDS                         = "personalTaskIds";
  
  public static final String IS_GET_DYNAMIC_COLLECTION                 = "isGetDynamicCollection";
  public static final String IS_CALENDAR_VIEW                          = "isCalendarView";
  public static final String SELECTED_TIME_RANGE                       = "selectedTimeRange";
  public static final String GET_ALL_CHILDREN                          = "getAllChildren";
  public static final String IS_LOAD_MORE                              = "isLoadMore";
  public static final String KPI_ID                                    = "kpiId";
  public static final String IS_ARCHIVE_PORTAL                         = "isArchivePortal";
  public static final String GET_FOLDERS                               = "getFolders";
  public static final String GET_LEAVES                                = "getLeaves";
  
  public Collection<String> getDimensionalTagIds();
  
  public void setDimensionalTagIds(Collection<String> dimensionalTagIds);
  
  public String getCurrentUserId();
  
  public void setCurrentUserId(String currentUserId);
  
  public List<IReferencedSectionElementModel> getReferencedRelationships();
  
  public void setReferencedRelationships(List<IReferencedSectionElementModel> typeKlass);
  
  public Boolean getIsGetChildren();
  
  public void setIsGetChildren(Boolean isGetChildren);
  
  public List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> getContentRelationships();
  
  public void setContentRelationships(
      List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> contentRelationships);
  
  Map<String, String> getFlatProperties();
  
  void setFlatProperties(Map<String, String> flatProperties);
  
  /*public void setCategoryInfo(List<IConfigEntityTreeInformationModel> categoryInfo);
  
  public List<IConfigEntityTreeInformationModel> getCategoryInfo();*/
  
  public List<String> getModuleEntities();
  
  public void setModuleEntities(List<String> moduleEntities);
  
  public String getVariantInstanceId();
  
  public void setVariantInstanceId(String variantInstanceId);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public IReferencedContextModel getReferencedContexts();
  
  public void setReferencedContexts(IReferencedContextModel referencedContexts);
  
  public List<IReferencedSectionElementModel> getReferencedNatureRelationships();
  
  public void setReferencedNatureRelationships(
      List<IReferencedSectionElementModel> referencedNatureRelationships);
  
  public List<String> getVariantInstanceIds();
  
  public void setVariantInstanceIds(List<String> variantInstanceIds);
  
  public List<String> getReferencedEventIds();
  
  public void setReferencedEventIds(List<String> eventIds);
  
  public Boolean getIsLinked();
  
  public void setIsLinked(Boolean isLinked);
  
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IReferencedKlassDetailStrategyModel> klasses);
  
  public List<String> getChildrenIds();
  
  public void setChildrenIds(List<String> childrenIds);
  
  public Boolean getIsSearchOnSelectedTaxonomy();
  
  public void setIsSearchOnSelectedTaxonomy(Boolean isSearchOnSelectedTaxonomy);
  
  public Boolean getShouldGetMasterPromotionsOnly();
  
  public void setShouldGetMasterPromotionsOnly(Boolean shouldGetMasterPromotionsOnly);
  
  public Boolean getShouldGetOnlyMaster();
  
  public void setShouldGetOnlyMaster(Boolean shouldGetOnlyMaster);
  
  public Map<String, Object> getTaskIdsForRolesHavingReadPermission();
  
  public void setTaskIdsForRolesHavingReadPermission(
      Map<String, Object> taskIdsForRolesHavingReadPermission);
  
  public Set<String> getTaskIdsHavingReadPermissions();
  
  public void setTaskIdsHavingReadPermissions(Set<String> taskIdsHavingReadPermissions);
  
  public Set<String> getPersonalTaskIds();
  
  public void setPersonalTaskIds(Set<String> personalTaskIds);
  
  public Boolean getIsGetDynamicCollection();
  
  public void setIsGetDynamicCollection(Boolean isGetDynamicCollection);
  
  public Boolean getIsCalendarView();
  
  public void setIsCalendarView(Boolean isCalendarView);
  
  public ITimeRange getSelectedTimeRange();
  
  public void setSelectedTimeRange(ITimeRange selectedTimeRange);
  
  public Boolean getGetAllChildren();
  
  public void setGetAllChildren(Boolean getAllChildren);
  
  public Boolean getIsLoadMore();
  
  public void setIsLoadMore(Boolean isLoadMore);
  
  public String getKpiId();
  
  public void setKpiId(String kpiId);
  
  public Boolean getIsArchivePortal();
  
  public void setIsArchivePortal(Boolean isArchivePortal);
  
  public Boolean getGetFolders();
  
  public void setGetFolders(Boolean getFolders);
  
  public Boolean getGetLeaves();
  
  public void setGetLeaves(Boolean getLeaves);
}
