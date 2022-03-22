package com.cs.dam.config.strategy.usecase.assetinstance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.asset.IGetAllRenditionKlassesWithPermissionStrategy;
import com.cs.core.runtime.interactor.model.assetinstance.BulkDownloadConfigInformationResponseModel;
import com.cs.core.runtime.interactor.model.assetinstance.IBulkDownloadConfigInformationResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListWithUserModel;

@Component
public class OrientDBGetAllRenditionKlassesWithPermissionStrategy extends OrientDBBaseStrategy
    implements IGetAllRenditionKlassesWithPermissionStrategy {
  
  @Override
  public IBulkDownloadConfigInformationResponseModel execute(IIdsListWithUserModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IIdsListWithUserModel.IDS, model.getIds());
    requestMap.put(IIdsListWithUserModel.USER_ID, model.getUserId());
    return super.execute(OrientDBBaseStrategy.GET_ALL_RENDITION_KLASSES_WITH_PERMISSION, requestMap,
        BulkDownloadConfigInformationResponseModel.class);
  }
}
