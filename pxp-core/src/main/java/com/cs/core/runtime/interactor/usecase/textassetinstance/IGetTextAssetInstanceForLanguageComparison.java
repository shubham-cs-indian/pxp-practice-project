package com.cs.core.runtime.interactor.usecase.textassetinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceForLanguageComparisonRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.ILanguageComparisonResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetTextAssetInstanceForLanguageComparison extends
IRuntimeInteractor<IGetInstanceForLanguageComparisonRequestModel, ILanguageComparisonResponseModel> {

}
