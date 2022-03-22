
import CS from '../../../../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { view as VisualSectionView } from './visual-section-view';
import { view as ClassAvailableListSearchView } from '../../../../../viewlibraries/classavailablelistsearchview/class-available-list-search-view';

const oEvents = {
  CLASS_CONFIG_SECTION_TEMPLATE_ADD_CLICKED: "class_config_section_template_add_clicked",
  CLASS_CONFIG__SELECT_DEFAULT_SECTION: "class_config__select_default_section"
};

const oPropTypes = {
  activeClass: ReactPropTypes.object,
  dragStatus: ReactPropTypes.object,
  attributeListModel: ReactPropTypes.array,
  addedTagList: ReactPropTypes.array,
  selectedSectionId: ReactPropTypes.string,
  availableAttributeListModel: ReactPropTypes.array,
  availableMandatoryAttributeListModel: ReactPropTypes.array,
  availableTagListModel: ReactPropTypes.array,
  availableRoleListModel: ReactPropTypes.array,
  masterEntitiesForSection: ReactPropTypes.object,
  context: ReactPropTypes.string,
  tagList: ReactPropTypes.array,
  isAddSectionIconVisible: ReactPropTypes.bool

};

// @CS.SafeComponent
class ClassConfigSectionView extends React.Component {
  static propTypes = oPropTypes;

  componentDidMount() {
    this.updateChanges();
  }

  componentDidUpdate() {
    this.updateChanges();
  }

  updateChanges = () => {
    var sSelectedSectionId = this.props.selectedSectionId;
    var oActiveClass = this.props.activeClass;
    var aSections = oActiveClass.sections;

    if(CS.isEmpty(sSelectedSectionId) && aSections.length > 1) {
      var sFirstSection = aSections[0].id;
      EventBus.dispatch(oEvents.CLASS_CONFIG__SELECT_DEFAULT_SECTION, this, sFirstSection);
    }
  };

  getDraggableElementsNew = () => {
    var __props = this.props;

    return (
        <ClassAvailableListSearchView
            availableAttributeListModel={__props.availableAttributeListModel}
            availableRoleListModel={__props.availableRoleListModel}
            availableTagListModel={__props.availableTagListModel}
        />
    );
  };

  getSections = () => {
    var oActiveClass = this.props.activeClass;
    var aSections = oActiveClass.sections;
    var aSectionView = [];
    var visualAttributeDragStatus = this.props.dragStatus;

    CS.forEach(aSections, function (oSection, iIndex) {
      aSectionView.push(
          <VisualSectionView
            key={iIndex}
            section={oSection}
            activeClass={oActiveClass}
            attributeListModel={this.props.attributeListModel}
            visualAttributeDragStatus={visualAttributeDragStatus}
            addedTagList={this.props.addedTagList}
            selectedSectionId={this.props.selectedSectionId}
            masterEntitiesForSection={this.props.masterEntitiesForSection}
            tagList={this.props.tagList}
            klassesForRelationship = {this.props.klassesForRelationship}
          />
      );
    }.bind(this));

    return aSectionView;
  };

  handleTemplateAddButtonClicked = () => {
    EventBus.dispatch(oEvents.CLASS_CONFIG_SECTION_TEMPLATE_ADD_CLICKED, this);
  };

  render() {

    var aAvailableElements = this.getDraggableElementsNew();
    var aSectionList = this.getSections();
    var bIsAddSectionIconVisible = this.props.isAddSectionIconVisible;
    var sAddSectionButtonClassName = "classSectionHeaderButton addTemplate ";
    sAddSectionButtonClassName += bIsAddSectionIconVisible ? "" : "noShow ";

    return (
        <div className="classSectionContainer">
          <div className="classSectionHeader">
            <div className="classSectionHeaderTitle">{getTranslation().SECTION}</div>
            <div className={sAddSectionButtonClassName} title={getTranslation().ADD_SECTION} onClick={this.handleTemplateAddButtonClicked}></div>
          </div>
          <div className="classElementListContainer">{aAvailableElements}</div>
          <div className="classSectionListContainer">{aSectionList}</div>
        </div>
    );
  }
}

export const view = ClassConfigSectionView;
export const events = oEvents;
