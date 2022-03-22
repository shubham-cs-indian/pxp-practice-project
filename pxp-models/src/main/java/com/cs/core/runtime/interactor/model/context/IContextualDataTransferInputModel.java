package com.cs.core.runtime.interactor.model.context;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.transfer.IDataTransferInputModel;

import java.util.List;

public interface IContextualDataTransferInputModel extends IModel {
  
  public static final String DATA_TRANSFER                            = "dataTransfer";
  public static final String VARIANT_IDS_TO_EXCLUDE_FOR_DATA_TRANSFER = "variantIdsToExcludeForDataTransfer";
  
  public List<String> getVariantIdsToExcludeForDataTransfer();
  
  public void setvariantIdsToExcludeForDataTransfer(
      List<String> variantIdsToExcludeForDataTransfer);
  
  public IDataTransferInputModel getDataTransfer();
  
  public void setDataTransfer(IDataTransferInputModel dataTransfer);
}
