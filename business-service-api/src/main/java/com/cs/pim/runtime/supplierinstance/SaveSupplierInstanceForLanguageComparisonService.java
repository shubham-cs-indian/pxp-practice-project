package com.cs.pim.runtime.supplierinstance;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.supplierinstance.ISupplierInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;

@Service
public class SaveSupplierInstanceForLanguageComparisonService extends SaveSupplierInstanceForTabsService
    implements ISaveSupplierInstanceForLanguageComparisonService {
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ISupplierInstanceSaveModel klassInstancesModel)
      throws Exception
  {
    IGetKlassInstanceModel response = null;
    try {
      response = super.executeInternal(klassInstancesModel);
    }
    catch (KlassNotFoundException e) {
      throw new ArticleKlassNotFoundException(e);
    }
    return response;
  }
}
