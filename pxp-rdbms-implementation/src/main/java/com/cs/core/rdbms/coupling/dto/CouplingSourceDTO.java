package com.cs.core.rdbms.coupling.dto;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.data.Text;
import com.cs.core.rdbms.coupling.idto.ICouplingSourceDTO;
import com.cs.core.rdbms.dto.RootDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CouplingSourceDTO extends RootDTO implements ICouplingSourceDTO {

  String      id;
  Set<String> couplingTargets;

  @Override
  public String getId()
  {
    return id;
  }

  @Override
  public void setId(String id)
  {
    this.id = id;
  }

  @Override
  public Set<String> getCouplingTargets()
  {
    return couplingTargets;
  }

  @Override
  public void setCouplingTargets(Set<String> couplingTargets)
  {
    this.couplingTargets = couplingTargets;
  }

  @Override public boolean isNull()
  {
    return false;
  }

  @Override public ICSEElement toCSExpressID() throws CSFormatException
  {
    CSEObject object = (CSEObject) new CSEObject(ICSEElement.CSEObjectType.CouplingSource);
    List<String> collect = couplingTargets.stream().map(x -> x.replace(COUPLING_SPLITTER, ENTITY_PROPERTY_SPLITTER)).collect(Collectors.toList());
    object.setCode(this.id);
    object.setSpecification(Keyword.$target, Text.join(TARGET_SPLITTER, collect));
    return object;
  }

  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    CSEObject cs = (CSEObject) cse;
    String target = cs.getSpecification(Keyword.$target);
    List<String> targets =  Arrays.asList(target.split(TARGET_SPLITTER));
    couplingTargets.addAll(targets.stream().map(x -> x.replace(COUPLING_SPLITTER, ENTITY_PROPERTY_SPLITTER)).collect(Collectors.toList()));
  }
}
