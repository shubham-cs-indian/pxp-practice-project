package com.cs.core.runtime.interactor.model.variants;

import java.util.Map;

import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.runtime.interactor.model.configdetails.IBaseKlassTemplateConfigDetails;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;

public interface IConfigDetailsForGetVariantInstancesInTableViewModel
    extends IBaseKlassTemplateConfigDetails {
  
  public static String       FILTER_INFO                 = "filterInfo";
  public static final String REFERENCED_VARIANT_CONTEXTS = "referencedVariantContexts";
  String                     INSTANCE_ID_VS_REFERENCED_ELEMENTS = "instanceIdVsReferencedElements";
  String                     REFERENCED_TAXONOMIES              = "referencedTaxonomies";
  
  public IGetFilterInfoModel getFilterInfo();
  
  public void setFilterInfo(IGetFilterInfoModel filterInfo);
  
  public IReferencedContextModel getReferencedVariantContexts();
  
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts);
  
  Map<String, Map<String, IReferencedSectionElementModel>> getInstanceIdVsReferencedElements();
  void setInstanceIdVsReferencedElements(Map<String, Map<String, IReferencedSectionElementModel>> instanceIdVsReferencedElements);
  
  Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  void setReferencedTaxonomies(Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
}
