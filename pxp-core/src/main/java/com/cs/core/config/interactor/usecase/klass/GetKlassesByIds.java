package com.cs.core.config.interactor.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.klass.IGetKlassesByIdsService;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class GetKlassesByIds
    extends AbstractGetConfigInteractor<IIdsListParameterModel, IListModel<IKlass>>
    implements IGetKlassesByIds {
  
  @Autowired
  IGetKlassesByIdsService getKlassesByIdsService;
  
  @Override
  public IListModel<IKlass> executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return getKlassesByIdsService.execute(dataModel);
  }
}
