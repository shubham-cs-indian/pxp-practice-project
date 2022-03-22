package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetTagsByIdStrategy
    extends IConfigStrategy<IIdsListParameterModel, IListModel<ITag>> {
  
}
