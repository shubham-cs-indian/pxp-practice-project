package com.cs.pim.runtime.strategy.usecase.textassetinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceForLanguageComparisonRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.ILanguageComparisonResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.textassetinstance.IGetTextAssetInstanceForLanguageComparison;
import com.cs.pim.runtime.textassetinstance.IGetTextAssetInstanceForLanguageComparisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTextAssetInstanceForLanguageComparison extends AbstractRuntimeInteractor<IGetInstanceForLanguageComparisonRequestModel, ILanguageComparisonResponseModel>
    implements IGetTextAssetInstanceForLanguageComparison {

  @Autowired
  protected IGetTextAssetInstanceForLanguageComparisonService getTextAssetInstanceForLanguageComparisonService;

  @Override
  protected ILanguageComparisonResponseModel executeInternal(
      IGetInstanceForLanguageComparisonRequestModel klassInstancesModel) throws Exception
  {
      return getTextAssetInstanceForLanguageComparisonService.execute(klassInstancesModel);
  }
}
