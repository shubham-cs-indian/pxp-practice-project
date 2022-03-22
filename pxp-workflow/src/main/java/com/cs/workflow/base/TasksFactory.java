package com.cs.workflow.base;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("tasksFactory")
public class TasksFactory {
	public static final String PROCESS_START_EVENT = "processStartEvent";
	public static final String PROCESS_END_EVENT = "processEndEvent";
	@Autowired
	private Map<String, IAbstractTask> tasksMap;

	public IAbstractTask getTask(String taskId) {
		return tasksMap.get(taskId);
	}

	public Set<String> getTaskIds() {
		return tasksMap.keySet();
	}

	public IAbstractTask getStartEvent() {
		return tasksMap.get(PROCESS_START_EVENT);
	}

	public IAbstractTask getEndEvent() {
		return tasksMap.get(PROCESS_END_EVENT);
	}
}
