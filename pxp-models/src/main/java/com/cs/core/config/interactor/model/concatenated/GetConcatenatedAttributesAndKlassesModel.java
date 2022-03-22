package com.cs.core.config.interactor.model.concatenated;

import java.util.Map;

public class GetConcatenatedAttributesAndKlassesModel
    implements IGetConcatenatedAttributesAndKlassesModel {
  
  private static final long     serialVersionUID = 1L;
  protected Map<String, Object> concatenatedAttributes;
  protected Map<String, Object> klassesAndTaxonomiesHavingConcatenatedAttributes;
  protected Map<String, Object> referencedTags;
  
  @Override
  public Map<String, Object> getConcatenatedAttributes()
  {
    return concatenatedAttributes;
  }
  
  @Override
  public void setConcatenatedAttributes(Map<String, Object> concatenatedAttributes)
  {
    this.concatenatedAttributes = concatenatedAttributes;
  }
  
  @Override
  public Map<String, Object> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  public void setReferencedTags(Map<String, Object> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, Object> getKlassesAndTaxonomiesHavingConcatenatedAttributes()
  {
    return klassesAndTaxonomiesHavingConcatenatedAttributes;
  }
  
  @Override
  public void setKlassesAndTaxonomiesHavingConcatenatedAttributes(
      Map<String, Object> klassesHavingConcatenatedAttributes)
  {
    this.klassesAndTaxonomiesHavingConcatenatedAttributes = klassesHavingConcatenatedAttributes;
  }
}
