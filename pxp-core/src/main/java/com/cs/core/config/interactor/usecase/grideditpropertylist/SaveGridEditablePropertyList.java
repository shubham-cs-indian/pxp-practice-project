package com.cs.core.config.interactor.usecase.grideditpropertylist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.grideditpropertylist.ISaveGridEditablePropertyListService;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListSuccessModel;
import com.cs.core.config.interactor.model.grideditpropertylist.ISaveGridEditablePropertyListModel;

@Service
public class SaveGridEditablePropertyList
    extends AbstractSaveConfigInteractor<ISaveGridEditablePropertyListModel, IGetGridEditPropertyListSuccessModel>
    implements ISaveGridEditablePropertyList {
  
  @Autowired
  protected ISaveGridEditablePropertyListService saveGridEditablePropertyListService;
  
  public IGetGridEditPropertyListSuccessModel executeInternal(ISaveGridEditablePropertyListModel dataModel) throws Exception
  {
    return saveGridEditablePropertyListService.execute(dataModel);
  }
}
