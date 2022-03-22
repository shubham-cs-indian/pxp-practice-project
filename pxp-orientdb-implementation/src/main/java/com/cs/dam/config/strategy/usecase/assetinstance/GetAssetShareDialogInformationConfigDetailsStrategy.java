package com.cs.dam.config.strategy.usecase.assetinstance;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.asset.IGetAssetShareDialogInformationConfigDetailsStrategy;
import com.cs.core.runtime.interactor.model.assetinstance.AssetShareDialogInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetShareDialogInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListWithUserModel;

@Component
public class GetAssetShareDialogInformationConfigDetailsStrategy extends OrientDBBaseStrategy
    implements IGetAssetShareDialogInformationConfigDetailsStrategy {
  
  @Override
  public IAssetShareDialogInformationModel execute(IIdsListWithUserModel model)
      throws Exception
  {
    return super.execute(OrientDBBaseStrategy.GET_ASSET_SHARE_DIALOG_INFORMATION, model, AssetShareDialogInformationModel.class);
  }
  
}
