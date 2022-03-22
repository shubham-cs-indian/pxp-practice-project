package com.cs.core.config.interactor.model.role;

import com.cs.core.config.interactor.entity.standard.role.ISubscriberRole;
import com.cs.core.config.interactor.entity.standard.role.SubscriberRole;
import com.cs.core.config.interactor.model.configdetails.AbstractMandatoryRoleModel;

public class SubscriberRoleModel extends AbstractMandatoryRoleModel
    implements ISubscriberRoleModel {
  
  private static final long serialVersionUID = 1L;
  
  public SubscriberRoleModel()
  {
    super(new SubscriberRole());
  }
  
  public SubscriberRoleModel(ISubscriberRole role)
  {
    super(role);
  }
}
