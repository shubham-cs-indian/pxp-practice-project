package com.cs.core.config.strategy.usecase.standard.properties;

import com.cs.core.config.interactor.entity.datarule.IMandatoryProperty;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllStandardPropertiesStrategy
    extends IConfigStrategy<IConfigModel, IListModel<IMandatoryProperty>> {
  
}
