package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetDisplayTagIdsStrategy extends IConfigStrategy<ITagModel, IListModel<String>> {
  
}
