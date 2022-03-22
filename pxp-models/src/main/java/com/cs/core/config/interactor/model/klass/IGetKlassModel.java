package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetKlassModel extends IModel {
  
  public static final String KLASS                                                = "klass";
  public static final String DATARULES_OF_KLASS                                   = "dataRulesOfKlass";
  public static final String TECHNICAL_IMAGEVARIANT_CONTEXT_WITH_AUTOCREATEENABLE = "technicalImageVariantContextWithAutoCreateEnable";
  public static final String REFERENCED_KLASSES                                   = "referencedKlasses";
  public static final String REFERENCED_STRUCTURES                                = "referencedStructures";
  public static final String REFERENCED_TAGS                                      = "referencedTags";
  public static final String REFERENCED_ATTRIBUTES                                = "referencedAttributes";
  public static final String REFERENCED_CONTEXTS                                  = "referencedContexts";
  
  public IKlass getKlass();
  
  public void setKlass(IKlass klass);
  
  public List<? extends IKlass> getReferencedKlasses();
  
  public void setReferencedKlasses(List<? extends IKlass> klasses);
  
  public Map<String, ? extends IStructure> getReferencedStructures();
  
  public void setReferencedStructures(Map<String, ? extends IStructure> referencedStructures);
  
  public List<IDataRuleModel> getDataRulesOfKlass();
  
  public void setDataRulesOfKlass(List<IDataRuleModel> dataRulesOfKlass);
  
  public Map<String, ? extends ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ? extends ITag> referencedTags);
  
  public Map<String, ? extends IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, ? extends IAttribute> referencedAttributes);
  
  public List<IGetVariantContextModel> getReferencedContexts();
  
  public void setReferencedContexts(List<IGetVariantContextModel> referencedContexts);
  
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable();
  
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable);
}
