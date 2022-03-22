package com.cs.core.config.interactor.model.role;

import com.cs.core.config.interactor.entity.standard.role.IOwnerRole;
import com.cs.core.config.interactor.entity.standard.role.OwnerRole;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryRoleModel;

public class OwnerRoleModel extends AbstractMandatoryRoleModel implements IOwnerRoleModel {
  
  private static final long serialVersionUID = 1L;
  
  public OwnerRoleModel()
  {
    super(new OwnerRole());
  }
  
  public OwnerRoleModel(IOwnerRole role)
  {
    super(role);
  }
}
