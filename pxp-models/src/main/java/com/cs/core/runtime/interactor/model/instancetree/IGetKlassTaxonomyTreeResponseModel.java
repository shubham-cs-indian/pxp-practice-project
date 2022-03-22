package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;

public interface IGetKlassTaxonomyTreeResponseModel extends IModel {
  
  public static final String NATURE_KLASSES      = "natureClasses";
  public static final String ATTRIBUTION_CLASSES = "attributionClasses";
  public static final String TAXONOMIES          = "taxonomies";
  public static final String CONFIG_DETAILS      = "configDetails";
  
  public List<ICategoryInformationModel> getNatureClasses();
  
  public void setNatureClasses(List<ICategoryInformationModel> natureClasses);
  
  public List<ICategoryInformationModel> getAttributionClasses();
  
  public void setAttributionClasses(List<ICategoryInformationModel> attributionClasses);
  
  public List<ICategoryInformationModel> getTaxonomies();
  
  public void setTaxonomies(List<ICategoryInformationModel> taxonomies);
  
  public IGetSelectedKlassesAndTaxonomiesByIdsResponseModel getConfigDetails();
  
  public void setConfigDetails(IGetSelectedKlassesAndTaxonomiesByIdsResponseModel configDetails);
  
  public List<ICategoryInformationModel> getListBySelectionType(String selection);
  
}
