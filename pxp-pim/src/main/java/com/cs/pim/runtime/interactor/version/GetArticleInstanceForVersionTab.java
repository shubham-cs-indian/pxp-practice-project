package com.cs.pim.runtime.interactor.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.articleinstance.IGetArticleInstanceForVersionTabService;

@Service
public class GetArticleInstanceForVersionTab extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceVersionsForTimeLineModel>
    implements IGetArticleInstanceForVersionTab {
  
  @Autowired
  protected IGetArticleInstanceForVersionTabService getArticleInstanceForVersionTabService;
  
  @Override
  public IGetKlassInstanceVersionsForTimeLineModel executeInternal(
      IGetInstanceRequestModel dataModel) throws Exception
  {
    return getArticleInstanceForVersionTabService.execute(dataModel);
  }

  
}
