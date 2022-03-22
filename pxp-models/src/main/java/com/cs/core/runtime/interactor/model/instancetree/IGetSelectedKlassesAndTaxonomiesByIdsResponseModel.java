package com.cs.core.runtime.interactor.model.instancetree;

import java.util.Map;

import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.IConfigTaxonomyHierarchyInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetSelectedKlassesAndTaxonomiesByIdsResponseModel extends IModel {
  
  public static final String REFERENCED_KLASSES    = "referencedKlasses";
  public static final String REFERENCED_TAXONOMIES = "referencedTaxonomies";
  
  public Map<String, IKlassInformationModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IKlassInformationModel> referencedKlasses);
  
  public Map<String, IConfigTaxonomyHierarchyInformationModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(Map<String, IConfigTaxonomyHierarchyInformationModel> referencedTaxonomies);
  
}
