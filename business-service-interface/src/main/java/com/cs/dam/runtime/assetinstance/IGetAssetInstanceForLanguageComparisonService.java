package com.cs.dam.runtime.assetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceForLanguageComparisonRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.ILanguageComparisonResponseModel;

public interface IGetAssetInstanceForLanguageComparisonService extends
    IRuntimeService<IGetInstanceForLanguageComparisonRequestModel, ILanguageComparisonResponseModel> {

}
