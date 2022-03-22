package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IArticleInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ISaveArticleInstanceForTabs
    extends IRuntimeInteractor<IArticleInstanceSaveModel, IGetKlassInstanceModel> {
  
}
