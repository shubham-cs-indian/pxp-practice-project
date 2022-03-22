package com.cs.core.config.grideditpropertylist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListRequestModel;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListSuccessModel;
import com.cs.core.config.strategy.usecase.grideditpropertylist.IGetAllGridEditablePropertyListWithAvaliableSequenceStrategy;

@Service
public class GetAllGridEditablePropertyListWithAvaliableSequenceService extends
    AbstractGetConfigService<IGetGridEditPropertyListRequestModel, IGetGridEditPropertyListSuccessModel>
    implements IGetAllGridEditablePropertyListWithAvaliableSequenceService {
  
  @Autowired
  protected IGetAllGridEditablePropertyListWithAvaliableSequenceStrategy getAllGridEditablePropertyListWithAvaliableSequenceStrategy;
  
  @Override
  public IGetGridEditPropertyListSuccessModel executeInternal(
      IGetGridEditPropertyListRequestModel dataModel) throws Exception
  {
    return getAllGridEditablePropertyListWithAvaliableSequenceStrategy.execute(dataModel);
  }
}
