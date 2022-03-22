package com.cs.core.config.interactor.usecase.assetserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.asset.IAssetUploadDataModel;

@Service
public class AssetUploadTaskExecutorImpl {
  
  @Autowired
  ThreadPoolTaskExecutor taskExecutor;
  
  @Autowired
  ApplicationContext     applicationContext;
  
  public void uploadAsset(String sourcePath, IAssetUploadDataModel uploadDataModel)
  {
    AssetUploadTask assetUploadTask = applicationContext.getBean(AssetUploadTask.class, sourcePath,
        uploadDataModel);
    // assetUploadTask.setAssetUploadDataModel(uploadDataModel);
    taskExecutor.execute(assetUploadTask);
  }
}
