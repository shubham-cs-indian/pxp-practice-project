package com.cs.core.runtime.strategy.usecase.dataintegration;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.dataintegration.IEntityLabelCodeMapModel;
import com.cs.core.runtime.interactor.model.dataintegration.ITaxonomyCodeLevelIdMapModel;

public interface IGetLevelIdsByTaxonomyCodesStrategy
    extends IConfigStrategy<IEntityLabelCodeMapModel, ITaxonomyCodeLevelIdMapModel> {
  
}
