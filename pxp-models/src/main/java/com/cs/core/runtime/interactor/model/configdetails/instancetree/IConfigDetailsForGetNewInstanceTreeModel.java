package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.List;

import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;
import com.cs.core.runtime.interactor.model.instancetree.INewApplicableFilterModel;

public interface IConfigDetailsForGetNewInstanceTreeModel extends IConfigDetailsForNewInstanceTreeModel {
  
  public static final String TAXONOMY_IDS_FOR_KPI        = "taxonomyIdsForKPI";
  public static final String KLASS_IDS_FOR_KPI           = "klassIdsForKPI";
  public static final String SIDE2_LINKED_VARIANT_KR_IDS = "side2LinkedVariantKrIds";
  public static final String LINKED_VARIANT_CODES        = "linkedVariantCodes";
  public static final String X_RAY_CONFIG_DETAILS        = "xrayConfigDetails";

  public List<String> getKlassIdsForKPI();
  public void setKlassIdsForKPI(List<String> klassIdsForKPI);
  
  public List<String> getTaxonomyIdsForKPI();
  public void setTaxonomyIdsForKPI(List<String> taxonomyIdsForKPI);
  
  public List<String> getSide2LinkedVariantKrIds();
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds);
  
  public void setFilterData(List<INewApplicableFilterModel> filterData); 
  public List<INewApplicableFilterModel> getFilterData();

  public List<String> getLinkedVariantCodes();
  public void setLinkedVariantCodes(List<String> linkedVariantCodes);
  
  public IXRayConfigDetailsModel getXRayConfigDetails();
  public void setXRayConfigDetails(IXRayConfigDetailsModel xRayConfigDetails);
}
