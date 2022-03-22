import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as NatureCommonSectionView } from './nature-common-section-view';

const oEvents = {
};

const oPropTypes = {
  natureSectionViewData: ReactPropTypes.object
};

// @CS.SafeComponent
class NatureSectionView extends React.Component {
  static propTypes = oPropTypes;

  state = {
    index: 0
  };

  render() {
    let __props = this.props;
    var oNatureSectionViewData = __props.natureSectionViewData;
    let oFilterContext = __props.filterContext;
    return (
      <div className="natureSectionView">
        <NatureCommonSectionView filterContext={oFilterContext}
          natureSectionViewData={oNatureSectionViewData}/>
      </div>
    );
  }
}

export const view = NatureSectionView;
export const events = oEvents;
