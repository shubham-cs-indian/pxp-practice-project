import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view.js';
import SectionLayout from '../tack/class-config-layout-data';

const oEvents = {
};

const oPropTypes = {
  sectionLayoutModel: ReactPropTypes.object,
  disabledFields: ReactPropTypes.array,
};

// @CS.SafeComponent
class ClassConfigBasicInformationView extends React.Component {
  static propTypes = oPropTypes;

  render() {
    var oSectionLayoutModel = this.props.sectionLayoutModel;
    var aDisabledFields = this.props.disabledFields;
    let oSectionLayout = new SectionLayout();
    return (
        <div className="classBasicInfoWrapper">
          <CommonConfigSectionView context="class" sectionLayout={oSectionLayout.classBasicInformation}
             data={oSectionLayoutModel} disabledFields={aDisabledFields} />
        </div>
    );
  }
}

export const view = ClassConfigBasicInformationView;
export const events = oEvents;
