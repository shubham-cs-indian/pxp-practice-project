
package com.cs.pim.runtime.strategy.usecase.articleinstance;
import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceBulkCloneModel;
import com.cs.core.runtime.interactor.model.klassinstance.IBulkCreateKlassInstanceCloneResponseModel;
import com.cs.core.runtime.interactor.usecase.bulkpropagation.AbstractCreateInstanceBulkClone;
import com.cs.pim.runtime.articleinstance.ICreateArticleInstanceBulkCloneService;
import com.cs.pim.runtime.interactor.usecase.articleinstance.ICreateArticleInstanceBulkClone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateArticleInstanceBulkClone extends AbstractCreateInstanceBulkClone<ICreateKlassInstanceBulkCloneModel, IBulkCreateKlassInstanceCloneResponseModel>
    implements ICreateArticleInstanceBulkClone {
  
  @Autowired
  protected ICreateArticleInstanceBulkCloneService createArticleInstanceBulkCloneService;

  @Override
  protected IBulkCreateKlassInstanceCloneResponseModel executeInternal(ICreateKlassInstanceBulkCloneModel dataModel) throws Exception
  {
    return createArticleInstanceBulkCloneService.execute(dataModel);
  }

}