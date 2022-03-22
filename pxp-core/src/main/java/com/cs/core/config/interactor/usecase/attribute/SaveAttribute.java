package com.cs.core.config.interactor.usecase.attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.businessapi.attribute.ISaveAttributeService;
import com.cs.core.config.interactor.model.attribute.IBulkSaveAttributeResponseModel;
import com.cs.core.config.interactor.model.attribute.ISaveAttributeModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;

@Service
public class SaveAttribute extends AbstractSaveConfigInteractor<IListModel<ISaveAttributeModel>, IBulkSaveAttributeResponseModel>
    implements ISaveAttribute {
  
  @Autowired
  protected ISaveAttributeService saveAttributeService;
  
  @Override
  public IBulkSaveAttributeResponseModel executeInternal(IListModel<ISaveAttributeModel> attributeModel) throws Exception
  {
    return saveAttributeService.execute(attributeModel);
  }
}
