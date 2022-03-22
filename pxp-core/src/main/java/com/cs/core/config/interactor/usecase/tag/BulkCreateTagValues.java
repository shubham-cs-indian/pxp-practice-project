package com.cs.core.config.interactor.usecase.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.business.tagtype.IBulkCreateTagValuesService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.IBulkCreateTagValuesResponseModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.interactor.usecase.tagtype.IBulkCreateTagValues;

@Service
public class BulkCreateTagValues
    extends AbstractCreateConfigInteractor<IListModel<ITagModel>, IBulkCreateTagValuesResponseModel>
    implements IBulkCreateTagValues {
  
  @Autowired
  IBulkCreateTagValuesService bulkCreateTagValuesService;
  
  @Override
  public IBulkCreateTagValuesResponseModel executeInternal(IListModel<ITagModel> dataModel) throws Exception
  {
    return bulkCreateTagValuesService.execute(dataModel);
  }
}
