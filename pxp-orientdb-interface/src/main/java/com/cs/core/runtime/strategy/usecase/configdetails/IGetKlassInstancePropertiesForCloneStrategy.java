package com.cs.core.runtime.strategy.usecase.configdetails;


import com.cs.core.config.interactor.model.IGetCloneWizardForRequestStrategyModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.clone.IGetKlassInstancePropertiesForCloneModel;

public interface IGetKlassInstancePropertiesForCloneStrategy
    extends IConfigStrategy<IGetCloneWizardForRequestStrategyModel, IGetKlassInstancePropertiesForCloneModel> {
  
}