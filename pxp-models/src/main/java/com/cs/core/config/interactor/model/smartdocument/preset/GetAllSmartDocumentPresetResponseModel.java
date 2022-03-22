package com.cs.core.config.interactor.model.smartdocument.preset;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetAllSmartDocumentPresetResponseModel
    implements IGetAllSmartDocumentPresetResponseModel {
  
  private static final long         serialVersionUID = 1L;
  protected List<IIdLabelCodeModel> list;
  protected Long                    count;
  
  @Override
  public List<IIdLabelCodeModel> getList()
  {
    return list;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setList(List<IIdLabelCodeModel> list)
  {
    this.list = list;
  }
  
  @Override
  public Long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
}
