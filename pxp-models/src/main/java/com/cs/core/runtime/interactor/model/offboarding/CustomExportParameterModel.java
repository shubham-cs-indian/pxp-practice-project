package com.cs.core.runtime.interactor.model.offboarding;

import java.util.*;

public class CustomExportParameterModel implements ICustomExportParameterModel {
  
  private static final long serialVersionUID          = 1L;
  Map<String, String>       attributeMapValues        = new HashMap<>();;
  Map<String, String>       tagMapValues              = new HashMap<>();
  Map<String, String>       klassMapValues            = new HashMap<>();
  Map<String, String>       taxonomyMapValues         = new HashMap<>();
  List<String>              productList               = new ArrayList<>();
  Map<String, Set<String>>  embeddedVariantArticleMap = new HashMap<>();
  List<String>              allNatureKlassIds         = new ArrayList<>();
  List<String>              successIds                = new ArrayList<>();
  LinkedHashSet<String>     header                    = new LinkedHashSet<String>();;
  Boolean                   IsMultiClassEnabled;
  Map<String, Object>       standardHeaders           = new HashMap<>();
  List<String>              excludedAttributes        = new ArrayList<>();
  
  public Map<String, String> getAttributeMapValues()
  {
    return attributeMapValues;
  }
  
  public void setAttributeMapValues(Map<String, String> attributeMapValues)
  {
    this.attributeMapValues = attributeMapValues;
  }
  
  public Map<String, String> getTagMapValues()
  {
    return tagMapValues;
  }
  
  public void setTagMapValues(Map<String, String> tagMapValues)
  {
    this.tagMapValues = tagMapValues;
  }
  
  public Map<String, String> getKlassMapValues()
  {
    return klassMapValues;
  }
  
  public void setKlassMapValues(Map<String, String> klassMapValues)
  {
    this.klassMapValues = klassMapValues;
  }
  
  public Map<String, String> getTaxonomyMapValues()
  {
    return taxonomyMapValues;
  }
  
  public void setTaxonomyMapValues(Map<String, String> taxonomyMapValues)
  {
    this.taxonomyMapValues = taxonomyMapValues;
  }
  
  public List<String> getProductList()
  {
    return productList;
  }
  
  public void setProductList(List<String> productList)
  {
    this.productList = productList;
  }
  
  public Map<String, Set<String>> getEmbeddedVariantArticleMap()
  {
    return embeddedVariantArticleMap;
  }
  
  public void setEmbeddedVariantArticleMap(Map<String, Set<String>> embeddedVariantArticleMap)
  {
    this.embeddedVariantArticleMap = embeddedVariantArticleMap;
  }
  
  public List<String> getAllNatureKlassIds()
  {
    return allNatureKlassIds;
  }
  
  public void setAllNatureKlassIds(List<String> allNatureKlassIds)
  {
    this.allNatureKlassIds = allNatureKlassIds;
  }
  
  public List<String> getSuccessIds()
  {
    return successIds;
  }
  
  public void setSuccessIds(List<String> successIds)
  {
    this.successIds = successIds;
  }
  
  public LinkedHashSet<String> getHeader()
  {
    return header;
  }
  
  public void setHeader(LinkedHashSet<String> header)
  {
    this.header = header;
  }
  
  public Boolean getIsMultiClassEnabled()
  {
    return IsMultiClassEnabled;
  }
  
  public void setIsMultiClassEnabled(Boolean isMultiClassEnabled)
  {
    IsMultiClassEnabled = isMultiClassEnabled;
  }
  
  public Map<String, Object> getStandardHeaders()
  {
    return standardHeaders;
  }
  
  public void setStandardHeaders(Map<String, Object> standardHeaders)
  {
    this.standardHeaders = standardHeaders;
  }
  
  public List<String> getExcludedAttributes()
  {
    return excludedAttributes;
  }
  
  public void setExcludedAttributes(List<String> excludedAttributes)
  {
    this.excludedAttributes = excludedAttributes;
  }
}
