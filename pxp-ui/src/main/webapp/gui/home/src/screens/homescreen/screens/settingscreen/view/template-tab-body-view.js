import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as ContextMenuViewNew } from './../../../../../viewlibraries/contextmenuwithsearchview/context-menu-view-new';
import DragViewModel from './../../../../../viewlibraries/dragndropview/model/drag-view-model';
import { view as DragView } from './../../../../../viewlibraries/dragndropview/drag-view.js';
import DropViewModel from './../../../../../viewlibraries/dragndropview/model/drop-view-model';
import { view as DropView } from './../../../../../viewlibraries/dragndropview/drop-view.js';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import ViewUtils from './utils/view-utils';

const oEvents = {
  TEMPLATE_SECTION_ACTION_BAR_CLICKED: "template_section_action_bar_clicked",
  TEMPLATE_SECTION_ADDED: "template_section_added",
  TEMPLATE_SECTION_DROP_DOWN_SEARCH_LOAD_MORE_CLICKED: "template_section_property_collection_search_load_more_clicked"
};

const oPropTypes = {
  sectionsData: ReactPropTypes.object,
  context: ReactPropTypes.string,
  currentTab: ReactPropTypes.string
};

// @CS.SafeComponent
class TemplateTabBodyView extends React.Component {
  static propTypes = oPropTypes;

  state = {
    showSectionDropdown: false
  };

  handleSearchAndLoadMore = (oData) => {
    EventBus.dispatch(oEvents.TEMPLATE_SECTION_DROP_DOWN_SEARCH_LOAD_MORE_CLICKED, oData);
  };

  handleOnDropDownLoadMore = (sEntity) => {
    var oData = {
      loadMore: true,
      entities: [sEntity]
    };
    this.handleSearchAndLoadMore(oData);
  };

  handleOnDropDownSearch = () => {
    var oData = {
      searchText: "",
      entities: []
    };
    this.handleSearchAndLoadMore(oData);
  };

  handleActionBarClicked = (sButtonType, sTemplateId) => {
    EventBus.dispatch(oEvents.TEMPLATE_SECTION_ACTION_BAR_CLICKED, sButtonType, sTemplateId);
  };

  getDragViewForSection = (oDragViewModel, sSectionId) => {
    if (!CS.isEmpty(oDragViewModel)) {
      return (
          <DragView model={oDragViewModel} key={sSectionId}>
          </DragView>
      );
    }
  };

  getSectionRows = () => {
    var _this = this;
    var oSectionsData = this.props.sectionsData;
    var aSections = oSectionsData.sections;
    var aSectionMasterList = oSectionsData.sectionMasterList;
    var oDragDetails = oSectionsData.dragDetails["dragTemplateSection"] || {};
    var aSectionViews = CS.map(aSections, function (sSectionId) {
      var oSection = CS.find(aSectionMasterList, {id: sSectionId}) || {};
      var oSectionIconView = oSection.icon ?
          (<div className="sectionIcon"><img src={ViewUtils.getIconUrl(oSection.icon)}/></div>) :
          (<div className="sectionIcon noIcon"></div>);
      var oDragViewModel = new DragViewModel(sSectionId, oSection.label, true, "dragTemplateSection", {className: "dragIcon"});
      var oDragView = _this.getDragViewForSection(oDragViewModel, sSectionId);
      var oDropProperties = {
        dragStatus: !!oDragDetails.dragStatus
      };
      var oDropModel = new DropViewModel(sSectionId, oDragDetails.draggedSectionId != sSectionId, "dragTemplateSection", oDropProperties);
      return (
          <DropView model={oDropModel} key={sSectionId}>
            <div className="section">
              {oDragView}
              {oSectionIconView}
              <div className="sectionLabel">{oSection.label}</div>
              <div className="actionButtons">
                <TooltipView label={getTranslation().MOVE_UP}>
                  <div className="actionButton up"
                       onClick={_this.handleActionBarClicked.bind(_this, "up", sSectionId)}></div>
                </TooltipView>
                <TooltipView label={getTranslation().MOVE_DOWN}>
                  <div className="actionButton down"
                       onClick={_this.handleActionBarClicked.bind(_this, "down", sSectionId)}></div>
                </TooltipView>
                {oSectionsData.isCustom ? <TooltipView label={getTranslation().DELETE}>
                      <div className="actionButton delete"
                           onClick={_this.handleActionBarClicked.bind(_this, "delete", sSectionId)}></div>
                    </TooltipView> : null}
              </div>
            </div>
          </DropView>
      );
    });
    return aSectionViews;
  };

  addSectionFromDropdown = (oModel) => {
    var sSectionId = oModel.id;
    var sContext = oModel.properties["context"];
    EventBus.dispatch(oEvents.TEMPLATE_SECTION_ADDED, sSectionId, sContext);
    this.setState({
      showSectionDropdown: false
    });
  };

  showSectionDropdown = () => {
    this.setState({
      showSectionDropdown: true
    });
  };

  render() {
    var oSectionsData = this.props.sectionsData;
    var sSectionName = oSectionsData.sectionName;
    var oTabBodyView = null;
    if (sSectionName) {
      var aSectionViews = this.getSectionRows();
      var aDropdownModels = oSectionsData.dropdownSectionModels;
      var sSearchText = ViewUtils.getEntitySearchText();
      var oAddSectionView = oSectionsData.isCustom ?
          [<ContextMenuViewNew
                searchText={sSearchText}
                searchHandler={this.handleOnDropDownSearch}
                loadMoreHandler={this.handleOnDropDownLoadMore}
                contextMenuViewModel={aDropdownModels}
                onClickHandler={this.addSectionFromDropdown}>
              <div className="addTemplateSection" onClick={this.showSectionDropdown}>{sSectionName}</div>
            </ContextMenuViewNew>
          ] :
          null;
      oTabBodyView = (
          <div className="tabBody">
            {oAddSectionView}
            <div className="sections">
              {aSectionViews}
            </div>
          </div>
      );
    }
    return oTabBodyView;
  }
}

export const view = TemplateTabBodyView;
export const events = oEvents;
