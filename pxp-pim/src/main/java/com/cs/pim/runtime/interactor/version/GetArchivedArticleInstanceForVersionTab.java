package com.cs.pim.runtime.interactor.version;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.articleinstance.IGetArchivedArticleInstanceForVersionTabService;

@Service
public class GetArchivedArticleInstanceForVersionTab extends AbstractRuntimeInteractor<IGetInstanceRequestModel, IGetKlassInstanceVersionsForTimeLineModel>
    implements IGetArchivedArticleInstanceForVersionTab {
  
  @Autowired
  protected IGetArchivedArticleInstanceForVersionTabService getArchivedArticleInstanceForVersionTabService;
  
  @Override
  public IGetKlassInstanceVersionsForTimeLineModel executeInternal(
      IGetInstanceRequestModel dataModel) throws Exception
  {
    return getArchivedArticleInstanceForVersionTabService.execute(dataModel);
  }

}
