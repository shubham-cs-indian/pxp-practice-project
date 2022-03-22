package com.cs.pim.runtime.supplierinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceForLanguageComparisonRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.ILanguageComparisonResponseModel;

public interface IGetSupplierInstanceForLanguageComparisonService extends
    IRuntimeService<IGetInstanceForLanguageComparisonRequestModel, ILanguageComparisonResponseModel> {

}
