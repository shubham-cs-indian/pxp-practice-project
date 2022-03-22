package com.cs.core.runtime.interactor.model.dataintegration;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = false)
@JsonSubTypes({ @Type(name = "inboundSummary", value = InboundSummaryTileInfoModel.class),
    @Type(name = "inboundLastUpload", value = InboundTileInfoModel.class),
    @Type(name = "outbound", value = OutboundTileInfoModel.class) })
public class TileInfoModel implements ITileInfoModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          type;
  
  @Override
  public String getType()
  {
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
}
