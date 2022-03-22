package com.cs.core.config.interactor.model.datarule;

import java.util.List;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ICategoryTreeInformationModel extends IModel {
  
  public static final String CATEGORY_INFO = "categoryInfo";
  public static final String KLASSES_IDS   = "klassesIds";
  
  
  public List<IConfigEntityTreeInformationModel> getCategoryInfo();
  public void setCategoryInfo(List<IConfigEntityTreeInformationModel> categoryInfo);
  
  public List<String> getKlassesIds();
  
  public void setKlassesIds(List<String> klassesIds);
}
