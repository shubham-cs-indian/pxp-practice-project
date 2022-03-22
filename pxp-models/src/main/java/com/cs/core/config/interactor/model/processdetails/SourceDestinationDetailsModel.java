package com.cs.core.config.interactor.model.processdetails;

public class SourceDestinationDetailsModel extends ProcessModel
    implements ISourceDestinationDetailsModel {
  
  private static final long serialVersionUID = 1L;
  protected String          docType;
  protected String          sourceId;
  protected String          destinationId;
  
  public String getDocType()
  {
    return docType;
  }
  
  public void setDocType(String docType)
  {
    this.docType = docType;
  }
  
  public String getSourceId()
  {
    return sourceId;
  }
  
  public void setSourceId(String sourceId)
  {
    this.sourceId = sourceId;
  }
  
  public String getDestinationId()
  {
    return destinationId;
  }
  
  public void setDestinationId(String destinationId)
  {
    this.destinationId = destinationId;
  }
}
