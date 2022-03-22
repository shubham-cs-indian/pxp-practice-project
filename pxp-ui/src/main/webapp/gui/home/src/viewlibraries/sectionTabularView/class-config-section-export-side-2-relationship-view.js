
import CS from '../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as SectionListView } from '../../viewlibraries/sectionListView/section-list-view';

const oEvents = {};

const oPropTypes = {
  sectionLabel: ReactPropTypes.string,
  context: ReactPropTypes.string,
  isInherited: ReactPropTypes.bool,
  isCollapsed: ReactPropTypes.bool,
  side2RelationshipData: ReactPropTypes.object,
  sectionToggleButtonClicked: ReactPropTypes.func,
};


// @CS.SafeComponent
class ClassConfigSectionExportSide2RelationshipView extends React.Component{

  constructor(props) {
    super(props);
  }

  handleSectionExpandToggle = () => {
    let _props = this.props;
    if(_props.sectionToggleButtonClicked) {
      let sSectionId = _props.sectionId;
      let sContextId =_props.context;
      _props.sectionToggleButtonClicked(sContextId, sSectionId);
    }
  };

  render() {
    let _props = this.props;
    let oSectionsView = _props.isCollapsed ? null : <SectionListView elementData = {_props.side2RelationshipData}/>;

    let sSectionToggleClass = "sectionToggle sectionExpanded";
    if (_props.isCollapsed) {
      sSectionToggleClass = "sectionToggle sectionCollapsed";
    }

    return (
          <div className="classConfigTabularPermissionContainer">
            <div className="classConfigPermissionContainerHeader">
              <div className={sSectionToggleClass} onClick={this.handleSectionExpandToggle}></div>
              <span className="sectionName">{_props.sectionLabel}</span>
            </div>
            <div className="classConfigPermissionContainerBody">
              <div className="permissionSectionListContainer">
                {oSectionsView}
              </div>
            </div>
          </div>
    );
  }
}

ClassConfigSectionExportSide2RelationshipView.propTypes = oPropTypes;

export const view = ClassConfigSectionExportSide2RelationshipView;
export const events = oEvents;
