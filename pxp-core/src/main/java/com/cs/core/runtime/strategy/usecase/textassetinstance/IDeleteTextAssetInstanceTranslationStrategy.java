package com.cs.core.runtime.strategy.usecase.textassetinstance;

import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationResponseModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IDeleteTextAssetInstanceTranslationStrategy extends IRuntimeStrategy<IDeleteTranslationRequestModel, IDeleteTranslationResponseModel>{
  
}
