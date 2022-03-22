package com.cs.core.config.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.GetKlassModel;
import com.cs.core.config.interactor.model.klass.IGetKlassModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.klass.IGetKlassForModuleService;
import com.cs.core.config.strategy.usecase.klass.IGetKlassForModuleStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetKlassForModuleService
    extends AbstractGetConfigService<IIdParameterModel, IListModel<IKlassInformationModel>>
    implements IGetKlassForModuleService {
  
  @Autowired
  IGetKlassForModuleStrategy neo4jGetKlassForModuleStrategy;
  
  @Override
  public IListModel<IKlassInformationModel> executeInternal(IIdParameterModel klassModel) throws Exception
  {
    IGetKlassModel model = new GetKlassModel();
    
    return neo4jGetKlassForModuleStrategy.execute(klassModel);
  }
}
