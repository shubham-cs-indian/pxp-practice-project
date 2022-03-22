package com.cs.workflow.base;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EventType {
	BUSINESS_PROCESS, INTEGRATION, APPLICATION ;
	public final static List<String> EVENT_TYPE_LIST = Stream.of(EventType.values())
            .map(Enum::name)
            .collect(Collectors.toList());
}
  
