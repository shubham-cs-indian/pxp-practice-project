package com.cs.core.config.interactor.usecase.tagtype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.business.tagtype.IGetTagTagValuesService;
import com.cs.core.config.interactor.model.tag.IGetTagTagValuesRequestModel;
import com.cs.core.config.interactor.model.tag.IGetTagTagValuesResponseModel;

@Service
public class GetTagTagValuesInteractor
    extends AbstractGetConfigInteractor<IGetTagTagValuesRequestModel, IGetTagTagValuesResponseModel>
    implements IGetTagTagValuesInteractor {
  
  @Autowired
  IGetTagTagValuesService getTagTagValuesService;
  
  @Override
  public IGetTagTagValuesResponseModel executeInternal(IGetTagTagValuesRequestModel dataModel)
      throws Exception
  {
    return getTagTagValuesService.execute(dataModel);
  }
}
