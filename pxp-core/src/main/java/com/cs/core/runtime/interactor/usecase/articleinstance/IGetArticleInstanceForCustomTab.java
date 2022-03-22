package com.cs.core.runtime.interactor.usecase.articleinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetArticleInstanceForCustomTab
    extends IRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceCustomTabModel> {
}
