import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as ContextMenuViewNew } from '../../../../../viewlibraries/contextmenuwithsearchview/context-menu-view-new';
import ContextMenuViewModel from '../../../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';

const oEvents = {
  CUSTOM_TEMPLATE_DETAIL_VIEW_SELECTED_ENTITY_CROSS_ICON_CLICKED: "custom_template_detail_view_selected_entity_cross_icon_clicked",
  CUSTOM_TEMPLATE_DETAIL_VIEW_SELECTED_ENTITIES_CHANGED: "custom_template_detail_view_selected_entities_changed",
  CUSTOM_TEMPLATE_DETAIL_VIEW_POPOVER_OPEN: "custom_template_detail_view_popover_open",
  CUSTOM_TEMPLATE_DETAIL_VIEW_SEARCH_CLICKED: "custom_template_detail_view_search_clicked",
  CUSTOM_TEMPLATE_DETAIL_VIEW_LOAD_MORE_CLICKED: "custom_template_detail_view_load_more_clicked"
};

const oPropTypes = {
  sectionData: ReactPropTypes.arrayOf(
      ReactPropTypes.shape({
        id: ReactPropTypes.string,
        label: ReactPropTypes.string,
        masterList: ReactPropTypes.arrayOf(
            ReactPropTypes.shape({
              id: ReactPropTypes.string,
              label: ReactPropTypes.string
            })),
        selectedEntityList: ReactPropTypes.arrayOf(
            ReactPropTypes.shape({
              id: ReactPropTypes.string,
              label: ReactPropTypes.string
            }))
      })
  )
};

// @CS.SafeComponent
class CustomTemplateDetailView extends React.Component {

  constructor (props) {
    super(props);
    this.state = {
      searchText: "",
      openPopoverEntityId: ""
    };

  }

  handleCrossIconClicked (sContext, sId) {
    EventBus.dispatch(oEvents.CUSTOM_TEMPLATE_DETAIL_VIEW_SELECTED_ENTITY_CROSS_ICON_CLICKED, sContext, sId);
  }

  searchHandler = (sContext, sSearchText) => {
    this.setState({searchText: sSearchText});
    let aEntityList = [sContext];
    EventBus.dispatch(oEvents.CUSTOM_TEMPLATE_DETAIL_VIEW_SEARCH_CLICKED, aEntityList, sSearchText);
  };

  loadMoreHandler (sContext) {
    let aEntityList = [sContext];
    EventBus.dispatch(oEvents.CUSTOM_TEMPLATE_DETAIL_VIEW_LOAD_MORE_CLICKED, aEntityList);
  }

  onApplyHandler (aPreviousSelectedItems, sId, aSelectedItems) {
    this.setState({searchText: "", openPopoverEntityId: ""});
    if (!CS.isEqual(aSelectedItems, aPreviousSelectedItems)) {
      EventBus.dispatch(oEvents.CUSTOM_TEMPLATE_DETAIL_VIEW_SELECTED_ENTITIES_CHANGED, aSelectedItems, sId);
    }

  }

  onPopOverClose = () => {
    this.setState({searchText: "", openPopoverEntityId: ""});
  };

  onPopOverOpen (sContext) {
    EventBus.dispatch(oEvents.CUSTOM_TEMPLATE_DETAIL_VIEW_POPOVER_OPEN, sContext);
  }

  addEntityButtonOnClickHandler = (sId) => {
    this.setState({openPopoverEntityId: sId});
  };

  getSelectedEntityListView (sId, aList) {
    let aListView = [];
    let _this = this;
    CS.forEach(aList, function (oListItem) {
      aListView.push(
          <div className="selectedEntityWrapper">
            <div className="selectedEntityLabel">
              {oListItem.label}
            </div>
            <TooltipView label={getTranslation().REMOVE}>
              <div className="removeIcon" onClick={_this.handleCrossIconClicked.bind(_this, sId, oListItem.id)}>
              </div>
            </TooltipView>
          </div>
      );
    });
    return aListView;
  }

  getCustomTemplateAddEntityButton (sLabel, sId, aMasterList, aSelectedItems) {
    let aContextMenuViewModel = [];

    CS.forEach(aMasterList, function (oMasterListItem) {
      let oProperties = {
        context: "template",
        code: oMasterListItem.code || ""
      };

      aContextMenuViewModel.push(new ContextMenuViewModel(
          oMasterListItem.id,
          oMasterListItem.label,
          false,
          oMasterListItem.icon,
          oProperties
      ))
    });
    var oAnchorOrigin = {horizontal: 'left', vertical: 'bottom'};
    var oTargetOrigin = {horizontal: 'left', vertical: 'top'};
    return (
        <ContextMenuViewNew
            selectedItems={aSelectedItems}
            isMultiselect={true}
            onApplyHandler={this.onApplyHandler.bind(this, aSelectedItems, sId)}
            showSelectedItems={true}
            showColor={true}
            anchorOrigin={oAnchorOrigin}
            targetOrigin={oTargetOrigin}
            useAnchorElementWidth={true}
            disabled={false}
            searchText={this.state.searchText}
            searchHandler={this.searchHandler.bind(this, sId)}
            onPopOverOpenedHandler={this.onPopOverOpen.bind(this, sId)}
            contextMenuViewModel={aContextMenuViewModel}
            showPopover={this.state.openPopoverEntityId === sId}
            showHidePopoverHandler={this.onPopOverClose}
            className= 'customTemplateDropDown'
            loadMoreHandler={this.loadMoreHandler.bind(this, sId)}
        >
          <TooltipView label={sLabel}>
            <div className="customTemplateAddEntityButton" onClick={this.addEntityButtonOnClickHandler.bind(this, sId)}>
            </div>
          </TooltipView>
        </ContextMenuViewNew>)

  }

  getCustomTemplateDetailView () {
    let aSectionDetails = [];
    let _this = this;
    let aSectionData = this.props.sectionData;

    CS.forEach(aSectionData, function (oData) {
      let sLabelForAddButton = getTranslation().ADD + ' ' + oData.label;
      let aSelectedItemList = CS.map(oData.selectedEntityList, 'id');
      let oCustomTemplateAddEntityButton = _this.getCustomTemplateAddEntityButton(sLabelForAddButton, oData.id, oData.masterList, aSelectedItemList);
      let oSelectedEntityList = _this.getSelectedEntityListView(oData.id, oData.selectedEntityList);
      aSectionDetails.push(
          <div className="customTemplateDetailSectionWrapper">
            <div className="customTemplateDetailSectionHeaderWrapper">
              <div className="customTemplateTitle">
                {oData.label}
              </div>
              <div className="customTemplateAddEntityButtonWrapper">
                {oCustomTemplateAddEntityButton}
              </div>
            </div>
            <div className="selectedEntityList">
              {oSelectedEntityList}
            </div>
          </div>
      )
    });
    return aSectionDetails;
  }


  render () {
    let oCustomTemplateDetailView = this.getCustomTemplateDetailView();
    return (
        <div className="customTemplateSectionContainer">
          {oCustomTemplateDetailView}
        </div>);

  }
}

CustomTemplateDetailView.propTypes = oPropTypes;

export const view = CustomTemplateDetailView;
export const events = oEvents;
