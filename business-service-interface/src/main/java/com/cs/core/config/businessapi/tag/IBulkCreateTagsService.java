package com.cs.core.config.businessapi.tag;

import com.cs.config.businessapi.base.ICreateConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.tag.ITagModel;

public interface IBulkCreateTagsService extends ICreateConfigService<IListModel<ITagModel>, IPluginSummaryModel> {
  
}
