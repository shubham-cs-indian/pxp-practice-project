package com.cs.core.runtime.interactor.model.smartdocument;

import java.util.ArrayList;
import java.util.List;

public class GenerateSmartDocumentSuccessModel implements IGenerateSmartDocumentSuccessModel {
  
  private static final long serialVersionUID = 1L;
  protected List<byte[]>    pdfBytes         = new ArrayList<>();
  protected Boolean         showPreview      = false;
  
  @Override
  public List<byte[]> getPdfBytes()
  {
    return pdfBytes;
  }
  
  @Override
  public void setPdfBytes(List<byte[]> pdfBytes)
  {
    this.pdfBytes = pdfBytes;
  }
  
  @Override
  public Boolean getShowPreview()
  {
    return showPreview;
  }
  
  @Override
  public void setShowPreview(Boolean showPreview)
  {
    this.showPreview = showPreview;
  }
}
