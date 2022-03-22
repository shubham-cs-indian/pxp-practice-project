package com.cs.pim.runtime.articleinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;

public interface IGetArticleInstanceForCustomTabService
    extends IRuntimeService<IGetInstanceRequestModel, IGetKlassInstanceCustomTabModel> {
}
