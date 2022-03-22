package com.cs.core.runtime.interactor.model.instancetree;

import java.util.HashMap;
import java.util.Map;

import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.klass.KlassInformationModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.ConfigTaxonomyHierarchyInformationModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.IConfigTaxonomyHierarchyInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetSelectedKlassesAndTaxonomiesByIdsResponseModel implements IGetSelectedKlassesAndTaxonomiesByIdsResponseModel {
  
  private static final long                                       serialVersionUID = 1L;
  protected Map<String, IKlassInformationModel>                   referencedKlasses;
  protected Map<String, IConfigTaxonomyHierarchyInformationModel> referencedTaxonomies;
  
  @Override
  public Map<String, IKlassInformationModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @JsonDeserialize(contentAs = KlassInformationModel.class)
  @Override
  public void setReferencedKlasses(Map<String, IKlassInformationModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  @Override
  public Map<String, IConfigTaxonomyHierarchyInformationModel> getReferencedTaxonomies()
  {
    if(referencedTaxonomies == null) {
      referencedTaxonomies = new HashMap<String, IConfigTaxonomyHierarchyInformationModel>();
    }
    return referencedTaxonomies;
  }
  
  @JsonDeserialize(contentAs = ConfigTaxonomyHierarchyInformationModel.class)
  @Override
  public void setReferencedTaxonomies(
      Map<String, IConfigTaxonomyHierarchyInformationModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
}
