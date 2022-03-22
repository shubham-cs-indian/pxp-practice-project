package com.cs.pim.runtime.interactor.usecase.supplierinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceForLanguageComparisonRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.ILanguageComparisonResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetSupplierInstanceForLanguageComparison extends
IRuntimeInteractor<IGetInstanceForLanguageComparisonRequestModel, ILanguageComparisonResponseModel> {

}
