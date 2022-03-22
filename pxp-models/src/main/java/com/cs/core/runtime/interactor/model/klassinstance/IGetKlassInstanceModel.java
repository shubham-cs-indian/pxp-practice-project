package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassRoleSetting;
import com.cs.core.config.interactor.entity.structure.IStructure;
import com.cs.core.config.interactor.model.configdetails.IGetDefaultKlassesModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.assetinstance.IReferenceAssetModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

@Deprecated
public interface IGetKlassInstanceModel extends IModel {
  
  public static final String KLASS_INSTANCE                           = "klassInstance";
  public static final String REFERENCED_KLASSES                       = "referencedKlasses";
  public static final String REFERENCED_STRUCTURES                    = "referencedStructures";
  public static final String GLOBAL_PERMISSION                        = "globalPermission";
  public static final String STRUCTURE_DIFF                           = "structureDiff";
  public static final String KLASS_VIEW_SETTING                       = "klassViewSetting";
  public static final String ALLOWED_TYPES                            = "allowedTypes";
  public static final String TYPE_KLASS                               = "typeKlass";
  public static final String DEFAULT_TYPES                            = "defaultTypes";
  public static final String REFERENCED_ASSETS                        = "referencedAssets";
  public static final String REFERENCE_RELATIONSHIP_INSTANCE_ELEMENTS = "referenceRelationshipInstanceElements";
  
  public IKlassInstance getKlassInstance();
  
  public void setKlassInstance(IKlassInstance klassInstance);
  
  public List<? extends IKlass> getReferencedKlasses();
  
  public void setReferencedKlasses(List<? extends IKlass> referencedKlasses);
  
  public Map<String, ? extends IStructure> getReferencedStructures();
  
  public void setReferencedStructures(Map<String, ? extends IStructure> referencedStructures);
  
  public IGlobalPermission getGlobalPermission();
  
  public void setGlobalPermission(IGlobalPermission globalPermission);
  
  public IKlassStructureDiffModel getStructureDiff();
  
  public void setStructureDiff(IKlassStructureDiffModel diff);
  
  public IKlassRoleSetting getKlassViewSetting();
  
  public void setKlassViewSetting(IKlassRoleSetting roleSetting);
  
  public List<String> getAllowedTypes();
  
  public void setAllowedTypes(List<String> allowedTypes);
  
  public IKlass getTypeKlass();
  
  public void setTypeKlass(IKlass typeKlass);
  
  public IGetDefaultKlassesModel getDefaultTypes();
  
  public void setDefaultTypes(IGetDefaultKlassesModel defaultTypes);
  
  public Map<String, ? extends IReferenceAssetModel> getReferencedAssets();
  
  public void setReferencedAssets(Map<String, ? extends IReferenceAssetModel> referencedAssets);
  
  public Map<String, List<IKlassInstanceInformationModel>> getReferenceRelationshipInstanceElements();
  
  public void setReferenceRelationshipInstanceElements(
      Map<String, List<IKlassInstanceInformationModel>> referenceRelationshipInstances);
}
