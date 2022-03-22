package com.cs.core.config.interactor.usecase.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.asset.IDAMConfigurationDetailsResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.strategy.usecase.asset.IGetDAMConfigurationDetailsStrategy;

/**
 * Get DAM configuration service layer.
 * @author pranav.huchche
 *
 */

@Service
public class GetDAMConfigurationDetails
    extends AbstractGetConfigInteractor<IIdParameterModel, IDAMConfigurationDetailsResponseModel>
    implements IGetDAMConfigurationDetails {
  
  @Autowired
  IGetDAMConfigurationDetailsStrategy getDAMConfigurationDetailsStrategy;
  
  @Override
  protected IDAMConfigurationDetailsResponseModel executeInternal(IIdParameterModel model)
      throws Exception
  {
    return getDAMConfigurationDetailsStrategy.execute(model);
  }
  
}
