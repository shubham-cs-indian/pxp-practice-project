package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

import com.cs.core.bgprocess.idto.IEmbeddedInheritanceInTaxonomyDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class EmbeddedInheritanceInTaxonomyDTO extends SimpleDTO implements IEmbeddedInheritanceInTaxonomyDTO {
  
  private final String ID                    = "id";
  private final String CODE                  = "code";
  private final String LABEL                 = "label";
  private final String ADDED_CONTEXT_KLASSES = "addedContextKlasses";
  
  private String       id;
  private String       code;
  private String       label;
  private List<String> addedContextKlasses   = new ArrayList<>();
  
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
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public List<String> getAddedContextKlasses()
  {
    return addedContextKlasses;
  }
  
  @Override
  public void setAddedContextKlasses(List<String> addedContextKlasses)
  {
    this.addedContextKlasses = addedContextKlasses;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONStringArray(ADDED_CONTEXT_KLASSES, addedContextKlasses),
        JSONBuilder.newJSONField(ID, id), JSONBuilder.newJSONField(CODE, code), JSONBuilder.newJSONField(LABEL, label));
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    addedContextKlasses.clear();
    JSONArray jsonArray = json.getJSONArray(ADDED_CONTEXT_KLASSES);
    for (Object jsonV : jsonArray) {
      addedContextKlasses.add(jsonV.toString());
    }
    id = json.getString(ID);
    code = json.getString(CODE);
    label = json.getString(LABEL);
  }
}
