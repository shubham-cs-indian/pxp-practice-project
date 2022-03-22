package com.cs.core.config.interactor.model.iconlibrary;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IUploadMultipleIconsRequestModel extends IModel{
  
  public static final String MULTI_PART_FILE_INFO_LIST = "multiPartFileInfoList";
  public static final String FILE_KEY_VS_CODE_MAP      = "fileKeyVsCodeMap";
  public static final String FILE_KEY_VS_NAME_MAP      = "fileKeyVsNameMap";
  
  public List<IMultiPartFileInfoModel> getMultiPartFileInfoList();
  public void setMultiPartFileInfoList(List<IMultiPartFileInfoModel> multiPartFileInfoList);
  
  public Map<String, Object> getFileKeyVsCodeMap();
  public void setFileKeyVsCodeMap(Map<String, Object> fileKeyVsCodeMap);
  
  public Map<String, Object> getFileKeyVsNameMap();
  public void setFileKeyVsNameMap(Map<String, Object> fileKeyVsNameMap);
  
}
