package com.cs.core.config.interactor.usecase.systemstatictranslation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.entity.hidden.ISaveEntityPropertyResponseModel;
import com.cs.core.config.interactor.model.hidden.IPropertyModificationInputModel;
import com.cs.core.config.strategy.usecase.systemstatictranslation.ISaveEntityPropertyStrategy;

@Service
public class SaveEntityProperty extends
    AbstractSaveConfigInteractor<IPropertyModificationInputModel, ISaveEntityPropertyResponseModel>
    implements ISaveEntityProperty {
  
  @Autowired
  ISaveEntityPropertyStrategy saveEntityPropertyStrategy;
  
  @Override
  public ISaveEntityPropertyResponseModel executeInternal(IPropertyModificationInputModel dataModel) throws Exception
  {
    return saveEntityPropertyStrategy.execute(dataModel);
  }
}
