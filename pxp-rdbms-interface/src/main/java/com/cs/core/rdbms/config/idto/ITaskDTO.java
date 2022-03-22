package com.cs.core.rdbms.config.idto;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO representation of a task in configuration (synchronized from configuration DB)
 *
 * @author vallee
 */
public interface ITaskDTO extends IRootConfigDTO {

  /**
   * @return the code of the task
   */
  public default String getTaskCode() {
    return getCode();
  }

  ;
  
  /**
   * @return the type of the catalog or DI
   */
  public TaskType getTaskType();

  /**
   * Constant for task types
   */
  public enum TaskType {

    UNDEFINED, SHARED, PERSONAL;

    private static final TaskType[] values = values();

    public static TaskType valueOf(int ordinal) {
      return values[ordinal];
    }
  }

   enum TaskColor {
    a("#603D20"), b("#A07400"), c("#CC8A00"), d("#330072"), e("#5F3DA5"), f("#7D55C7"), g("#9678D3"), h("#FCDC8C"), i("#004C45"), j(
        "#007A53"), k("#40BC99"), l("#6ECEB2"), m("#862633"), n("#A50034"), o("#FAA4A0"), p("#CC0000"), q("#3F5429"), r("#949300"), s(
        "#B5BD00"), t("#D0DF00"), u("#00677F"), v("#009CA6"), w("#6AD1E3"), x("#B4E6F0"), y("#F1501A"), z("#F1701A"), aa("#F9A266"), ab(
        "#FED4A6"), ac("#5F5F5F"), ad("#969696"), ae("#B4B4B4"), af("#D2D2D2");

    final String code;

    TaskColor(String code)
    {
      this.code = code;
    }

    public static List<String> getColors(){
      return Arrays.stream(values()).map(x -> x.code).collect(Collectors.toList());
    }
  }
}

