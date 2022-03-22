package com.cs.core.config.strategy.usecase.swift;

import com.cs.core.config.interactor.model.assetstatus.AssetUploadStatusResultModel;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusCheckModel;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusModel;
import com.cs.core.config.interactor.model.assetstatus.IAssetUploadStatusResultModel;
import org.springframework.stereotype.Component;

@Component
public class GetAssetUploadStatusStrategy implements IGetAssetUploadStatusStrategy {
  
  /*@Autowired
  IElasticGetAssetUploadStatusStrategy getAssetUploadStatusStrategy;*/
  
  @Override
  public IAssetUploadStatusResultModel execute(IAssetUploadStatusCheckModel model) throws Exception
  {
    IAssetUploadStatusResultModel returnModel = new AssetUploadStatusResultModel();
    IAssetUploadStatusModel statusResponse = null; /*getAssetUploadStatusStrategy.execute(model)*/
    ;
    if (statusResponse == null) {
      returnModel.setStatus(100);
      return returnModel;
    }
    if (statusResponse != null && statusResponse.getStatus()
        .equalsIgnoreCase("Failed")) {
      throw new Exception(
          "Asset Upload Failed. Container = " + model.getContainer() + ", Key = " + model.getKey());
    }
    /*else if(statusResponse.getStatus().equalsIgnoreCase("InProgress"))
    {
      throw new AssetUploadInProgressException(statusResponse.getProgress());
    } */
    returnModel.setStatus(Integer.parseInt(statusResponse.getProgress()));
    return returnModel;
  }
}
