package com.cs.core.config.interactor.usecase.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.businessapi.tag.ISaveTagService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.IBulkSaveTagResponseModel;
import com.cs.core.config.interactor.model.tag.ISaveTagModel;

@Service
public class SaveTag
    extends AbstractSaveConfigInteractor<IListModel<ISaveTagModel>, IBulkSaveTagResponseModel>
    implements ISaveTag {
  
  @Autowired
  protected ISaveTagService saveTagService;
  
  @Override
  public IBulkSaveTagResponseModel executeInternal(IListModel<ISaveTagModel> dataModel)
      throws Exception
  {
    return saveTagService.execute(dataModel);
  }
}
