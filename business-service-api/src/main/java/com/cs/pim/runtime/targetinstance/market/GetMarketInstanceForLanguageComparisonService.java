package com.cs.pim.runtime.targetinstance.market;

import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceForLanguageComparisonRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.ILanguageComparisonResponseModel;
import com.cs.core.runtime.klassinstance.AbstractGetInstanceForLanguageComparisonService;
import org.springframework.stereotype.Service;

@Service
public class GetMarketInstanceForLanguageComparisonService extends AbstractGetInstanceForLanguageComparisonService<IGetInstanceForLanguageComparisonRequestModel, ILanguageComparisonResponseModel>
    implements IGetMarketInstanceForLanguageComparisonService {
  
  @Override
  protected ILanguageComparisonResponseModel executeInternal(
      IGetInstanceForLanguageComparisonRequestModel klassInstancesModel) throws Exception
  {
    ILanguageComparisonResponseModel response = null;
    try {
      response = super.executeInternal(klassInstancesModel);
    }
    catch (KlassNotFoundException e) {
      throw new ArticleKlassNotFoundException(e);
    }
    return response;
  }
}
