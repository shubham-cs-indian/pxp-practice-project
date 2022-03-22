package com.cs.core.runtime.interactor.model.taskinstance;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.taskinstance.ITaskRoleEntity;
import com.cs.core.runtime.interactor.entity.taskinstance.TaskRoleEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TaskInstanceBasicInfoModel implements ITaskInstanceBasicInfoModel {
  
  private static final long serialVersionUID = 1L;

  protected String              id;
  protected List<String>        types;
  protected String              createdBy;
  protected List<IRoleInstance> roles            = new ArrayList<>();
  protected String              name;
  protected Boolean             isCamundaCreated = false;
  protected String              camundaTaskInstanceId;
  protected ITaskRoleEntity     responsible;
  protected ITaskRoleEntity     accountable;
  protected ITaskRoleEntity     consulted;
  protected ITaskRoleEntity     informed;
  protected ITaskRoleEntity     verify;
  protected ITaskRoleEntity     signoff;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public List<String> getTypes()
  {
    return types;
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    this.types = types;
  }
  
  @Override
  public String getCreatedBy()
  {
    return createdBy;
  }
  
  @Override
  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
  }
  
  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
  public Boolean getIsCamundaCreated()
  {
    return isCamundaCreated;
  }

  @Override
  public void setIsCamundaCreated(Boolean isCamundaCreated)
  {
    this.isCamundaCreated = isCamundaCreated;    
  }

  @Override
  public String getCamundaTaskInstanceId()
  {
    return camundaTaskInstanceId;
  }

  @Override
  public void setCamundaTaskInstanceId(String camundaTaskInstanceId)
  {
    this.camundaTaskInstanceId = camundaTaskInstanceId;
  }
  
  @Override
  public ITaskRoleEntity getResponsible()
  {
    if(responsible == null) {
      responsible = new TaskRoleEntity();
    }
    return responsible;
  }

  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setResponsible(ITaskRoleEntity responsible)
  {
    this.responsible = responsible;
  }

  @Override
  public ITaskRoleEntity getAccountable()
  {
    if(accountable == null) {
      accountable = new TaskRoleEntity();
    }
    return accountable;
  }

  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setAccountable(ITaskRoleEntity accountable)
  {
    this.accountable = accountable;
  }

  @Override
  public ITaskRoleEntity getConsulted()
  {
    if(consulted == null) {
      consulted = new TaskRoleEntity();
    }
    return consulted;
  }

  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setConsulted(ITaskRoleEntity consulted)
  {
    this.consulted = consulted;
  }  

  @Override
  public ITaskRoleEntity getInformed()
  {
    if(informed == null) {
      informed = new TaskRoleEntity();
    }
    return informed;
  }

  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setInformed(ITaskRoleEntity informed)
  {
    this.informed = informed;
  }

  @Override
  public ITaskRoleEntity getVerify()
  {
    if(verify == null) {
      verify = new TaskRoleEntity();
    }
    return verify;
  }
  
  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setVerify(ITaskRoleEntity verify)
  {
    this.verify = verify;
  }
 
  @Override
  public ITaskRoleEntity getSignoff()
  {
    if(signoff == null) {
      signoff = new TaskRoleEntity();
    }
    return signoff;
  }

  @Override
  @JsonDeserialize(as = TaskRoleEntity.class)
  public void setSignoff(ITaskRoleEntity signoff)
  {
    this.signoff = signoff;
  }
  
}
