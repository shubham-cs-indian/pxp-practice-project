package com.cs.core.config.interactor.usecase.taxonomy;

import com.cs.core.config.interactor.model.configdetails.IGetFilterAndSortDataRequestModel;
import com.cs.core.config.interactor.model.klass.GetFilterAndSortDataRequestModel;
import com.cs.core.config.interactor.usecase.klass.AbstractGetFilterAndSortDataForKlass;
import com.cs.core.config.strategy.usecase.klass.IGetFilterAndSortDataStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;
import com.cs.core.runtime.interactor.usecase.taxonomy.IGetFilterAndSortDataForArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetFilterAndSortDataForArticle
    extends AbstractGetFilterAndSortDataForKlass<IIdsListParameterModel, IGetFilterInformationModel>
    implements IGetFilterAndSortDataForArticle {
  
  @Autowired
  IGetFilterAndSortDataStrategy getFilterAndSortDataForArticleStrategy;
  
  @Override
  public IGetFilterInformationModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    return super.executeInternal(model);
  }
  
  @Override
  protected IGetFilterInformationModel executeGetFilterAndSortDataForArticle(
      IIdsListParameterModel model) throws Exception
  {
    IGetFilterAndSortDataRequestModel requestModel = new GetFilterAndSortDataRequestModel();
    requestModel.setTypeIds(model.getIds());
    return getFilterAndSortDataForArticleStrategy.execute(requestModel);
  }
}
