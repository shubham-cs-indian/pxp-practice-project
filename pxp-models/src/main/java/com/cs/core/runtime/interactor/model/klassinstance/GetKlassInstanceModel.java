package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassRoleSetting;
import com.cs.core.config.interactor.entity.klass.KlassRoleSetting;
import com.cs.core.config.interactor.entity.structure.AbstractStructure;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.model.configdetails.IGetDefaultKlassesModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.assetinstance.IReferenceAssetModel;
import com.cs.core.runtime.interactor.model.assetinstance.ReferenceAssetModel;
import com.cs.core.runtime.interactor.model.customdeserializer.customDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Deprecated
public class GetKlassInstanceModel implements IGetKlassInstanceModel {
  
  private static final long                                   serialVersionUID               = 1L;
  
  protected IKlassInstance                                    klassInstance;
  
  protected List<? extends IKlassModel>                       referencedKlasses              = new ArrayList<>();
  
  protected Map<String, ? extends IStructure>                 referencedStructures           = new HashMap<>();
  
  protected IGlobalPermission                                 globalPermission;
  
  protected KlassStructureDiffModel                           structureDiff;
  
  protected KlassRoleSetting                                  roleSetting;
  
  protected List<String>                                      allowedTypes;
  
  protected IKlass                                            typeKlass;
  
  protected IGetDefaultKlassesModel                           defaultTypes;
  
  protected Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstances = new HashMap<>();
  
  protected Map<String, ? extends IReferenceAssetModel>       referencedAssets               = new HashMap<>();
  
  @Override
  public IKlassInstance getKlassInstance()
  {
    return this.klassInstance;
  }
  
  @Override
  public void setKlassInstance(IKlassInstance klassInstance)
  {
    this.klassInstance = klassInstance;
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
  
  @JsonDeserialize(contentAs = AbstractStructure.class)
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
  
  @JsonDeserialize(as = GlobalPermission.class)
  @Override
  public IGlobalPermission getGlobalPermission()
  {
    return globalPermission;
  }
  
  @JsonDeserialize(as = GlobalPermission.class)
  @Override
  public void setGlobalPermission(IGlobalPermission globalPermission)
  {
    this.globalPermission = globalPermission;
  }
  
  @Override
  public IKlassStructureDiffModel getStructureDiff()
  {
    return this.structureDiff;
  }
  
  @Override
  public void setStructureDiff(IKlassStructureDiffModel diff)
  {
    this.structureDiff = (KlassStructureDiffModel) diff;
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
  
  @Override
  public List<String> getAllowedTypes()
  {
    if (allowedTypes == null) {
      allowedTypes = new ArrayList<String>();
    }
    return allowedTypes;
  }
  
  @Override
  public void setAllowedTypes(List<String> allowedTypes)
  {
    this.allowedTypes = allowedTypes;
  }
  
  @Override
  public IKlass getTypeKlass()
  {
    return typeKlass;
  }
  
  @Override
  public void setTypeKlass(IKlass typeKlass)
  {
    this.typeKlass = typeKlass;
  }
  
  @Override
  public IGetDefaultKlassesModel getDefaultTypes()
  {
    return defaultTypes;
  }
  
  @Override
  public void setDefaultTypes(IGetDefaultKlassesModel defaultTypes)
  {
    this.defaultTypes = defaultTypes;
  }
  
  @Override
  public Map<String, ? extends IReferenceAssetModel> getReferencedAssets()
  {
    return referencedAssets;
  }
  
  @JsonDeserialize(contentAs = ReferenceAssetModel.class)
  @Override
  public void setReferencedAssets(Map<String, ? extends IReferenceAssetModel> referencedAssets)
  {
    this.referencedAssets = referencedAssets;
  }
  
  @Override
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceRelationshipInstanceElements()
  {
    return referenceRelationshipInstances;
  }
  
  @JsonDeserialize(contentUsing = customDeserializer.class)
  @Override
  public void setReferenceRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstances)
  {
    this.referenceRelationshipInstances = referenceRelationshipInstances;
  }
}
