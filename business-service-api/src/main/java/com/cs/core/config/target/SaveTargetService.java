package com.cs.core.config.target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.klass.ITypesListModel;
import com.cs.core.config.klass.AbstractSaveKlassService;
import com.cs.core.config.strategy.usecase.target.ISaveTargetStrategy;

@Service
public class SaveTargetService extends AbstractSaveKlassService<IKlassSaveModel, IGetKlassEntityWithoutKPModel>
    implements ISaveTargetService {
  
  @Autowired
  ISaveTargetStrategy saveTargetStrategy;
  
  /*@Autowired
  IGetKlassInstancesByKlassAndTaxonomyIdsStrategy getMarketInstancesByKlassAndTaxonomyIdsStrategy;*/
  
  @Override
  public IGetKlassEntityWithoutKPModel execute(IKlassSaveModel klassModel) throws Exception
  {
    return super.execute(klassModel);
  }
  
  @Override
  protected IGetKlassEntityWithoutKPStrategyResponseModel executeSaveKlass(
      IKlassSaveModel klassModel) throws Exception
  {
    IGetKlassEntityWithoutKPStrategyResponseModel returnModel = saveTargetStrategy
        .execute(klassModel);
    return returnModel;
  }
  
  @Override
  protected IListModel<IContentTypeIdsInfoModel> getKlassInstancesByKlassAndTaxonomyIds(
      ITypesListModel klassAndTaxonomyListModel, String type) throws Exception
  {
    /*
      IListModel<IContentTypeIdsInfoModel> contentKlassIdsAndTaxonomysModel = null;
      if (type.equals(Constants.MARKET_KLASS_TYPE)) {
        contentKlassIdsAndTaxonomysModel = getMarketInstancesByKlassAndTaxonomyIdsStrategy
            .execute(klassAndTaxonomyListModel);
      }
      return contentKlassIdsAndTaxonomysModel;
    */
    return null;
  }
}
