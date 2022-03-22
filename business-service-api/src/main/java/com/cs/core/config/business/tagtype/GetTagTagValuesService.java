package com.cs.core.config.business.tagtype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.tag.IGetTagTagValuesRequestModel;
import com.cs.core.config.interactor.model.tag.IGetTagTagValuesResponseModel;
import com.cs.core.config.strategy.usecase.configdata.IGetTagTagValuesStrategy;

@Service
public class GetTagTagValuesService
    extends AbstractGetConfigService<IGetTagTagValuesRequestModel, IGetTagTagValuesResponseModel>
    implements IGetTagTagValuesService {
  
  @Autowired
  IGetTagTagValuesStrategy getTagTagValuesStrategy;
  
  @Override
  public IGetTagTagValuesResponseModel executeInternal(IGetTagTagValuesRequestModel dataModel)
      throws Exception
  {
    return getTagTagValuesStrategy.execute(dataModel);
  }
}
