package com.cs.core.config.interactor.model.globalpermissions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.template.HeaderPermission;
import com.cs.core.config.interactor.entity.template.IHeaderPermission;
import com.cs.core.config.interactor.entity.template.ITabPermission;
import com.cs.core.config.interactor.entity.template.TabPermission;
import com.cs.core.config.interactor.model.permission.FunctionPermissionModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonIgnoreProperties(value = "versionId")
public class ReferencedTemplatePermissionModel implements IReferencedTemplatePermissionModel {
  
  private static final long          serialVersionUID = 1L;
  
  protected ITabPermission           tabPermission;
  protected IHeaderPermission        headerPermission;
  protected Set<String>              visiblePropertyCollectionIds;
  protected Set<String>              editablePropertyCollectionIds;
  protected Set<String>              expandablePropertyCollectionIds;
  protected Set<String>              visiblePropertyIds;
  protected Set<String>              editablePropertyIds;
  protected Set<String>              visibleRelationshipIds;
  protected Set<String>              canAddRelationshipIds;
  protected Set<String>              canDeleteRelationshipIds;
  protected Set<String>              canEditContextRelationshipIds;
  protected IGlobalPermission        globalPermission;
  protected Set<String>              visibleTabTypes;
  protected Map<String, Object>      taskIdsForRolesHavingReadPermission;
  protected Set<String>              taskIdsHavingReadPermissions;
  protected Set<String>              entitiesHavingReadPermission;
  protected Set<String>              klassIdsHavingReadPermission;
  protected Set<String>              taxonomyIdsHavingRP;
  protected Set<String>              allTaxonomyIdsHavingRP;
  protected Map<String, Set<String>> klassIdVsTemplateIdsMap;
  protected IFunctionPermissionModel functionPermission;
  
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
  public ITabPermission getTabPermission()
  {
    return tabPermission;
  }
  
  @Override
  @JsonDeserialize(as = TabPermission.class)
  public void setTabPermission(ITabPermission tabPermission)
  {
    this.tabPermission = tabPermission;
  }
  
  @Override
  public IHeaderPermission getHeaderPermission()
  {
    return headerPermission;
  }
  
  @JsonDeserialize(as = HeaderPermission.class)
  public void setHeaderPermission(IHeaderPermission headerPermission)
  {
    this.headerPermission = headerPermission;
  }
  
  public Set<String> getVisiblePropertyCollectionIds()
  {
    if (visiblePropertyCollectionIds == null) {
      return new HashSet<String>();
    }
    return visiblePropertyCollectionIds;
  }
  
  public void setVisiblePropertyCollectionIds(Set<String> visiblePropertyCollectionIds)
  {
    this.visiblePropertyCollectionIds = visiblePropertyCollectionIds;
  }
  
  public Set<String> getEditablePropertyCollectionIds()
  {
    if (editablePropertyCollectionIds == null) {
      return new HashSet<String>();
    }
    return editablePropertyCollectionIds;
  }
  
  @Override
  public void setEditablePropertyCollectionIds(Set<String> editablePropertyCollectionIds)
  {
    this.editablePropertyCollectionIds = editablePropertyCollectionIds;
  }
  
  @Override
  public Set<String> getExpandablePropertyCollectionIds()
  {
    if (expandablePropertyCollectionIds == null) {
      return new HashSet<String>();
    }
    return expandablePropertyCollectionIds;
  }
  
  @Override
  public void setExpandablePropertyCollectionIds(Set<String> expandablePropertyCollectionIds)
  {
    this.expandablePropertyCollectionIds = expandablePropertyCollectionIds;
  }
  
  @Override
  public Set<String> getVisiblePropertyIds()
  {
    if (visiblePropertyIds == null) {
      return new HashSet<String>();
    }
    return visiblePropertyIds;
  }
  
  @Override
  public void setVisiblePropertyIds(Set<String> visiblePropertyIds)
  {
    this.visiblePropertyIds = visiblePropertyIds;
  }
  
  @Override
  public Set<String> getEditablePropertyIds()
  {
    if (editablePropertyIds == null) {
      return new HashSet<String>();
    }
    return editablePropertyIds;
  }
  
  @Override
  public void setEditablePropertyIds(Set<String> editablePropertyIds)
  {
    this.editablePropertyIds = editablePropertyIds;
  }
  
  @Override
  public Set<String> getVisibleRelationshipIds()
  {
    if (visibleRelationshipIds == null) {
      return new HashSet<String>();
    }
    return visibleRelationshipIds;
  }
  
  @Override
  public void setVisibleRelationshipIds(Set<String> visibleRelationshipIds)
  {
    this.visibleRelationshipIds = visibleRelationshipIds;
  }
  
  @Override
  public Set<String> getCanAddRelationshipIds()
  {
    if (canAddRelationshipIds == null) {
      return new HashSet<String>();
    }
    return canAddRelationshipIds;
  }
  
  @Override
  public void setCanAddRelationshipIds(Set<String> canAddRelationshipIds)
  {
    this.canAddRelationshipIds = canAddRelationshipIds;
  }
  
  public Set<String> getCanDeleteRelationshipIds()
  {
    if (canDeleteRelationshipIds == null) {
      return new HashSet<String>();
    }
    return canDeleteRelationshipIds;
  }
  
  public void setCanDeleteRelationshipIds(Set<String> canDeleteRelationshipIds)
  {
    this.canDeleteRelationshipIds = canDeleteRelationshipIds;
  }
  
  @Override
  public IGlobalPermission getGlobalPermission()
  {
    return globalPermission;
  }
  
  @JsonDeserialize(as = GlobalPermission.class)
  @Override
  public void setGlobalPermission(IGlobalPermission globalPermission)
  {
    this.globalPermission = globalPermission;
  }
  
  @Override
  public Set<String> getVisibleTabTypes()
  {
    return visibleTabTypes;
  }
  
  @Override
  public void setVisibleTabTypes(Set<String> visibleTabTypes)
  {
    this.visibleTabTypes = visibleTabTypes;
  }
  
  @Override
  public Set<String> getEntitiesHavingRP()
  {
    return entitiesHavingReadPermission;
  }
  
  @Override
  public void setEntitiesHavingRP(Set<String> entitiesHavingReadPermission)
  {
    this.entitiesHavingReadPermission = entitiesHavingReadPermission;
  }
  
  @Override
  public Set<String> getKlassIdsHavingRP()
  {
    return klassIdsHavingReadPermission;
  }
  
  @Override
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingReadPermission)
  {
    this.klassIdsHavingReadPermission = klassIdsHavingReadPermission;
  }
  
  @Override
  public Set<String> getTaxonomyIdsHavingRP()
  {
    return taxonomyIdsHavingRP;
  }
  
  @Override
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP)
  {
    this.taxonomyIdsHavingRP = taxonomyIdsHavingRP;
  }
  
  @Override
  public Set<String> getAllTaxonomyIdsHavingRP()
  {
    if (allTaxonomyIdsHavingRP == null) {
      allTaxonomyIdsHavingRP = new HashSet<>();
    }
    return allTaxonomyIdsHavingRP;
  }
  
  @Override
  public void setAllTaxonomyIdsHavingRP(Set<String> allTaxonomyIdsHavingRP)
  {
    this.allTaxonomyIdsHavingRP = allTaxonomyIdsHavingRP;
  }
  
  @Override
  public Map<String, Set<String>> getKlassIdVsTemplateIdsMap()
  {
    if (klassIdVsTemplateIdsMap == null) {
      klassIdVsTemplateIdsMap = new HashMap<>();
    }
    return klassIdVsTemplateIdsMap;
  }
  
  @Override
  public void setKlassIdVsTemplateIdsMap(Map<String, Set<String>> klassIdVsTemplateIdsMap)
  {
    this.klassIdVsTemplateIdsMap = klassIdVsTemplateIdsMap;
  }
  
  @Override
  public IFunctionPermissionModel getFunctionPermission()
  {
    return functionPermission;
  }
  
  @Override
  @JsonDeserialize(as = FunctionPermissionModel.class)
  public void setFunctionPermission(IFunctionPermissionModel functionPermission)
  {
    this.functionPermission = functionPermission;
  }

  @Override
  public Set<String> getCanEditContextRelationshipIds()
  {
    return this.canEditContextRelationshipIds;
  }

  @Override
  public void setCanEditContextRelationshipIds(Set<String> canEditContextRelationshipIds)
  {
    this.canEditContextRelationshipIds = canEditContextRelationshipIds;
  }
}
