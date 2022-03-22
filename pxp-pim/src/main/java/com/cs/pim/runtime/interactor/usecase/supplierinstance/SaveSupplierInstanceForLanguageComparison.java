package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.supplierinstance.ISupplierInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.supplierinstance.ISaveSupplierInstanceForLanguageComparisonService;

@Service
public class SaveSupplierInstanceForLanguageComparison extends AbstractRuntimeInteractor<ISupplierInstanceSaveModel, IGetKlassInstanceModel>
    implements ISaveSupplierInstanceForLanguageComparison {
  
  @Autowired
  protected ISaveSupplierInstanceForLanguageComparisonService saveSupplierInstanceForLanguageComparisonService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ISupplierInstanceSaveModel klassInstancesModel) throws Exception
  {
    
    return saveSupplierInstanceForLanguageComparisonService.execute(klassInstancesModel);
  }
}
