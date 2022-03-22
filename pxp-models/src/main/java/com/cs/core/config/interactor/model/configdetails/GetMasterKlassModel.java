package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassRoleSetting;
import com.cs.core.config.interactor.entity.klass.KlassRoleSetting;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.model.klass.IGetMasterKlassModel;

public class GetMasterKlassModel implements IGetMasterKlassModel {
  
  private static final long                   serialVersionUID     = 1L;
  
  protected IKlass                            klass;
  protected Map<String, ? extends IStructure> referencedStructures = new HashMap<>();
  protected Collection<? extends IKlass>      referencedKlasses    = new ArrayList<>();
  protected KlassRoleSetting                  roleSetting;
  
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
  public Collection<? extends IKlass> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @Override
  public void setReferencedKlasses(Collection<? extends IKlass> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
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
  public IKlassRoleSetting getKlassViewSetting()
  {
    return this.roleSetting;
  }
  
  @Override
  public void setKlassViewSetting(IKlassRoleSetting roleSetting)
  {
    this.roleSetting = (KlassRoleSetting) roleSetting;
  }
}
