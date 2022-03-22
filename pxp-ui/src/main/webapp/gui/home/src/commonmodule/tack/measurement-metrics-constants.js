/**
 * Created by CS84 on 25-08-2016.
 */

/** WARNING: Currency exchange rates will come from backend, and they are stored in HomeScreenAppData */

export default {
  DEFAULT_DECIMAL_SEPARATOR: ".",
  LENGTH_VALUE: 12,
  DEFAULT_PRECISION: 0,
  PRECISION_OPTIONS: ["0","1","2","3","4","5","6","7","8","9"],
  WEIGHT_PER_AREA_EXCHANGE_RATES_BASE_KG_PER_M2:{
    "gm/m2" : 0.1,
    "lb/yd2": 1.843345,
    "lb/ft2": 0.2048161,
    "lb/in2": 0.001422334,
    "oz/in2": 0.02275735,
    "oz/ft2": 3.277058,
    "tn/mi2": 2854.973,
    "tn/ac1": 4.460896,
    "t/kg2" : 1000,
    "kg/m2" : 1
  },
  LUMINOSITY_EXCHANGE_RATES_BASE_CANDELA:{
    "lm" : "12.57",
    "mcd": "1000",
    "cd" : "1"
  },
  THERMAL_INSULATION_EXCHANGE_RATES_BASE_M2K_PER_W: {
    "hr-ft2-F/BTU": "5.678",
    "m2K/W"       : "1"
  },
  PROPORTION_EXCHANGE_RATES_BASE_PERCENTAGE: {
    "x/y": "0.01",
    "%"  : "1"
  },
  HEATING_RATE_EXCHANGE_RATES_BASE_CELSIUS_PER_SECOND: {
    "°C/hr" : "3600",
    "°C/min": "60",
    "°C/sec" : "1"
  },

  DENSITY_EXCHANGE_RATES_BASE_KG_PER_M3: {
    "g/cm3" : "0.001",
    "g/m3" : "1000",
    "kg/cm3": "0.000001",
    "kg/m3" : "1"
  },
  WEIGHT_PER_TIME_EXCHANGE_RATES_BASE_GM_PER_SEC: {
    "kg/day" : "86.4",
    "kg/hour": "3.6",
    "kg/min" : "0.06",
    "kg/sec" : "0.001",
    "gm/day": "86400",
    "gm/hr" : "3600",
    "gm/min" : "60",
    "gm/sec": "1"
  },
  VOLUME_FLOW_RATE_EXCHANGE_RATES_BASE_M3_PER_SECOND: {
    "L/hr" : "3600000",
    "L/min": "60000",
    "L/sec" : "1000",
    "m3/hr" : "3600",
    "m3/min": "60",
    "m3/sec" : "1"
  },
  AREA_PER_VOLUME_EXCHANGE_RATES_BASE_CM2_PER_ML: {
    "m2/L" : "0.1",
    "m2/mL" : "0.0001",
    "cm2/L": "1000",
    "cm2/mL" : "1"
  },
  ROTATION_FREQUENCY_EXCHANGE_RATES_BASE_REV_PER_SECOND: {
    "rev/hr" : "3600",
    "rev/min": "60",
    "rev/sec" : "1"
  },
  POWER_EXCHANGE_BASE_ON_WATT: {
    "kW" : "0.001",
    "W": "1",
    "hp" : "0.00134102",
    "mW" : "1000"
  }
};
