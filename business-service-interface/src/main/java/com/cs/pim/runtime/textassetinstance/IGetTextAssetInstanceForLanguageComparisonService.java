package com.cs.pim.runtime.textassetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceForLanguageComparisonRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.ILanguageComparisonResponseModel;

public interface IGetTextAssetInstanceForLanguageComparisonService extends
    IRuntimeService<IGetInstanceForLanguageComparisonRequestModel, ILanguageComparisonResponseModel> {

}
