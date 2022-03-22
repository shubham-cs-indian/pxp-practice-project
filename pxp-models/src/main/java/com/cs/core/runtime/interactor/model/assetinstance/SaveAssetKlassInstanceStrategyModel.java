package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.model.klass.IKlassModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveAssetKlassInstanceStrategyModel implements ISaveAssetKlassInstanceStrategyModel {
  
  @SuppressWarnings("unused")
  private static final long                   serialVersionUID     = 1L;
  
  protected IAssetInstanceSaveModel           klassInstance;
  
  protected List<IKlassModel>                 referencedKlasses    = new ArrayList<>();
  
  protected Map<String, ? extends IStructure> referencedStructures = new HashMap<>();
  
  protected IKlass                            typeKlass;
  
  @Override
  public IAssetInstanceSaveModel getKlassInstance()
  {
    return this.klassInstance;
  }
  
  @Override
  public void setKlassInstance(IAssetInstanceSaveModel klassInstance)
  {
    this.klassInstance = (IAssetInstanceSaveModel) klassInstance;
  }
  
  @Override
  public List<? extends IKlass> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void setReferencedKlasses(List<? extends IKlass> referencedKlasses)
  {
    this.referencedKlasses = (List<IKlassModel>) referencedKlasses;
  }
  
  @Override
  public Map<String, ? extends IStructure> getReferencedStructures()
  {
    return referencedStructures;
  }
  
  @Override
  public void setReferencedStructures(Map<String, ? extends IStructure> referencedStructures)
  {
    this.referencedStructures = referencedStructures;
  }
  
  @Override
  public IKlass getTypeKlass()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setTypeKlass(IKlass typeKlass)
  {
    this.typeKlass = typeKlass;
  }
}
