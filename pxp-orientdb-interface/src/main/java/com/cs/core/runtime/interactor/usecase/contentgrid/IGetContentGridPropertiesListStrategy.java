package com.cs.core.runtime.interactor.usecase.contentgrid;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

public interface IGetContentGridPropertiesListStrategy
    extends IConfigStrategy<IIdParameterModel, IListModel<IConfigEntityInformationModel>> {
}
