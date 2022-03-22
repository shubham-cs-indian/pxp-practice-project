import React from 'react';
import ReactPropTypes from 'prop-types';
import CS from '../../libraries/cs';
import ScreenModeUtils from '../../screens/homescreen/screens/contentscreen/store/helper/screen-mode-utils';

import { view as CustomTextFieldView } from '../customtextfieldview/custom-text-field-view';
import { view as CustomMaterialButtonView } from '../custommaterialbuttonview/custom-material-button-view';
import ViewUtils from "../../screens/homescreen/screens/contentscreen/view/utils/view-utils";
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";
const Fragment = React.Fragment;
const getTranslation = ScreenModeUtils.getTranslationDictionary;

const [SEARCH, SEARCHIN, DEFAULTGROUPICON, ITEMNOTFOUND] = [getTranslation().SEARCH, getTranslation().SEARCH_IN, "", getTranslation().DATA_NOT_FOUND];


const oPropTypes = {
  handleOptionClicked: ReactPropTypes.func,
  groupsData: ReactPropTypes.object,
  activeOptions: ReactPropTypes.array,
  store: ReactPropTypes.object,
  isDirty: ReactPropTypes.bool
};

/**
 * @class GroupMssContextMenuView
 * @memberOf Views
 * @property {array} [activeOptions] - PreSelected Options of groups.
 * @property {func} [handleOptionClicked] - Used to handle checkbox click functionality.
 * @property {array} [groupsData] - Request data for getting items from backend.
 * @property {object} [store] - Store instance.
 */

// @CS.SafeComponent
class GroupMssContextMenuView extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      placeHolderString: SEARCH,
      searchString: "",
      groupsData: props.groupsData,
      isSearch: false,
      activeOptions: props.activeOptions,
    };
  }

  /**
   * If options is dirty get group data from state else from props
   */
  static getDerivedStateFromProps (oNextProps, oState) {
    if (oState.isSearch && !oNextProps.store.getRequestResponseInfo(oNextProps.store.getActiveGroup())) {
      return ({
        groupsData: oState.groupsData,
        activeOptions: oNextProps.isDirty ? oState.activeOptions : oNextProps.activeOptions
      });
    } else {
      return ({
        groupsData: oNextProps.store.getGroupsData(),
        activeOptions: oNextProps.isDirty ? oState.activeOptions : oNextProps.activeOptions
      });
    }
  }

  componentWillUnmount() {
    this.props.store.setActiveGroup("");
  }

  /**
   * Function to handle selection of checbox in context menu
   * @param {object} oOption Clicked Checkbox option
   * @param {string} sGroupType option group type
   */
  handleOptionsCheckboxClicked = (oOption, sGroupType) => {
    let isMultiSelect = this.props.isMultiSelect;
    let aActiveOptions = this.state.activeOptions;
    if (isMultiSelect) {
      let removedItem = CS.remove(aActiveOptions, (option) => {
        return oOption.id == option.id
      })
      if (removedItem.length == 0) {
        aActiveOptions = [...aActiveOptions, { id: oOption.id, groupType: sGroupType, label: oOption.label }];
        oOption.groupType = sGroupType;
        this.props.store.setGroupsDataMap(oOption)
      }
      this.setState({activeOptions: aActiveOptions});
      if(typeof this.props.handleOptionClicked === 'function')
        this.props.handleOptionClicked(oOption);
    } else {
      aActiveOptions = [{ id: oOption.id, groupType: sGroupType, label: oOption.label }];
      oOption.groupType = sGroupType;
      this.props.store.setGroupsDataMap(oOption);
      this.setState({activeOptions: aActiveOptions});
      if(typeof this.props.handleOptionClicked === 'function')
        this.props.handleOptionClicked(oOption);
      this.handleApplyButtonClick(null, aActiveOptions);
    }
  };

  /**
   * Function to expand/Collapse the groups to show/hide its respective options
   * @param {string} sClickedGroupType type of group to expand
   */
  expandGroup = (sClickedGroupType) => {
    let sGroupType;
    let sPlaceHolderValue = SEARCH;
    let sActiveGroup = this.props.store.getActiveGroup();
    if (sActiveGroup !== sClickedGroupType) {
      sGroupType = sClickedGroupType;
      sPlaceHolderValue = SEARCHIN + " " + (sGroupType.charAt(0).toUpperCase() + sGroupType.substr(1));
      this.props.store.setActiveGroup(sGroupType);
      this.handleApiCall();
    } else {
      sGroupType = "";
      this.props.store.setActiveGroup(sGroupType);
    }
    this.setState({
      placeHolderString: sPlaceHolderValue
    })
  }

  getIconView = (oGroup) => {
    let sSrcURL = ViewUtils.getIconUrl(oGroup.icon);
    let oIconDom = null;
    if (CS.isEmpty(oGroup.icon)) {
      oIconDom = <div className={"customIcon " + oGroup.customIconClassName} key="classIcon"></div>;
    }
    else {
      oIconDom = <ImageFitToContainerView imageSrc={sSrcURL}/>;
    }
    return oIconDom;
  };

  getSelectedCountDOM = (iSelectedOptions) => {
    if (iSelectedOptions && !this.props.hideSelectedCount) {
      return (
          <span className="selectedCount">{iSelectedOptions} {getTranslation().SELECTED}</span>
      );
    }
    return null;
  };

  /**
   * Function to convert context menu data to react dom element
   */
  getGroups = () => {
    let aGroupData = this.state.groupsData;
    let aGroups = [];
    let aSelectedOptions = this.state.activeOptions || [];
    let sActiveGroup = this.props.store.getActiveGroup();
    CS.forOwn(aGroupData, (oGroup, sKey) => {
      let oOptions;
      let sClassName = "groupHeader ";
      let sGroupType = oGroup.id;
      if (sActiveGroup == sGroupType) {
        oOptions = this.getOptionsDom(oGroup.list, sGroupType, oGroup.showLoadMore)
      }
      let iSelectedOptions = 0;
      aSelectedOptions.forEach((opt) => {
        if (opt.groupType == sGroupType) iSelectedOptions = iSelectedOptions + 1;
      })
      sClassName = (sActiveGroup == sGroupType) ? sClassName + "groupExpand" : sClassName + "groupCollapse";
      let sGroupClassName = (sActiveGroup == sGroupType) ? "groupsList expanded" : "groupsList collapsed";
      aGroups.push(<div className={sGroupClassName} key={sGroupType}>
        <div className={sClassName} onClick={() => this.expandGroup(sGroupType)}>
          {this.getIconView(oGroup)}
          <label className="groupTitle">{oGroup.label}</label>
          {this.getSelectedCountDOM(iSelectedOptions)}
        </div>
        {oOptions}
      </div>);
    })
    return aGroups;
  }

  /**
   * Function to convert options to react dom element
   * @param {object} aOptions array contains options list
   * @param {string} sGroupType type of group
   */
  getOptionsDom = (aOptions, sGroupType, bShowLoadMore) => {
    let aOptionsDom = [];
    let sDomElement
    let aSelectedOptions = this.state.activeOptions;
    if (aOptions.length == 0) {
      sDomElement = <div className="groupNoItem"><span>{ITEMNOTFOUND}</span></div>;
    } else {
      CS.forEach(aOptions, (oOption) => {
        let fOnCheckHandler = this.handleOptionsCheckboxClicked.bind(this, oOption, sGroupType);
        let sClassName = "groupOptionsCheck ";
        let sLabelClassName = "";
        let sIconKey = ViewUtils.getIconUrl(oOption.iconKey);
        if (CS.find(aSelectedOptions, { id: oOption.id })) {
          sClassName = sClassName + "checked";
          sLabelClassName = "selected";
          oOption.groupType = sGroupType;
          this.props.store.setGroupsDataMap(oOption)
        }
        let oLabelDOM = CS.getLabelOrCode(oOption);
        let sSearchedText = this.state.searchString;
        if (CS.isNotEmpty(sSearchedText)) {
          oLabelDOM = ViewUtils.getHighlightedHeaderText(oLabelDOM, sSearchedText, "groupMSSContextMenuHighlightedText");
        }
        let sHtml = <div className="groupOptions" onClick={fOnCheckHandler} key={oOption.id}>
          <div className={sClassName}></div>
          <div className="customIcon">
            <ImageFitToContainerView imageSrc={sIconKey}/>
          </div>
          <label className={sLabelClassName} htmlFor={oOption.label + oOption.id}>{oLabelDOM}</label>
        </div>
        aOptionsDom.push(sHtml)
      })
      let sLoadMoreDom = bShowLoadMore ? <div className="loadMoreMessage" onClick={() => this.props.store.handleLoadMoreClicked(sGroupType)}>
        {getTranslation().LOAD_MORE}</div> : null;
      sDomElement = (<div className="groupOptionsWrapper">
          {aOptionsDom}
          {sLoadMoreDom}
      </div>)
    }
    return (sDomElement);
  }

  /**
   * Function make api call to fetch options of groups
   * If search happened isSearch flag is true & the reset all group data
   * If group data is empty it will make api call to fetch options list
   * If search value is empty set isSearch flag as false
   */
  handleApiCall = () => {
    let sSearchString = this.state.searchString;
    if (!sSearchString && this.state.isSearch) {
      this.props.store.resetGroupsData();
    }
    let sActiveGroup = this.props.store.getActiveGroup();
    let aGroupData = this.props.store.getGroupsData();
    let aActiveGroupData = aGroupData[sActiveGroup];
    let oStateData = {};
    sSearchString ? oStateData['isSearch'] = true : oStateData['isSearch'] = false;
    if (sActiveGroup && ((!aActiveGroupData || aActiveGroupData.list.length == 0) || oStateData.isSearch)) {
      let aFilteredData = this.props.store.handleSearchInput(sSearchString);
      if (aFilteredData) {
        oStateData["groupsData"] = aFilteredData;
      }
    }
    this.setState(oStateData)
  }

  /**
   * Function to update the state value on entering search string
   * @param {string} sText Entered input box text value
   */
  handleSearchText = (sText) => {
    this.setState({
      searchString: sText
    })
  }

  /**
   * Function to filter options value on pressing enter key
   * @param {object} oEvent click event
   */
  searchTextOnKeyDown = (oEvent) => {
    if (oEvent.keyCode == 13) { //13 -> Enter key
      CS.isFunction(this.props.searchHandler) ? this.props.searchHandler(oEvent.target.value) : this.handleApiCall();
    }
  }

  handleApplyButtonClick = (event, aActiveOptions) => {
    let activeOptions = aActiveOptions || this.state.activeOptions;
    this.props.handleApplyButton(activeOptions);
    this.props.closePopover && this.props.closePopover();
  }

  /**
   * Function to create dom element for search input box
   */
  getSearchView = () => {
    return (<div className="searchWrapper"
    >
      <CustomTextFieldView
        value={this.state.searchString}
        onChange={(evt) => this.handleSearchText(evt)}
        forceBlur={true}
        shouldFocus={false}
        hintText={this.state.placeHolderString}
        onKeyDown={this.searchTextOnKeyDown}
      />
      <span className="searchButton"
        onClick={() => this.handleApiCall()}>
      </span>
    </div>)
  }

  getButtons = () => {
    let oStyle = {height: '26px', minHeight: '26px', width: '100%', margin : 0,
      padding: '0px 10px', boxShadow: 'none', color: 'rgb(255, 255, 255)'}
    let oButtons = this.props.isMultiSelect ? (<Fragment><CustomMaterialButtonView
      label={getTranslation().APPLY} isRaisedButton={true} isDisabled={false}
      onButtonClick={this.handleApplyButtonClick.bind(this)} style={oStyle}/>
     </Fragment>) : (null);
      return oButtons;
  }

  render() {
    let aChildrens = this.getGroups();
    let sApplyButtonCss = "groupButtonApply visible";
    let sGroupWrapper = this.props.showApply ? " showApply groupBody" : "groupBody ";
    return (
      <Fragment>
        {this.getSearchView()}
        <div className={sGroupWrapper}>
          {aChildrens}
        </div>
        {this.props.showApply ?
        <div className="groupButtonWrapper">
          <div className={sApplyButtonCss}>
            {this.getButtons()}
          </div>
        </div> : null }
      </Fragment>
    )
  }
}

GroupMssContextMenuView.propTypes = oPropTypes;

export const view = GroupMssContextMenuView;
export const propTypes = oPropTypes;
