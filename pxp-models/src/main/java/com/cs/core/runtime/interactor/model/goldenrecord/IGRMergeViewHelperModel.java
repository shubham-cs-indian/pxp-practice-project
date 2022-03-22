package com.cs.core.runtime.interactor.model.goldenrecord;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGRMergeViewHelperModel extends IModel {
  
  String SUPPLIER_TAGS                       = "supplierTags";
  String SUPPLIER_ATTRIBUTES                 = "supplierAttributes";
  String SUPPLIER_DEPENDENT_ATTRIBUTES       = "supplierDependentAttributes";
  
  String LAST_MODIFIED_TAGS                  = "lastModifiedTags";
  String LAST_MODIFIED_ATTRIBUTES            = "lastModifiedAttributes";
  String LAST_MODIFIED_DEPENDENT_ATTRIBUTES  = "lastModifiedDependentAttributes";
  
  String DEPENDENT_ATTRIBUTES_RECOMMENDATION = "dependentAttributesRecommendation";
  
  void setSupplierTags(Map<String, List<ITagRecommendationModel>> supplierTags);
  Map<String, List<ITagRecommendationModel>> getSupplierTags();
  
  void setSupplierAttributes(Map<String, List<IAttributeRecommendationModel>> supplierAttributes);
  Map<String, List<IAttributeRecommendationModel>> getSupplierAttributes();
  
  void setSupplierDependentAttributes(Map<String, Map<String, List<IAttributeRecommendationModel>>> supplierDependentAttributes);
  Map<String, Map<String, List<IAttributeRecommendationModel>>> getSupplierDependentAttributes();
  
  void setLastModifiedTags(Map<String, List<ITagRecommendationModel>> lastModifiedTags);
  Map<String, List<ITagRecommendationModel>> getLastModifiedTags();
  
  void setLastModifiedAttributes(Map<String, List<IAttributeRecommendationModel>> lastModifiedAttributes);
  Map<String, List<IAttributeRecommendationModel>> getLastModifiedAttributes();
  
  void setLastModifiedDependentAttributes(Map<String, Map<String, List<IAttributeRecommendationModel>>> lastModifiedDependentAttributes);
  Map<String, Map<String, List<IAttributeRecommendationModel>>> getLastModifiedDependentAttributes();
  
  void setDependentAttributesRecommendation(Map<String, Map<String, IRecommendationModel>> dependentAttributesRecommendation);
  Map<String, Map<String, IRecommendationModel>> getDependentAttributesRecommendation();
   
}
