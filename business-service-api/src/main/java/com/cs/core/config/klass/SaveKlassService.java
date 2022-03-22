package com.cs.core.config.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.klass.ITypesListModel;
import com.cs.core.config.strategy.usecase.klass.IGetKlassWithReferencedKlassesStrategy;
import com.cs.core.config.strategy.usecase.klass.ISaveKlassStrategy;

@Service
public class SaveKlassService extends AbstractSaveKlassService<IKlassSaveModel, IGetKlassEntityWithoutKPModel>
    implements ISaveKlassService {
  
  @Autowired
  protected ISaveKlassStrategy                     saveKlassStrategy;
  
  @Autowired
  protected IGetKlassWithReferencedKlassesStrategy orientGetKlassWithReferencedKlassesStrategy;
  
  /*@Autowired
  protected IGetKlassInstancesByKlassAndTaxonomyIdsStrategy getArticleInstancesByKlassAndTaxonomyIdsStrategy;
  
  @Autowired
  protected IDeleteRelationshipInstancesStrategy            deleteRelationshipInstancesStrategy;*/
  
  @Override
  public IGetKlassEntityWithoutKPModel execute(IKlassSaveModel klassModel) throws Exception
  {
    return super.execute(klassModel);
  }
  
  @Override
  protected IGetKlassEntityWithoutKPStrategyResponseModel executeSaveKlass(
      IKlassSaveModel klassModel) throws Exception
  {
    IGetKlassEntityWithoutKPStrategyResponseModel response = saveKlassStrategy.execute(klassModel);
    
    return response;
  }
  
  @Override
  protected IListModel<IContentTypeIdsInfoModel> getKlassInstancesByKlassAndTaxonomyIds(
      ITypesListModel klassAndTaxonomyListModel, String type) throws Exception
  {
    /*
      IListModel<IContentTypeIdsInfoModel> contentKlassIdsAndTaxonomysModel = getArticleInstancesByKlassAndTaxonomyIdsStrategy
          .execute(klassAndTaxonomyListModel);
      return contentKlassIdsAndTaxonomysModel;
    */
    return null;
  }
}
