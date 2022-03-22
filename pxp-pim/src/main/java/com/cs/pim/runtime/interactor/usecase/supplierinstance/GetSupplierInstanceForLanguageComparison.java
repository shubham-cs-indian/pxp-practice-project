package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceForLanguageComparisonRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.ILanguageComparisonResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.supplierinstance.IGetSupplierInstanceForLanguageComparisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetSupplierInstanceForLanguageComparison
    extends AbstractRuntimeInteractor<IGetInstanceForLanguageComparisonRequestModel, ILanguageComparisonResponseModel>
    implements IGetSupplierInstanceForLanguageComparison {

  @Autowired
  protected IGetSupplierInstanceForLanguageComparisonService getSupplierInstanceForLanguageComparisonService;

  @Override
  protected ILanguageComparisonResponseModel executeInternal(IGetInstanceForLanguageComparisonRequestModel klassInstancesModel)
      throws Exception
  {
    return getSupplierInstanceForLanguageComparisonService.execute(klassInstancesModel);
  }
}
