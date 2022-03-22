package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO;
import com.cs.core.rdbms.entity.idto.IAggregationResultDTO;

import java.util.Map;

public class AggregationResultDTO implements IAggregationResultDTO {

  public String            code;
  public Map<String, Long> count;


  @Override
  public Map<String, Long> getCount()
  {
    return count;
  }

  @Override
  public void setCount(Map<String, Long> count)
  {
    this.count = count;
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

}
