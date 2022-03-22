package com.cs.pim.runtime.interactor.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.klassinstance.version.AbstractRestoreInstance;
import com.cs.pim.runtime.articleinstance.IRestoreArticleInstanceService;

@Service
public class RestoreArticleInstance extends AbstractRestoreInstance<IIdsListParameterModel, IBulkResponseModel>
    implements IRestoreArticleInstance {
  
  @Autowired
  protected IRestoreArticleInstanceService restoreArticleInstanceService;
  
  @Override
  protected IBulkResponseModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    return restoreArticleInstanceService.execute(model);
  }
  
}
