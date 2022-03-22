package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.model.configdetails.CategoryInformationModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.instancetree.GetSelectedKlassesAndTaxonomiesByIdsResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetSelectedKlassesAndTaxonomiesByIdsResponseModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetKlassTaxonomyTreeResponseModel implements IGetKlassTaxonomyTreeResponseModel {
  
  private static final long                         serialVersionUID = 1L;
  
  protected List<ICategoryInformationModel>                    natureClasses;
  protected List<ICategoryInformationModel>                    attributionClasses;
  protected List<ICategoryInformationModel>                    taxonomies;
  protected IGetSelectedKlassesAndTaxonomiesByIdsResponseModel configDetails;
  
  @Override
  public List<ICategoryInformationModel> getNatureClasses()
  {
    if (natureClasses == null) {
      natureClasses = new ArrayList<>();
    }
    return natureClasses;
  }

  @Override
  @JsonDeserialize(contentAs = CategoryInformationModel.class)
  public void setNatureClasses(List<ICategoryInformationModel> natureClasses)
  {
    this.natureClasses = natureClasses;
  }

  @Override
  public List<ICategoryInformationModel> getAttributionClasses()
  {
    if (attributionClasses == null) {
      attributionClasses = new ArrayList<>();
    }
    return attributionClasses;
  }

  @Override
  @JsonDeserialize(contentAs = CategoryInformationModel.class)
  public void setAttributionClasses(List<ICategoryInformationModel> attributionClasses)
  {
    this.attributionClasses = attributionClasses;
  }

  @Override
  public List<ICategoryInformationModel> getTaxonomies()
  {
    if (taxonomies == null) {
      taxonomies = new ArrayList<>();
    }
    return taxonomies;
  }

  @Override
  @JsonDeserialize(contentAs = CategoryInformationModel.class)
  public void setTaxonomies(List<ICategoryInformationModel> taxonomies)
  {
    this.taxonomies = taxonomies;
  } 
  
  @Override
  public List<ICategoryInformationModel> getListBySelectionType(String selection){
    
    if(selection.equals("natureClasses")) {
      return getNatureClasses();
    }
    else if(selection.equals("attributionClasses")) {
      return getAttributionClasses();
    }
    else {
      return getTaxonomies();
    }
  }

  @Override
  public IGetSelectedKlassesAndTaxonomiesByIdsResponseModel getConfigDetails()
  {
    if(configDetails == null) {
      return new GetSelectedKlassesAndTaxonomiesByIdsResponseModel();
    }
    return configDetails;
  }

  @JsonDeserialize(as = GetSelectedKlassesAndTaxonomiesByIdsResponseModel.class)
  @Override
  public void setConfigDetails(IGetSelectedKlassesAndTaxonomiesByIdsResponseModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
}
