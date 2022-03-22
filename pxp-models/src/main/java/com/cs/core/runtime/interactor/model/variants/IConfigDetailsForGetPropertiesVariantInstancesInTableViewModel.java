package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.runtime.interactor.model.configdetails.IBaseKlassTemplateConfigDetails;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;

import java.util.List;

public interface IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel
    extends IBaseKlassTemplateConfigDetails {
  
  public static String       FILTER_INFO                 = "filterInfo";
  public static final String REFERENCED_VARIANT_CONTEXTS = "referencedVariantContexts";
  public static final String VERSIONABLE_ATTRIBUTES      = "versionableAttributes";
  public static final String VERSIONABLE_TAGS            = "versionableTags";
  public static final String MANDATORY_ATTRIBUTE_IDS     = "mandatoryAttributeIds";
  public static final String MANDATORY_TAG_IDS           = "mandatoryTagIds";
  public static final String SHOULD_ATTRIBUTE_IDS        = "shouldAttributeIds";
  public static final String SHOULD_TAG_IDS              = "shouldTagIds";
  
  public IGetFilterInfoModel getFilterInfo();
  
  public void setFilterInfo(IGetFilterInfoModel filterInfo);
  
  public IReferencedContextModel getReferencedVariantContexts();
  
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts);
  
  public List<String> getVersionableAttributes();
  
  public void setVersionableAttributes(List<String> versionableAttributes);
  
  public List<String> getVersionableTags();
  
  public void setVersionableTags(List<String> versionableTags);
  
  public List<String> getMandatoryAttributeIds();
  
  public void setMandatoryAttributeIds(List<String> mandatoryAttributeIds);
  
  public List<String> getMandatoryTagIds();
  
  public void setMandatoryTagIds(List<String> mandatoryTagIds);
  
  public List<String> getShouldAttributeIds();
  
  public void setShouldAttributeIds(List<String> shouldAttributeIds);
  
  public List<String> getShouldTagIds();
  
  public void setShouldTagIds(List<String> shouldTagIds);
}
