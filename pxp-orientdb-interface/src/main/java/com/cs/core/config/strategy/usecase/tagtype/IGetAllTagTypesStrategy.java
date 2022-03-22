package com.cs.core.config.strategy.usecase.tagtype;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagTypeModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetAllTagTypesStrategy
    extends IConfigStrategy<ITagTypeModel, IListModel<ITagTypeModel>> {
  
}
