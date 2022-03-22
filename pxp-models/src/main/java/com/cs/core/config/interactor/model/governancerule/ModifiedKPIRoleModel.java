package com.cs.core.config.interactor.model.governancerule;

import java.util.List;

public class ModifiedKPIRoleModel implements IModifiedKPIRoleModel {
  
  private static final long serialVersionUID = 1L;
  protected String          roleId;
  protected List<String>    addedCandidates;
  protected List<String>    deletedCandidates;
  
  public String getRoleId()
  {
    return roleId;
  }
  
  public void setRoleId(String roleId)
  {
    this.roleId = roleId;
  }
  
  public List<String> getAddedCandidates()
  {
    return addedCandidates;
  }
  
  public void setAddedCandidates(List<String> addedCandidates)
  {
    this.addedCandidates = addedCandidates;
  }
  
  public List<String> getDeletedCandidates()
  {
    return deletedCandidates;
  }
  
  public void setDeletedCandidates(List<String> deletedCandidates)
  {
    this.deletedCandidates = deletedCandidates;
  }
}
