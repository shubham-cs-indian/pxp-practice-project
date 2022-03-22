package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;

import java.util.List;
import java.util.Map;

public interface IConfigDetailsForRelationshipPaginationModel
    extends IConfigDetailsForTaxonomyHierarchyModel {
  
  public static final String X_RAY_CONFIG_DETAILS = "xrayConfigDetails";
  public static final String RFERENCED_ELEMENTS   = "referencedElements";
  public static final String SIDE2_LINKED_VARIANT_KR_IDS   = "side2LinkedVariantKrIds";
  
  public IXRayConfigDetailsModel getXRayConfigDetails();
  
  public void setXRayConfigDetails(IXRayConfigDetailsModel xRayConfigDetails);
  
  // This referenced elements contains only relationship not attribute and tags.
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  public List<String> getSide2LinkedVariantKrIds();
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds);
}
