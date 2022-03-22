package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface ISaveArticleInstanceStrategy
    extends IRuntimeStrategy<IKlassInstanceSaveModel, VoidModel> {
  
}
