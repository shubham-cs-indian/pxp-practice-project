package com.cs.dam.runtime.interactor.version;

import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForComparisonModel;
import com.cs.core.runtime.interactor.model.version.IKlassInstanceVersionsComparisonModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.IGetAssetInstanceVersionsForComparisonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAssetInstanceVersionsForComparison extends AbstractRuntimeInteractor<IKlassInstanceVersionsComparisonModel, IGetKlassInstanceVersionsForComparisonModel>
    implements IGetAssetInstanceVersionsForComparison {
  
  @Autowired
  protected IGetAssetInstanceVersionsForComparisonService getAssetInstanceVersionsForComparisonService;
  
  @Override
  public IGetKlassInstanceVersionsForComparisonModel executeInternal(
      IKlassInstanceVersionsComparisonModel dataModel) throws Exception
  {
    return getAssetInstanceVersionsForComparisonService.execute(dataModel);
  }
}
