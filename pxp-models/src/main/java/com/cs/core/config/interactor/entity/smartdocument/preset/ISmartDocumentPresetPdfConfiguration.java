package com.cs.core.config.interactor.entity.smartdocument.preset;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface ISmartDocumentPresetPdfConfiguration extends IEntity {
  
  public static final String PDF_TITLE              = "pdfTitle";
  public static final String PDF_SUBJECT            = "pdfSubject";
  public static final String PDF_AUTHOR             = "pdfAuthor";
  public static final String PDF_KEYWORDS           = "pdfKeywords";
  public static final String PDF_USER_PASSWORD      = "pdfUserPassword";
  public static final String PDF_OWNER_PASSWORD     = "pdfOwnerPassword";
  public static final String PDF_ALLOW_PRINTING     = "pdfAllowPrinting";
  public static final String PDF_ALLOW_COPY_CONTENT = "pdfAllowCopyContent";
  public static final String PDF_ALLOW_MODIFICATION = "pdfAllowModifications";
  public static final String PDF_ALLOW_ANNOTATIONS  = "pdfAllowAnnotations";
  
  public String getPdfTitle();
  
  public void setPdfTitle(String pdfTitle);
  
  public String getPdfSubject();
  
  public void setPdfSubject(String pdfSubject);
  
  public String getPdfAuthor();
  
  public void setPdfAuthor(String pdfAuthor);
  
  public String getPdfKeywords();
  
  public void setPdfKeywords(String pdfKeywords);
  
  public String getPdfUserPassword();
  
  public void setPdfUserPassword(String pdfUserPassword);
  
  public String getPdfOwnerPassword();
  
  public void setPdfOwnerPassword(String pdfOwnerPassword);
  
  public Boolean getPdfAllowPrinting();
  
  public void setPdfAllowPrinting(Boolean pdfAllowPrinting);
  
  public Boolean getPdfAllowCopyContent();
  
  public void setPdfAllowCopyContent(Boolean pdfAllowCopyContent);
  
  public Boolean getPdfAllowModifications();
  
  public void setPdfAllowModifications(Boolean pdfAllowModifications);
  
  public Boolean getPdfAllowAnnotations();
  
  public void setPdfAllowAnnotations(Boolean pdfAllowAnnotations);
  
  public String getPdfColorSpace();

  public void setPdfColorSpace(String pdfColorSpace);
  
  public String getPdfMarksBleeds();

  public void setPdfMarksBleeds(String pdfMarksBleeds);
}
