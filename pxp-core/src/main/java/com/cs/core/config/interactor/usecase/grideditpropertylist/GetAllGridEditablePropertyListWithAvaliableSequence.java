package com.cs.core.config.interactor.usecase.grideditpropertylist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.grideditpropertylist.IGetAllGridEditablePropertyListWithAvaliableSequenceService;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListRequestModel;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListSuccessModel;

@Service
public class GetAllGridEditablePropertyListWithAvaliableSequence
    extends AbstractGetConfigInteractor<IGetGridEditPropertyListRequestModel, IGetGridEditPropertyListSuccessModel>
    implements IGetAllGridEditablePropertyListWithAvaliableSequence {
  
  @Autowired
  protected IGetAllGridEditablePropertyListWithAvaliableSequenceService getAllGridEditablePropertyListWithAvaliableSequenceService;
  
  @Override
  public IGetGridEditPropertyListSuccessModel executeInternal(IGetGridEditPropertyListRequestModel dataModel) throws Exception
  {
    return getAllGridEditablePropertyListWithAvaliableSequenceService.execute(dataModel);
  }
}
