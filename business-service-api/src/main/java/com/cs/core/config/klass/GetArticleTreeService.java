package com.cs.core.config.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.klass.IGetArticleTreeService;
import com.cs.core.config.strategy.usecase.klass.IGetKlassTreeStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetArticleTreeService
    extends AbstractGetConfigService<IIdParameterModel, IConfigEntityTreeInformationModel>
    implements IGetArticleTreeService {
  
  @Autowired
  IGetKlassTreeStrategy getArticleTreeStrategy;
  
  @Override
  public IConfigEntityTreeInformationModel executeInternal(IIdParameterModel klassModel) throws Exception
  {
    return getArticleTreeStrategy.execute(klassModel);
  }
}
