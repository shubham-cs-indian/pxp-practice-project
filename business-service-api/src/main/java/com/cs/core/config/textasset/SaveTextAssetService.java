package com.cs.core.config.textasset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.klass.ITypesListModel;
import com.cs.core.config.klass.AbstractSaveKlassService;
import com.cs.core.config.strategy.usecase.textasset.ISaveTextAssetStrategy;

@Service
public class SaveTextAssetService extends AbstractSaveKlassService<IKlassSaveModel, IGetKlassEntityWithoutKPModel>
    implements ISaveTextAssetService {
  
  @Autowired
  ISaveTextAssetStrategy saveTextAssetStrategy;
  
  /* @Autowired
  IGetKlassInstancesByKlassAndTaxonomyIdsStrategy getTextAssetInstancesByKlassAndTaxonomyIdsStrategy;*/
  
  @Override
  public IGetKlassEntityWithoutKPModel execute(IKlassSaveModel klassModel) throws Exception
  {
    return super.execute(klassModel);
  }
  
  @Override
  protected IGetKlassEntityWithoutKPStrategyResponseModel executeSaveKlass(
      IKlassSaveModel klassModel) throws Exception
  {
    return saveTextAssetStrategy.execute(klassModel);
  }
  
  @Override
  protected IListModel<IContentTypeIdsInfoModel> getKlassInstancesByKlassAndTaxonomyIds(
      ITypesListModel klassAndTaxonomyListModel, String type) throws Exception
  {
    /*
      IListModel<IContentTypeIdsInfoModel> contentKlassIdsAndTaxonomysModel = getTextAssetInstancesByKlassAndTaxonomyIdsStrategy
          .execute(klassAndTaxonomyListModel);
      return contentKlassIdsAndTaxonomysModel;
    */
    return null;
  }
}
