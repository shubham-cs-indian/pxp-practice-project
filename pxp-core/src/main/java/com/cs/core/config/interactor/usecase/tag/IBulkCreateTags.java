package com.cs.core.config.interactor.usecase.tag;

import com.cs.config.interactor.usecase.base.ICreateConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.tag.ITagModel;

public interface IBulkCreateTags
    extends ICreateConfigInteractor<IListModel<ITagModel>, IPluginSummaryModel> {
  
}
