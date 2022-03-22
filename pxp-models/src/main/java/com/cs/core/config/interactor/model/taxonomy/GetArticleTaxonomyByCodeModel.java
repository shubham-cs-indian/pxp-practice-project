package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetArticleTaxonomyByCodeModel implements IGetArticleTaxonomyByCodeModel {
  
  private static final long                         serialVersionUID = 1L;
  protected String                                  code;
  protected List<IConfigEntityTreeInformationModel> taxonomy         = new ArrayList<>();
  
  @Override
  public String getCode()
  {
    
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public List<IConfigEntityTreeInformationModel> getTaxonomy()
  {
    
    return taxonomy;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityTreeInformationModel.class)
  public void setTaxonomy(List<IConfigEntityTreeInformationModel> taxonomy)
  {
    this.taxonomy = taxonomy;
  }
}
