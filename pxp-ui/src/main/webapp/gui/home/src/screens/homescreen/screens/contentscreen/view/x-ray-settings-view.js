import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactDOM from "react-dom";
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import { view as GroupMssWrapperView } from '../../../../../viewlibraries/filter/group-mss-wrapper-view';
import KeyboardNavigationForLists, { oCustomEvents } from '../../../../../commonmodule/HOC/keyboard-navigation-for-lists';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  HANDLE_X_RAY_PROPERTY_CLICKED: "handle_x_ray_property_clicked",
  HANDLE_SHOW_X_RAY_PROPERTY_GROUPS_CLICKED: "handle_show_x_ray_property_groups_clicked",
  HANDLE_X_RAY_PROPERTY_GROUP_CLICKED: "handle_x_ray_property_group_clicked",
  HANDLE_CLOSE_ACTIVE_X_RAY_PROPERTY_GROUP_CLICKED: "handle_close_active_x_ray_property_group_clicked",
};

const oPropTypes = {
  attributeMap: ReactPropTypes.object,
  tagMap: ReactPropTypes.object,
  searchText: ReactPropTypes.string,
  propertyGroupList: ReactPropTypes.array,
  activeXRayPropertyGroup: ReactPropTypes.object,
  onClose: ReactPropTypes.func,
  filterContext: ReactPropTypes.object.isRequired,
  relationshipId: ReactPropTypes.string
};

// @KeyboardNavigationForLists
// @CS.SafeComponent
class XRaySettingsView extends React.Component {
  constructor (props) {
    super(props);

    this.xRayPropertySearch = React.createRef();
    this.groupMss = React.createRef();

    this.setRef =( sRef, element) => {
      this[sRef] = element;
    };

    this.setRefForScrollbar =( sRef, element) => {
      let oElement = ReactDOM.findDOMNode(element);
      if(element) {
        this[sRef] = oElement.firstChild;
      }
    };

  }

  static propTypes = oPropTypes;
  static defaultProps = function() {}();

  state = {
    mode: 'group',
    searchInput: "",
    attributeCollapsed: true,
    tagCollapsed: true,
    showSelected: false, //for showing only the selected properties
    isDirty: false
  };

  componentDidMount() {
    var oXRaySearchInputDOM = this.searchBox;
    if (!CS.isEmpty(oXRaySearchInputDOM)) {
      oXRaySearchInputDOM.focus();
    }
    this.updateSearchInput();
    this.props.registerCustomEvent(oCustomEvents.SELECTION_OF_ITEM, this.handleXRayPropertyGroupClicked);
  }

  componentDidUpdate() {
    this.updateSearchInput();
  }

  updateSearchInput = () => {
    if (this.state.mode === "all") {
      var oSearchInputDOM = this.xRayPropertySearch.current;
      if (oSearchInputDOM) {
        oSearchInputDOM.value = this.props.searchText || "";
      }
    }
  };

  handleXRayPropertyApply = (sRelationshipId, oProperties) => {
    var aProperties = [];
    CS.each(oProperties, oProperty => {
      aProperties.push({id: oProperty.id, type: oProperty.groupType, label: oProperty.label})
    })

    EventBus.dispatch(oEvents.HANDLE_X_RAY_PROPERTY_CLICKED, aProperties, sRelationshipId, this.props.relationshipIdToFetchData, this.props.filterContext);
    this.props.onClose();
  };

  handleXRayPropertyGroupClicked = (iIndex) => {
    if (iIndex > -1) {
      let iId = this.props.propertyGroupList[iIndex].id;
      EventBus.dispatch(oEvents.HANDLE_X_RAY_PROPERTY_GROUP_CLICKED, iId, this.props.relationshipId, this.props.relationshipIdToFetchData, this.props.filterContext);
    }
  };

  handleCloseActiveXRayPropertyGroupClicked = (sRelationshipId, _this) => {
    EventBus.dispatch(oEvents.HANDLE_CLOSE_ACTIVE_X_RAY_PROPERTY_GROUP_CLICKED, sRelationshipId, this.props.relationshipIdToFetchData, this.props.filterContext);
    _this.groupMss.current.setState({}) // Resetting group mss view
  };

  handleSearchInputChanged = (oEvent) => {
    var sSearchInput = oEvent.target.value;
    this.setState({
      searchInput: sSearchInput
    });
  };

  handleAllModeButtonClicked = () => {
    let oXRaySearchInputDOM = this.xRayPropertySearch.current;
    if (!CS.isEmpty(oXRaySearchInputDOM)) {
      oXRaySearchInputDOM.focus();
    }
    this.setState({
      mode: 'all',
      searchInput: ""
    });
  };

  handleGroupModeButtonClicked = () => {
    var oXRaySearchInputDOM = this.searchBox;
    if (!CS.isEmpty(oXRaySearchInputDOM)) {
      oXRaySearchInputDOM.focus();
    }
    EventBus.dispatch(oEvents.HANDLE_SHOW_X_RAY_PROPERTY_GROUPS_CLICKED, undefined, undefined, this.props.filterContext);
    this.setState({
      mode: 'group',
      searchInput: "",
      attributeCollapsed: true,
      tagCollapsed: true,
      isDirty: false
    });
  };

  handleSearchInputClearClicked = () => {
    this.setState({
      searchInput: ""
    });
    var oXRaySearchInputDOM = this.searchBox;
    if (!CS.isEmpty(oXRaySearchInputDOM)) {
      oXRaySearchInputDOM.focus();
    }
  };

  getPropertyListViews = () => {
    let oActiveXRayPropertyGroup = this.props.activeXRayPropertyGroup;
    return (
        <GroupMssWrapperView
            groupsData={this.props.groupData}
            handleApplyButton={this.handleXRayPropertyApply.bind(this, this.props.relationshipId)}
            activeOptions={oActiveXRayPropertyGroup.properties}
            hideChips={true} // Chips is hidden always in xray view
            showPopup={false}
            showApply={this.state.isDirty}
            isMultiSelect={true}
            handleOptionClicked={this.handleOptionClicked}
            ref={this.groupMss}
            isDirty={this.state.isDirty}
            removeOption={this.handleOptionClicked}
        />
    );
  };

 handleOptionClicked = () => {
   this.setState({
     isDirty: true
   });
 }

  handleSearchBoxClicked =()=> {
    this.props.setItemInFocus(-1);
  };

  getPropertyGroupListView = () => {
    let aPropertyGroupList = this.props.propertyGroupList;
    var oActiveXRayPropertyGroup = this.props.activeXRayPropertyGroup;
    var aPropertyGroupListViews = [];
    var _this = this;
    let iItemIndex = 0;
    this.props.setIndexMap({});
    let oIndexMap = [];
    CS.forEach(aPropertyGroupList, function (oPropertyGroup, iIndex) {
      var sId = oPropertyGroup.id;
      var sLabel = CS.getLabelOrCode(oPropertyGroup);
      var sSearchInput = _this.state.searchInput;
      var sListItemClass = "listItem ";
      if (sLabel.toLowerCase().includes(sSearchInput.toLowerCase())) {
        if (sId === oActiveXRayPropertyGroup.id) {
          sListItemClass += "isSelected ";
        }

        if (_this.props.itemInFocus == iItemIndex) {
          sListItemClass += "inFocus";
        }

        oIndexMap[iItemIndex] = iIndex;

        aPropertyGroupListViews.push(
            <div key={sId} className={sListItemClass} title={sLabel} ref = {_this.setRef.bind(_this, 'contextMenuItem'+ iItemIndex++)}>
              <div className="listItemLabel" onClick={_this.handleXRayPropertyGroupClicked.bind(_this, iIndex , sLabel)}>{sLabel}</div>
            </div>
        );
      }
    });

    this.props.setIndexMap(oIndexMap);

    if (!CS.isEmpty(aPropertyGroupListViews)) {
      return (
          <div className="listWrapper"> {aPropertyGroupListViews}</div>
      );
    }
    else {
      return (
          <div className="nothingFoundMessage">{getTranslation().NOTHING_FOUND}</div>
      );
    }
  };

  getActiveXrayLabelDOM = () => {
    let oActiveXRayPropertyGroup = this.props.activeXRayPropertyGroup;
    var sRelationshipId = this.props.relationshipId;
    let sActiveGroupSectionClass = "activeGroupSection visible";
    let sActiveGroupInformationClass = "activeGroupInformation ";
    return (
        <div className={sActiveGroupSectionClass}>
          <div className="activeGroupSectionInnerContainer">
            <div className={sActiveGroupInformationClass}>
              <div className="activeGroupLabel">{CS.getLabelOrCode(oActiveXRayPropertyGroup)}</div>
              <div className="closeActiveGroupButton"
                   onClick={() => this.handleCloseActiveXRayPropertyGroupClicked(sRelationshipId, this)}></div>
            </div>
          </div>
        </div>
    );
  };

  render() {
    var oActiveXRayPropertyGroup = this.props.activeXRayPropertyGroup;
    var sAllModeButtonClass = "modeButton allMode ";
    var sGroupModeButtonClass = "modeButton groupMode ";
    var oListView = null;

    let oStyle = {
      outline: "none"
    };

    let fOnKeyPress = null;
    let fOnBlur = null;
    let fOnChange = null;
    let oSearchInputDOM = null;
    let oActiveDOM = null;

    switch (this.state.mode) {

      case "all" :
        oListView = this.getPropertyListViews();
        sAllModeButtonClass += "isSelected";
        let bIsActiveXRayGroupDOM = CS.isNotEmpty(oActiveXRayPropertyGroup) && !oActiveXRayPropertyGroup.createNew;
        oActiveDOM = bIsActiveXRayGroupDOM && this.getActiveXrayLabelDOM();
        break;

      case "group" :
        oListView = this.getPropertyGroupListView();
        sGroupModeButtonClass += "isSelected";
        fOnChange = this.handleSearchInputChanged;
        oSearchInputDOM = <div className="searchInputContainer">
          <input className="searchInput"
             onKeyPress={fOnKeyPress}
             onBlur={fOnBlur}
             onChange={fOnChange}
             value={this.state.searchInput}
             type="text"
             ref = {this.setRef.bind(this, 'searchBox')}
             onClick={this.handleSearchBoxClicked}
             placeholder={getTranslation().SEARCH}/>
          {this.state.searchInput ? <div className="clearSearch" onClick={this.handleSearchInputClearClicked}></div> : null}
        </div>
        break;
    }

    return (
        <div className="xRaySettings" style={oStyle}>
          <div className="arrowSection"></div>
          <div className="mainSection" style={oStyle} onKeyDown={this.props.onKeyPressHandler} tabIndex={0} ref={this.setRef.bind(this, 'contextMenuView')}>

            <div className="headingLabel">{getTranslation().X_RAY_VISION_SETTINGS}</div>
            <div className="modeButtonsContainer">
              <div className={sGroupModeButtonClass}
                   onClick={this.handleGroupModeButtonClicked}>{getTranslation().GROUPS}</div>
              <div className={sAllModeButtonClass}
                   onClick={this.handleAllModeButtonClicked}>{getTranslation().PROPERTIES}</div>
            </div>
            {oSearchInputDOM}
            <div className="listContainer" ref={this.setRefForScrollbar.bind(this, 'scrollbar')}>
              {oListView}
            </div>
            {oActiveDOM}
          </div>
        </div>
    );
  }
}

export const view = KeyboardNavigationForLists(XRaySettingsView);
export const events = oEvents;
