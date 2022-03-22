package com.cs.core.runtime.interactor.model.filter;

import com.cs.core.config.interactor.model.role.IRoleModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("rawtypes")
public class KlassInstanceRelationshipsFilterModel
    implements IKlassInstanceRelationshipsFilterModel {
  
  private static final long                              serialVersionUID     = 1L;
  protected List<? extends IPropertyInstanceFilterModel> attributes;
  protected List<? extends IPropertyInstanceFilterModel> tags;
  protected String                                       allSearch;
  protected String                                       id;
  protected List<IRoleModel>                             roles;
  protected String                                       currentUserId;
  protected Collection<String>                           dimensionalTagIds;
  protected List<String>                                 klassIds;
  protected Integer                                      from;
  protected Integer                                      size;
  protected Boolean                                      getFolders           = false;
  protected Boolean                                      getLeaves            = false;
  protected List<? extends IFilterValueModel>            selectedRoles;
  protected List<String>                                 selectedTypes;
  protected String                                       relationshipId;
  protected String                                       baseType;
  protected Boolean                                      isRed;
  protected Boolean                                      isYellow;
  protected Boolean                                      isOrange;
  protected Boolean                                      isGreen;
  protected List<String>                                 selectedTaxonomyIds;
  protected String                                       parentTaxonomyId;
  protected List<ISortModel>                             sortOptions;
  protected String                                       variantInstanceId;
  protected String                                       contextId;
  protected List<String>                                 variantInstanceIds;
  protected Boolean                                      isLinked             = true;
  protected List<String>                                 xRayAttributes       = new ArrayList<>();
  protected List<String>                                 xRayTags             = new ArrayList<>();
  protected Boolean                                      isNatureRelationship = false;
  protected String                                       sideId;
  
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
  
  @SuppressWarnings("unchecked")
  @Override
  public List<IPropertyInstanceFilterModel> getAttributes()
  {
    return (List<IPropertyInstanceFilterModel>) this.attributes;
  }
  
  @JsonDeserialize(contentAs = PropertyInstanceValueTypeFilterModer.class)
  @Override
  public void setAttributes(List<IPropertyInstanceFilterModel> attributes)
  {
    this.attributes = attributes;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<IPropertyInstanceFilterModel> getTags()
  {
    return (List<IPropertyInstanceFilterModel>) this.tags;
  }
  
  @JsonDeserialize(contentAs = PropertyInstanceValueTypeFilterModer.class)
  @Override
  public void setTags(List<IPropertyInstanceFilterModel> tags)
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
  public Collection<String> getDimensionalTagIds()
  {
    return dimensionalTagIds;
  }
  
  @Override
  public void setDimensionalTagIds(Collection<String> dimensionalTagIds)
  {
    this.dimensionalTagIds = dimensionalTagIds;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
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
  public List<IFilterValueModel> getSelectedRoles()
  {
    if (selectedRoles == null) {
      selectedRoles = new ArrayList();
    }
    return (List<IFilterValueModel>) selectedRoles;
  }
  
  @JsonDeserialize(contentAs = IFilterValueModel.class)
  @Override
  public void setSelectedRoles(List<IFilterValueModel> selectedRoles)
  {
    this.selectedRoles = selectedRoles;
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
    return relationshipId;
  }
  
  @Override
  public void setRelationshipId(String relationshipMappingId)
  {
    this.relationshipId = relationshipMappingId;
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
  public List<ISortModel> getSortOptions()
  {
    if (sortOptions == null) {
      sortOptions = new ArrayList<>();
    }
    return sortOptions;
  }
  
  @Override
  public void setSortOptions(List<ISortModel> sortOptions)
  {
    this.sortOptions = sortOptions;
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
}
