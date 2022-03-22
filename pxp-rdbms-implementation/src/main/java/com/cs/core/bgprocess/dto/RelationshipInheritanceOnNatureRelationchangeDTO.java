package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.simple.JSONArray;

import com.cs.core.bgprocess.idto.IEntityRelationshipInfoDTO;
import com.cs.core.bgprocess.idto.IRelationshipInheritanceOnNatureRelationchangeDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.technical.exception.CSFormatException;

public class RelationshipInheritanceOnNatureRelationchangeDTO extends InitializeBGProcessDTO
    implements IRelationshipInheritanceOnNatureRelationchangeDTO {
  
  private static final long  serialVersionUID       = 1L;
  
  public static final String SOURCE_CONTENT_ID                  = "sourceContentId";
  public static final String NATURE_RELATIONSHIP_RESPONSE_MODEL = "natureRelationshipSaveResponseModel";
  public static final String IS_MANUALLY_CREATED                = "isManuallyCreated";
  
  private Long                                    sourceContentId;
  private  List<IEntityRelationshipInfoDTO> natureRelationshipSaveResponseModel = new ArrayList<>();
  private boolean                                 isManuallyCreated;
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(), 
        JSONBuilder.newJSONArray(NATURE_RELATIONSHIP_RESPONSE_MODEL, natureRelationshipSaveResponseModel),JSONBuilder.newJSONField(SOURCE_CONTENT_ID, sourceContentId),
        JSONBuilder.newJSONField(IS_MANUALLY_CREATED, isManuallyCreated));
    
  }
  
  @Override
  public Long getSourceContentId()
  {
    return sourceContentId;
  }
  
  @Override
  public void setSourceContentId(Long sourceContentId)
  {
    this.sourceContentId = sourceContentId;
  }
  
  @Override
  public Boolean getIsManuallyCreated()
  {
    return isManuallyCreated;
  }
  
  @Override
  public void setIsManuallyCreated(Boolean isManuallyCreated)
  {
    this.isManuallyCreated = isManuallyCreated;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    natureRelationshipSaveResponseModel.clear();
    json.getJSONArray(NATURE_RELATIONSHIP_RESPONSE_MODEL).forEach((iid) -> {
      IEntityRelationshipInfoDTO addedRemovedElementDTO = new EntityRelationshipInfoDTO();
      try {
        addedRemovedElementDTO.fromJSON(iid.toString());
        natureRelationshipSaveResponseModel.add(addedRemovedElementDTO);
      }
      catch (CSFormatException e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    });
    sourceContentId = json.getLong(SOURCE_CONTENT_ID);
    isManuallyCreated = json.getBoolean(IS_MANUALLY_CREATED);
  }

  @Override
  public List<IEntityRelationshipInfoDTO> getEntityRelationshipInfo()
  {
    if(natureRelationshipSaveResponseModel.size()==0) {
      this.natureRelationshipSaveResponseModel = new ArrayList<IEntityRelationshipInfoDTO>();
    }
    return natureRelationshipSaveResponseModel;
  }

  @Override
  public void setEntityRelationshipInfo(List<IEntityRelationshipInfoDTO> natureRelationshipSaveResponseModel)
  {
   this.natureRelationshipSaveResponseModel = natureRelationshipSaveResponseModel;
  }
  
}