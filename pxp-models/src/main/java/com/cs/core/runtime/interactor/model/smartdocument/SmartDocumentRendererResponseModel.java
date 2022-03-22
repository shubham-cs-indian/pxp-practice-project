package com.cs.core.runtime.interactor.model.smartdocument;

public class SmartDocumentRendererResponseModel implements ISmartDocumentRendererResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected byte[]          pdfBytes;
  
  @Override
  public byte[] getPdfBytes()
  {
    return pdfBytes;
  }
  
  @Override
  public void setPdfBytes(byte[] pdfBytes)
  {
    this.pdfBytes = pdfBytes;
  }
}
