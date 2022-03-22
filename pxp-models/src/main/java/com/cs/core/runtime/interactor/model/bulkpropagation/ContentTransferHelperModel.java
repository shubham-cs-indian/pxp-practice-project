package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ContentTransferHelperModel implements IContentTransferHelperModel {
  
  private static final long      serialVersionUID = 1L;
  protected String               id;
  protected String               baseType;
  protected List<IIdAndBaseType> contentsToTransfer;
  
  @Override
  public List<IIdAndBaseType> getContentsToTransfer()
  {
    if (contentsToTransfer == null) {
      contentsToTransfer = new ArrayList<>();
    }
    return contentsToTransfer;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndBaseType.class)
  public void setContentsToTransfer(List<IIdAndBaseType> contentsToTransfer)
  {
    this.contentsToTransfer = contentsToTransfer;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
}
