package com.cs.core.config.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.klass.IGetKlassesListByNatureTypeService;
import com.cs.core.runtime.interactor.usecase.klass.IGetKlassesListByNatureTypeStrategy;

@Service
public class GetKlassesListByNatureTypeService extends
    AbstractGetConfigService<IConfigGetAllRequestModel, IGetConfigDataEntityResponseModel>
    implements IGetKlassesListByNatureTypeService {
  
  @Autowired
  protected IGetKlassesListByNatureTypeStrategy GetKlassesByNatureTypeStrategy;
  
  @Override
  public IGetConfigDataEntityResponseModel executeInternal(IConfigGetAllRequestModel dataModel)
      throws Exception
  {
    return GetKlassesByNatureTypeStrategy.execute(dataModel);
  }
}
