package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.role.IRoleCandidate;

import java.util.List;

public interface IModifiedRoleInstanceModel extends IRoleInstance {
  
  public static final String ADDED_CANDIDATES    = "addedCandidates";
  public static final String DELETED_CANDIDATES  = "deletedCandidates";
  public static final String MODIFIED_CANDIDATES = "modifiedCandidates";
  
  public List<? extends IRoleCandidate> getAddedCandidates();
  
  public void setAddedCandidates(List<? extends IRoleCandidate> addedRoleCandidates);
  
  public List<String> getDeletedCandidates();
  
  public void setDeletedCandidates(List<String> deletedRoleCandidates);
  
  public List<? extends IRoleCandidate> getModifiedCandidates();
  
  public void setModifiedCandidates(List<? extends IRoleCandidate> modifiedRoleCandidates);
}
