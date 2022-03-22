package com.cs.core.config.textasset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.klass.AbstractCreateKlassService;
import com.cs.core.config.strategy.usecase.textasset.ICreateTextAssetStrategy;


@Service
public class CreateTextAssetService extends AbstractCreateKlassService<ITextAssetModel, IGetKlassEntityWithoutKPModel> implements ICreateTextAssetService {
  
  @Autowired
  protected ICreateTextAssetStrategy orientdbCreateTextAssetStrategy;

  @Override
  public IGetKlassEntityWithoutKPModel executeCreateKlass(ITextAssetModel klassModel)
      throws Exception
  {
    return orientdbCreateTextAssetStrategy.execute(klassModel);
  }
}
