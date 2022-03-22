package com.cs.core.config.grideditpropertylist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListSuccessModel;
import com.cs.core.config.interactor.model.grideditpropertylist.ISaveGridEditablePropertyListModel;
import com.cs.core.config.strategy.usecase.grideditpropertylist.ISaveGridEditablePropertyListStrategy;

@Service
public class SaveGridEditablePropertyListService
    extends AbstractSaveConfigService<ISaveGridEditablePropertyListModel, IGetGridEditPropertyListSuccessModel>
    implements ISaveGridEditablePropertyListService {
  
  @Autowired
  protected ISaveGridEditablePropertyListStrategy saveGridEditablePropertyListStrategy;
  
  public IGetGridEditPropertyListSuccessModel executeInternal(ISaveGridEditablePropertyListModel dataModel) throws Exception
  {
    return saveGridEditablePropertyListStrategy.execute(dataModel);
  }
}
