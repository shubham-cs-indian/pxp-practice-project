package com.cs.core.config.businessapi.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.usecase.tag.IBulkCreateTagsStrategy;

@Component
public class BulkCreateTagsService extends AbstractCreateConfigService<IListModel<ITagModel>, IPluginSummaryModel>
    implements IBulkCreateTagsService {
  
  @Autowired
  IBulkCreateTagsStrategy orientDBBulkCreateTagsStrategy;
  
  @Override
  public IPluginSummaryModel executeInternal(IListModel<ITagModel> dataModel) throws Exception
  {
    return orientDBBulkCreateTagsStrategy.execute(dataModel);
  }
}
