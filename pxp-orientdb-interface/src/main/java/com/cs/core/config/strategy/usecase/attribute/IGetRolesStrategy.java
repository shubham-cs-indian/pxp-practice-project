package com.cs.core.config.strategy.usecase.attribute;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetRolesStrategy
    extends IConfigStrategy<IIdsListParameterModel, IListModel<IConfigEntityInformationModel>> {
  
}
