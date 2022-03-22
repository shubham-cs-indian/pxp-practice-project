package com.cs.core.config.interactor.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.klass.IGetAllKlassesService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetAllKlasses
    extends AbstractGetConfigInteractor<IIdParameterModel, IListModel<IKlassInformationModel>>
    implements IGetAllKlasses {
  
  @Autowired
  IGetAllKlassesService getAllKlassesService;
  
  @Override
  public IListModel<IKlassInformationModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getAllKlassesService.execute(dataModel);
  }
}
