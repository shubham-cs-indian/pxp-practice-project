package com.cs.config.strategy.plugin.usecase.themeandviewconfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.exception.themeconfiguration.ThemeConfigurationNotFoundException;
import com.cs.core.config.interactor.exception.viewconfiguration.ViewConfigurationNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetAdminConfiguration extends AbstractOrientPlugin {

	public GetAdminConfiguration(OServerCommandConfiguration iConfiguration) {
		super(iConfiguration);
	}

	@Override
	public String[] getNames() {
		return new String[] { "POST|GetAdminConfiguration/*" };
	}

	@Override
	protected Object execute(Map<String, Object> requestMap) throws Exception {
		List<String> fieldsToFetch = new ArrayList<String>();
		Vertex themeConfigurationVertex = null;
		Vertex viewConfigurationVertex = null;
		try {
			themeConfigurationVertex = UtilClass.getVertexByIndexedId(SystemLevelIds.THEME_CODE,
					VertexLabelConstants.LOGO_CONFIGURATION);
		} catch (NotFoundException e) {
			throw new ThemeConfigurationNotFoundException(e);
		}
		
		try {
			viewConfigurationVertex = UtilClass.getVertexByIndexedId(SystemLevelIds.VIEW_CODE,
					VertexLabelConstants.VIEW_CONFIGURATION);
		} catch(NotFoundException e) {
			throw new ViewConfigurationNotFoundException(e);
		}
		//configuration fetched
		Map<String, Object> themeConfiguration = UtilClass.getMapFromVertex(fieldsToFetch,themeConfigurationVertex);
		Map<String, Object> viewConfiguration = UtilClass.getMapFromVertex(fieldsToFetch, viewConfigurationVertex);
		viewConfiguration.putAll(themeConfiguration);
		return viewConfiguration;
	}
}
