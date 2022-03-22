package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.structure.AbstractStructure;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.config.interactor.model.variantcontext.TechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetKlassModel implements IGetKlassModel {
  
  private static final long                                       serialVersionUID                                 = 1L;
  
  protected IKlass                                                klass;
  protected List<? extends IKlass>                                referencedKlasses                                = new ArrayList<>();
  protected Map<String, ? extends IStructure>                     referencedStructures                             = new HashMap<>();
  protected List<IDataRuleModel>                                  dataRulesOfKlass                                 = new ArrayList<>();
  protected Map<String, ? extends ITag>                           referencedTags                                   = new HashMap<>();
  protected Map<String, ? extends IAttribute>                     referencedAttributes                             = new HashMap<>();
  protected List<IGetVariantContextModel>                         referencedContexts                               = new ArrayList<>();
  protected List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable = new ArrayList<>();
  
  @Override
  public Map<String, ? extends ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @JsonDeserialize(contentAs = Tag.class)
  @Override
  public void setReferencedTags(Map<String, ? extends ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public List<IGetVariantContextModel> getReferencedContexts()
  {
    return referencedContexts;
  }
  
  @Override
  public void setReferencedContexts(List<IGetVariantContextModel> referencedContexts)
  {
    this.referencedContexts = referencedContexts;
  }
  
  @Override
  public Map<String, ? extends IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  @Override
  public void setReferencedAttributes(Map<String, ? extends IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public IKlass getKlass()
  {
    return this.klass;
  }
  
  @Override
  public void setKlass(IKlass klass)
  {
    this.klass = klass;
  }
  
  @Override
  public List<? extends IKlass> getReferencedKlasses()
  {
    return this.referencedKlasses;
  }
  
  @Override
  public void setReferencedKlasses(List<? extends IKlass> klasses)
  {
    this.referencedKlasses = (List<IKlass>) klasses;
  }
  
  @Override
  public Map<String, ? extends IStructure> getReferencedStructures()
  {
    return referencedStructures;
  }
  
  @JsonDeserialize(contentAs = AbstractStructure.class)
  @Override
  public void setReferencedStructures(Map<String, ? extends IStructure> referencedStructures)
  {
    this.referencedStructures = referencedStructures;
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public List<IDataRuleModel> getDataRulesOfKlass()
  {
    if (dataRulesOfKlass == null) {
      dataRulesOfKlass = new ArrayList<>();
    }
    return dataRulesOfKlass;
  }
  
  @JsonDeserialize(contentAs = DataRuleModel.class)
  @Override
  public void setDataRulesOfKlass(List<IDataRuleModel> dataRulesOfKlass)
  {
    this.dataRulesOfKlass = dataRulesOfKlass;
  }
  
  @Override
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable()
  {
    return technicalImageVariantContextWithAutoCreateEnable;
  }
  
  @JsonDeserialize(contentAs = TechnicalImageVariantWithAutoCreateEnableModel.class)
  @Override
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable)
  {
    this.technicalImageVariantContextWithAutoCreateEnable = technicalImageVariantContextWithAutoCreateEnable;
  }
}
