import React from 'react';
import ReactPropTypes from 'prop-types';
import NatureTypeDictionary from './../../../../../commonmodule/tack/nature-type-dictionary';

const oEvents = {
};

const oPropTypes = {
  nature: ReactPropTypes.string,
  isFirst: ReactPropTypes.bool,
  isLast: ReactPropTypes.bool,
  isNatureThumbConnectorHidden: ReactPropTypes.bool
};

// @CS.SafeComponent
class NatureThumbnailConnectorView extends React.Component {
  static propTypes = oPropTypes;

  render() {

    var sConnectorClass = "";
    if (this.props.isNatureThumbConnectorHidden) {
      return null;
    }

    switch (this.props.nature) {

      case NatureTypeDictionary.FIXED_BUNDLE:
        if (this.props.isFirst) {
          sConnectorClass += "ribbon first";
        } else if (this.props.isLast) {
          sConnectorClass += "ribbon last";
        } else {
          sConnectorClass += "ribbon ";
        }
        break;

      case NatureTypeDictionary.SET_OF_PRODUCTS:
        if (this.props.isFirst) {
          return null;
        } else if (this.props.isLast) {
          return null;
        } else {
          sConnectorClass += "plusConnector ";
        }
        break;
    }

    return (
        <div className="natureConnectorWrapper">
          <div className="natureConnector">
            <div className={sConnectorClass}></div>
          </div>
        </div>
    );
  }
}

export const view = NatureThumbnailConnectorView;
export const events = oEvents;
