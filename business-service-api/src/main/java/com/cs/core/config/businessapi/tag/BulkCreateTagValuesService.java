package com.cs.core.config.businessapi.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.business.tagtype.IBulkCreateTagValuesService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.IBulkCreateTagValuesResponseModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.usecase.tag.IBulkCreateTagValuesStrategy;

@Service
public class BulkCreateTagValuesService extends AbstractCreateConfigService<IListModel<ITagModel>, IBulkCreateTagValuesResponseModel>
    implements IBulkCreateTagValuesService {
  
  @Autowired
  IBulkCreateTagValuesStrategy bulkCreateTagValuesStrategy;
  
  @Override
  public IBulkCreateTagValuesResponseModel executeInternal(IListModel<ITagModel> dataModel) throws Exception
  {
    return bulkCreateTagValuesStrategy.execute(dataModel);
  }
}
