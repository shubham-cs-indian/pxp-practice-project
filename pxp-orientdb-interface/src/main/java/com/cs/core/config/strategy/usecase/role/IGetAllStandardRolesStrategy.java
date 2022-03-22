package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.entity.datarule.IMandatoryRole;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.role.IMandatoryRoleModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllStandardRolesStrategy
    extends IConfigStrategy<IMandatoryRoleModel, IListModel<IMandatoryRole>> {
  
}
