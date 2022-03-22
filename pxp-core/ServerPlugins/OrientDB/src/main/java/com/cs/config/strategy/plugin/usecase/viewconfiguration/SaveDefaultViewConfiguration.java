package com.cs.config.strategy.plugin.usecase.viewconfiguration;

import java.util.Map;

import com.orientechnologies.orient.server.config.OServerCommandConfiguration;

public class SaveDefaultViewConfiguration extends SaveViewConfiguration {

	public SaveDefaultViewConfiguration(OServerCommandConfiguration iConfiguration) {
		super(iConfiguration);
	}

	@Override
	public String[] getNames() {
		return new String[] { "POST|SaveDefaultViewConfiguration/*" };
	}

	@Override
	protected Object execute(Map<String, Object> requestMap) throws Exception {

		return super.execute(requestMap);
	}

}
