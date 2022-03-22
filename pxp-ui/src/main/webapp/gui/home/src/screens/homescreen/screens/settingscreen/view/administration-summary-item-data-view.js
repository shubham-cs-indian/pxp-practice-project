import CS from "../../../../../libraries/cs";
import React from 'react';
import ReactPropTypes from 'prop-types';

const oPropTypes = {
  administrationSummaryData: ReactPropTypes.array,
};

// @CS.SafeComponent
class AdministrationSummaryView extends React.Component {
  static propTypes = oPropTypes;

  getItemData = () => {
    let administrationSummaryData = this.props.administrationSummaryData;
    let aItemDOM = [];
    CS.forEach(administrationSummaryData.children, (items) => {
      let sClassNameLabel = "itemLabel ";
      sClassNameLabel += items.children ? "bold" : " ";
      let sClassNameCount = "itemLabel ";
      sClassNameCount += items.children ? "bold" : " ";
      aItemDOM.push(
          <div className={"administrationItemData"}>
            <div className={"administrationSubType"}>
              <div className={sClassNameLabel}>{items.label}</div>
              <div className={sClassNameCount}>{items.count}</div>
            </div>
            {items.children && this.getSubItemType(items.children)}
          </div>
      )
    });
    return aItemDOM;
  };

  getSubItemType = (aSubItem) => {
    let aSubItemType = [];
    CS.forEach(aSubItem, function (oSubItem) {
      aSubItemType.push(
          <div className={"administrationSubTypeItem"}>
            <div className={"subItemLabel"}>{oSubItem.label}</div>
            <div className={"subItemCount"}>{oSubItem.count}</div>
          </div>
      )
    });
    return aSubItemType;
  };

  render () {
    return (
        <div className={"administrationSummaryViewItemWrapper"}>
          {this.getItemData()}
        </div>

    );
  }
}


export {AdministrationSummaryView as view} ;

