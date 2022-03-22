package com.cs.core.runtime.strategy.offboarding;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.offboarding.ICustomExportComponentConfigModel;

public interface IGetAllPIDAndSingleArticleKlassIdsStrategy
    extends IConfigStrategy<IModel, ICustomExportComponentConfigModel> {
  
}
