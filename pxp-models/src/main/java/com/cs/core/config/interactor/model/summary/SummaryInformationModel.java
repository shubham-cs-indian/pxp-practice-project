package com.cs.core.config.interactor.model.summary;

public class SummaryInformationModel implements ISummaryInformationModel {
  
  protected String id;
  protected String label;

  //value constructor
  public SummaryInformationModel(String id, String label)
  {
    this.id = id;
    this.label = label;
  }

  //default constructor
  public SummaryInformationModel()
  {

  }

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
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
}
