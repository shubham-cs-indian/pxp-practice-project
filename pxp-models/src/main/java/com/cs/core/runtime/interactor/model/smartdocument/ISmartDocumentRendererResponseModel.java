package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISmartDocumentRendererResponseModel extends IModel {
  
  public static final String PDF_BYTES = "pdfBytes";
  
  public byte[] getPdfBytes();
  
  public void setPdfBytes(byte[] pdfBytes);
}
