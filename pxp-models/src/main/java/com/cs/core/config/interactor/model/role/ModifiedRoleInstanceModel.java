package com.cs.core.config.interactor.model.role;

import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.role.AbstractRoleCandidate;
import com.cs.core.runtime.interactor.entity.role.IRoleCandidate;
import com.cs.core.runtime.interactor.entity.role.RoleInstance;
import com.cs.core.runtime.interactor.model.configdetails.RoleInstanceModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedRoleInstanceModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, defaultImpl = ModifiedRoleInstanceModel.class)
public class ModifiedRoleInstanceModel extends RoleInstanceModel
    implements IModifiedRoleInstanceModel {
  
  private static final long                serialVersionUID = 1L;
  
  protected List<? extends IRoleCandidate> addedCandidates;
  
  protected List<String>                   deletedCandidates;
  
  protected List<? extends IRoleCandidate> modifiedCandidates;
  
  public ModifiedRoleInstanceModel()
  {
    super(new RoleInstance());
  }
  
  public ModifiedRoleInstanceModel(IRoleInstance roleInstance)
  {
    super(roleInstance);
  }
  
  @Override
  public List<? extends IRoleCandidate> getAddedCandidates()
  {
    return addedCandidates;
  }
  
  @JsonDeserialize(contentAs = AbstractRoleCandidate.class)
  @Override
  public void setAddedCandidates(List<? extends IRoleCandidate> addedCandidates)
  {
    this.addedCandidates = addedCandidates;
  }
  
  @Override
  public List<String> getDeletedCandidates()
  {
    return deletedCandidates;
  }
  
  @Override
  public void setDeletedCandidates(List<String> deletedCandidates)
  {
    this.deletedCandidates = deletedCandidates;
  }
  
  @Override
  public List<? extends IRoleCandidate> getModifiedCandidates()
  {
    return modifiedCandidates;
  }
  
  @JsonDeserialize(contentAs = AbstractRoleCandidate.class)
  @Override
  public void setModifiedCandidates(List<? extends IRoleCandidate> modifiedCandidates)
  {
    this.modifiedCandidates = modifiedCandidates;
  }
  
  @JsonIgnore
  @Override
  public List<? extends IRoleCandidate> getCandidates()
  {
    throw new RuntimeException("Do not use on Modified Model");
  }
  
  @JsonIgnore
  @Override
  public void setCandidates(List<? extends IRoleCandidate> candidates)
  {
    throw new RuntimeException("Do not use on Modified Model");
  }
}
