package com.cs.config.idto;

import com.cs.core.dataintegration.idto.IPXON;

import java.util.List;
import java.util.Set;

public interface IConfigTabDTO extends IConfigJSONDTO {

  String IS_STANDARD               = "isStandard";
  String CODE                      = "code";
  String LABEL                     = "label";
  String ICON                      = "icon";
  String SEQUENCE                  = "sequence";
  String PROPERTY_SEQUENCE_LIST    = "propertySequenceList";

  List<String> getPropertySequenceList();
  void setPropertySequenceList(List<String> propertySequenceList);

  String getIcon();
  void setIcon(String icon);

  String getLabel();
  void setLabel(String label);

  Boolean getIsStandard();
  void setIsStandard(Boolean isStandard);

  Integer getSequence();
  void setSequence(Integer sequence);

  String getCode();
  void setCode(String code);
}
