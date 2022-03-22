package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface ISaveAssetKlassInstanceStrategyModel extends IModel {
  
  public static final String KLASS_INSTANCE        = "klassInstance";
  public static final String REFERENCED_KLASSES    = "referencedKlasses";
  public static final String REFERENCED_STRUCTURES = "referencedStructures";
  public static final String TYPE_KLASS            = "typeKlass";
  
  public IAssetInstanceSaveModel getKlassInstance();
  
  public void setKlassInstance(IAssetInstanceSaveModel klassInstance);
  
  public List<? extends IKlass> getReferencedKlasses();
  
  public void setReferencedKlasses(List<? extends IKlass> referencedKlasses);
  
  public Map<String, ? extends IStructure> getReferencedStructures();
  
  public void setReferencedStructures(Map<String, ? extends IStructure> referencedStructures);
  
  public IKlass getTypeKlass();
  
  public void setTypeKlass(IKlass typeKlass);
}
