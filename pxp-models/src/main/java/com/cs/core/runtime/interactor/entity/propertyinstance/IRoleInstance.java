package com.cs.core.runtime.interactor.entity.propertyinstance;

import com.cs.core.runtime.interactor.entity.role.IRoleCandidate;
import java.util.List;

public interface IRoleInstance extends IPropertyInstance {
  
  public static final String CANDIDATES = "candidates";
  public static final String ROLE_ID    = "roleId";
  
  public List<? extends IRoleCandidate> getCandidates();
  
  public void setCandidates(List<? extends IRoleCandidate> candidates);
  
  public String getRoleId();
  
  public void setRoleId(String roleId);
}
