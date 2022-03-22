import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import { view as ContentFilterElementsView } from './content-filter-elements-view';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as ItemListPanelView } from '../../../../../viewlibraries/itemListPanelView/item-list-panel-view';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  CONTENT_FILTER_BUTTON_CLICKED: "content_filter_button_clicked",
  CONTENT_FILTER_CLEAR_BUTTON_CLICKED: "content_filter_clear_button_clicked",
  HANDLE_ADVANCED_SEARCH_CANCEL_CLICKED: "handle_advanced_search_cancel_clicked"
};

const oPropTypes = {
  advancedSearchPanelData: ReactPropTypes.object,
  appliedFilterData: ReactPropTypes.array,
  appliedFilterDataClone: ReactPropTypes.array,
  masterAttributeList: ReactPropTypes.array,
  filterContext: ReactPropTypes.object.isRequired,
  selectedHierarchyContext: ReactPropTypes.string,
  context:ReactPropTypes.string
};

// @CS.SafeComponent
class AdvancedSearchPanelView extends React.Component {
  static propTypes = oPropTypes;

  constructor(props) {
    super(props);

    this.advancedSearchInput = React.createRef();
  }

  state = {
    isAttributeCollapsed: false,
    isTagCollapsed: false,
    isRoleCollapsed: false,
  };

  componentDidMount() {
    this.setSearchInputFromProps();
  }

  componentDidUpdate() {
    this.setSearchInputFromProps();
  }

  setSearchInputFromProps = () => {
    var oAdvancedSearchInputDOM = this.advancedSearchInput.current || {};
    var oAdvancedSearchPanelData = this.props.advancedSearchPanelData || {};
    oAdvancedSearchInputDOM.value = oAdvancedSearchPanelData.searchInput || "";
  };

  handleRequestPopoverCancel = () => {
    EventBus.dispatch(oEvents.HANDLE_ADVANCED_SEARCH_CANCEL_CLICKED, this.props.filterContext);
  };

  handleApplySearchClicked = (bIsEmptySelectedItems, bIsFilterDirty) => {
    let sSelectedHierarchyContext = this.props.selectedHierarchyContext;
    if(!bIsFilterDirty){
      return;
    }
    EventBus.dispatch(oEvents.CONTENT_FILTER_BUTTON_CLICKED, sSelectedHierarchyContext, this.props.filterContext);
  };

  handleClearSearchClicked = (bIsEmptySelectedItems) => {
    if(bIsEmptySelectedItems){
      return;
    }
    EventBus.dispatch(oEvents.CONTENT_FILTER_CLEAR_BUTTON_CLICKED, this.props.filterContext);
  };

  handleButtonClick = (sButtonId) => {
    if(sButtonId === "apply") {
      var aAppliedFilterData = this.getAppliedFilterData();
      var oAdvancedSearchPanelData = this.props.advancedSearchPanelData;
      var bIsFilterDirty = oAdvancedSearchPanelData.isFilterDirty;
      var bIsEmptySelectedItems = (CS.isEmpty(aAppliedFilterData));
      this.handleApplySearchClicked(bIsEmptySelectedItems,bIsFilterDirty);
    }
    else if (sButtonId === "clear") {
      this.handleClearSearchClicked();
    } else {
      this.handleRequestPopoverCancel();
    }
  };

  getChildContext() {
    var oLoadedTags = this.props.advancedSearchPanelData.loadedTags;
    return {
      masterTagList: oLoadedTags
    };
  }

  getAppliedFilterData = () => {
    /** plz ALWAYS use "NULL" check for 'appliedFilterDataClone' instead of CS.isEmpty or empty array check*/
    return (this.props.appliedFilterDataClone != null) ? this.props.appliedFilterDataClone : this.props.appliedFilterData;
  };

  render() {
    var aAppliedFilterData = this.getAppliedFilterData();
    var oAdvancedSearchPanelData = this.props.advancedSearchPanelData;
    var bShowAdvancedSearchPanel = oAdvancedSearchPanelData.showAdvancedSearchPanel;
    var bIsFilterDirty = oAdvancedSearchPanelData.isFilterDirty;
    var aUserList = oAdvancedSearchPanelData.masterUserList;

    var bIsEmptySelectedItems = (CS.isEmpty(aAppliedFilterData));

    var oBodyStyle = {
      overflow: "hidden",
      padding: "15px 24px 15px 24px"
    };

    var oContentStyle = {//main dialog (container style)
      padding: "0",
      overflow: "hidden",
      width: '90%',
      maxWidth: "90%",
    };

    var oCancelButtonLabelStyles = {
      fontSize: 12,
      lineHeight: "30px"
    };
    if(!bIsEmptySelectedItems) {
      oCancelButtonLabelStyles.color = "black";
    }

    var aButtonData=[
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isFlat: true,
        isDisabled: false
      },
      {
        id: "clear",
        label: getTranslation().CLEAR,
        isFlat: true,
        isDisabled: bIsEmptySelectedItems
      },
    ];

    let sButtonId = bIsFilterDirty ? "apply" : "ok";
    let sButtonLabel = bIsFilterDirty ? getTranslation().APPLY : getTranslation().OK;
    let fButtonHandler = this.handleButtonClick;

    aButtonData.push({
            id: sButtonId,
            label: sButtonLabel,
            isFlat: false,
        });

    return (
        <CustomDialogView
            open={bShowAdvancedSearchPanel}
            bodyStyle={oBodyStyle}
            contentStyle={oContentStyle}
            modal={true}
            buttonData={aButtonData}
            onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
            buttonClickHandler= {fButtonHandler}
            bodyClassName="advancedFilterDialogModel"
            contentClassName="advancedFilterDialog"
            title={getTranslation().ADVANCED_FILTERS}>
          <div className={"advancedSearchPanelMainWrapper"}>
            <div className="advancedSearchPanelBody">
                <ItemListPanelView
                  data={oAdvancedSearchPanelData}
                  context="advancedSearch"
                  filterContext={this.props.filterContext}
                />
              <div className="advanceSearchPanelViewWrapper">
              <div className="bulkEditRightPanelView">
                <ContentFilterElementsView
                    appliedFilterData={this.getAppliedFilterData()}
                    appliedFilterCollapseStatusMap={oAdvancedSearchPanelData.appliedFilterCollapseStatusMap}
                    userList={aUserList}
                    loadedAttributes={oAdvancedSearchPanelData.loadedAttributes}
                    loadedTags={oAdvancedSearchPanelData.loadedTags}
                    loadedRoles={oAdvancedSearchPanelData.loadedRoles}
                    filterContext={this.props.filterContext}
                    context = {this.props.context}
                    showDefaultIcon={true}
                />
              </div>
              </div>
            </div>
          </div>
        </CustomDialogView>

    );
  }
}



export const  view = AdvancedSearchPanelView;
export const  events  = oEvents;
