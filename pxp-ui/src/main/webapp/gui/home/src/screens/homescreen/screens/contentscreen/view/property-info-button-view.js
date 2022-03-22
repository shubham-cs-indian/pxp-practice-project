import React from 'react';
import ReactPropTypes from 'prop-types';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import {view as PropertyInfoListNodeView} from "../../../../../viewlibraries/propertyinfolistview/property-info-list-node-view";
import {view as CustomPopoverView} from "../../../../../../../home/src/viewlibraries/customPopoverView/custom-popover-view";

let getTranslation = ScreenModeUtils.getTranslationDictionary;

const oPropTypes = {
  infoDialogData: ReactPropTypes.object
};

class PropertyInfoButtonView extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      openDropdown: false
    };
    this.triggerElement = React.createRef();
  }

  closeDropdown = (oEvent) => {
    oEvent.stopPropagation();
    this.setState({
      openDropdown: false
    });
  };


  showDropdown = () => {
    this.setState({
      openDropdown: true,
      anchorElement: this.triggerElement.current
    });
  };

  getPropertyInfoView = () => {
    var oInfoDialogData = this.props.infoDialogData;
    let oPropertyInfoData = oInfoDialogData.propertyInfoData;
    let aListNodeView = [];
    for (let property of oPropertyInfoData) {
      aListNodeView.push(
          <PropertyInfoListNodeView key={property.label} model={property}/>
      );
    }

    return (
        <div>
          <CustomPopoverView
              className={"popover-root"}
              open={this.state.openDropdown}
              anchorEl={this.state.anchorElement}
              anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
              transformOrigin={{horizontal: 'left', vertical: 'top'}}
              style={{}}
              onClose={this.closeDropdown}>

            <div className="propertyInfoListViewContainer">
              <div className="customHeaderView">{getTranslation().PROPERTY_INFO}</div>
              <div className="propertyInfoListView">
                {aListNodeView}
              </div>
            </div>
          </CustomPopoverView>
        </div>
    )
  };

  render() {
    let oEditViewDOM = (
      <div className={"clickableIcon propertyInfoIcon "} onMouseOver={this.showDropdown} ref={this.triggerElement}>
      </div>
    );

    let oPropertyInfoDetailView = this.getPropertyInfoView();

    return <React.Fragment>
      {oEditViewDOM}
      {oPropertyInfoDetailView}
    </React.Fragment>

  }
}

PropertyInfoButtonView.propTypes = oPropTypes;

export const view = PropertyInfoButtonView;