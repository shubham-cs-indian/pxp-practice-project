package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.klass.*;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ReferencedContextModel;
import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.entity.eventinstance.TimeRange;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;

public class GetKlassInstanceTreeStrategyModel extends InstanceSearchStrategyModel
    implements IGetKlassInstanceTreeStrategyModel {
  
  private static final long                                                serialVersionUID              = 1L;
  protected Collection<String>                                             dimensionalTagIds;
  protected String                                                         currentUserId;
  protected Boolean                                                        isGetChildren;
  protected List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> contentRelationships;
  protected Map<String, String>                                            flatProperties                = new HashMap<>();
  // protected List<IConfigEntityTreeInformationModel> categoryInfo;
  protected List<IReferencedSectionElementModel>                           referencedRelationships;
  protected List<String>                                                   moduleEntities;
  protected String                                                         variantInstanceId;
  protected String                                                         contextId;
  protected IReferencedContextModel                                        referencedContexts;
  protected List<IReferencedSectionElementModel>                           referencedNatureRelationships;
  protected List<String>                                                   variantInstanceIds;
  protected List<String>                                                   referencedEventIds            = new ArrayList<>();
  protected Boolean                                                        isLinked                      = true;
  protected Map<String, IReferencedKlassDetailStrategyModel>               klasses;
  protected List<String>                                                   childrenIds;
  protected Boolean                                                        isSearchOnSelectedTaxonomy;
  protected Boolean                                                        shouldGetMasterPromotionsOnly = false;
  protected Boolean                                                        shouldGetOnlyMaster           = false;
  protected Map<String, Object>                                            taskIdsForRolesHavingReadPermission;
  protected Set<String>                                                    taskIdsHavingReadPermissions;
  protected Set<String>                                                    personalTaskIds;
  protected Boolean                                                        isGetDynamicCollection;
  protected Boolean                                                        isLoadMore                    = false;
  protected Boolean                                                        getAllChildren;
  protected String                                                         kpiId;
  protected Boolean                                                        isArchivePortal;
  protected Boolean                                                        isCalendarView                = false;
  protected ITimeRange                                                     selectedTimeRange;
  protected Boolean                                                        getFolders                    = true;
  protected Boolean                                                        getLeaves                     = false;
  
  public GetKlassInstanceTreeStrategyModel()
  {
  }
  
  public GetKlassInstanceTreeStrategyModel(Integer from, Integer size, Boolean shouldGetTreeData,
      Boolean getAllChildren)
  {
    super();
    this.from = from;
    this.size = size;
    this.isLoadMore = shouldGetTreeData;
    this.getAllChildren = getAllChildren;
  }
  
  @Override
  public Boolean getShouldGetMasterPromotionsOnly()
  {
    return shouldGetMasterPromotionsOnly;
  }
  
  @Override
  public void setShouldGetMasterPromotionsOnly(Boolean shouldGetMasterPromotionsOnly)
  {
    this.shouldGetMasterPromotionsOnly = shouldGetMasterPromotionsOnly;
  }
  
  @Override
  public List<String> getVariantInstanceIds()
  {
    if (variantInstanceIds == null) {
      variantInstanceIds = new ArrayList<String>();
    }
    return variantInstanceIds;
  }
  
  @Override
  public void setVariantInstanceIds(List<String> variantInstanceIds)
  {
    this.variantInstanceIds = variantInstanceIds;
  }
  
  @Override
  public Collection<String> getDimensionalTagIds()
  {
    if (dimensionalTagIds == null) {
      dimensionalTagIds = new ArrayList<String>();
    }
    return dimensionalTagIds;
  }
  
  @Override
  public void setDimensionalTagIds(Collection<String> dimensionalTagIds)
  {
    this.dimensionalTagIds = dimensionalTagIds;
  }
  
  @Override
  public String getCurrentUserId()
  {
    return currentUserId;
  }
  
  @Override
  public void setCurrentUserId(String currentUserId)
  {
    this.currentUserId = currentUserId;
  }
  
  @Override
  public Boolean getIsGetChildren()
  {
    return isGetChildren;
  }
  
  @Override
  public void setIsGetChildren(Boolean isGetChildren)
  {
    this.isGetChildren = isGetChildren;
  }
  
  @Override
  public List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> getContentRelationships()
  {
    return contentRelationships;
  }
  
  @JsonDeserialize(contentAs = GetKlassInstanceRelationshipTreeStrategyModel.class)
  @Override
  public void setContentRelationships(
      List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> contentRelationships)
  {
    this.contentRelationships = contentRelationships;
  }
  
  @Override
  public Map<String, String> getFlatProperties()
  {
    return flatProperties;
  }
  
  @Override
  public void setFlatProperties(Map<String, String> flatProperties)
  {
    this.flatProperties = flatProperties;
  }
  
  /*@Override
  public List<IConfigEntityTreeInformationModel> getCategoryInfo()
  {
    if (categoryInfo == null) {
      categoryInfo = new ArrayList<>();
    }
    return categoryInfo;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityTreeInformationModel.class)
  @Override
  public void setCategoryInfo(List<IConfigEntityTreeInformationModel> categoryInfo)
  {
    this.categoryInfo = categoryInfo;
  }*/
  
  @Override
  public List<IReferencedSectionElementModel> getReferencedRelationships()
  {
    if (referencedRelationships == null) {
      referencedRelationships = new ArrayList<>();
    }
    return referencedRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedSectionRelationshipModel.class)
  public void setReferencedRelationships(
      List<IReferencedSectionElementModel> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }
  
  @Override
  public List<String> getModuleEntities()
  {
    return moduleEntities;
  }
  
  @Override
  public void setModuleEntities(List<String> moduleEntities)
  {
    this.moduleEntities = moduleEntities;
  }
  
  @Override
  public String getVariantInstanceId()
  {
    
    return variantInstanceId;
  }
  
  @Override
  public void setVariantInstanceId(String variantInstanceId)
  {
    this.variantInstanceId = variantInstanceId;
  }
  
  @Override
  public String getContextId()
  {
    
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public IReferencedContextModel getReferencedContexts()
  {
    return referencedContexts;
  }
  
  @Override
  @JsonDeserialize(as = ReferencedContextModel.class)
  public void setReferencedContexts(IReferencedContextModel referencedContexts)
  {
    this.referencedContexts = referencedContexts;
  }
  
  @Override
  public List<IReferencedSectionElementModel> getReferencedNatureRelationships()
  {
    if (referencedNatureRelationships == null) {
      referencedNatureRelationships = new ArrayList<>();
    }
    return referencedNatureRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedSectionRelationshipModel.class)
  public void setReferencedNatureRelationships(
      List<IReferencedSectionElementModel> referencedNatureRelationships)
  {
    this.referencedNatureRelationships = referencedNatureRelationships;
  }
  
  @Override
  public List<String> getReferencedEventIds()
  {
    return referencedEventIds;
  }
  
  @Override
  public void setReferencedEventIds(List<String> eventIds)
  {
    this.referencedEventIds = eventIds;
  }
  
  @Override
  public Boolean getIsLinked()
  {
    return this.isLinked;
  }
  
  @Override
  public void setIsLinked(Boolean isLinked)
  {
    this.isLinked = isLinked;
  }
  
  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    return klasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedKlassDetailsModel.class)
  public void setReferencedKlasses(Map<String, IReferencedKlassDetailStrategyModel> klasses)
  {
    this.klasses = klasses;
  }
  
  @Override
  public List<String> getChildrenIds()
  {
    if (childrenIds == null) {
      childrenIds = new ArrayList<>();
    }
    return childrenIds;
  }
  
  @Override
  public void setChildrenIds(List<String> childrenIds)
  {
    this.childrenIds = childrenIds;
  }
  
  @Override
  public Boolean getIsSearchOnSelectedTaxonomy()
  {
    if (isSearchOnSelectedTaxonomy == null) {
      isSearchOnSelectedTaxonomy = false;
    }
    return isSearchOnSelectedTaxonomy;
  }
  
  @Override
  public void setIsSearchOnSelectedTaxonomy(Boolean isSearchOnSelectedTaxonomy)
  {
    this.isSearchOnSelectedTaxonomy = isSearchOnSelectedTaxonomy;
  }
  
  @Override
  public Boolean getShouldGetOnlyMaster()
  {
    return shouldGetOnlyMaster;
  }
  
  @Override
  public void setShouldGetOnlyMaster(Boolean shouldGetOnlyMaster)
  {
    this.shouldGetOnlyMaster = shouldGetOnlyMaster;
  }
  
  @Override
  public Set<String> getTaskIdsHavingReadPermissions()
  {
    return taskIdsHavingReadPermissions;
  }
  
  @Override
  public void setTaskIdsHavingReadPermissions(Set<String> taskIdsHavingReadPermissions)
  {
    this.taskIdsHavingReadPermissions = taskIdsHavingReadPermissions;
  }
  
  @Override
  public Map<String, Object> getTaskIdsForRolesHavingReadPermission()
  {
    return taskIdsForRolesHavingReadPermission;
  }
  
  @Override
  public void setTaskIdsForRolesHavingReadPermission(
      Map<String, Object> taskIdsForRolesHavingReadPermission)
  {
    this.taskIdsForRolesHavingReadPermission = taskIdsForRolesHavingReadPermission;
  }
  
  @Override
  public Set<String> getPersonalTaskIds()
  {
    return personalTaskIds;
  }
  
  @Override
  public void setPersonalTaskIds(Set<String> personalTaskIds)
  {
    this.personalTaskIds = personalTaskIds;
  }
  
  public Boolean getIsGetDynamicCollection()
  {
    return isGetDynamicCollection;
  }
  
  public void setIsGetDynamicCollection(Boolean isGetDynamicCollection)
  {
    this.isGetDynamicCollection = isGetDynamicCollection;
  }
  
  @Override
  public Boolean getGetFolders()
  {
    return getFolders;
  }
  
  @Override
  public void setGetFolders(Boolean getFolders)
  {
    this.getFolders = getFolders;
  }
  
  @Override
  public Boolean getGetLeaves()
  {
    return getLeaves;
  }
  
  @Override
  public void setGetLeaves(Boolean getLeaves)
  {
    this.getLeaves = getLeaves;
  }
  
  @Override
  public Boolean getIsCalendarView()
  {
    return isCalendarView;
  }
  
  @Override
  public void setIsCalendarView(Boolean isCalendarView)
  {
    this.isCalendarView = isCalendarView;
  }
  
  @Override
  public ITimeRange getSelectedTimeRange()
  {
    return selectedTimeRange;
  }
  
  @JsonDeserialize(as = TimeRange.class)
  @Override
  public void setSelectedTimeRange(ITimeRange selectedDateRange)
  {
    this.selectedTimeRange = selectedDateRange;
  }
  
  @Override
  public Boolean getGetAllChildren()
  {
    return getAllChildren;
  }
  
  @Override
  public void setGetAllChildren(Boolean getAllChildren)
  {
    this.getAllChildren = getAllChildren;
  }
  
  @Override
  public Boolean getIsLoadMore()
  {
    return isLoadMore;
  }
  
  @Override
  public void setIsLoadMore(Boolean isLoadMore)
  {
    this.isLoadMore = isLoadMore;
  }
  
  @Override
  public Boolean getIsArchivePortal()
  {
    return isArchivePortal;
  }
  
  @Override
  public void setIsArchivePortal(Boolean isArchivePortal)
  {
    this.isArchivePortal = isArchivePortal;
  }
  
  @Override
  public String getKpiId()
  {
    return kpiId;
  }
  
  @Override
  public void setKpiId(String kpiId)
  {
    this.kpiId = kpiId;
  }
}
