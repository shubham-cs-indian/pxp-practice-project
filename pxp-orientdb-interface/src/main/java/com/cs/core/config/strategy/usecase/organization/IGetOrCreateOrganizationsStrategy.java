package com.cs.core.config.strategy.usecase.organization;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.organization.IOrganizationModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetOrCreateOrganizationsStrategy
    extends IConfigStrategy<IListModel<IOrganizationModel>, IListModel<IOrganizationModel>> {
  
}
