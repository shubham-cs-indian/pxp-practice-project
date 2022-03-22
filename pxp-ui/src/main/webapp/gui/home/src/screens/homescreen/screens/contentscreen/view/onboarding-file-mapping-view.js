import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as EndpointMappingView } from './endpoint-mapping-view';

const oEvents = {};

const oPropTypes = {
  activeEndpoint: ReactPropTypes.object,
  endPointMappingList: ReactPropTypes.array,
  activeTabId: ReactPropTypes.string,
  selectedMappingFilterId: ReactPropTypes.string,
  mappingFilterProps: ReactPropTypes.object,
  unmappedColumnValuesList: ReactPropTypes.object,
  endPointReferencedData: ReactPropTypes.object,
  endPointReqResInfo: ReactPropTypes.object,
  propertyRowTypeData: ReactPropTypes.object,
};

// @CS.SafeComponent
class OnBoardingFileMappingView extends React.Component {
  static propTypes = oPropTypes;

  getProfileDetailedView = () => {
    let oProps = this.props;

    return (<EndpointMappingView
        unmappedColumnValuesList={oProps.unmappedColumnValuesList}
        activeEndpoint={oProps.activeEndpoint || {}}
        endPointMappingList={oProps.endPointMappingList}
        selectedMappingRows={oProps.selectedMappingRows || []}
        activeTabId={oProps.activeTabId}
        selectedMappingFilterId={oProps.selectedMappingFilterId}
        mappingFilterProps={oProps.mappingFilterProps}
        endPointReqResInfo={oProps.endPointReqResInfo}
        propertyRowTypeData={oProps.propertyRowTypeData}
        endPointReferencedData={oProps.endPointReferencedData}
    />)
  };

  render() {


    return (
        <div className="onBoardingFileMappingViewContainer">
          {this.getProfileDetailedView()}
        </div>

    );
  }
}

export const view = OnBoardingFileMappingView;
export const events = oEvents;
