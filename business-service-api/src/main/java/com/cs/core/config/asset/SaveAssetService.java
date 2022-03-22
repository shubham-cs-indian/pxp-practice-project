package com.cs.core.config.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.klass.ITypesListModel;
import com.cs.core.config.klass.AbstractSaveKlassService;
import com.cs.core.config.strategy.usecase.asset.ISaveAssetStrategy;

@Service
public class SaveAssetService extends AbstractSaveKlassService<IKlassSaveModel, IGetKlassEntityWithoutKPModel>
    implements ISaveAssetService {
  
  @Autowired
  ISaveAssetStrategy neo4jSaveAssetStrategy;
  
  /*@Autowired
  IGetKlassInstancesByKlassAndTaxonomyIdsStrategy getAssetInstancesByKlassAndTaxonomyIdsStrategy;*/
  
  @Override
  public IGetKlassEntityWithoutKPModel execute(IKlassSaveModel klassModel) throws Exception
  {
    return super.execute(klassModel);
  }
  
  @Override
  protected IGetKlassEntityWithoutKPStrategyResponseModel executeSaveKlass(
      IKlassSaveModel klassModel) throws Exception
  {
    IGetKlassEntityWithoutKPStrategyResponseModel execute = neo4jSaveAssetStrategy
        .execute(klassModel);
    return execute;
  }
  
  @Override
  protected IListModel<IContentTypeIdsInfoModel> getKlassInstancesByKlassAndTaxonomyIds(
      ITypesListModel klassAndTaxonomyListModel, String type) throws Exception
  {
    /*
      IListModel<IContentTypeIdsInfoModel> contentKlassIdsAndTaxonomysModel = getAssetInstancesByKlassAndTaxonomyIdsStrategy
          .execute(klassAndTaxonomyListModel);
      return contentKlassIdsAndTaxonomysModel;
    */
    return null;
  }
}
