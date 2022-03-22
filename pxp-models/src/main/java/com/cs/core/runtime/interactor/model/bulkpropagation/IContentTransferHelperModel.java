package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IContentTransferHelperModel extends IModel {
  
  public static final String ID                   = "id";
  public static final String BASE_TYPE            = "baseType";
  public static final String CONTENTS_TO_TRANSFER = "contentsToTransfer";
  
  public String getId();
  
  public void setId(String id);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<IIdAndBaseType> getContentsToTransfer();
  
  public void setContentsToTransfer(List<IIdAndBaseType> contentsToTransfer);
}
