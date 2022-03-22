package com.cs.core.config.interactor.model.concatenated;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetConcatenatedAttributesAndKlassesModel extends IModel {
  
  public static String CONCATENATED_ATTRIBUTES                               = "concatenatedAttributes";
  public static String KLASSES_AND_TAXONOMIES_HAVING_CONCATENATED_ATTRIBUTES = "klassesAndTaxonomiesHavingConcatenatedAttributes";
  public static String REFERENCED_TAGS                                       = "referencedTags";
  
  public Map<String, Object> getConcatenatedAttributes();
  
  public void setConcatenatedAttributes(Map<String, Object> concatenatedAttributes);
  
  public Map<String, Object> getKlassesAndTaxonomiesHavingConcatenatedAttributes();
  
  public void setKlassesAndTaxonomiesHavingConcatenatedAttributes(
      Map<String, Object> klassesAndTaxonomiesHavingConcatenatedAttributes);
  
  public Map<String, Object> getReferencedTags();
  
  public void setReferencedTags(Map<String, Object> referencedTags);
}
