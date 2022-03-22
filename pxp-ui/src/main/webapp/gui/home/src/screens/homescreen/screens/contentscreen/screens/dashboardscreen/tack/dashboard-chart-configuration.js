import DashboardChartTypeDictionary from './dashboard-chart-type-dictionary';
import { getTranslations as getTranslation } from '../../../../../../../commonmodule/store/helper/translation-manager';

let DashboardChartConfiguration = {
  backgroundColors: {
    "completeness": "#FFD54F",
    "uniqueness": "#9FA8DA",
    "accuracy": "#ddd",
    "conformity": "#81C784",
  },
  borderColors: {
    "completeness": "#ff0000",
    "uniqueness": "#ff0000",
    "accuracy": "#ff0000",
    "conformity": "#ff0000",
  }
};

DashboardChartConfiguration[DashboardChartTypeDictionary.GROUPED_BAR] = function () {
  return {
    maintainAspectRatio: false,
    legend: {
      position: 'bottom',
    },
    scales: {
      yAxes: [{
        ticks: {
          suggestedMin: 0,
          suggestedMax: 100
        },
        scaleLabel: {
          display: true,
          labelString: getTranslation().PERCENTAGE+" (%)"
        }
      }],
      xAxes: [{
        ticks: {
          autoSkip: false
        }
      }]
    },
  }
};


export default DashboardChartConfiguration;