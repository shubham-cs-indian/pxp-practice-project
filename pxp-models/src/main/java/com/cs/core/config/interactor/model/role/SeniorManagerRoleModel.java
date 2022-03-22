package com.cs.core.config.interactor.model.role;

import com.cs.core.config.interactor.entity.standard.role.ISeniorManagerRole;
import com.cs.core.config.interactor.entity.standard.role.SeniorManagerRole;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryRoleModel;

public class SeniorManagerRoleModel extends AbstractMandatoryRoleModel
    implements ISeniorManagerRoleModel {
  
  private static final long serialVersionUID = 1L;
  
  public SeniorManagerRoleModel()
  {
    super(new SeniorManagerRole());
  }
  
  public SeniorManagerRoleModel(ISeniorManagerRole role)
  {
    super(role);
  }
}
