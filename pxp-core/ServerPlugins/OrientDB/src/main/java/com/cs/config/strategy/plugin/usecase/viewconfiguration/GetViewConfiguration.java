package com.cs.config.strategy.plugin.usecase.viewconfiguration;

import java.util.ArrayList;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.exception.themeconfiguration.ThemeConfigurationNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetViewConfiguration extends AbstractOrientPlugin {

	public GetViewConfiguration(OServerCommandConfiguration iConfiguration) {
		super(iConfiguration);
	}

	@Override
	public String[] getNames() {
		return new String[] { "POST|GetViewConfiguration/*" };
	}

	@Override
	protected Object execute(Map<String, Object> requestMap) throws Exception {

		Vertex viewConfigurationVertex = null;
		try {
			viewConfigurationVertex = UtilClass.getVertexByIndexedId(SystemLevelIds.VIEW_CODE,
					VertexLabelConstants.VIEW_CONFIGURATION);
		} catch (NotFoundException e) {
			throw new ThemeConfigurationNotFoundException(e);
		}

		Map<String, Object> viewConfiguration = UtilClass.getMapFromVertex(new ArrayList<String>(),
				viewConfigurationVertex);
		return viewConfiguration;
	}
}
