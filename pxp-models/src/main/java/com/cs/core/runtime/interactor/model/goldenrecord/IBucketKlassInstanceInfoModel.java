package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IContentIdentifierModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IBucketKlassInstanceInfoModel extends IModel {
  
  public static final String ID                        = "id";
  public static final String DEFAULT_ASSET_INSTANCE_ID = "defaultAssetInstanceId";
  public static final String NAME                      = "name";
  public static final String TAXONOMY_IDS              = "taxonomyIds";
  public static final String TYPES                     = "types";
  public static final String PARTNER_SOURCE            = "partnerSource";
  public static final String BRANCH_OF_LABEL           = "branchOfLabel";
  public static final String VARIANT_OF_LABEL          = "variantOfLabel";
  
  public String getId();
  
  public void setId(String id);
  
  public String getDefaultAssetInstanceId();
  
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId);
  
  public String getName();
  
  public void setName(String name);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
  
  public IContentIdentifierModel getPartnerSource();
  
  public void setPartnerSource(IContentIdentifierModel partnerSource);
  
  public String getBranchOfLabel();
  
  public void setBranchOfLabel(String branchOfLabel);

  public String getVariantOfLabel();
  public void setVariantOfLabel(String variantOfLabel);
}
