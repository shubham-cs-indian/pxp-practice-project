package com.cs.core.config.interactor.entity.smartdocument.preset;

public class SmartDocumentPresetPdfConfiguration implements ISmartDocumentPresetPdfConfiguration {
  
  private static final long serialVersionUID      = 1L;
  protected String          pdfTitle;
  protected String          pdfSubject;
  protected String          pdfAuthor;
  protected String          pdfKeywords;
  protected String          pdfUserPassword;
  protected String          pdfOwnerPassword;
  protected Boolean         pdfAllowPrinting      = false;
  protected Boolean         pdfAllowCopyContent   = false;
  protected Boolean         pdfAllowModifications = false;
  protected Boolean         pdfAllowAnnotations   = false;
  protected Long            versionId;
  protected Long            versionTimestamp;
  protected String          lastModifiedBy;
  protected String          code;
  protected String          id;
  protected String          pdfColorSpace;
  protected String          pdfMarksBleeds;


  
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
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public String getPdfTitle()
  {
    return pdfTitle;
  }
  
  @Override
  public void setPdfTitle(String pdfTitle)
  {
    this.pdfTitle = pdfTitle;
  }
  
  @Override
  public String getPdfSubject()
  {
    return pdfSubject;
  }
  
  @Override
  public void setPdfSubject(String pdfSubject)
  {
    this.pdfSubject = pdfSubject;
  }
  
  @Override
  public String getPdfAuthor()
  {
    return pdfAuthor;
  }
  
  @Override
  public void setPdfAuthor(String pdfAuthor)
  {
    this.pdfAuthor = pdfAuthor;
  }
  
  @Override
  public String getPdfKeywords()
  {
    return pdfKeywords;
  }
  
  @Override
  public void setPdfKeywords(String pdfKeywords)
  {
    this.pdfKeywords = pdfKeywords;
  }
  
  @Override
  public String getPdfUserPassword()
  {
    return pdfUserPassword;
  }
  
  @Override
  public void setPdfUserPassword(String pdfUserPassword)
  {
    this.pdfUserPassword = pdfUserPassword;
  }
  
  @Override
  public String getPdfOwnerPassword()
  {
    return pdfOwnerPassword;
  }
  
  @Override
  public void setPdfOwnerPassword(String pdfOwnerPassword)
  {
    this.pdfOwnerPassword = pdfOwnerPassword;
  }
  
  @Override
  public Boolean getPdfAllowPrinting()
  {
    return pdfAllowPrinting;
  }
  
  @Override
  public void setPdfAllowPrinting(Boolean pdfAllowPrinting)
  {
    this.pdfAllowPrinting = pdfAllowPrinting;
  }
  
  @Override
  public Boolean getPdfAllowCopyContent()
  {
    return pdfAllowCopyContent;
  }
  
  @Override
  public void setPdfAllowCopyContent(Boolean pdfAllowCopyContent)
  {
    this.pdfAllowCopyContent = pdfAllowCopyContent;
  }
  
  @Override
  public Boolean getPdfAllowModifications()
  {
    return pdfAllowModifications;
  }
  
  @Override
  public void setPdfAllowModifications(Boolean pdfAllowModifications)
  {
    this.pdfAllowModifications = pdfAllowModifications;
  }
  
  @Override
  public Boolean getPdfAllowAnnotations()
  {
    return pdfAllowAnnotations;
  }
  
  @Override
  public void setPdfAllowAnnotations(Boolean pdfAllowAnnotations)
  {
    this.pdfAllowAnnotations = pdfAllowAnnotations;
  }
  
  @Override
  public String getPdfColorSpace()
  {
    return pdfColorSpace;
  }

  @Override
  public void setPdfColorSpace(String pdfColorSpace)
  {
    this.pdfColorSpace = pdfColorSpace;
  }
  
  @Override
  public String getPdfMarksBleeds()
  {
    return pdfMarksBleeds;
  }

  @Override
  public void setPdfMarksBleeds(String pdfMarksBleeds)
  {
    this.pdfMarksBleeds = pdfMarksBleeds;
  }
}
