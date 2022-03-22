package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IModifiedKPIRoleModel extends IModel {
  
  public static final String ROLE_ID            = "roleId";
  public static final String ADDED_CANDIDATES   = "addedCandidates";
  public static final String DELETED_CANDIDATES = "deletedCandidates";
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public List<String> getAddedCandidates();
  
  public void setAddedCandidates(List<String> addedCandidates);
  
  public List<String> getDeletedCandidates();
  
  public void setDeletedCandidates(List<String> deletedCandidates);
}
