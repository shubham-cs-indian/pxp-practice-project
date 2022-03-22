package com.cs.pim.runtime.articleinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.klassinstance.IArticleInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;

public interface ICreateTranslatableArticleInstanceService
    extends IRuntimeService<IArticleInstanceSaveModel, IGetKlassInstanceModel> {
}
