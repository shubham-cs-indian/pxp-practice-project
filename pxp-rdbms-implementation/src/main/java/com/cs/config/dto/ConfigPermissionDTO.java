package com.cs.config.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.json.simple.JSONArray;

import com.cs.config.idto.IConfigGlobalPermissionDTO;
import com.cs.config.idto.IConfigHeaderPermisionDTO;
import com.cs.config.idto.IConfigPermissionDTO;
import com.cs.config.idto.IConfigPropertyPermissionDTO;
import com.cs.config.idto.IConfigRelationshipPermissionDTO;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.ijosn.IJSONContent;

public class ConfigPermissionDTO extends AbstractConfigJSONDTO implements IConfigPermissionDTO {
  
  private static final long                       serialVersionUID  = 1L;
  /**
   * Tag keys and its corresponding PXON key map
   */
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  /** Definition of the Classifier and its PXON key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.entityId, IPXON.PXONTag.entityid.toReadOnlyTag());
    
    // update only Properties
    KEY_MAP.put(ConfigTag.permissionType, IPXON.PXONTag.permissionType.toTag());
    KEY_MAP.put(ConfigTag.roleId, IPXON.PXONTag.roleId.toTag());
    KEY_MAP.put(ConfigTag.globalPermission, IPXON.PXONTag.globalPermission.toJSONContentTag());
    KEY_MAP.put(ConfigTag.headerPermission, IPXON.PXONTag.headerPermission.toJSONContentTag());
    KEY_MAP.put(ConfigTag.propertyPermissions, IPXON.PXONTag.propertyPermissions.toJSONArrayTag());
    KEY_MAP.put(ConfigTag.relationshipPermissions,
        IPXON.PXONTag.relationshipPermissions.toJSONArrayTag());
    
 
    IGNORED_PXON_TAGS.add(ConfigTag.code);
  }
  
  private ConfigGlobalPermissionDTO              globalPermissionDTO     = new ConfigGlobalPermissionDTO();
  private  ConfigHeaderPermisionDTO               headerPermissionDTO     = new ConfigHeaderPermisionDTO();
  
  private final List<IConfigPropertyPermissionDTO>     propertyPermissions     = new ArrayList<>();
  private final List<IConfigRelationshipPermissionDTO> relationshipPermissions = new ArrayList<>();
  
  /**
   * Initializing the static key map
   */
  public ConfigPermissionDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    CSEObject cse = new CSEObject(CSEObjectType.Permission);
    String entityId = getString(ConfigTag.entityId);
    cse.setCode(entityId);
    //cse.setSpecification(Keyword.$name, entityId);
    return cse;
  }
  
  @Override
  void fromCSExpression(JSONContentParser parser) throws CSFormatException
  {
    fromCSExpressID(parser.getCSEElement(IPXON.PXONTag.csid.toTag()));
  }
  
  @Override
  public IConfigGlobalPermissionDTO getGlobalPermission()
  {
    return globalPermissionDTO;
  }
  
  @Override
  public IConfigHeaderPermisionDTO getHeaderPermission()
  {
    return headerPermissionDTO;
  }
  
  @Override
  public List<IConfigPropertyPermissionDTO> getPropertyPermission()
  {
    return propertyPermissions;
  }
  
  @Override
  public List<IConfigRelationshipPermissionDTO> getRelationshipPermission()
  {
    return relationshipPermissions;
  }
  
  public void setGlobalPermissionDTO(ConfigGlobalPermissionDTO configGlobalPermissionDTO)
  {
    this.globalPermissionDTO = configGlobalPermissionDTO;
  }

  public void setHeaderPermissionDTO(ConfigHeaderPermisionDTO headerPermissionDTO)
  {
    this.headerPermissionDTO = headerPermissionDTO;
  }

  public void fromPXON(String json) throws CSFormatException
  {
    super.fromPXON(json);
    // Prepare globalPermissionDTO
    IJSONContent globalPermissionJsonContent = getJSONContent(ConfigTag.globalPermission);
    globalPermissionDTO.fromPXON(globalPermissionJsonContent.toString());
    
    // Prepare headerPermissionDTO
    IJSONContent headerPermissionJsonContent = getJSONContent(ConfigTag.headerPermission);
    headerPermissionDTO.fromPXON(headerPermissionJsonContent.toString());
    
    // Prepare list of propertyPermissions
    propertyPermissions.clear();
    JSONArray propertyPermissionsList = getJSONArray(ConfigTag.propertyPermissions);
    for (Object propertyPermission : propertyPermissionsList) {
      ConfigPropertyPermissionDTO propertyPermissionDTO = new ConfigPropertyPermissionDTO();
      propertyPermissionDTO.fromPXON(propertyPermission.toString());
      propertyPermissions.add(propertyPermissionDTO);
    }
    
    // Prepare list of relationshipPermissions
    relationshipPermissions.clear();
    JSONArray relationshipPermissionList = getJSONArray(ConfigTag.relationshipPermissions);
    for (Object relationshipPermission : relationshipPermissionList) {
      ConfigRelationshipPermissionDTO relationshipPermissionDTO = new ConfigRelationshipPermissionDTO();
      relationshipPermissionDTO.fromPXON(relationshipPermission.toString());
      relationshipPermissions.add(relationshipPermissionDTO);
    }
  }
  
  @Override
  public void fromJSON(String json) throws CSFormatException
  {
    super.fromJSON(json);
    
    IJSONContent globalPermissionJsonContent = getJSONContent(ConfigTag.globalPermission);
    globalPermissionDTO.fromJSON(globalPermissionJsonContent.toString());
    
    IJSONContent headerPermissionJsonContent = getJSONContent(ConfigTag.headerPermission);
    headerPermissionDTO.fromJSON(headerPermissionJsonContent.toString());
    
    propertyPermissions.clear();
    JSONArray propertyPermissionsList = getJSONArray(ConfigTag.propertyPermissions);
    for (Object propertyPermission : propertyPermissionsList) {
      ConfigPropertyPermissionDTO propertyPermissionDTO = new ConfigPropertyPermissionDTO();
      propertyPermissionDTO.fromJSON(propertyPermission.toString());
      propertyPermissions.add(propertyPermissionDTO);
    }
    
    relationshipPermissions.clear();
    JSONArray relationshipPermissionList = getJSONArray(ConfigTag.relationshipPermissions);
    for (Object relationshipPermission : relationshipPermissionList) {
      ConfigRelationshipPermissionDTO relationshipPermissionDTO = new ConfigRelationshipPermissionDTO();
      relationshipPermissionDTO.fromJSON(relationshipPermission.toString());
      relationshipPermissions.add(relationshipPermissionDTO);
    }
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected JSONContent toConfigPXONContent() throws CSFormatException
  {
    JSONContent pxonOutput = super.toConfigPXONContent();
    // property Permissions preparation
    JSONArray pxonPropertyPermissions = new JSONArray();
    for (IConfigPropertyPermissionDTO propertyPermission : propertyPermissions) {
      pxonPropertyPermissions
          .add(((ConfigPropertyPermissionDTO) propertyPermission).toConfigPXONContent()
              .toJSONObject());
    }
    pxonOutput.setField(KEY_MAP.get(ConfigTag.propertyPermissions), pxonPropertyPermissions);
    
    // relationship Permissions preparation
    JSONArray pxonRelationshipPermissions = new JSONArray();
    for (IConfigRelationshipPermissionDTO relationshipPermissionDTO : relationshipPermissions) {
      pxonRelationshipPermissions
          .add(((ConfigRelationshipPermissionDTO) relationshipPermissionDTO).toConfigPXONContent()
              .toJSONObject());
    }
    pxonOutput.setField(KEY_MAP.get(ConfigTag.relationshipPermissions),
        pxonRelationshipPermissions);
    // global Permissions preparation
    pxonOutput.setField(KEY_MAP.get(ConfigTag.globalPermission),
        globalPermissionDTO.toConfigPXONContent()
            .toJSONObject());
    // header Permissions preparation
    pxonOutput.setField(KEY_MAP.get(ConfigTag.headerPermission),
        headerPermissionDTO.toConfigPXONContent()
            .toJSONObject());
    
    return pxonOutput;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    List<Object> propertyPermissioneDTOs = new ArrayList<>();
    for (IConfigPropertyPermissionDTO propertyPermission : propertyPermissions) {
      propertyPermissioneDTOs.add(propertyPermission);
    }
    configData.setField(ConfigTag.propertyPermissions.toString(), propertyPermissioneDTOs);
    
    List<Object> relationshipPermissionDTOs = new ArrayList<>();
    for (IConfigRelationshipPermissionDTO relationshipPermissionDTO : relationshipPermissions) {
      relationshipPermissionDTOs.add(relationshipPermissionDTO);
    }
    configData.setField(ConfigTag.relationshipPermissions.toString(), relationshipPermissionDTOs);
    
    configData.setField(ConfigTag.globalPermission.toString(), globalPermissionDTO);
    configData.setField(ConfigTag.headerPermission.toString(), headerPermissionDTO);
    return super.toJSONBuffer();
  }
  
  @Override
  public String getPermissionType()
  {
    return getString(ConfigTag.permissionType);
  }
  
  @Override
  public void setPermissionType(String permissionType)
  {
    setString(ConfigTag.permissionType, permissionType);
    
  }
  
  @Override
  public String getRoleId()
  {
    return getString(ConfigTag.roleId);
  }
  
  @Override
  public void setRoleId(String roleId)
  {
    setString(ConfigTag.roleId, roleId);
    
  }

  @Override
  public boolean isNull()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String getEntityId()
  {
    return getString(ConfigTag.entityId);
  }

  @Override
  public void setEntityId(String entityId)
  {
    setString(ConfigTag.entityId, entityId);
    
  }
  
}
