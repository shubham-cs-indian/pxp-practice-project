package com.cs.core.config.interactor.model.tag;

import java.util.List;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CreateTagResponseModel extends ConfigResponseWithAuditLogModel
implements ICreateTagResponseModel {

private static final long          serialVersionUID = 1L;
protected List<ITagModel>          tagResponseModel;
protected List<IAuditLogModel> auditLogInfo;

@Override
public List<ITagModel> getTagResponseModel()
{
return this.tagResponseModel;
}

@Override
@JsonTypeInfo(use = Id.NONE)
@JsonDeserialize(contentAs = AbstractTagModel.class)
public void setTagResponseModel(List<ITagModel> tagResponseModel)
{
this.tagResponseModel = tagResponseModel;
}

}
