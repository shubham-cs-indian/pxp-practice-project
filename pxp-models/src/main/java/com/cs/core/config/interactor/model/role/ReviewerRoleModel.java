package com.cs.core.config.interactor.model.role;

import com.cs.core.config.interactor.entity.standard.role.IReviewerRole;
import com.cs.core.config.interactor.entity.standard.role.ReviewerRole;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryRoleModel;

public class ReviewerRoleModel extends AbstractMandatoryRoleModel implements IReviewerRoleModel {
  
  private static final long serialVersionUID = 1L;
  
  public ReviewerRoleModel()
  {
    super(new ReviewerRole());
  }
  
  public ReviewerRoleModel(IReviewerRole role)
  {
    super(role);
  }
}
