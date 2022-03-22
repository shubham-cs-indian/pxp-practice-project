package com.cs.pim.runtime.articleinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceSingleCloneModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;

public interface ICreateArticleInstanceSingleCloneService
    extends IRuntimeService<ICreateKlassInstanceSingleCloneModel, IGetKlassInstanceModel> {

}