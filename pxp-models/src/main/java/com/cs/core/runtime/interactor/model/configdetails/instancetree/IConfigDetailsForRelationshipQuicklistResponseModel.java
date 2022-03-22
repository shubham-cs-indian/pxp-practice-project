package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.List;

import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.runtime.interactor.model.instancetree.IAppliedSortModel;
import com.cs.core.runtime.interactor.model.instancetree.INewApplicableFilterModel;

public interface IConfigDetailsForRelationshipQuicklistResponseModel extends IConfigDetailsForNewInstanceTreeModel {
  
  public static final String TARGET_IDS                  = "targetIds";
  public static final String IS_TARGET_TAXONOMY          = "isTargetTaxonomy";
  public static final String SORT_DATA                   = "sortData";
  public static final String FILTER_DATA                 = "filterData";
  public static final String SIDE2_LINKED_VARIANT_KR_IDS = "side2LinkedVariantKrIds";
  public static final String RELATIONSHIP_CONFIG         = "relationshipConfig";
  public static final String LINKED_VARIANT_CODES        = "linkedVariantCodes";
  
  public List<String> getTargetIds();
  public void setTargetIds(List<String> targetIds);
  
  public Boolean getIsTargetTaxonomy();
  public void setIsTargetTaxonomy(Boolean isTargetTaxonomy);
  
  public List<IAppliedSortModel> getSortData();
  public void setSortData(List<IAppliedSortModel> sortData); 
  
  public List<INewApplicableFilterModel> getFilterData();
  public void setFilterData(List<INewApplicableFilterModel> filterData); 
  
  public List<String> getSide2LinkedVariantKrIds();
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds);
  
  public IRelationship getRelationshipConfig();
  public void setRelationshipConfig(IRelationship relationshipConfig);
  
  public List<String> getLinkedVariantCodes();
  public void setLinkedVariantCodes(List<String> linkedVariantCodes);
  
}
