package com.cs.core.config.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataEntityResponseModel;
import com.cs.core.config.interactor.model.klass.IGetKlassesByBaseTypeModel;
import com.cs.core.config.klass.IGetKlassesListByBaseTypeService;
import com.cs.core.runtime.interactor.usecase.klass.IGetKlassesListByBaseTypeStrategy;

@Service
public class GetKlassesListByBaseTypeService extends
    AbstractGetConfigService<IGetKlassesByBaseTypeModel, IGetConfigDataEntityResponseModel>
    implements IGetKlassesListByBaseTypeService {
  
  @Autowired
  protected IGetKlassesListByBaseTypeStrategy getKlassesListByBaseTypeStrategy;
  
  @Override
  public IGetConfigDataEntityResponseModel executeInternal(IGetKlassesByBaseTypeModel dataModel)
      throws Exception
  {
    return getKlassesListByBaseTypeStrategy.execute(dataModel);
  }
}
