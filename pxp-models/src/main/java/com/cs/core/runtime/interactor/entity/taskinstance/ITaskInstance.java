package com.cs.core.runtime.interactor.entity.taskinstance;

public interface ITaskInstance extends IBaseTaskInstance {
  
  public static final String RESPONSIBLE = "responsible";
  public static final String ACCOUNTABLE = "accountable";
  public static final String CONSULTED   = "consulted";
  public static final String INFORMED    = "informed";
  public static final String VERIFY      = "verify";
  public static final String SIGN_OFF    = "signoff";
  
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
