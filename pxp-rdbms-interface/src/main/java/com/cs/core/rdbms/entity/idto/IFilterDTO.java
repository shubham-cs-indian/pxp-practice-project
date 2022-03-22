package com.cs.core.rdbms.entity.idto;

import org.apache.commons.lang3.Range;

import java.io.Serializable;

public interface IFilterDTO extends Serializable {

  FilterType getType();

  ValueType getValueType();

  Object getValue();

  enum FilterType {
    exact, start, end, empty, notempty, lt, gt, range, contains;
  }

  enum ValueType {
    Range(Range.class), String(String.class), Numeric(Number.class);

    Class value;

    ValueType(Class rangeClass)
    {
      value = rangeClass;
    }

    public Class getValue()
    {
      return value;
    }

  }

}
