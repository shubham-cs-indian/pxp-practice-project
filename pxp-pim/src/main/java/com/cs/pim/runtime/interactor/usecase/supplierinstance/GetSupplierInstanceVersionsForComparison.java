package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForComparisonModel;
import com.cs.core.runtime.interactor.model.version.IKlassInstanceVersionsComparisonModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.supplierinstance.IGetSupplierInstanceVersionsForComparisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetSupplierInstanceVersionsForComparison extends AbstractRuntimeInteractor<IKlassInstanceVersionsComparisonModel, IGetKlassInstanceVersionsForComparisonModel>
    implements IGetSupplierInstanceVersionsForComparison {
  
  @Autowired
  protected IGetSupplierInstanceVersionsForComparisonService getSupplierInstanceVersionsForComparisonService;
  
  @Override
  public IGetKlassInstanceVersionsForComparisonModel executeInternal(
      IKlassInstanceVersionsComparisonModel dataModel) throws Exception
  {
    
    return getSupplierInstanceVersionsForComparisonService.execute(dataModel);
  }

}
