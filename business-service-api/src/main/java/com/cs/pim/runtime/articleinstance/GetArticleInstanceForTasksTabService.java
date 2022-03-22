package com.cs.pim.runtime.articleinstance;

import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.taskinstance.GetTaskInstanceResponseModel;
import com.cs.core.runtime.interactor.model.taskinstance.IGetTaskInstanceResponseModel;
import com.cs.core.runtime.klassinstance.AbstractGetInstanceForTaskTabService;
import org.springframework.stereotype.Service;

@Service
public class GetArticleInstanceForTasksTabService extends AbstractGetInstanceForTaskTabService<IGetInstanceRequestModel, IGetTaskInstanceResponseModel>
implements IGetArticleInstanceForTasksTabService {
  
  @Override
  protected IGetTaskInstanceResponseModel executeInternal(
      IGetInstanceRequestModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    try {
      return super.executeInternal(getKlassInstanceTreeStrategyModel);
    }
    catch (KlassNotFoundException e) { 
      throw new ArticleKlassNotFoundException(e);
    }
    
  }
  
  @Override
  protected IGetTaskInstanceResponseModel executeGetKlassInstance(
      IGetInstanceRequestStrategyModel getKlassInstanceTreeStrategyModel) throws Exception
  {
    IGetTaskInstanceResponseModel getTaskInstanceResponseModel = new GetTaskInstanceResponseModel();
    return getTaskInstanceResponseModel;
  }
  
  @Override
  protected IKlassInstanceTypeModel getKlassInstanceType(String id) throws Exception
  {
    IKlassInstanceTypeModel klassInstanceTypeModel = new KlassInstanceTypeModel();
    IIdParameterModel idParameterModel = new IdParameterModel(id);
    return klassInstanceTypeModel;
  }

 
}
