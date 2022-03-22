package com.cs.pim.runtime.articleinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceResponseModel;

public interface IGetArticleInstanceForTasksTabService
    extends IRuntimeService<IGetInstanceRequestModel, IGetTaskInstanceResponseModel> {
  
}
