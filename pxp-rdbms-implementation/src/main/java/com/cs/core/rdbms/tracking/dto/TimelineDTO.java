package com.cs.core.rdbms.tracking.dto;

import com.cs.core.csexpress.CSEList;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEList;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.rdbms.idto.IRootDTO;

import java.util.*;

/**
 * @author vallee
 */
public final class TimelineDTO implements ITimelineDTO {
  
  private static final String                       LOCALES_SCHEMA = "locales";
  private final Map<ChangeCategory, List<IRootDTO>> changedDTOs    = new HashMap<>();
  private final Map<ChangeCategory, ICSEList>       timelineData   = new HashMap<>();
  private final List<String>                        localeSchema   = new ArrayList<>();
  
  /**
   * Default empty constructor
   */
  public TimelineDTO()
  {
  }
  
  /**
   * Constructor with dtos to initialize in a category
   *
   * @param <T>
   * @param category
   * @param dtos
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public <T extends IRootDTO> TimelineDTO(ChangeCategory category, Collection<T> dtos)
      throws CSFormatException
  {
    register(category, dtos);
  }

  /**
   * Constructor with dtos to initialize in a category
   *
   * @param <T>
   * @param category
   * @param dtos
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public <T extends IRootDTO> TimelineDTO(ChangeCategory category, T... dtos)
      throws CSFormatException
  {
    register(category, dtos);
  }

  @Override
  public List<String> getInheritanceSchema()
  {
    return localeSchema;
  }
  
  @Override
  public ITimelineDTO setInheritanceSchema(List<String> schema)
  {
    localeSchema.clear();
    localeSchema.addAll(schema);
    return this;
  }
  
  private <T extends IRootDTO> void registerDTO(ChangeCategory category, T dto)
      throws CSFormatException
  {
    if (!timelineData.containsKey(category)) {
      timelineData.put(category, new CSEList());
      changedDTOs.put(category, new ArrayList<>());
    }
    timelineData.get(category)
        .addElement(dto.toCSExpressID());
    changedDTOs.get(category)
        .add(dto);
  }
  
  @Override
  public ITimelineDTO register(ChangeCategory category, ICSEObject cseObject)
  {
    if (!timelineData.containsKey(category)) {
      timelineData.put(category, new CSEList());
      changedDTOs.put(category, new ArrayList<>());
    }
    timelineData.get(category).addElement(cseObject);
    return this;
  }
  
  
  
  @Override
  public <T extends IRootDTO> ITimelineDTO register(ChangeCategory category, Collection<T> dtos)
      throws CSFormatException
  {
    for (T dto : dtos)
      registerDTO(category, dto);
    return this;
  }
  
  @Override
  public <T extends IRootDTO> TimelineDTO register(ChangeCategory category, T... dtos)
      throws CSFormatException
  {
    for (T dto : dtos)
      registerDTO(category, dto);
    return this;
  }
  
  @Override
  public ITimelineDTO register(ChangeCategory category, ICSEList elements)
  {
    if (timelineData.containsKey(category))
      timelineData.get(category).getSubElements().addAll(elements.getSubElements());
    else
      timelineData.put(category, (CSEList) elements);
    return this;
  }
  
  @Override
  public ITimelineDTO register(ChangeCategory category, IPXON.PXONMeta meta, String metaText)
  {
    if (!timelineData.containsKey(category))
      timelineData.put(category, new CSEList());
    timelineData.get(category).addMeta(meta, metaText);
    return this;
  }
  
  @Override
  public Collection<ChangeCategory> getCategories()
  {
    return timelineData.keySet();
  }
  
  @Override
  public boolean contains(ChangeCategory category)
  {
    return timelineData.containsKey(category);
  }
  
  @Override
  public ICSEList getElements(ChangeCategory category) throws CSFormatException
  {
    return timelineData.get(category);
  }
  
  /**
   * @param category
   *          a category of change
   * @return the list of DTOs registered for the category of change
   */
  public List<IRootDTO> getRegisteredList(ChangeCategory category)
  {
    if (!timelineData.containsKey(category))
      return new ArrayList<>();
    return changedDTOs.get(category);
  }
  
  @Override
  public void fromJSON(String json) throws CSFormatException
  {
    timelineData.clear();
    JSONContentParser parser = new JSONContentParser(json);
    Iterator<String> catItr = parser.toJSONObject().keySet().iterator();
    while (catItr.hasNext()) {
      String catStr = catItr.next();
      if (catStr.equals(LOCALES_SCHEMA))
        continue;
      CSEList elements = (CSEList) (new CSEParser()).parseDefinition(parser.getString(catStr));
      timelineData.put(ChangeCategory.valueOf(catStr), elements);
    }
    localeSchema.clear();
    parser.getJSONArray(LOCALES_SCHEMA)
        .forEach((localeID) -> {
          localeSchema.add((String) localeID);
        });
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    Iterator<ChangeCategory> catItr = timelineData.keySet()
        .iterator();
    StringBuffer[] fields = new StringBuffer[timelineData.keySet().size() + 1];
    for (int i = 0; catItr.hasNext(); i++) {
      ChangeCategory category = catItr.next();
      ICSEList elements = timelineData.get(category);
      if (!elements.isEmpty())
        fields[i] = JSONBuilder.newJSONField(category.toString(), elements.toString(), true);
      else
        fields[i] = JSONBuilder.VOID_FIELD;
    }
    fields[timelineData.keySet().size()] = !localeSchema.isEmpty()
            ? JSONBuilder.newJSONStringArray(LOCALES_SCHEMA, localeSchema)
            : JSONBuilder.VOID_FIELD;
    return JSONBuilder.assembleJSONBuffer(fields);
  }

  @Override
  public void clear()
  {
    changedDTOs.clear();
    timelineData.clear();
    localeSchema.clear();
  }
  
  @Override
  public Map<ChangeCategory, ICSEList> getTimelines()
  {
    return timelineData;
  }
  
}
