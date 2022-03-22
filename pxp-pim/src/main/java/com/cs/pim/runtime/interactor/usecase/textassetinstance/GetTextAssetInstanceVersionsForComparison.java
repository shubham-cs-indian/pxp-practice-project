package com.cs.pim.runtime.interactor.usecase.textassetinstance;

import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForComparisonModel;
import com.cs.core.runtime.interactor.model.version.IKlassInstanceVersionsComparisonModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.textassetinstance.IGetTextAssetInstanceVersionsForComparisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetTextAssetInstanceVersionsForComparison extends AbstractRuntimeInteractor<IKlassInstanceVersionsComparisonModel, IGetKlassInstanceVersionsForComparisonModel>
    implements IGetTextAssetInstanceVersionsForComparison {
  
  @Autowired
  protected IGetTextAssetInstanceVersionsForComparisonService getTextAssetInstanceVersionsForComparisonService;
  
  @Override
  public IGetKlassInstanceVersionsForComparisonModel executeInternal(
      IKlassInstanceVersionsComparisonModel dataModel) throws Exception
  {
    return getTextAssetInstanceVersionsForComparisonService.execute(dataModel);
  }

}
