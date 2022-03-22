package com.cs.core.runtime.interactor.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IArticleInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateTranslatableArticleInstance
    extends IRuntimeInteractor<IArticleInstanceSaveModel, IGetKlassInstanceModel> {
}
