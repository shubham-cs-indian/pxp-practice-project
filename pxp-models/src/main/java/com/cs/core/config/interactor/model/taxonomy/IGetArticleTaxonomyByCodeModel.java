package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetArticleTaxonomyByCodeModel extends IModel {
  
  public static final String CODE     = "code";
  public static final String TAXONOMY = "taxonomy";
  
  public String getCode();
  
  public void setCode(String code);
  
  public List<IConfigEntityTreeInformationModel> getTaxonomy();
  
  public void setTaxonomy(List<IConfigEntityTreeInformationModel> taxonomy);
}
