package com.cs.core.runtime.interactor.model.taskinstance;

import java.util.List;

public class TaskRoleSaveEntity implements ITaskRoleSaveEntity {
  
  private static final long serialVersionUID = 1L;
  
  List<String>              addedUserIds;
  List<String>              deletedUserIds;
  
  List<String>              addedRoleIds;
  List<String>              deletedRoleIds;
  
  @Override
  public List<String> getAddedUserIds()
  {
    return addedUserIds;
  }
  
  @Override
  public void setAddedUserIds(List<String> addedUserIds)
  {
    this.addedUserIds = addedUserIds;
  }
  
  @Override
  public List<String> getDeletedUserIds()
  {
    return deletedUserIds;
  }
  
  @Override
  public void setDeletedUserIds(List<String> deletedUserIds)
  {
    this.deletedUserIds = deletedUserIds;
  }
  
  @Override
  public List<String> getAddedRoleIds()
  {
    return addedRoleIds;
  }
  
  @Override
  public void setAddedRoleIds(List<String> addedRoleIds)
  {
    this.addedRoleIds = addedRoleIds;
  }
  
  @Override
  public List<String> getDeletedRoleIds()
  {
    return deletedRoleIds;
  }
  
  @Override
  public void setDeletedRoleIds(List<String> deletedRoleIds)
  {
    this.deletedRoleIds = deletedRoleIds;
  }
}
