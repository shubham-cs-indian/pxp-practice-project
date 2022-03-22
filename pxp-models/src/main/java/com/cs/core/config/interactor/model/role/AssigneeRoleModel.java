package com.cs.core.config.interactor.model.role;

import com.cs.core.config.interactor.entity.standard.role.AssigneeRole;
import com.cs.core.config.interactor.entity.standard.role.IAssigneeRole;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryRoleModel;

public class AssigneeRoleModel extends AbstractMandatoryRoleModel implements IAssigneeRoleModel {
  
  private static final long serialVersionUID = 1L;
  
  public AssigneeRoleModel()
  {
    super(new AssigneeRole());
  }
  
  public AssigneeRoleModel(IAssigneeRole attribute)
  {
    super(attribute);
  }
}
