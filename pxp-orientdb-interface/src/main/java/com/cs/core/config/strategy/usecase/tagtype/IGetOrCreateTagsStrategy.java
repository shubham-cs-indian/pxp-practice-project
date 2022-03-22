package com.cs.core.config.strategy.usecase.tagtype;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetOrCreateTagsStrategy
    extends IConfigStrategy<IListModel<ITagModel>, IListModel<ITagModel>> {
  
}
