package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGenerateSmartDocumentSuccessModel extends IModel {
  
  public static final String PDF_BYTES    = "pdfBytes";
  public static final String SHOW_PREVIEW = "showPreview";
  
  public List<byte[]> getPdfBytes();
  
  public void setPdfBytes(List<byte[]> pdfBytes);
  
  public Boolean getShowPreview();
  
  public void setShowPreview(Boolean showPreview);
}
