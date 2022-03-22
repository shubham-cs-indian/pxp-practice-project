import React from 'react';
import ReactPropTypes from 'prop-types';
import {view as CommonConfigSectionView} from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view';
import {view as ConfigHeaderView} from '../../../../../viewlibraries/configheaderview/config-header-view';
import SectionLayout from "../tack/pdfReactorServerConfigLayoutData";

const oPropTypes = {
  pdfReactorServerConfigData: ReactPropTypes.object,
  pdfReactorServerData: ReactPropTypes.object,
};


const oEvents = {}
class PdfReactorServerConfigView extends React.Component {
  static propTypes = oPropTypes;


  render () {

    let aSectionLayout = new SectionLayout();
    let oPdfReactorConfigDetails = this.props.pdfReactorServerData;

    return (<div className={"pdfReactorWrapperView"}>
      <ConfigHeaderView showSaveDiscardButtons={oPdfReactorConfigDetails.isDirty}
                        context={"LogoConfiguration"}
                        hideSearchBar={true}/>
      <CommonConfigSectionView context="pdfReactorConfig" sectionLayout={aSectionLayout}
                               data={this.props.pdfReactorServerConfigData}/>
    </div>)
  }

}


export const view = PdfReactorServerConfigView;
export const events = oEvents;
