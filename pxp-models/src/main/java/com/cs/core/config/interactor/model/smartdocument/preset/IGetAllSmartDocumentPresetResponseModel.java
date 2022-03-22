package com.cs.core.config.interactor.model.smartdocument.preset;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetAllSmartDocumentPresetResponseModel extends IModel {
  
  public static final String LIST  = "list";
  public static final String COUNT = "count";
  
  public List<IIdLabelCodeModel> getList();
  
  public void setList(List<IIdLabelCodeModel> list);
  
  public Long getCount();
  
  public void setCount(Long count);
}
