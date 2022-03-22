package com.cs.pim.runtime.strategy.usecase.articleinstance;


import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.statistics.IGetStatisticsForContentResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.klassinstance.IGetStatisticsForContentService;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IGetStatisticsForContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetStatisticsForContent
    extends AbstractRuntimeInteractor<IIdAndTypeModel, IGetStatisticsForContentResponseModel>
    implements IGetStatisticsForContent {

  @Autowired
  protected IGetStatisticsForContentService getStatisticsForContentService;
  
  @Override
  public IGetStatisticsForContentResponseModel executeInternal(IIdAndTypeModel dataModel) throws Exception
  {
    return getStatisticsForContentService.execute(dataModel);
  }

}