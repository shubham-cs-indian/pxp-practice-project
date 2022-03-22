package com.cs.core.config.interactor.model.iconlibrary;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.config.interactor.model.asset.MultiPartFileInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class UploadMultipleIconsRequestModel implements IUploadMultipleIconsRequestModel {

  private static final long serialVersionUID = 1L;
  
  protected List<IMultiPartFileInfoModel> multiPartFileInfoList;
  protected Map<String, Object>           fileKeyVsCodeMap;
  protected Map<String, Object>           fileKeyVsNameMap;

  @Override
  public List<IMultiPartFileInfoModel> getMultiPartFileInfoList()
  {
    return multiPartFileInfoList;
  }

  @Override
  @JsonDeserialize(contentAs = MultiPartFileInfoModel.class)
  public void setMultiPartFileInfoList(List<IMultiPartFileInfoModel> multiPartFileInfoList)
  {
    this.multiPartFileInfoList = multiPartFileInfoList;
  }

  @Override
  public Map<String, Object> getFileKeyVsCodeMap()
  {
    return fileKeyVsCodeMap;
  }

  @Override
  public void setFileKeyVsCodeMap(Map<String, Object> fileKeyVsCodeMap)
  {
    this.fileKeyVsCodeMap = fileKeyVsCodeMap;
  }

  @Override
  public Map<String, Object> getFileKeyVsNameMap()
  {
    return fileKeyVsNameMap;
  }

  @Override
  public void setFileKeyVsNameMap(Map<String, Object> fileKeyVsNameMap)
  {
    this.fileKeyVsNameMap = fileKeyVsNameMap;
  }

}
