import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import ViewLibraryUtils from '../utils/view-library-utils';
import { view as ImageFitToContainerView } from './../imagefittocontainerview/image-fit-to-container-view';
import TooltipView from './../tooltipview/tooltip-view';

const oEvents = {
  EXPANDABLE_NESTED_MENU_LIST_EXPAND_TOGGLED: "expandable_nested_menu_list_expand_toggle_clicked",
  EXPANDABLE_NESTED_MENU_LIST_TOGGLE_BUTTON_CLICKED: "expandable_nested_menu_list_toggle_button_clicked"
};

const oPropTypes = {
  context: ReactPropTypes.string,
  linkItemsData: ReactPropTypes.arrayOf(
      ReactPropTypes.shape({
        id: ReactPropTypes.string,
        label: ReactPropTypes.string,
        icon: ReactPropTypes.string,
        children: ReactPropTypes.arrayOf(
            ReactPropTypes.shape((
                {
                  id: ReactPropTypes.string,
                  label: ReactPropTypes.string,
                  icon: ReactPropTypes.string,
                }
            ))
        )
      })
  ),
  selectedItemId: ReactPropTypes.string,
  onItemClick: ReactPropTypes.func,
  selectDefaultItem: ReactPropTypes.func,
  isNested: ReactPropTypes.bool,
  itemsValuesMap: ReactPropTypes.object
};
/**
 * @class ExpandableNestedMenuListView - use for display Sidebar of mainSection.
 * @memberOf Views
 * @property {string} [context] -  a context.
 * @property {array} [linkItemsData] -  an array which contain link items data in sidebar.
 * @property {string} [selectedItemId] -  a string of selected item id.
 * @property {func} [onItemClick] -  function which used onItemClick event.
 * @property {func} [selectDefaultItem] -  function which used selectDefaultItem event.
 * @property {bool} [isNested] -  boolean value for isNested or not.
 * @property {object} [itemsValuesMap] -  object which contain itemsValuesMap.
 */

// @CS.SafeComponent
class ExpandableNestedMenuListView extends React.Component {

  constructor (props) {
    super(props);

    this.state = {
      isExpanded: CS.isBoolean(this.props.isExpanded) ? this.props.isExpanded : true
    };
  }

  static getDerivedStateFromProps(props, state) {
    if (props.hasOwnProperty("isExpanded") && props.isExpanded != state.isExpanded) {
        let newState = {isExpanded : props.isExpanded};
        return newState;
    }

    return null;
  }

  componentDidMount () {
    this.selectDefaultItem();
  }

  componentDidUpdate (prevProps, prevState) {
    this.selectDefaultItem();
  }

  toggleButtonClicked = () => {
    let bIsExpanded = this.state.isExpanded;
    this.setState({
      isExpanded: !bIsExpanded
    });
    if(this.props.hasOwnProperty("isExpanded")) {
      EventBus.dispatch(oEvents.EXPANDABLE_NESTED_MENU_LIST_TOGGLE_BUTTON_CLICKED);
    }
  };

  handleExpandMenuItemClicked = (sItemId) => {
    EventBus.dispatch(oEvents.EXPANDABLE_NESTED_MENU_LIST_EXPAND_TOGGLED, sItemId, this.props.context);
  };

  handleItemClicked = (sId, sParentId) => {
    if (CS.isFunction(this.props.onItemClick)) {
      this.props.onItemClick(sId, sParentId);
    }
  };

  selectDefaultItem = () => {
    let __props = this.props;
    if (CS.isEmpty(__props.selectedItemId) && CS.isFunction(__props.selectDefaultItem)) {
      let oFirstItem = __props.linkItemsData[0];
      let oFirstItemValuesMap = oFirstItem && __props.itemsValuesMap[oFirstItem.id] || {};
      let bIsNested = this.props.isNested;
      if(bIsNested && oFirstItem && !CS.isEmpty(oFirstItem.children) && oFirstItemValuesMap && oFirstItemValuesMap.isExpanded) {
        __props.selectDefaultItem(oFirstItem.children[0].id, oFirstItem.id);
      } else {
        if (!CS.isEmpty(oFirstItem)) {
          __props.selectDefaultItem(oFirstItem.id, null);
        }
      }
    }
  };

  getIconView = (oItem) => {
    let sIconId = oItem.iconKey;
    if (sIconId) {
      let sIconURL = ViewLibraryUtils.getIconUrl(sIconId);
      return (
          <div className="iconContainer">
            <ImageFitToContainerView imageSrc={sIconURL}/>
          </div>
      )
    } else if (!CS.isEmpty(oItem.className)) {
      return (
          <div className="iconContainer">
            <div className={'treeNodeIcon ' + oItem.className}/>
          </div>
      )
    } else {
      //show first 2 letters
      let sLabel = CS.getLabelOrCode(oItem);
      return (
          <div className="itemInitials">{sLabel[0] + (sLabel[1] || "")}</div>
      )
    }
  };

  getListNodeView = (oItem, bIsParent, sParentId, iLevel, bIsChild) => {
    let _this = this;
    let sId = oItem.id;
    let sLabel = CS.getLabelOrCode(oItem);
    let aListNodeView = [];
    let bIsNested = _this.props.isNested;
    let sSelectedItemId = _this.props.selectedItemId;

    let sListItemClass = "listItem ";
    let oListItemStyle = {};
    if(bIsChild) {
      let sListItemClass = "listItem child ";
      let iMarginLeft = 30 * iLevel;
      let iWidthToSubtract = iMarginLeft + 10;
      oListItemStyle = {
        marginLeft: iMarginLeft + "px",
        width: "calc(100% - " + (iWidthToSubtract + "px ") + ")"
      };
    }
    if(bIsNested) {
      if(!bIsParent) {
        if(sId === sSelectedItemId) {
          sListItemClass += "selected ";
        }
      } else {
        if(CS.isEmpty(oItem.children) && sId === sSelectedItemId) {
          sListItemClass += "selected cannotExpand";
        }
      }
    } else {
      if(sId === sSelectedItemId) {
        sListItemClass += "selected ";
      }
    }

    if(oItem.isDisabled) {
      sListItemClass += "disabled ";
    }

    let oIconView = _this.getIconView(oItem);
    let oExpandIconView = null;
    let fListItemOnClick = null;
    let oSuperScriptView = null;

    if(bIsNested && bIsParent && !CS.isEmpty(oItem.children)) {
      //show expand-collapse icon on parent of nested tree
      let oItemsValuesMap = _this.props.itemsValuesMap;
      let bIsExpanded = oItemsValuesMap[oItem.id] && oItemsValuesMap[oItem.id].isExpanded;
      let sClassName = bIsExpanded ? "expandIcon expanded " : "expandIcon ";
      oExpandIconView = bIsParent ? (<div className={sClassName}></div>) : null;

      //To Expand-Collapse parent of nested tree if it has children
      fListItemOnClick = _this.handleExpandMenuItemClicked.bind(_this, sId);
    } else {
      //Select tree item to open right panel
      fListItemOnClick = _this.handleItemClicked.bind(_this, sId, sParentId);
    }
    if (oItem.superScriptLabel) {
      oSuperScriptView = <sup> {oItem.superScriptLabel}</sup>;
    }
    let oMenuView = (
        <div className={sListItemClass} key={sId} onClick={fListItemOnClick} style={oListItemStyle}>
          <div className="iconSection">
          {oIconView}
          </div>
          <div className="labelSection">{sLabel}{oSuperScriptView}</div>
          {oExpandIconView}
        </div>);

   /* if (this.state.isExpanded) {
      aListNodeView.push(oMenuView);
    }
    else {*/
      aListNodeView.push(
        <TooltipView key={`tooltip${sId}`} placement="right" label={sLabel}>
            {oMenuView}
          </TooltipView>
      );
    /*}*/

    return aListNodeView;
  };

  getListItemsView = (aListItems, aListItemViews, bIsParent, sParentId, iLevel, bIsChild) => {
    let _this = this;
    let bIsNested = _this.props.isNested;
    let oItemsValuesMap = _this.props.itemsValuesMap;
    CS.forEach(aListItems, function (oItem) {
      aListItemViews.push(_this.getListNodeView(oItem, bIsParent, sParentId, iLevel, bIsChild));
      //show children data only if it tree is nested & parent is expanded
      let aChildren = oItem.children;
      let bIsCurrentItemParent = (aChildren && aChildren.length > 0);
      let sCurrentItemId = oItem.id;
      if(CS.isNotEmpty(aChildren) && bIsNested && oItemsValuesMap[sCurrentItemId] && oItemsValuesMap[sCurrentItemId].isExpanded) {
        _this.getListItemsView(aChildren, aListItemViews, bIsCurrentItemParent, sCurrentItemId, iLevel + 1, true);
      }
    });

    return aListItemViews;
  };

  render () {

    let _this = this;
    let aListItems = this.props.linkItemsData;

    let aListItemViews = _this.getListItemsView(aListItems, [], true, null, 0);

    let bIsNested = this.props.isNested;
    let sViewClass = bIsNested ? "expandableMenuListView isNested " : "expandableMenuListView ";
    if (this.state.isExpanded) {
      sViewClass += "expanded ";
    } else {
      sViewClass += "collapsed ";
    }

    return (
        <div className={sViewClass}>

          <div className="listItemsSection">
              {aListItemViews}
          </div>

          <div className="toggleButtonSection" onClick={this.toggleButtonClicked}>
            <div className="toggleButtonSectionIcon"></div>
          </div>

        </div>
    );
  }
}

ExpandableNestedMenuListView.propTypes = oPropTypes;

export const view = ExpandableNestedMenuListView;
export const events = oEvents;
