package com.cs.core.runtime.interactor.model.contextdatatransfer;

import com.cs.core.runtime.interactor.model.bulkpropagation.IContentTransferHelperModel;

public class ContetxtualDataTransferOnConfigChangeInputModel
    implements IContetxtualDataTransferOnConfigChangeInputModel {
  
  private static final long                            serialVersionUID = 1L;
  protected IContextInfoForContextualDataTransferModel contextInfoForContextualDataTransfer;
  protected IContentTransferHelperModel                contentTransferMapping;
  
  @Override
  public IContentTransferHelperModel getContentTransferMapping()
  {
    return contentTransferMapping;
  }
  
  @Override
  public void setContentTransferMapping(IContentTransferHelperModel contentTransferMapping)
  {
    this.contentTransferMapping = contentTransferMapping;
  }
  
  @Override
  public IContextInfoForContextualDataTransferModel getContextInfoForContextualDataTransfer()
  {
    return contextInfoForContextualDataTransfer;
  }
  
  @Override
  public void setContextInfoForContextualDataTransfer(
      IContextInfoForContextualDataTransferModel contextInfoForContextualDataTransfer)
  {
    this.contextInfoForContextualDataTransfer = contextInfoForContextualDataTransfer;
  }
}
