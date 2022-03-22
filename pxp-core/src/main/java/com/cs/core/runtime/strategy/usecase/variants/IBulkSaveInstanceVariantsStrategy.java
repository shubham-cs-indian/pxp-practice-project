package com.cs.core.runtime.strategy.usecase.variants;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsStrategyModel;
import com.cs.core.runtime.interactor.model.variants.IBulkSaveKlassInstanceVariantsStrategyModel;

public interface IBulkSaveInstanceVariantsStrategy
    extends IConfigStrategy<IBulkSaveInstanceVariantsStrategyModel, IBulkSaveKlassInstanceVariantsStrategyModel> {
  
}
