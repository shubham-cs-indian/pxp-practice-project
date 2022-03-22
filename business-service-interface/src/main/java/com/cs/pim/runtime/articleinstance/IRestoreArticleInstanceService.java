package com.cs.pim.runtime.articleinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IRestoreArticleInstanceService extends IRuntimeService<IIdsListParameterModel, IBulkResponseModel> {
  
}
