package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.ReferencedSectionRelationshipModel;
import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.runtime.interactor.entity.eventinstance.ITimeRange;
import com.cs.core.runtime.interactor.model.filter.IFilterValueModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.filter.PropertyInstanceValueTypeFilterModer;
import com.cs.core.runtime.interactor.model.filter.SortModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetKlassInstanceRelationshipsStrategyModel
    implements IGetKlassInstanceRelationshipsStrategyModel {
  
  private static final long                                                serialVersionUID              = 1L;
  protected Integer                                                        from;
  protected Integer                                                        size;
  protected Collection<String>                                             dimensionalTagIds;
  protected String                                                         currentUserId;
  protected List<IRoleModel>                                               roles;
  protected String                                                         id;
  protected Boolean                                                        isLoadMore                    = false;
  protected List<? extends IPropertyInstanceFilterModel>                   attributes;
  protected List<? extends IPropertyInstanceFilterModel>                   tags;
  protected String                                                         allSearch;
  protected Boolean                                                        isGetChildren;
  protected Set<String>                                                    klassIds;
  protected Boolean                                                        getAllChildren;
  protected IKlass                                                         typeKlass;
  protected Collection<String>                                             singleSelectTagIdList;
  protected Boolean                                                        getFolders                    = true;
  protected Boolean                                                        getLeaves                     = true;
  protected List<? extends IFilterValueModel>                              selectedRoles;
  protected List<String>                                                   selectedTypes;
  protected List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> contentRelationships;
  protected String                                                         relationshipMappingId;
  protected String                                                         baseType;
  protected Map<String, String>                                            flatProperties                = new HashMap<String, String>();
  protected Boolean                                                        isRed;
  protected Boolean                                                        isYellow;
  protected Boolean                                                        isOrange;
  protected Boolean                                                        isGreen;
  protected IGetFilterInfoModel                                            filterInfo;
  // protected List<IConfigEntityTreeInformationModel> categoryInfo;
  protected List<String>                                                   selectedTaxonomyIds;
  protected String                                                         parentTaxonomyId;
  protected String                                                         moduleId;
  protected List<String>                                                   searchableAttributes;
  protected List<ISortModel>                                               sortOptions;
  protected List<IReferencedSectionElementModel>                           referencedRelationships;
  protected List<String>                                                   moduleEntities;
  protected String                                                         variantInstanceId;
  protected String                                                         contextId;
  protected List<IReferencedSectionElementModel>                           referencedNatureRelationships;
  protected List<String>                                                   variantInstanceIds;
  protected Boolean                                                        isLinked                      = true;
  protected List<String>                                                   childrenIds;
  protected List<String>                                                   xRayAttributes                = new ArrayList<>();
  protected List<String>                                                   xRayTags                      = new ArrayList<>();
  protected Boolean                                                        shouldGetMasterPromotionsOnly = false;
  protected Set<String>                                                    entities;
  protected Boolean                                                        shouldGetOnlyMaster           = false;
  protected Boolean                                                        isNatureRelationship          = false;
  protected Set<String>                                                    taxonomyIdsHavingRP;
  protected String                                                         kpiId;
  protected String                                                         sideId;
  protected Map<String, IReferencedSectionElementModel>                    referencedElements;
  protected Integer                                                        expiryStatus                  = 0;
  
  public GetKlassInstanceRelationshipsStrategyModel()
  {
  }
  
  public GetKlassInstanceRelationshipsStrategyModel(Integer from, Integer size,
      Boolean shouldGetTreeData, Boolean getAllChildren)
  {
    super();
    this.from = from;
    this.size = size;
    this.isLoadMore = shouldGetTreeData;
    this.getAllChildren = getAllChildren;
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
  public Boolean getIsGreen()
  {
    return isGreen;
  }
  
  @Override
  public void setIsGreen(Boolean isGreen)
  {
    this.isGreen = isGreen;
  }
  
  @Override
  public Boolean getIsRed()
  {
    return isRed;
  }
  
  @Override
  public void setIsRed(Boolean isRed)
  {
    this.isRed = isRed;
  }
  
  @Override
  public Boolean getIsOrange()
  {
    return isOrange;
  }
  
  @Override
  public void setIsOrange(Boolean isOrange)
  {
    this.isOrange = isOrange;
  }
  
  @Override
  public Boolean getIsYellow()
  {
    return isYellow;
  }
  
  @Override
  public void setIsYellow(Boolean isYellow)
  {
    this.isYellow = isYellow;
  }
  
  public Boolean getGetAllChildren()
  {
    return getAllChildren;
  }
  
  public void setGetAllChildren(Boolean getAllChildren)
  {
    this.getAllChildren = getAllChildren;
  }
  
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
  public List<IRoleModel> getRoles()
  {
    if (roles == null) {
      roles = new ArrayList<IRoleModel>();
    }
    return roles;
  }
  
  @Override
  public void setRoles(List<IRoleModel> roles)
  {
    this.roles = roles;
  }
  
  @Override
  public Integer getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Integer from)
  {
    this.from = from;
  }
  
  @Override
  public Integer getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Integer size)
  {
    this.size = size;
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
  public List<? extends IPropertyInstanceFilterModel> getAttributes()
  {
    return attributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyInstanceValueTypeFilterModer.class)
  public void setAttributes(List<? extends IPropertyInstanceFilterModel> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<? extends IPropertyInstanceFilterModel> getTags()
  {
    return tags;
  }
  
  @Override
  public void setTags(List<? extends IPropertyInstanceFilterModel> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public String getAllSearch()
  {
    return allSearch;
  }
  
  @Override
  public void setAllSearch(String allSearch)
  {
    this.allSearch = allSearch;
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
  public Set<String> getKlassIdsHavingRP()
  {
    if (klassIds == null) {
      klassIds = new HashSet();
    }
    return klassIds;
  }
  
  @Override
  public void setKlassIdsHavingRP(Set<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  /*
   * @Override public void setTypeKlass(IKlass typeKlass) { this.typeKlass =
   * typeKlass; }
   *
   * @Override public IKlass getTypeKlass() { return typeKlass; }
   *
   * @Override public Collection<String> getSingleSelectTagIdList() { return
   * singleSelectTagIdList; }
   *
   * @Override public void setSingleSelectTagIdList(Collection<String>
   * singleSelectTagIdList) { this.singleSelectTagIdList =
   * singleSelectTagIdList; }
   */
  
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
  public List<? extends IFilterValueModel> getSelectedRoles()
  {
    if (selectedRoles == null) {
      selectedRoles = new ArrayList<>();
    }
    return selectedRoles;
  }
  
  @Override
  public void setSelectedRoles(List<? extends IFilterValueModel> selectedRoles)
  {
    this.selectedRoles = selectedRoles;
  }
  
  @Override
  public List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> getContentRelationships()
  {
    return contentRelationships;
  }
  
  @Override
  public void setContentRelationships(
      List<? extends IGetKlassInstanceRelationshipTreeStrategyModel> contentRelationships)
  {
    this.contentRelationships = contentRelationships;
  }
  
  @Override
  public List<String> getSelectedTypes()
  {
    return selectedTypes;
  }
  
  @Override
  public void setSelectedTypes(List<String> selectedTypes)
  {
    this.selectedTypes = selectedTypes;
  }
  
  @Override
  public String getRelationshipId()
  {
    return relationshipMappingId;
  }
  
  @Override
  public void setRelationshipId(String relationshipMappingId)
  {
    this.relationshipMappingId = relationshipMappingId;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public Map<String, String> getFlatProperties()
  {
    return this.flatProperties;
  }
  
  @Override
  public void setFlatProperties(Map<String, String> flatProperties)
  {
    this.flatProperties = flatProperties;
  }
  
  @Override
  public IGetFilterInfoModel getFilterInfo()
  {
    return filterInfo;
  }
  
  @Override
  public void setFilterInfo(IGetFilterInfoModel filterInfo)
  {
    this.filterInfo = filterInfo;
  }
  
  /*@Override
  public List<IConfigEntityTreeInformationModel> getCategoryInfo()
  {
    return categoryInfo;
  }
  
  @JsonDeserialize(contentAs = CategoryInformationModel.class)
  @Override
  public void setCategoryInfo(List<IConfigEntityTreeInformationModel> categoryInfo)
  {
    this.categoryInfo = categoryInfo;
  }*/
  
  @Override
  public List<String> getSelectedTaxonomyIds()
  {
    return selectedTaxonomyIds;
  }
  
  @Override
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds)
  {
    this.selectedTaxonomyIds = selectedTaxonomyIds;
  }
  
  @Override
  public String getParentTaxonomyId()
  {
    return parentTaxonomyId;
  }
  
  @Override
  public void setParentTaxonomyId(String parentTaxonomyId)
  {
    this.parentTaxonomyId = parentTaxonomyId;
  }
  
  @Override
  public String getModuleId()
  {
    return moduleId;
  }
  
  @Override
  public void setModuleId(String moduleId)
  {
    this.moduleId = moduleId;
  }
  
  @Override
  public List<String> getSearchableAttributes()
  {
    return searchableAttributes;
  }
  
  @Override
  public void setSearchableAttributes(List<String> searchableAttributes)
  {
    this.searchableAttributes = searchableAttributes;
  }
  
  @Override
  public List<ISortModel> getSortOptions()
  {
    if (sortOptions == null) {
      sortOptions = new ArrayList<>();
    }
    return sortOptions;
  }
  
  @JsonDeserialize(contentAs = SortModel.class)
  @Override
  public void setSortOptions(List<ISortModel> sortOptions)
  {
    this.sortOptions = sortOptions;
  }
  
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
  public List<String> getXRayAttributes()
  {
    return xRayAttributes;
  }
  
  @Override
  public void setXRayAttributes(List<String> xRayAttributes)
  {
    this.xRayAttributes = xRayAttributes;
  }
  
  @Override
  public List<String> getXRayTags()
  {
    return xRayTags;
  }
  
  @Override
  public void setXRayTags(List<String> xRayTags)
  {
    this.xRayTags = xRayTags;
  }
  
  @Override
  public IReferencedContextModel getReferencedContexts()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setReferencedContexts(IReferencedContextModel typeKlass)
  {
    // TODO Auto-generated method stub
    
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
  public Boolean getIsCalendarView()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setIsCalendarView(Boolean isCalendarView)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public ITimeRange getSelectedTimeRange()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setSelectedTimeRange(ITimeRange selectedTimeRange)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public List<String> getReferencedEventIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setReferencedEventIds(List<String> eventIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Boolean getIsLinked()
  {
    return isLinked;
  }
  
  @Override
  public void setIsLinked(Boolean isLinked)
  {
    this.isLinked = isLinked;
  }
  
  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setReferencedKlasses(Map<String, IReferencedKlassDetailStrategyModel> klasses)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public List<String> getChildrenIds()
  {
    if (childrenIds == null) {
      this.childrenIds = new ArrayList<>();
    }
    return childrenIds;
  }
  
  @Override
  public void setChildrenIds(List<String> childrenIds)
  {
    this.childrenIds = childrenIds;
  }
  
  @Override
  @JsonIgnore
  public Boolean getIsSearchOnSelectedTaxonomy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setIsSearchOnSelectedTaxonomy(Boolean isSearchOnSelectedTaxonomy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Set<String> getEntities()
  {
    return entities;
  }
  
  @Override
  public void setEntities(Set<String> entities)
  {
    this.entities = entities;
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
  public Boolean getIsNatureRelationship()
  {
    return isNatureRelationship;
  }
  
  @Override
  public void setIsNatureRelationship(Boolean isNatureRelationship)
  {
    this.isNatureRelationship = isNatureRelationship;
  }
  
  @Override
  public String getSideId()
  {
    return sideId;
  }
  
  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
  }
  
  @Override
  public Map<String, Object> getTaskIdsForRolesHavingReadPermission()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setTaskIdsForRolesHavingReadPermission(
      Map<String, Object> taskIdsForRolesHavingReadPermission)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Set<String> getTaskIdsHavingReadPermissions()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setTaskIdsHavingReadPermissions(Set<String> taskIdsHavingReadPermissions)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Set<String> getPersonalTaskIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setPersonalTaskIds(Set<String> personalTaskIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Set<String> getTaxonomyIdsHavingRP()
  {
    if (taxonomyIdsHavingRP == null) {
      taxonomyIdsHavingRP = new HashSet<>();
    }
    return taxonomyIdsHavingRP;
  }
  
  @Override
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP)
  {
    this.taxonomyIdsHavingRP = taxonomyIdsHavingRP;
  }
  
  @Override
  public Map<String, IReferencedSectionElementModel> getReferencedElements()
  {
    return referencedElements;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractReferencedSectionElementModel.class)
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    this.referencedElements = referencedElements;
  }
  
  @Override
  public String getOrganizationId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getSystemId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getPhysicalCatalogId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLogicalCatalogId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLogicalCatalogId(String logicalCatalogId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getEndpointId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Boolean getIsGetDynamicCollection()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setIsGetDynamicCollection(Boolean isGetDynamicCollection)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Boolean getIsArchivePortal()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setIsArchivePortal(Boolean isArchivePortal)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Integer getExpiryStatus()
  {
    return expiryStatus;
  }
  
  @Override
  public void setExpiryStatus(Integer expiryStatus)
  {
    this.expiryStatus = expiryStatus;
  }
}
