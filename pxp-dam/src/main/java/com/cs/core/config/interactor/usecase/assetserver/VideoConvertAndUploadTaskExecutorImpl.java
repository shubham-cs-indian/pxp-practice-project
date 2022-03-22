package com.cs.core.config.interactor.usecase.assetserver;

import com.cs.core.config.interactor.model.asset.IAssetUploadDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class VideoConvertAndUploadTaskExecutorImpl {
  
  @Autowired
  ThreadPoolTaskExecutor taskExecutor;
  
  @Autowired
  ApplicationContext     applicationContext;
  
  public void videoConvertAndUpload(String videoSourcePath, IAssetUploadDataModel uploadDataModel)
  {
    VideoConvertAndUploadTask task = applicationContext.getBean(VideoConvertAndUploadTask.class,
        videoSourcePath, uploadDataModel);
    taskExecutor.execute(task);
  }
}
