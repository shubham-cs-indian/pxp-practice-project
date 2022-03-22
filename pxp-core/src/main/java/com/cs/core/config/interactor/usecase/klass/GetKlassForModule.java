package com.cs.core.config.interactor.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.GetKlassModel;
import com.cs.core.config.interactor.model.klass.IGetKlassModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.klass.IGetKlassForModuleService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetKlassForModule
    extends AbstractGetConfigInteractor<IIdParameterModel, IListModel<IKlassInformationModel>>
    implements IGetKlassForModule {
  
  @Autowired
  IGetKlassForModuleService getKlassForModuleService;
  
  @Override
  public IListModel<IKlassInformationModel> executeInternal(IIdParameterModel klassModel) throws Exception
  {
    IGetKlassModel model = new GetKlassModel();
    
    return getKlassForModuleService.execute(klassModel);
  }
}
