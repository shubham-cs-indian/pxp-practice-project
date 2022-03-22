package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.bgprocess.idto.IAssetFileDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class AssetFileDTO extends SimpleDTO implements IAssetFileDTO{
  
  private static final String EXTRACT_METADATA        = "extractMetadata";
  private static final String EXTENSION_CONFIGURATION = "extensionConfiguration";
  private static final String IS_EXTRACTED            = "isExtracted";
  private static final String EXTENSION_TYPE          = "extensionType";
  private static final String EXTRACT_RENDITION       = "extractRendition";
  private static final String KLASS_ID                = "klassID";
  private static final String FILE_PATH               = "filePath";
  private static final String CODE                    = "code";
  private static final String NAME                    = "name";
  private static final String IDS                     = "ids";
  private static final String IS_IN_DESIGN_SERVER_ENABLED = "isInDesignServerEnabled";
  
  private String              klassID;
  private String              filePath;
  private Map<String, Object> metaData;
  private String              hash;
  private String              extensionType;
  private Boolean             isExtracted             = false;
  private Map<String, Object> extensionConfiguration  = new HashMap<>();
  private String              code;
  private String              name;
  private List<String>        ids                     = new ArrayList<String>();
  private boolean             isInDesignServerEnabled = false;
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    klassID = json.getString(KLASS_ID);
    filePath = json.getString(FILE_PATH);
    extensionType = json.getString(EXTENSION_TYPE);
    isExtracted = json.getBoolean(IS_EXTRACTED);
    code = json.getString(CODE);
    name = json.getString(NAME);
    ids = json.getJSONArray(IDS);
    isInDesignServerEnabled = json.getBoolean(IS_IN_DESIGN_SERVER_ENABLED);
    JSONContentParser extensionConfigurationParser = json.getJSONParser(EXTENSION_CONFIGURATION);
    if (!extensionConfigurationParser.isEmpty()) {
      extensionConfiguration.put(EXTRACT_RENDITION, extensionConfigurationParser.getBoolean(EXTRACT_RENDITION));
      extensionConfiguration.put(EXTRACT_METADATA, extensionConfigurationParser.getBoolean(EXTRACT_METADATA));
    }
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    StringBuffer jsonExtensionConfigurationMap = new StringBuffer();
    extensionConfiguration.keySet().forEach(key -> {
              jsonExtensionConfigurationMap.append(JSONBuilder.newJSONField(key, (boolean) extensionConfiguration.get(key)));
              jsonExtensionConfigurationMap.append(",");
            });
    if (extensionConfiguration.size() > 0) {
      jsonExtensionConfigurationMap.setLength(jsonExtensionConfigurationMap.length() - 1);
    }
    
    
    return JSONBuilder.assembleJSONBuffer(
         JSONBuilder.newJSONField(KLASS_ID, klassID)
        ,JSONBuilder.newJSONField(FILE_PATH, filePath, true)
        ,JSONBuilder.newJSONField(EXTENSION_TYPE, extensionType)
        ,JSONBuilder.newJSONField(IS_EXTRACTED, isExtracted)
        ,JSONBuilder.newJSONField(CODE, code)
        ,JSONBuilder.newJSONField(NAME, name)
        ,JSONBuilder.newJSONStringArray(IDS, ids)
        ,JSONBuilder.newJSONField(IS_IN_DESIGN_SERVER_ENABLED, isInDesignServerEnabled)
        ,extensionConfiguration.size() > 0 ? 
            JSONBuilder.newJSONField(EXTENSION_CONFIGURATION, jsonExtensionConfigurationMap) : JSONBuilder.VOID_FIELD);
  }

  @Override
  public void setKlassID(String klassID)
  {
    this.klassID = klassID;
  }

  @Override
  public String getKlassID()
  {
    return klassID;
  }

  @Override
  public void setFilePath(String filePath)
  {
    this.filePath = filePath;
  }

  @Override
  public String getFilePath()
  {
    return filePath;
  }
  
  @Override
  public Map<String, Object> getMetaData()
  {
    return metaData;
  }

  @Override
  public void setMetaData(Map<String, Object> metaData)
  {
    this.metaData = metaData;
  }

  @Override
  public String getHash()
  {
    return hash;
  }

  @Override
  public void setHash(String hash)
  {
    this.hash = hash;
  }
  
  @Override
  public String getExtensionType()
  {
    return extensionType;
  }
  
  @Override
  public void setExtensionType(String extensionType)
  {
    this.extensionType = extensionType;
  }
  
  @Override
  public Boolean getIsExtracted()
  {
    return isExtracted;
  }
  
  @Override
  public void setIsExtracted(Boolean isExtracted)
  {
    this.isExtracted = isExtracted;
  }
  
  @Override
  public Map<String, Object> getExtensionConfiguration()
  {
    return extensionConfiguration;
  }
  
  @Override
  public void setExtensionConfiguration(Map<String, Object> extensionConfiguration)
  {
    this.extensionConfiguration = extensionConfiguration;
  }

  @Override
  public String getCode()
  {
    return code;
  }

  @Override
  public void setCode(String code)
  {
    this.code = code;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public void setName(String name)
  {
    this.name = name;
  }

  @Override
  public List<String> getIds()
  {
    if (ids == null) {
      ids = new ArrayList<String>();
    }
    return ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }

  @Override
  public boolean getIsInDesignServerEnabled()
  {
    return isInDesignServerEnabled;
  }

  @Override
  public void setIsInDesignServerEnabled(boolean isInDesignServerEnabled)
  {
    this.isInDesignServerEnabled = isInDesignServerEnabled;
  }
}
