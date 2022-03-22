package com.cs.core.config.interactor.model.role;

import com.cs.core.config.interactor.entity.standard.role.ContributorRole;
import com.cs.core.config.interactor.entity.standard.role.IContributorRole;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryRoleModel;

public class ContributorRoleModel extends AbstractMandatoryRoleModel
    implements IContributorRoleModel {
  
  private static final long serialVersionUID = 1L;
  
  public ContributorRoleModel()
  {
    super(new ContributorRole());
  }
  
  public ContributorRoleModel(IContributorRole role)
  {
    super(role);
  }
}
