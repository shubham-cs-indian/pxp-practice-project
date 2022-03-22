package com.cs.core.config.interactor.entity.klass;

import com.cs.core.config.interactor.entity.structure.IStructure;

import java.util.Map;

public interface IKlassStructure extends IKlass, IStructure {
  
  public Map<String, Integer> getReferencedClassIds();
  
  public void setReferencedClassIds(Map<String, Integer> referencedClasIds);
  
  public Map<String, Integer> getReferencedAttributeIds();
  
  public void setReferencedAttributeIds(Map<String, Integer> referencedAttributeIds);
  
  public IKlassViewSetting getKlassViewSetting();
  
  public void setKlassViewSetting(IKlassViewSetting klassViewSetting);
  
  public Boolean getShouldVersion();
  
  public void setShouldVersion(Boolean shouldVersion);
}
