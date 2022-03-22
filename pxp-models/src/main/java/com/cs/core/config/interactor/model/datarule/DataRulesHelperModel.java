package com.cs.core.config.interactor.model.datarule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DataRulesHelperModel implements IDataRulesHelperModel {
  
  protected Map<Long, List<Long>>                       productIdentifiers          = new HashMap<>();
  protected List<Long>                                  must                        = new ArrayList<>();
  protected List<Long>                                  should                      = new ArrayList<>();
  protected List<Long>                                  removedMandatoryIdentifiers = new ArrayList<>();
  protected List<Long>                                  translatablePropertyIIDs    = new ArrayList<>();
  protected Map<String, IReferencedSectionElementModel> referencedElements;
  protected Map<String, IAttribute>                     referencedAttributes;
  protected Map<String, ITag>                           referencedTags;
  
  @Override
  public List<Long> getRemovedMandatoryIdentifiers()
  {
    return removedMandatoryIdentifiers;
  }
  
  @Override
  public void setRemovedMandatoryIdentifiers(List<Long> removedMandatoryIdentifiers)
  {
    this.removedMandatoryIdentifiers = removedMandatoryIdentifiers;
  }
  
  @Override
  public List<Long> getTranslatablePropertyIIDs()
  {
    return translatablePropertyIIDs;
  }
  
  @Override
  public void setTranslatablePropertyIIDs(List<Long> translatablePropertyIIDs)
  {
    this.translatablePropertyIIDs = translatablePropertyIIDs;
  }
  
  @Override
  public List<Long> getMust()
  {
    return must;
  }
  
  @Override
  public void setMust(List<Long> must)
  {
    this.must = must;
  }
  
  @Override
  public List<Long> getShould()
  {
    return should;
  }
  
  @Override
  public void setShould(List<Long> should)
  {
    this.should = should;
  }
  
  @Override
  public Map<Long, List<Long>> getProductIdentifiers()
  {
    return productIdentifiers;
  }
  
  @Override
  public void setProductIdentifiers(Map<Long, List<Long>> productIdentifiers)
  {
    this.productIdentifiers = productIdentifiers;
  }
  
  @Override
  public Map<String, IReferencedSectionElementModel> getReferencedElements()
  {
    return referencedElements;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractReferencedSectionElementModel.class)
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    this.referencedElements = referencedElements;
  }
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
}
