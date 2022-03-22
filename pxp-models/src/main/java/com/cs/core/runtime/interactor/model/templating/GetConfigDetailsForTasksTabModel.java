package com.cs.core.runtime.interactor.model.templating;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.config.interactor.model.klass.ConflictingValueModel;
import com.cs.core.config.interactor.model.klass.IConflictingValueModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetConfigDetailsForTasksTabModel extends AbstractGetConfigDetailsModel
    implements IGetConfigDetailsForTasksTabModel {
  
  private static final long                     serialVersionUID = 1L;
  protected Map<String, IConflictingValueModel> conflictingValues;
  protected Set<String>                         roleIdsHavingReadPermissionsForTask;
  protected String                              roleIdOfCurrentUser;
  protected Set<String>                         taskIdsHavingReadPermissions;
  protected List<String>                        side2LinkedVariantKrIds;
  protected List<String>                        linkedVariantCodes;
  
  @Override
  public Set<String> getRoleIdsHavingReadPermissionsForTask()
  {
    return roleIdsHavingReadPermissionsForTask;
  }
  
  @Override
  public void setRoleIdsHavingReadPermissionsForTask(Set<String> roleIdsHavingreadPermissions)
  {
    this.roleIdsHavingReadPermissionsForTask = roleIdsHavingreadPermissions;
  }
  
  @Override
  public Map<String, IConflictingValueModel> getConflictingValues()
  {
    return conflictingValues;
  }
  
  @JsonDeserialize(contentAs = ConflictingValueModel.class)
  @Override
  public void setConflictingValues(Map<String, IConflictingValueModel> conflictingValues)
  {
    this.conflictingValues = conflictingValues;
  }
  
  @Override
  public String getRoleIdOfCurrentUser()
  {
    return roleIdOfCurrentUser;
  }
  
  @Override
  public void setRoleIdOfCurrentUser(String roleIdOfCurrentUser)
  {
    this.roleIdOfCurrentUser = roleIdOfCurrentUser;
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
  public List<String> getSide2LinkedVariantKrIds()
  {
    if (side2LinkedVariantKrIds == null) {
      side2LinkedVariantKrIds = new ArrayList<String>();
    }
    return side2LinkedVariantKrIds;
  }
  
  @Override
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds)
  {
    this.side2LinkedVariantKrIds = side2LinkedVariantKrIds;
  }

  @Override
  public List<String> getLinkedVariantCodes()
  {
    if(linkedVariantCodes == null) {
      linkedVariantCodes = new ArrayList<>();
    }
    return linkedVariantCodes;
  }

  @Override
  public void setLinkedVariantCodes(List<String> linkedVariantCodes)
  {
    this.linkedVariantCodes = linkedVariantCodes;
  }
  
  
}
