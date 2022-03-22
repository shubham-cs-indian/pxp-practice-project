package com.cs.core.config.strategy.usecase.attribute;

import com.cs.core.config.interactor.entity.standard.attribute.IStandardAttribute;
import com.cs.core.config.interactor.model.attribute.IMandatoryAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllStandardAttributesStrategy
    extends IConfigStrategy<IMandatoryAttributeModel, IListModel<IStandardAttribute>> {
  
}
