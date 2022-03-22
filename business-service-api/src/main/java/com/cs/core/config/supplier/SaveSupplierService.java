package com.cs.core.config.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.klass.ITypesListModel;
import com.cs.core.config.klass.AbstractSaveKlassService;
import com.cs.core.config.strategy.usecase.supplier.ISaveSupplierStrategy;

@Service
public class SaveSupplierService extends AbstractSaveKlassService<IKlassSaveModel, IGetKlassEntityWithoutKPModel>
    implements ISaveSupplierService {
  
  @Autowired
  ISaveSupplierStrategy saveSupplierStrategy;
  
  /*@Autowired
  IGetKlassInstancesByKlassAndTaxonomyIdsStrategy getSupplierInstancesByKlassAndTaxonomyIdsStrategy;*/
  
  @Override
  public IGetKlassEntityWithoutKPModel execute(IKlassSaveModel klassModel) throws Exception
  {
    return super.execute(klassModel);
  }
  
  @Override
  protected IGetKlassEntityWithoutKPStrategyResponseModel executeSaveKlass(
      IKlassSaveModel klassModel) throws Exception
  {
    return saveSupplierStrategy.execute(klassModel);
  }
  
  @Override
  protected IListModel<IContentTypeIdsInfoModel> getKlassInstancesByKlassAndTaxonomyIds(
      ITypesListModel klassAndTaxonomyListModel, String type) throws Exception
  {
    /*
      IListModel<IContentTypeIdsInfoModel> contentKlassIdsAndTaxonomysModel = getSupplierInstancesByKlassAndTaxonomyIdsStrategy
          .execute(klassAndTaxonomyListModel);
      return contentKlassIdsAndTaxonomysModel;
    */
    return null;
  }
}
