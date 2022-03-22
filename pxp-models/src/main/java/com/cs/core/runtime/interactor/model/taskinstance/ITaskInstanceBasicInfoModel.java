package com.cs.core.runtime.interactor.model.taskinstance;

import java.util.List;

import com.cs.core.runtime.interactor.entity.taskinstance.ITaskRoleEntity;
import com.cs.core.runtime.interactor.model.configuration.IModel;


public interface ITaskInstanceBasicInfoModel extends IModel {
  
  public static final String ID                       = "id";
  public static final String TYPES                    = "types";
  public static final String CREATED_BY               = "createdBy";
  public static final String NAME                     = "name";
  public static final String CAMUNDA_TASK_INSTANCE_ID = "camundaTaskInstanceId";
  public static final String IS_CAMUNDA_CREATED       = "isCamundaCreated";
  public static final String RESPONSIBLE              = "responsible";
  public static final String ACCOUNTABLE              = "accountable";
  public static final String CONSULTED                = "consulted";
  public static final String INFORMED                 = "informed";
  public static final String VERIFY                   = "verify";
  public static final String SIGN_OFF                 = "signoff";
  
  
  public void setId(String id);
  public String getId();
  
  public void setTypes(List<String> types);
  public List<String> getTypes();
  
  public void setCreatedBy(String createdBy);
  public String getCreatedBy();
  
  public void setName(String name);
  public String getName();
  
  public String getCamundaTaskInstanceId();
  public void setCamundaTaskInstanceId(String camundaTaskInstanceId);
  
  public Boolean getIsCamundaCreated();
  public void setIsCamundaCreated(Boolean isCamundaCreated);
  
  public ITaskRoleEntity getResponsible();
  public void setResponsible(ITaskRoleEntity responsible);
  
  public ITaskRoleEntity getAccountable();
  public void setAccountable(ITaskRoleEntity accountable);
  
  public ITaskRoleEntity getConsulted();
  public void setConsulted(ITaskRoleEntity consulted);
  
  public ITaskRoleEntity getInformed();
  public void setInformed(ITaskRoleEntity informed);
  
  public ITaskRoleEntity getVerify();
  public void setVerify(ITaskRoleEntity verify);
  
  public ITaskRoleEntity getSignoff();
  public void setSignoff(ITaskRoleEntity signoff);
}

