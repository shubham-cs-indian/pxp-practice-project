package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassRoleSetting;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public interface IGetMasterKlassModel extends IModel, Serializable {
  
  public IKlass getKlass();
  
  public void setKlass(IKlass klass);
  
  public Collection<? extends IKlass> getReferencedKlasses();
  
  public void setReferencedKlasses(Collection<? extends IKlass> referencedKlasses);
  
  public Map<String, ? extends IStructure> getReferencedStructures();
  
  public void setReferencedStructures(Map<String, ? extends IStructure> referencedStructures);
  
  public IKlassRoleSetting getKlassViewSetting();
  
  public void setKlassViewSetting(IKlassRoleSetting roleSetting);
}
