package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.bgprocess.idto.IEntityRelationshipInfoDTO;
import com.cs.core.bgprocess.idto.IRelationshipInheritanceInfoDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.technical.exception.CSFormatException;

public class RelationshipInheritanceInfoDTO extends InitializeBGProcessDTO implements IRelationshipInheritanceInfoDTO{

  private static final long              serialVersionUID = 1L;
  
  public static final String SOURCE_CONTENT_ID = "sourceContentId";
  public static final String NATURE_RELATIONSHIP_RESPONSE_MODEL = "natureRelationshipSaveResponseModel";
  

  private  List<IEntityRelationshipInfoDTO> natureRelationshipSaveResponseModel = new ArrayList<>();
  private Long               sourceContentId;

  
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
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    // TODO Auto-generated method stub
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(),
        JSONBuilder.newJSONField(SOURCE_CONTENT_ID, sourceContentId), 
        JSONBuilder.newJSONArray(NATURE_RELATIONSHIP_RESPONSE_MODEL, natureRelationshipSaveResponseModel));  
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
