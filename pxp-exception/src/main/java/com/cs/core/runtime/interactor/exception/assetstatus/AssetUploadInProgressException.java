package com.cs.core.runtime.interactor.exception.assetstatus;

import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.model.pluginexception.DevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IDevExceptionDetailModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionDetailModel;

public class AssetUploadInProgressException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  protected String          progress;
  
  public AssetUploadInProgressException()
  {
    super();
  }
  
  public AssetUploadInProgressException(String progress)
  {
    // super();
    // this.progress = progress;
    createDefaultExceptionDetailsAndDevExceptionDetailsModel(progress);
  }
  
  private void createDefaultExceptionDetailsAndDevExceptionDetailsModel(String progress)
  {
    IExceptionDetailModel exceptionDetail = new ExceptionDetailModel();
    exceptionDetail.setKey(this.getClass()
        .getSimpleName());
    this.getExceptionDetails()
        .add(exceptionDetail);
    
    IDevExceptionDetailModel devExceptionDetail = new DevExceptionDetailModel();
    devExceptionDetail.setKey(this.getClass()
        .getSimpleName());
    devExceptionDetail.setDetailMessage(progress);
    devExceptionDetail.setStack(this.getStackTrace());
    devExceptionDetail.setExceptionClass(this.getClass()
        .getName());
    this.getDevExceptionDetails()
        .add(devExceptionDetail);
  }
}
