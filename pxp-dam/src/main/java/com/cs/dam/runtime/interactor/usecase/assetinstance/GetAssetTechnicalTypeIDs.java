package com.cs.dam.runtime.interactor.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetDownloadWithVariantsModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.IGetAssetTechnicalTypeIDsService;

@Service
public class GetAssetTechnicalTypeIDs extends
    AbstractRuntimeInteractor<IIdsListParameterModel, IBulkAssetDownloadWithVariantsModel>
    implements IGetAssetTechnicalTypeIDs {
  
  @Autowired
  protected IGetAssetTechnicalTypeIDsService getAssetTechnicalTypeIDsService;
  
  @Override
  public IBulkAssetDownloadWithVariantsModel executeInternal(
      IIdsListParameterModel dataModel) throws Exception
  {
    return getAssetTechnicalTypeIDsService.execute(dataModel);
  }

}
