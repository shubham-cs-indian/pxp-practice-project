package com.cs.pim.runtime.articleinstance;

import com.cs.core.runtime.interactor.exception.articleinstance.UserNotHaveCreatePermissionForArticle;
import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceBulkCloneModel;
import com.cs.core.runtime.interactor.model.klassinstance.IBulkCreateKlassInstanceCloneResponseModel;
import com.cs.core.runtime.klassinstance.AbstractCreateInstanceBulkClone;
import org.springframework.stereotype.Service;

@Service
public class CreateArticleInstanceBulkCloneService
    extends AbstractCreateInstanceBulkClone<ICreateKlassInstanceBulkCloneModel, IBulkCreateKlassInstanceCloneResponseModel>
    implements ICreateArticleInstanceBulkCloneService {

  @Override
  protected IBulkCreateKlassInstanceCloneResponseModel executeInternal(ICreateKlassInstanceBulkCloneModel dataModel)
      throws Exception
  {
    return super.executeInternal(dataModel);
  }

  @Override
  protected Exception getUserNotHaveCreatePermissionException()
  {
    return new UserNotHaveCreatePermissionForArticle();
  }
}