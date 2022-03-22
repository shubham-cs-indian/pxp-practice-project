import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import TooltipView from './../tooltipview/tooltip-view';
import { view as ContextMenuViewNew } from '../contextmenuwithsearchview/context-menu-view-new';
import ContextMenuViewModel from '../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';

const oPropTypes = {
  isMultiSelect: ReactPropTypes.bool,
  bShowIcon: ReactPropTypes.bool,
  items: ReactPropTypes.array,
  disabled: ReactPropTypes.bool,
  selectedItems: ReactPropTypes.array,
  onApply: ReactPropTypes.func,
  showColor: ReactPropTypes.bool,
  searchHandler: ReactPropTypes.func,
  loadMoreHandler: ReactPropTypes.func,
  label: ReactPropTypes.string,
  showLabel: ReactPropTypes.bool,
  hideTooltip: ReactPropTypes.bool,
  childView: ReactPropTypes.object,
  searchText: ReactPropTypes.string,
  onItemsChecked: ReactPropTypes.func,
  isSearchApplied: ReactPropTypes.bool
};
/**
 * @class TagMSSView
 * @memberOf Views
 * @property {bool} [isMultiSelect] - To select multiple items from dropdowm list flag is set to true and vice versa.
 * @property {bool} [bShowIcon] - Deprecated.
 * @property {array} [items] -  Data.
 * @property {bool} [disabled] - If true disabling TagMSSView.
 * @property {array} [selectedItems] - Contains selected items.
 * @property {func} [onApply] - Execute when apply button is clicked.
 * @property {bool} [showColor] - To change the style of item, if true(ex.display = 'block'; backgroundColor = sColor).
 * @property {func} [searchHandler] - Executes when text is searched.
 * @property {func} [loadMoreHandler] - Execute after load more option is clicked.
 * @property {string} [label] - Label
 * @property {bool} [showLabel] - Used to show label.
 * @property {bool} [hideTooltip] - To hide tooltip(If true - Hide).
 * @property {object} [childView] - Child view.
 * @property {string} [searchText] - Contains text which you have searched.
 * @property {func} [onItemsChecked] - Executes when item checkbox is clicked.
 * @property {bool} [isSearchApplied] -
 */

// @CS.SafeComponent
class TagMSSView extends React.Component{

  constructor(props){
    super(props)

    this.state = {
      selectedItems: TagMSSView.getClonedSelectedItems(this.props)
    }
  }
  static getDerivedStateFromProps (oNextProps, oState) {
    if (!oNextProps.isSearchApplied) {
     return {
        selectedItems: TagMSSView.getClonedSelectedItems(oNextProps),
      }
    }
    return null;
  }

  /*componentWillReceiveProps (oNextProps) {
    if (!oNextProps.isSearchApplied) {
      this.setState({
        selectedItems: this.getClonedSelectedItems(oNextProps),
      });
    }
  }*/

 /* shouldComponentUpdate: function (oNextProps, oNextState) {
    return !CS.isEqual(oNextProps, this.props) || !CS.isEqual(oNextState, this.state);
  },*/

  static getClonedSelectedItems =(oNextProps)=> {
    /*let aSelectedItems = [];
    if (oNextProps) {
      let aItems = oNextProps.items;
      CS.forEach(oNextProps.selectedItems, function (sId) {
        try {
          let oItem = CS.find(aItems, {id: sId});
          aSelectedItems.push(oItem.id);
        }
        catch (oException) {
          console.error(oException);
        }
      });

    }*/
    return CS.cloneDeep(oNextProps.selectedItems);
  }

  onPopoverClosed =(aSelectedItems)=> {
    let aPreviousSelectedItems = this.props.selectedItems;
    if (!CS.isEqual(aSelectedItems, aPreviousSelectedItems)) {
      if (CS.isFunction(this.props.onApply)) {
        this.props.onApply(aSelectedItems);
      }
    }
  };

  handleOnItemsChecked = (aCheckedItems, bIsCheckUnCheckAll) => {
    let aSelectedItems = this.state.selectedItems;

    if (!bIsCheckUnCheckAll) {
      CS.forEach(aCheckedItems, function (sCheckedId) {
        if (CS.includes(aSelectedItems, sCheckedId)) {
          CS.remove(aSelectedItems, function (sItemId) {
            return sItemId === sCheckedId;
          });
        } else {
          aSelectedItems.push(sCheckedId);
        }
      });
    } else {
      aSelectedItems = aCheckedItems;
    }

    this.setState({
      selectedItems: aSelectedItems
    })

    this.props.onItemsChecked(aCheckedItems)
  }

/*
  handleCrossIconClicked =(sId, oEvent)=> {
    oEvent.nativeEvent.dontRaise = true;
    let aSelectedItems = CS.cloneDeep(this.state.selectedItems);
    if (CS.includes(aSelectedItems, sId)) {
      CS.remove(aSelectedItems, function (sItemId) {
        return sItemId === sId;
      });

      if (CS.isFunction(this.props.onApply)) {
        this.props.onApply(aSelectedItems);
      }
    }
  }
*/

  handleChildFilterClicked =(oModel)=> {
    let sId = oModel.id;
    let bIsMultiSelect = this.props.isMultiSelect;
    let aSelectedItems = this.state.selectedItems || [];
    if (!bIsMultiSelect) {
      this.onPopoverClosed([sId]);
      this.setState({
        selectedItems: [sId]
      });
    } else {
      let aRemovedItems = CS.remove(aSelectedItems, function (sItemId) {
        return sItemId === sId
      });
      if (CS.isEmpty(aRemovedItems)) {
        aSelectedItems.push(sId);
      }

      this.setState({
        selectedItems: aSelectedItems,
      });
    }
  }

  getContextMenuModelList =(aItems)=> {
    let aItemModels = [];
    let bIsMultiSelect = this.props.isMultiSelect;
    let aSelectedItems = this.state.selectedItems;
    let sSelectedItemId = aSelectedItems[0];

    CS.forEach(aItems, function (oItem) {
      if (!bIsMultiSelect && sSelectedItemId === oItem.id) {
        return;
      }

      let sIcon = oItem.iconKey || "";
      let sColor = oItem.color || "";
      let oProperties = {
        color: sColor
      };
      let sLabel = CS.getLabelOrCode(oItem);

      aItemModels.push(new ContextMenuViewModel(
          oItem.id,
          sLabel,
          false, //bIsActive
          sIcon, //sIcon
          oProperties
      ));

    });
    return aItemModels;
  }

  handleMSSSearch =(sSearchText)=> {
    if (this.props.searchHandler) {
      this.props.searchHandler(sSearchText);
    }
  }

  handleMSSLoadMore =()=> {
    if (this.props.loadMoreHandler) {
      this.props.loadMoreHandler();
    }
  }

  render() {
    let aItems = this.props.items;
    let aSelectedItems = this.state.selectedItems;
    let oAnchorOrigin = {horizontal: 'left', vertical: 'bottom'};
    let oTargetOrigin = {horizontal: 'left', vertical: 'top'};
    let oMSSLabelView = null;
    let sLabel = this.props.label;

    if (!CS.isEmpty(sLabel) && this.props.showLabel) {
      let bHideTooltip = this.props.hideTooltip;
      oMSSLabelView = !bHideTooltip ?
          <TooltipView placement="bottom" label={sLabel}>
            <div className="mssLabel">{sLabel}</div>
          </TooltipView> :
          <div className="mssLabel">{sLabel}</div>;
    }

    let oSearchHandler = this.handleMSSSearch;
    let oLoadMoreHandler = this.handleMSSLoadMore;

    return (
        <div ref={this.domMounted} className="multiSelectSearchViewWrapper">
          {oMSSLabelView}
          <ContextMenuViewNew
              contextMenuViewModel={this.getContextMenuModelList(aItems)}
              context={this.props.context}
              selectedItems={this.props.selectedItems}
              isMultiselect={this.props.isMultiSelect}
              onApplyHandler={this.onPopoverClosed}
              onClickHandler={this.handleChildFilterClicked}
              showSelectedItems={this.props.isMultiSelect}
              showColor={this.props.showColor}
              anchorOrigin={oAnchorOrigin}
              targetOrigin={oTargetOrigin}
              useAnchorElementWidth={true}
              clearSearchOnPopoverClose={true}
              searchHandler={oSearchHandler}
              loadMoreHandler={oLoadMoreHandler}
              disabled={this.props.disabled}
              searchText={this.props.searchText}
              onItemsChecked={this.handleOnItemsChecked}
              showDefaultIcon ={this.props.showDefaultIcon}
          >
            {this.props.childView}
          </ContextMenuViewNew>
        </div>
    );
  }
}

TagMSSView.propTypes = oPropTypes;


export const view = TagMSSView;
