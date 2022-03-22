package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.datarule.IGetAllDisplayAndRelevanceTagIdsModel;

public interface IGetDisplayAndRelevanceTagIdsStrategy
    extends IConfigStrategy<ITagModel, IGetAllDisplayAndRelevanceTagIdsModel> {
  
}
