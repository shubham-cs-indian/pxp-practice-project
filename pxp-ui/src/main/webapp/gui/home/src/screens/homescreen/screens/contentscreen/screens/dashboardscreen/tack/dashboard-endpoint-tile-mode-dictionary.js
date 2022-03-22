import { getTranslations as getTranslation } from '../../../../../../../commonmodule/store/helper/translation-manager';
import DashboardConstants from './dashboard-constants';
const EndpointTypes = DashboardConstants.endpointTypes;
const EndpointTileModes = DashboardConstants.endpointTileModes;

export default function () {
	
return	{
  [EndpointTypes.INBOUND]: [
    {id: EndpointTileModes.LAST_UPLOAD, label: getTranslation().DASHBOARD_ENDPOINT_MODE_LAST_UPLOAD},
    {id: EndpointTileModes.ALL_UPLOADS, label: getTranslation().DASHBOARD_ENDPOINT_MODE_ALL_UPLOADS},
    {id: EndpointTileModes.UPLOAD_SUMMARY, label: getTranslation().DASHBOARD_ENDPOINT_MODE_UPLOAD_SUMMARY},
  ],

  [EndpointTypes.OUTBOUND]: [
    {id: EndpointTileModes.LAST_DOWNLOAD, label: getTranslation().DASHBOARD_ENDPOINT_MODE_LAST_DOWNLOAD},
    {id: EndpointTileModes.ALL_DOWNLOADS, label: getTranslation().DASHBOARD_ENDPOINT_MODE_ALL_DOWNLOADS},
    {id: EndpointTileModes.DOWNLOAD_SUMMARY, label: getTranslation().DASHBOARD_ENDPOINT_MODE_DOWNLOAD_SUMMARY},
  ],
}
};