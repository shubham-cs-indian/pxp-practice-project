package com.cs.core.config.interactor.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.klass.IGetKlassesListByNatureTypeService;
import com.cs.core.runtime.interactor.usecase.klass.IGetKlassesListByNatureTypeStrategy;

@Service
public class GetKlassesListByNatureType extends
    AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetConfigDataEntityResponseModel>
    implements IGetKlassesListByNatureType {
  
  @Autowired
  protected IGetKlassesListByNatureTypeService GetKlassesByNatureTypeService;
  
  @Override
  public IGetConfigDataEntityResponseModel executeInternal(IConfigGetAllRequestModel dataModel)
      throws Exception
  {
    return GetKlassesByNatureTypeService.execute(dataModel);
  }
}
