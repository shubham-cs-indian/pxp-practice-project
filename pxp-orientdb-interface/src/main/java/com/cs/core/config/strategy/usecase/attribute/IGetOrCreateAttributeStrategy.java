package com.cs.core.config.strategy.usecase.attribute;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetOrCreateAttributeStrategy
    extends IConfigStrategy<IListModel<IAttribute>, IListModel<IAttribute>> {
  
}
