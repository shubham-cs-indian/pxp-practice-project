package com.cs.pim.runtime.articleinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceBulkCloneModel;
import com.cs.core.runtime.interactor.model.klassinstance.IBulkCreateKlassInstanceCloneResponseModel;

public interface ICreateArticleInstanceBulkCloneService
    extends IRuntimeService<ICreateKlassInstanceBulkCloneModel, IBulkCreateKlassInstanceCloneResponseModel> {

}