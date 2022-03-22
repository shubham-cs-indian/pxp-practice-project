package com.cs.core.config.interactor.model.role;

import com.cs.core.config.interactor.entity.standard.role.ApproverRole;
import com.cs.core.config.interactor.entity.standard.role.IApproverRole;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryRoleModel;

public class ApproverRoleModel extends AbstractMandatoryRoleModel implements IApproverRoleModel {
  
  private static final long serialVersionUID = 1L;
  
  public ApproverRoleModel()
  {
    super(new ApproverRole());
  }
  
  public ApproverRoleModel(IApproverRole role)
  {
    super(role);
  }
}
