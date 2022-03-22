package com.cs.core.config.interactor.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.klass.IGetKlassesByBaseTypeModel;
import com.cs.core.config.klass.IGetKlassesListByBaseTypeService;

@Service
public class GetKlassesListByBaseType extends
    AbstractGetConfigInteractor<IGetKlassesByBaseTypeModel, IGetConfigDataEntityResponseModel>
    implements IGetKlassesListByBaseType {
  
  @Autowired
  protected IGetKlassesListByBaseTypeService getKlassesListByBaseTypeService;
  
  @Override
  public IGetConfigDataEntityResponseModel executeInternal(IGetKlassesByBaseTypeModel dataModel)
      throws Exception
  {
    return getKlassesListByBaseTypeService.execute(dataModel);
  }
}
