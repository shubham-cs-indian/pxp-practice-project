package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.ReferencedKlassDetailsModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;

public class AbstractGetInstanceRequestStrategyModel extends GetInstanceRequestModel
    implements IGetInstanceRequestStrategyModel {
  
  private static final long                                  serialVersionUID  = 1L;
  protected Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses;
  protected Set<String>                                      referencedTaskIds = new HashSet<>();
  protected Set<String>                                      taskIdsHavingReadPermissions;
  protected Map<String, Object>                              taskIdsForRolesHavingReadPermission;
  protected Set<String>                                      entitiesHavingReadPermission;
  protected Set<String>                                      klassIdsHavingReadPermission;
  protected Set<String>                                      personalTaskIds   = new HashSet<>();
  protected Set<String>                                      taxonomyIdsHavingReadPermission;
  
  public AbstractGetInstanceRequestStrategyModel(IGetInstanceRequestModel model)
  {
    this.id = model.getId();
    this.moduleId = model.getId();
    this.templateId = model.getTemplateId();
    this.childContextId = model.getChildContextId();
    this.sortField = model.getSortField();
    this.sortOrder = model.getSortOrder();
    this.from = model.getFrom();
    this.size = model.getSize();
    this.isGetAll = model.getIsGetAll();
    this.tabType = model.getTabType();
    this.contextId = model.getContextId();
    this.isForTaskAnnotation = model.getIsForTaskAnnotation();
  }
  
  public AbstractGetInstanceRequestStrategyModel()
  {
  }
  
  @Override
  public Boolean getHasReadPermission()
  {
    return hasReadPermission;
  }
  
  @Override
  public void setHasReadPermission(Boolean hasReadPermission)
  {
    this.hasReadPermission = hasReadPermission;
  }
  
  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedKlassDetailsModel.class)
  public void setReferencedKlasses(Map<String, IReferencedKlassDetailStrategyModel> klasses)
  {
    this.referencedKlasses = klasses;
  }
  
  public Set<String> getReferencedTaskIds()
  {
    if (referencedTaskIds == null) {
      referencedTaskIds = new HashSet<>();
    }
    return referencedTaskIds;
  }
  
  public void setReferencedTaskIds(Set<String> referencedTaskIds)
  {
    this.referencedTaskIds = referencedTaskIds;
  }
  
  @Override
  public Set<String> getTaskIdsHavingReadPermissions()
  {
    if (taskIdsHavingReadPermissions == null) {
      taskIdsHavingReadPermissions = new HashSet<String>();
    }
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
  public Set<String> getEntities()
  {
    return entitiesHavingReadPermission;
  }
  
  @Override
  public void setEntities(Set<String> entitiesHavingReadPermission)
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
  public Set<String> getPersonalTaskIds()
  {
    return personalTaskIds;
  }
  
  @Override
  public void setPersonalTaskIds(Set<String> personalTaskIds)
  {
    this.personalTaskIds = personalTaskIds;
  }
  
  @Override
  public Set<String> getTaxonomyIdsHavingRP()
  {
    if (taxonomyIdsHavingReadPermission == null) {
      taxonomyIdsHavingReadPermission = new HashSet<>();
    }
    return taxonomyIdsHavingReadPermission;
  }
  
  @Override
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingReadPermission)
  {
    this.taxonomyIdsHavingReadPermission = taxonomyIdsHavingReadPermission;
  }
}
