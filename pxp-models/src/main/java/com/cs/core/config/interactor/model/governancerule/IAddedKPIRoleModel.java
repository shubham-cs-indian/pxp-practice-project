package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IAddedKPIRoleModel extends IModel {
  
  public static final String ROLE_ID    = "roleId";
  public static final String CANDIDATES = "candidates";
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
  
  public List<String> getCandidates();
  
  public void setCandidates(List<String> candidates);
}
