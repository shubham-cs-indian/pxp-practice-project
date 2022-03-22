package com.cs.core.config.interactor.usecase.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.businessapi.tag.IBulkCreateTagsService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.tag.ITagModel;

@Component
public class BulkCreateTags
    extends AbstractCreateConfigInteractor<IListModel<ITagModel>, IPluginSummaryModel>
    implements IBulkCreateTags {
  
  @Autowired
  IBulkCreateTagsService bulkCreateTagService;
  
  @Override
  public IPluginSummaryModel executeInternal(IListModel<ITagModel> dataModel) throws Exception
  {
    return bulkCreateTagService.execute(dataModel);
  }
}
