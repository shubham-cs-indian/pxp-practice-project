package com.cs.core.config.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.klass.AbstractCreateKlassService;
import com.cs.core.config.strategy.usecase.asset.ICreateAssetStrategy;

@Service
public class CreateAssetService extends AbstractCreateKlassService<IAssetModel, IGetKlassEntityWithoutKPModel>
    implements ICreateAssetService {
  
  @Autowired
  protected ICreateAssetStrategy createAssetStrateg;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeCreateKlass(IAssetModel model) throws Exception
  {
    return createAssetStrateg.execute(model);
  }
}
