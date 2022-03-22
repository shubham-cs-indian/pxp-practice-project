package com.cs.core.config.interactor.usecase.propertycollection;

import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetOrCreatePropertyCollectionStrategy
    extends IConfigStrategy<IListModel<IPropertyCollection>, IListModel<IPropertyCollection>> {
  
}
