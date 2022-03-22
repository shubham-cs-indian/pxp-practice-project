package com.cs.core.rdbms.coupling.idto;

import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.technical.rdbms.idto.IRootDTO;

import java.util.Set;

public interface ICouplingSourceDTO extends IRootDTO {

  public static final String TARGET_SPLITTER = "#";
  public static final String ENTITY_PROPERTY_SPLITTER = ">";
  public static final String COUPLING_SPLITTER = ":";
  String getId();

  void setId(String id);

  Set<String> getCouplingTargets();
  void setCouplingTargets(Set<String> couplingTargets);
}
