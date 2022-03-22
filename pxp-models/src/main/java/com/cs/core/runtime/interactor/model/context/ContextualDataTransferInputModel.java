package com.cs.core.runtime.interactor.model.context;

import com.cs.core.runtime.interactor.model.transfer.IDataTransferInputModel;

import java.util.ArrayList;
import java.util.List;

public class ContextualDataTransferInputModel implements IContextualDataTransferInputModel {
  
  private static final long         serialVersionUID = 1L;
  protected List<String>            variantIdsToExcludeForDataTransfer;
  protected IDataTransferInputModel dataTransfer;
  
  @Override
  public List<String> getVariantIdsToExcludeForDataTransfer()
  {
    if (variantIdsToExcludeForDataTransfer == null) {
      variantIdsToExcludeForDataTransfer = new ArrayList<>();
    }
    return null;
  }
  
  @Override
  public void setvariantIdsToExcludeForDataTransfer(List<String> variantIdsToExcludeForDataTransfer)
  {
    this.variantIdsToExcludeForDataTransfer = variantIdsToExcludeForDataTransfer;
  }
  
  @Override
  public IDataTransferInputModel getDataTransfer()
  {
    return dataTransfer;
  }
  
  @Override
  public void setDataTransfer(IDataTransferInputModel dataTransfer)
  {
    this.dataTransfer = dataTransfer;
  }
}
