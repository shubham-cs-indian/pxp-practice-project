package com.cs.dam.runtime.interactor.usecase.assetinstance.linksharing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetShareDialogInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.linksharing.IGetAssetShareLinkDialogInformationService;

/***
 * This service will return the information for the link sharing dialog.
 * 
 * @author vannya.kalani
 *
 */
@Service
public class GetAssetShareLinkDialogInformation extends
    AbstractRuntimeInteractor<IIdsListParameterModel, IAssetShareDialogInformationModel>
    implements IGetAssetShareLinkDialogInformation {
  
  @Autowired
  IGetAssetShareLinkDialogInformationService getAssetShareLinkDialogInformationService;

  @Override
  protected IAssetShareDialogInformationModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    return getAssetShareLinkDialogInformationService.execute(model);
  }
}
