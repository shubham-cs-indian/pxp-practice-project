import CS from '../../libraries/cs';
import { getTranslations as oTranslations } from '../../commonmodule/store/helper/translation-manager.js';
import AttributeTypeDictionary from '../../commonmodule/tack/attribute-type-dictionary-new';

const oMeasurementMetricsAndImperial = function () {
  let aPriceUnits = CS.sortBy([
    {
      "id": "O_1",
      "unit": "EUR",
      "unitToDisplay": "€",
      "label": oTranslations().UNIT_EURO_EUR,
      "isBase": true
    },
    {
      "id": "O_2",
      "unit": "USD",
      "unitToDisplay": "$",
      "label": oTranslations().UNIT_US_DOLLAR_USD
    },
    {
      "id": "O_3",
      "unit": "AUD",
      "unitToDisplay": "AUD",
      "label": oTranslations().UNIT_AUSTRALIAN_DOLLAR_AUD
    },
    {
      "id": "O_4",
      "unit": "BGN",
      "unitToDisplay": "лв",
      "label": oTranslations().UNIT_BULGARIAN_LEV_BGN
    },
    {
      "id": "O_5",
      "unit": "BRL",
      "unitToDisplay": "R$",
      "label": oTranslations().UNIT_BRAZILIAN_REAL_BRL
    },
    {
      "id": "O_6",
      "unit": "CAD",
      "unitToDisplay": "CAD",
      "label": oTranslations().UNIT_CANADIAN_DOLLAR_CAD
    },
    {
      "id": "O_7",
      "unit": "CHF",
      "unitToDisplay": "CHF",
      "label": oTranslations().UNIT_SWISS_FRANC_CHF
    },
    {
      "id": "O_8",
      "unit": "CNY",
      "unitToDisplay": "¥",
      "label": oTranslations().UNIT_CHINESE_YUAN_CNY
    },
    {
      "id": "O_9",
      "unit": "CZK",
      "unitToDisplay": "Kč",
      "label": oTranslations().UNIT_CZECH_REPUBLIC_KORUNA_CZK
    },
    {
      "id": "O_10",
      "unit": "DKK",
      "unitToDisplay": "kr",
      "label": oTranslations().UNIT_DANISH_KRONE_DKK
    },
    {
      "id": "O_11",
      "unit": "GBP",
      "unitToDisplay": "£",
      "label": oTranslations().UNIT_BRITISH_POUND_GBP
    },
    {
      "id": "O_12",
      "unit": "HKD",
      "unitToDisplay": "HKD",
      "label": oTranslations().UNIT_HONG_KONG_DOLLAR_HKD
    },
    {
      "id": "O_13",
      "unit": "HRK",
      "unitToDisplay": "kn",
      "label": oTranslations().UNIT_CROATIAN_KUNA_HRK
    },
    {
      "id": "O_14",
      "unit": "HUF",
      "unitToDisplay": "Ft",
      "label": oTranslations().UNIT_HUNGARIAN_FORINT_HUF
    },
    {
      "id": "O_15",
      "unit": "IDR",
      "unitToDisplay": "Rp",
      "label": oTranslations().UNIT_INDONESIAN_RUPIAH_IDR
    },
    {
      "id": "O_16",
      "unit": "ILS",
      "unitToDisplay": "₪",
      "label": oTranslations().UNIT_ISRAELI_NEW_SHEQEL_ILS
    },
    {
      "id": "O_17",
      "unit": "INR",
      "unitToDisplay": "₹",
      "label": oTranslations().UNIT_INDIAN_RUPEE_INR
    },
    {
      "id": "O_18",
      "unit": "JPY",
      "unitToDisplay": "¥",
      "label": oTranslations().UNIT_JAPANESE_YEN_JPY
    },
    {
      "id": "O_19",
      "unit": "KRW",
      "unitToDisplay": "₩",
      "label": oTranslations().UNIT_SOUTH_KOREAN_WON_KRW
    },
    {
      "id": "O_20",
      "unit": "MXN",
      "unitToDisplay": "MXN",
      "label": oTranslations().UNIT_MEXICAN_PESO_MXN
    },
    {
      "id": "O_21",
      "unit": "MYR",
      "unitToDisplay": "RM",
      "label": oTranslations().UNIT_MALAYSIAN_RINGGIT_MYR
    },
    {
      "id": "O_22",
      "unit": "NOK",
      "unitToDisplay": "NOK",
      "label": oTranslations().UNIT_NORWEGIAN_KRONE_NOK
    },
    {
      "id": "O_23",
      "unit": "NZD",
      "unitToDisplay": "NZD",
      "label": oTranslations().UNIT_NEW_ZEALAND_DOLLAR_NZD
    },
    {
      "id": "O_24",
      "unit": "PHP",
      "unitToDisplay": "₱",
      "label": oTranslations().UNIT_PHILIPPINE_PESO_PHP
    },
    {
      "id": "O_25",
      "unit": "PLN",
      "unitToDisplay": "zł",
      "label": oTranslations().UNIT_POLISH_ZLOTY_PLN
    },
    {
      "id": "O_26",
      "unit": "RON",
      "unitToDisplay": "lei",
      "label": oTranslations().UNIT_ROMANIAN_LEU_RON
    },
    {
      "id": "O_27",
      "unit": "RUB",
      "unitToDisplay": "руб",
      "label": oTranslations().UNIT_RUSSIAN_RUBLE_RUB
    },
    {
      "id": "O_28",
      "unit": "SEK",
      "unitToDisplay": "SEK",
      "label": oTranslations().UNIT_SWEDISH_KRONA_SEK
    },
    {
      "id": "O_29",
      "unit": "SGD",
      "unitToDisplay": "SGD",
      "label": oTranslations().UNIT_SINGAPORE_DOLLAR_SGD
    },
    {
      "id": "O_30",
      "unit": "THB",
      "unitToDisplay": "฿",
      "label": oTranslations().UNIT_THAI_BAHT_THB
    },
    {
      "id": "O_31",
      "unit": "TRY",
      "unitToDisplay": "₺",
      "label": oTranslations().UNIT_TURKISH_LIRA_TRY
    },
    {
      "id": "O_32",
      "unit": "ZAR",
      "unitToDisplay": "R",
      "label": oTranslations().UNIT_SOUTH_AFRICAN_RAND_ZAR
    }
  ], 'label');


  return {
    "com.cs.core.config.interactor.entity.attribute.LengthAttribute": CS.sortBy([
      {
        "id": "A_1",
        "unit": "km",
        "unitToDisplay": "km",
        "label": oTranslations().UNIT_KILOMETER_km
      },
      {
        "id": "A_2",
        "unit": "m",
        "unitToDisplay": "m",
        "label": oTranslations().UNIT_METER_m
      },
      {
        "id": "A_3",
        "unit": "dm",
        "unitToDisplay": "dm",
        "label": oTranslations().UNIT_DECIMETER_dm
      },
      {
        "id": "A_4",
        "unit": "cm",
        "unitToDisplay": "cm",
        "label": oTranslations().UNIT_CENTIMETER_cm
      },
      {
        "id": "A_5",
        "unit": "mm",
        "unitToDisplay": "mm",
        "label": oTranslations().UNIT_MILLIMETER_mm,
        "isBase": true
      },
      {
        "id": "A_6",
        "unit": "um",
        "unitToDisplay": "um",
        "label": oTranslations().UNIT_MICROMETRE_um
      },
      {
        "id": "A_7",
        "unit": "nm",
        "unitToDisplay": "nm",
        "label": oTranslations().UNIT_NANOMETRE_nm
      },
      {
        "id": "A_8",
        "unit": "mi",
        "unitToDisplay": "mi",
        "label": oTranslations().UNIT_MILE_mi
      },
      {
        "id": "A_9",
        "unit": "yd",
        "unitToDisplay": "yd",
        "label": oTranslations().UNIT_YARD_yd
      },
      {
        "id": "A_10",
        "unit": "ft",
        "unitToDisplay": "ft",
        "label": oTranslations().UNIT_FEET_ft
      },
      {
        "id": "A_11",
        "unit": "in",
        "unitToDisplay": "in",
        "label": oTranslations().UNIT_INCH_in
      },
      {
        "id": "A_12",
        "unit": "nmi",
        "unitToDisplay": "nmi",
        "label": oTranslations().UNIT_NAUTICAL_MILE_nmi
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.CurrentAttribute": CS.sortBy([
      {
        "id": "B_1",
        "unit": "A",
        "unitToDisplay": "A",
        "label": oTranslations().UNIT_AMPERE_A
      },
      {
        "id": "B_2",
        "unit": "mA",
        "unitToDisplay": "mA",
        "label": oTranslations().UNIT_MILLIAMPERE_mA,
        "isBase": true
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.PotentialAttribute": CS.sortBy([
      {
        "id": "C_1",
        "unit": "V",
        "unitToDisplay": "V",
        "label": oTranslations().UNIT_VOLT_V
      },
      {
        "id": "C_2",
        "unit": "mV",
        "unitToDisplay": "mV",
        "label": oTranslations().UNIT_MILLIVOLT_mV,
        "isBase": true
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.FrequencyAttribute": CS.sortBy([
      {
        "id": "D_1",
        "unit": "Hz",
        "unitToDisplay": "Hz",
        "label": oTranslations().UNIT_HERTZ_Hz,
        "isBase": true
      },
      {
        "id": "D_2",
        "unit": "kHz",
        "unitToDisplay": "kHz",
        "label": oTranslations().UNIT_KILOHERTZ_kHz
      },
      {
        "id": "D_3",
        "unit": "MHz",
        "unitToDisplay": "MHz",
        "label": oTranslations().UNIT_MEGAHERTZ_MHz
      },
      {
        "id": "D_4",
        "unit": "GHz",
        "unitToDisplay": "GHz",
        "label": oTranslations().UNIT_GIGAHERTZ_GHz
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.TimeAttribute": CS.sortBy([
      {
        "id": "E_1",
        "unit": "ns",
        "unitToDisplay": "ns",
        "label": oTranslations().UNIT_NANOSECOND_ns
      },
      {
        "id": "E_2",
        "unit": "us",
        "unitToDisplay": "us",
        "label": oTranslations().UNIT_MICROSECOND_us
      },
      {
        "id": "E_3",
        "unit": "ms",
        "unitToDisplay": "ms",
        "label": oTranslations().UNIT_MILLISECOND_ms
      },
      {
        "id": "E_4",
        "unit": "sec",
        "unitToDisplay": "sec",
        "label": oTranslations().UNIT_SECOND_sec,
        "isBase": true
      },
      {
        "id": "E_5",
        "unit": "min",
        "unitToDisplay": "min",
        "label": oTranslations().UNIT_MINUTE_min
      },
      {
        "id": "E_6",
        "unit": "hr",
        "unitToDisplay": "hr",
        "label": oTranslations().UNIT_HOUR_hr
      },
      {
        "id": "E_7",
        "unit": "days",
        "unitToDisplay": "days",
        "label": oTranslations().DAY
      },
      {
        "id": "E_8",
        "unit": "weeks",
        "unitToDisplay": "weeks",
        "label": oTranslations().WEEK
      },
      {
        "id": "E_9",
        "unit": "years",
        "unitToDisplay": "years",
        "label": oTranslations().YEAR
      },
      {
        "id": "E_10",
        "unit": "decades",
        "unitToDisplay": "decades",
        "label": oTranslations().UNIT_DECADE_decades
      },
      {
        "id": "E_11",
        "unit": "centuries",
        "unitToDisplay": "centuries",
        "label": oTranslations().UNIT_CENTURY_centuries
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.TemperatureAttribute": CS.sortBy([
      {
        "id": "F_1",
        "unit": "tempC",
        "unitToDisplay": "°C",
        "label": oTranslations().UNIT_CELSIUS_tempC
      },
      {
        "id": "F_2",
        "unit": "tempF",
        "unitToDisplay": "°F",
        "label": oTranslations().UNIT_FAHRENHEIT_tempF
      },
      {
        "id": "F_3",
        "unit": "tempK",
        "unitToDisplay": "K",
        "label": oTranslations().UNIT_KELVIN_tempK,
        "isBase": true
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.VolumeAttribute": CS.sortBy([
      {
        "id": "G_1",
        "unit": "gal",
        "unitToDisplay": "gal",
        "label": oTranslations().UNIT_GALLON_gal
      },
      {
        "id": "G_2",
        "unit": "qt",
        "unitToDisplay": "qt",
        "label": oTranslations().UNIT_QUART_qt
      },
      {
        "id": "G_3",
        "unit": "pt",
        "unitToDisplay": "pt",
        "label": oTranslations().UNIT_PINT_pt
      },
      {
        "id": "G_4",
        "unit": "cu",
        "unitToDisplay": "cu",
        "label": oTranslations().UNIT_CUP_cu
      },
      {
        "id": "G_5",
        "unit": "floz",
        "unitToDisplay": "floz",
        "label": oTranslations().UNIT_OUNCE_floz
      },
      {
        "id": "G_6",
        "unit": "tbsp",
        "unitToDisplay": "tbsp",
        "label": oTranslations().UNIT_TABLESPOON_tbsp
      },
      {
        "id": "G_7",
        "unit": "tsp",
        "unitToDisplay": "tsp",
        "label": oTranslations().UNIT_TEASPOON_tsp
      },
      {
        "id": "G_8",
        "unit": "m3",
        "unitToDisplay": "m³",
        "label": oTranslations().UNIT_CUBIC_METRE_m3
      },
      {
        "id": "G_9",
        "unit": "L",
        "unitToDisplay": "L",
        "label": oTranslations().UNIT_LITRE_L,
        "isBase": true
      },
      {
        "id": "G_10",
        "unit": "ml",
        "unitToDisplay": "ml",
        "label": oTranslations().UNIT_MILLILITRE_ml
      },
      {
        "id": "G_11",
        "unit": "ft3",
        "unitToDisplay": "ft³",
        "label": oTranslations().UNIT_CUBIC_FOOT_ft3
      },
      {
        "id": "G_12",
        "unit": "in3",
        "unitToDisplay": "in³",
        "label": oTranslations().UNIT_CUBIC_INCH_in3
      },
      {
        "id": "G_13",
        "unit": "cm3",
        "unitToDisplay": "cm³",
        "label": oTranslations().UNIT_CUBIC_CENTIMETER_cm3
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.AreaAttribute": CS.sortBy([
      {
        "id": "H_1",
        "unit": "km2",
        "unitToDisplay": "km²",
        "label": oTranslations().UNIT_SQ_KILOMETRE_km2
      },
      {
        "id": "H_2",
        "unit": "m2",
        "unitToDisplay": "m²",
        "label": oTranslations().UNIT_SQ_METRE_m2,
        "isBase": true
      },
      {
        "id": "H_3",
        "unit": "mi2",
        "unitToDisplay": "mi²",
        "label": oTranslations().UNIT_SQ_MILE_mi2
      },
      {
        "id": "H_4",
        "unit": "yd2",
        "unitToDisplay": "yd²",
        "label": oTranslations().UNIT_SQ_YARD_yd2
      },
      {
        "id": "H_5",
        "unit": "sqft",
        "unitToDisplay": "sqft",
        "label": oTranslations().UNIT_SQFT_sqft
      },
      {
        "id": "H_6",
        "unit": "in2",
        "unitToDisplay": "in²",
        "label": oTranslations().UNIT_SQ_INCH_in2
      },
      {
        "id": "H_7",
        "unit": "hectare",
        "unitToDisplay": "hectare",
        "label": oTranslations().UNIT_HECTARE_hectare
      },
      {
        "id": "H_8",
        "unit": "acre",
        "unitToDisplay": "acre",
        "label": oTranslations().UNIT_ACRE_acre
      },
      {
        "id": "H_9",
        "unit": "cm2",
        "unitToDisplay": "cm2",
        "label": oTranslations().UNIT_SQ_CENTIMETRE_cm2
      },
      {
        "id": "H_10",
        "unit": "mm2",
        "unitToDisplay": "mm²",
        "label": oTranslations().UNIT_SQ_MILLIMETER_mm2
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.MassAttribute": CS.sortBy([
      {
        "id": "I_1",
        "unit": "tonne",
        "unitToDisplay": "tonne",
        "label": oTranslations().UNIT_TONNE_tonne
      },
      {
        "id": "I_2",
        "unit": "kg",
        "unitToDisplay": "kg",
        "label": oTranslations().UNIT_KILOGRAM_kg
      },
      {
        "id": "I_3",
        "unit": "g",
        "unitToDisplay": "g",
        "label": oTranslations().UNIT_GRAM_g,
        "isBase": true
      },
      {
        "id": "I_4",
        "unit": "mg",
        "unitToDisplay": "mg",
        "label": oTranslations().UNIT_MILLIGRAM_mg
      },
      {
        "id": "I_5",
        "unit": "ug",
        "unitToDisplay": "ug",
        "label": oTranslations().UNIT_MICROGRAM_ug
      },
      {
        "id": "I_6",
        "unit": "ton",
        "unitToDisplay": "ton",
        "label": oTranslations().UNIT_US_TON_ton
      },
      {
        "id": "I_7",
        "unit": "stone",
        "unitToDisplay": "stone",
        "label": oTranslations().UNIT_STONE_stone
      },
      {
        "id": "I_8",
        "unit": "lbs",
        "unitToDisplay": "lbs",
        "label": oTranslations().UNIT_LBS_lbs
      },
      {
        "id": "I_9",
        "unit": "oz",
        "unitToDisplay": "oz",
        "label": oTranslations().UNIT_OUNCE_oz
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.DigitalStorageAttribute": CS.sortBy([
      {
        "id": "J_1",
        "unit": "b",
        "unitToDisplay": "b",
        "label": oTranslations().UNIT_BIT_b
      },
      {
        "id": "J_2",
        "unit": "kb",
        "unitToDisplay": "kb",
        "label": oTranslations().UNIT_KILOBIT_kb
      },
      {
        "id": "J_3",
        "unit": "Kib",
        "unitToDisplay": "Kib",
        "label": oTranslations().UNIT_KIBIBIT_Kib
      },
      {
        "id": "J_4",
        "unit": "Mb",
        "unitToDisplay": "Mb",
        "label": oTranslations().UNIT_MEGABIT_Mb,
        "isBase": true
      },
      {
        "id": "J_5",
        "unit": "Mib",
        "unitToDisplay": "Mib",
        "label": oTranslations().UNIT_MEBIBIT_Mib
      },
      {
        "id": "J_6",
        "unit": "Gb",
        "unitToDisplay": "Gb",
        "label": oTranslations().UNIT_GIGABIT_Gb
      },
      {
        "id": "J_7",
        "unit": "Gib",
        "unitToDisplay": "Gib",
        "label": oTranslations().UNIT_GIBIBIT_Gib
      },
      {
        "id": "J_8",
        "unit": "Tb",
        "unitToDisplay": "Tb",
        "label": oTranslations().UNIT_TERABIT_Tb
      },
      {
        "id": "J_9",
        "unit": "Tib",
        "unitToDisplay": "Tib",
        "label": oTranslations().UNIT_TEBIBIT_Tib
      },
      {
        "id": "J_10",
        "unit": "Pb",
        "unitToDisplay": "Pb",
        "label": oTranslations().UNIT_PETABIT_P
      },
      {
        "id": "J_11",
        "unit": "Pib",
        "unitToDisplay": "Pib",
        "label": oTranslations().UNIT_PEBIBIT_Pib
      },
      {
        "id": "J_12",
        "unit": "B",
        "unitToDisplay": "B",
        "label": oTranslations().UNIT_BYTE_B
      },
      {
        "id": "J_13",
        "unit": "kB",
        "unitToDisplay": "kB",
        "label": oTranslations().UNIT_KILOBYTE_kB
      },
      {
        "id": "J_14",
        "unit": "KiB",
        "unitToDisplay": "KiB",
        "label": oTranslations().UNIT_KIBIBYTE_KiB
      },
      {
        "id": "J_15",
        "unit": "MB",
        "unitToDisplay": "MB",
        "label": oTranslations().UNIT_MEGABYTE_MB
      },
      {
        "id": "J_16",
        "unit": "MiB",
        "unitToDisplay": "MiB",
        "label": oTranslations().UNIT_MEBIBYTE_Mi
      },
      {
        "id": "J_17",
        "unit": "GB",
        "unitToDisplay": "GB",
        "label": oTranslations().UNIT_GIGABYTE_GB
      },
      {
        "id": "J_18",
        "unit": "GiB",
        "unitToDisplay": "GiB",
        "label": oTranslations().UNIT_GIBIBYTE_GiB
      },
      {
        "id": "J_19",
        "unit": "TB",
        "unitToDisplay": "TB",
        "label": oTranslations().UNIT_TERABYTE_TB
      },
      {
        "id": "J_20",
        "unit": "TiB",
        "unitToDisplay": "TiB",
        "label": oTranslations().UNIT_TEBIBYTE_TiB
      },
      {
        "id": "J_21",
        "unit": "PB",
        "unitToDisplay": "PB",
        "label": oTranslations().UNIT_PETABYTE_PB
      },
      {
        "id": "J_22",
        "unit": "PiB",
        "unitToDisplay": "PiB",
        "label": oTranslations().UNIT_PEBIBYTE_PiB
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.EnergyAttribute": CS.sortBy([
      {
        "id": "K_1",
        "unit": "J",
        "unitToDisplay": "J",
        "label": oTranslations().UNIT_JOULE_J,
        "isBase": true
      },
      {
        "id": "K_2",
        "unit": "kJ",
        "unitToDisplay": "kJ",
        "label": oTranslations().UNIT_KILOJOULE_kJ
      },
      {
        "id": "K_4",
        "unit": "Cal",
        "unitToDisplay": "Cal",
        "label": oTranslations().UNIT_CALORIE_Cal
      },
      {
        "id": "K_5",
        "unit": "Wh",
        "unitToDisplay": "Wh",
        "label": oTranslations().UNIT_WATT_HOUR_Wh
      },
      {
        "id": "K_6",
        "unit": "kWh",
        "unitToDisplay": "kWh",
        "label": oTranslations().UNIT_KILOWATT_HOUR_kWh
      },
      {
        "id": "K_7",
        "unit": "BTU",
        "unitToDisplay": "BTU",
        "label": oTranslations().UNIT_BRITISH_THERMAL_UNIT_BTU
      },
      {
        "id": "K_8",
        "unit": "therm",
        "unitToDisplay": "therm",
        "label": oTranslations().UNIT_US_THERM_therm
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.PlaneAngleAttribute": CS.sortBy([
      {
        "id": "L_1",
        "unit": "deg",
        "unitToDisplay": "deg",
        "label": oTranslations().UNIT_DEGREE_deg,
        "isBase": true
      },
      {
        "id": "L_2",
        "unit": "gon",
        "unitToDisplay": "gon",
        "label": oTranslations().UNIT_GRADIAN_gon
      },
      {
        "id": "L_3",
        "unit": "mrad",
        "unitToDisplay": "mrad",
        "label": oTranslations().UNIT_MILLIRADIAN_mrad
      },
      {
        "id": "L_4",
        "unit": "rad",
        "unitToDisplay": "rad",
        "label": oTranslations().UNIT_RADIAN_rad
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.PressureAttribute": CS.sortBy([
      {
        "id": "M_1",
        "unit": "atm",
        "unitToDisplay": "atm",
        "label": oTranslations().UNIT_ATMOSPHERE_atm
      },
      {
        "id": "M_2",
        "unit": "bar",
        "unitToDisplay": "bar",
        "label": oTranslations().UNIT_BAR_bar
      },
      {
        "id": "M_3",
        "unit": "Pa",
        "unitToDisplay": "Pa",
        "label": oTranslations().UNIT_PASCAL_Pa,
        "isBase": true
      },
      {
        "id": "M_4",
        "unit": "psi",
        "unitToDisplay": "psi",
        "label": oTranslations().UNIT_POUND_FORCE_PER_SQUARE_INCH_psi
      },
      {
        "id": "M_5",
        "unit": "torr",
        "unitToDisplay": "torr",
        "label": oTranslations().UNIT_TORR_torr
      },
      {
        "id": "M_6",
        "unit": "kPa",
        "unitToDisplay": "kPa",
        "label": oTranslations().UNIT_KILO_PASCAL_kPa
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.SpeedAttribute": CS.sortBy([
      {
        "id": "N_1",
        "unit": "mph",
        "unitToDisplay": "m/h",
        "label": oTranslations().UNIT_MILES_PER_HOUR_mph,
        "isBase": true
      },
      {
        "id": "N_2",
        "unit": "fps",
        "unitToDisplay": "f/s",
        "label": oTranslations().UNIT_FOOT_PER_SECOND_fps
      },
      {
        "id": "N_3",
        "unit": "m/s",
        "unitToDisplay": "m/s",
        "label": oTranslations().UNIT_METRE_PER_SECOND_mpers
      },
      {
        "id": "N_4",
        "unit": "kph",
        "unitToDisplay": "k/h",
        "label": oTranslations().UNIT_KILOMETRE_PER_HOUR_kph
      },
      {
        "id": "N_5",
        "unit": "kt",
        "unitToDisplay": "kt",
        "label": oTranslations().UNIT_KNOT_kt
      }
    ], 'label'),

    [AttributeTypeDictionary.PRICE]: aPriceUnits,

    "com.cs.core.config.interactor.entity.attribute.PowerAttribute": CS.sortBy([
      {
        "id": "P_1",
        "unit": "W",
        "unitToDisplay": "W",
        "label": oTranslations().UNIT_WATT_W,
        "isBase": true
      },
      {
        "id": "P_2",
        "unit": "hp",
        "unitToDisplay": "hp",
        "label": oTranslations().UNIT_HORSEPOWER_hp
      },
      {
        "id": "P_3",
        "unit": "kW",
        "unitToDisplay": "kW",
        "label": oTranslations().UNIT_KILOWATT_kW
      },
      {
        "id": "P_4",
        "unit": "mW",
        "unitToDisplay": "mW",
        "label": oTranslations().UNIT_MILLIWATT_mW
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.LuminosityAttribute": CS.sortBy([
      {
        "id": "Q_1",
        "unit": "cd",
        "unitToDisplay": "cd",
        "label": oTranslations().UNIT_CANDELA_cd,
        "isBase": true
      },
      {
        "id": "Q_2",
        "unit": "mcd",
        "unitToDisplay": "mcd",
        "label": oTranslations().UNIT_MILLICANDELA_mcd
      },
      {
        "id": "Q_3",
        "unit": "lm",
        "unitToDisplay": "lm",
        "label": oTranslations().UNIT_LUMEN_lm
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.RadiationAttribute": CS.sortBy([
      {
        "id": "R_1",
        "unit": "Gy",
        "unitToDisplay": "Gy",
        "label": oTranslations().UNIT_GRAY_Gy,
        "isBase": true
      },
      {
        "id": "R_2",
        "unit": "R",
        "unitToDisplay": "R",
        "label": oTranslations().UNIT_ROENTGEN_R
      },
      {
        "id": "R_3",
        "unit": "Sv",
        "unitToDisplay": "Sv",
        "label": oTranslations().UNIT_SIEVERT_Sv
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.IlluminanceAttribute": CS.sortBy([
      {
        "id": "S_1",
        "unit": "lux",
        "unitToDisplay": "lux",
        "label": oTranslations().UNIT_LUX_lux,
        "isBase": true
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.ForceAttribute": CS.sortBy([
      {
        "id": "T_1",
        "unit": "N",
        "unitToDisplay": "N",
        "label": oTranslations().UNIT_NEWTON_N,
        "isBase": true
      },
      {
        "id": "T_2",
        "unit": "dyn",
        "unitToDisplay": "dyn",
        "label": oTranslations().UNIT_DYNE_dyn
      },
      {
        "id": "T_3",
        "unit": "lbf",
        "unitToDisplay": "lbf",
        "label": oTranslations().UNIT_POUND_FORCE_lbf
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.AccelerationAttribute": CS.sortBy([
      {
        "id": "U_1",
        "unit": "gee",
        "unitToDisplay": "gee",
        "label": oTranslations().UNIT_GEE_gee,
        "isBase": true
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.CapacitanceAttribute": CS.sortBy([
      {
        "id": "V_1",
        "unit": "F",
        "unitToDisplay": "farad",
        "label": oTranslations().UNIT_FARAD_F,
        "isBase": true
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.ViscocityAttribute": CS.sortBy([
      {
        "id": "W_1",
        "unit": "P",
        "unitToDisplay": "P",
        "label": oTranslations().UNIT_POISE_P,
        "isBase": true
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.InductanceAttribute": CS.sortBy([
      {
        "id": "X_1",
        "unit": "H",
        "unitToDisplay": "H",
        "label": oTranslations().UNIT_HENRY_H,
        "isBase": true
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.ResistanceAttribute": CS.sortBy([
      {
        "id": "Y_1",
        "unit": "ohm",
        "unitToDisplay": "Ω",
        "label": oTranslations().UNIT_OHM_ohm,
        "isBase": true
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.MagnetismAttribute": CS.sortBy([
      {
        "id": "Z_1",
        "unit": "T",
        "unitToDisplay": "T",
        "label": oTranslations().UNIT_TESLA_T,
        "isBase": true
      },
      {
        "id": "Z_3",
        "unit": "G",
        "unitToDisplay": "G",
        "label": oTranslations().UNIT_GAUSS_G
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.ChargeAttribute": CS.sortBy([
      {
        "id": "AA_1",
        "unit": "C",
        "unitToDisplay": "C",
        "label": oTranslations().UNIT_COULOMB_C,
        "isBase": true
      },
      {
        "id": "AA_2",
        "unit": "Ah",
        "unitToDisplay": "Ah",
        "label": oTranslations().UNIT_AH_Ah
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.ConductanceAttribute": CS.sortBy([
      {
        "id": "BB_1",
        "unit": "S",
        "unitToDisplay": "S",
        "label": oTranslations().UNIT_SIEMENS_S,
        "isBase": true
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.SubstanceAttribute": CS.sortBy([
      {
        "id": "CC_1",
        "unit": "mol",
        "unitToDisplay": "mol",
        "label": oTranslations().UNIT_MOLE_mol,
        "isBase": true
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.WeightPerAreaAttribute": CS.sortBy([
      {
        "id": "DD_1",
        "unit": "kg/m2",
        "unitToDisplay": "kg/m²",
        "label": oTranslations().UNIT_KILOGRAM_PER_SQUARE_METRE_kg_m2,
        "isBase": true
      },
      {
        "id": "DD_2",
        "unit": "gm/m2",
        "unitToDisplay": "g/m²",
        "label": oTranslations().UNIT_GRAM_PER_SQUARE_METRE_gm_m2
      },
      {
        "id": "DD_3",
        "unit": "lb/yd2",
        "unitToDisplay": "lb/yd²",
        "label": oTranslations().UNIT_POUND_PER_SQUARE_YARD_lb_yd2
      },
      {
        "id": "DD_4",
        "unit": "lb/ft2",
        "unitToDisplay": "lb/ft²",
        "label": oTranslations().UNIT_POUND_PER_SQUARE_FOOT_lb_ft2
      },
      {
        "id": "DD_5",
        "unit": "lb/in2",
        "unitToDisplay": "lb/in²",
        "label": oTranslations().UNIT_POUND_PER_SQUARE_INCH_lb_in2
      },
      {
        "id": "DD_6",
        "unit": "oz/in2",
        "unitToDisplay": "oz/in²",
        "label": oTranslations().UNIT_OUNCE_PER_SQUARE_INCH_oz_in2
      },
      {
        "id": "DD_7",
        "unit": "oz/ft2",
        "unitToDisplay": "oz/ft²",
        "label": oTranslations().UNIT_OUNCE_PER_SQUARE_FOOT_oz_ft2
      },
      {
        "id": "DD_8",
        "unit": "tn/mi2",
        "unitToDisplay": "tn/mi²",
        "label": oTranslations().UNIT_TON_SHORT_US_PER_SQUARE_MILE_tn_mi2
      },
      {
        "id": "DD_9",
        "unit": "tn/ac1",
        "unitToDisplay": "tn/ac1",
        "label": oTranslations().UNIT_TON_SHORT_US_PER_ACRE_tn_ac1
      },
      {
        "id": "DD_10",
        "unit": "t/km2",
        "unitToDisplay": "t/km²",
        "label": oTranslations().UNIT_TONNES_METRIC_TON_PER_SQUARE_KM_t_km2
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.ThermalInsulationAttribute": CS.sortBy([
      {
        "id": "EE_1",
        "unit": "m2K/W",
        "unitToDisplay": "m²K/W",
        "label": oTranslations().UNIT_METRIC_R_VALUE_m2K_W,
        "isBase": true
      },
      {
        "id": "EE_2",
        "unit": "hr-ft2-F/BTU",
        "unitToDisplay": "hr-ft2-F/BTU",
        "label": oTranslations().UNIT_IMPERIAL_INCH_POUND_R_VALUE_hr_ft2_F_BTU
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.ProportionAttribute": CS.sortBy([
      {
        "id": "FF_1",
        "unit": "%",
        "unitToDisplay": "%",
        "label": oTranslations().UNIT_PROPORTION,
        "isBase": true
      },
      {
        "id": "FF_2",
        "unit": "x/y",
        "unitToDisplay": "x/y",
        "label": oTranslations().UNIT_PROPORTION_x_y
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.HeatingRateAttribute": CS.sortBy([
      {
        "id": "GG_1",
        "unit": "°C/sec",
        "unitToDisplay": "°C/sec",
        "label": oTranslations().UNIT_CELSIUS_PER_SECOND_C_sec,
        "isBase": true
      },
      {
        "id": "GG_2",
        "unit": "°C/min",
        "unitToDisplay": "°C/min",
        "label": oTranslations().UNIT_CELSIUS_PER_MINUTE_C_min
      },
      {
        "id": "GG_3",
        "unit": "°C/hr",
        "unitToDisplay": "°C/hr",
        "label": oTranslations().UNIT_CELSIUS_PER_HOUR_C_hr
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.DensityAttribute": CS.sortBy([
      {
        "id": "HH_1",
        "unit": "kg/m3",
        "unitToDisplay": "kg/m³",
        "label": oTranslations().UNIT_KILOGRAM_PER_CUBIC_METRE_kg_m3,
        "isBase": true
      },
      {
        "id": "HH_2",
        "unit": "kg/cm3",
        "unitToDisplay": "kg/cm³",
        "label": oTranslations().UNIT_KILOGRAM_PER_CUBIC_CENTIMETRE_kg_cm3
      },
      {
        "id": "HH_3",
        "unit": "g/m3",
        "unitToDisplay": "g/m³",
        "label": oTranslations().UNIT_GRAM_PER_CUBIC_METRE_g_m3
      },
      {
        "id": "HH_4",
        "unit": "g/cm3",
        "unitToDisplay": "g/cm³",
        "label": oTranslations().UNIT_GRAM_PER_CUBIC_CENTIMETRE_g_cm3
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.WeightPerTimeAttribute": CS.sortBy([
      {
        "id": "II_1",
        "unit": "gm/sec",
        "unitToDisplay": "gm/sec",
        "label": oTranslations().UNIT_GRAM_PER_SECOND_gm_sec,
        "isBase": true
      },
      {
        "id": "II_2",
        "unit": "gm/min",
        "unitToDisplay": "gm/min",
        "label": oTranslations().UNIT_GRAM_PER_MINUTE_gm_min
      },
      {
        "id": "II_3",
        "unit": "gm/hr",
        "unitToDisplay": "gm/hr",
        "label": oTranslations().UNIT_GRAM_PER_HOUR_gm_hr
      },
      {
        "id": "II_4",
        "unit": "gm/day",
        "unitToDisplay": "gm/day",
        "label": oTranslations().UNIT_GRAM_PER_DAY_gm_day
      },
      {
        "id": "II_5",
        "unit": "kg/sec",
        "unitToDisplay": "kg/sec",
        "label": oTranslations().UNIT_KILOGRAM_PER_SECOND_kg_sec
      },
      {
        "id": "II_6",
        "unit": "kg/min",
        "unitToDisplay": "kg/min",
        "label": oTranslations().UNIT_KILOGRAM_PER_MINUTE_kg_min
      },
      {
        "id": "II_7",
        "unit": "kg/hour",
        "unitToDisplay": "kg/hour",
        "label": oTranslations().UNIT_KILOGRAM_PER_HOUR_kg_hour
      },
      {
        "id": "II_8",
        "unit": "kg/day",
        "unitToDisplay": "kg/day",
        "label": oTranslations().UNIT_KILOGRAM_PER_DAY_kg_day
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.VolumeFlowRateAttribute": CS.sortBy([
      {
        "id": "JJ_1",
        "unit": "m3/sec",
        "unitToDisplay": "m³/sec",
        "label": oTranslations().UNIT_CUBIC_METRE_PER_SECOND_m3_sec,
        "isBase": true
      },
      {
        "id": "JJ_2",
        "unit": "m3/min",
        "unitToDisplay": "m³/min",
        "label": oTranslations().UNIT_CUBIC_METRE_PER_MINUTE_m3_min
      },
      {
        "id": "JJ_3",
        "unit": "m3/hr",
        "unitToDisplay": "m³/hr",
        "label": oTranslations().UNIT_CUBIC_METRE_PER_HOUR_m3_hr
      },
      {
        "id": "JJ_4",
        "unit": "L/sec",
        "unitToDisplay": "L/sec",
        "label": oTranslations().UNIT_LITRE_PER_SECOND_L_sec
      },
      {
        "id": "JJ_5",
        "unit": "L/min",
        "unitToDisplay": "L/min",
        "label": oTranslations().UNIT_LITRE_PER_MINUTE_L_min
      },
      {
        "id": "JJ_6",
        "unit": "L/hr",
        "unitToDisplay": "L/hr",
        "label": oTranslations().UNIT_LITRE_PER_HOUR_L_hr
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.AreaPerVolumeAttribute": CS.sortBy([
      {
        "id": "LL_1",
        "unit": "cm2/mL",
        "unitToDisplay": "cm²/mL",
        "label": oTranslations().UNIT_CENTIMETRE_SQUARE_PER_MILLILITRE_cm2_mL,
        "isBase": true
      },
      {
        "id": "LL_2",
        "unit": "cm2/L",
        "unitToDisplay": "cm²/L",
        "label": oTranslations().UNIT_CENTIMETRE_SQUARE_PER_LITRE_cm2_L
      },
      {
        "id": "LL_3",
        "unit": "m2/mL",
        "unitToDisplay": "m²/mL",
        "label": oTranslations().UNIT_METRE_SQUARE_PER_MILLILITRE_m2_mL
      },
      {
        "id": "LL_4",
        "unit": "m2/L",
        "unitToDisplay": "m²/L",
        "label": oTranslations().UNIT_METRE_SQUARE_PER_LITRE_m2_L
      }
    ], 'label'),
    "com.cs.core.config.interactor.entity.attribute.RotationFrequencyAttribute": CS.sortBy([
      {
        "id": "MM_1",
        "unit": "rev/sec",
        "unitToDisplay": "rev/sec",
        "label": oTranslations().UNIT_REVOLUTIONS_PER_SECOND_rev_sec,
        "isBase": true
      },
      {
        "id": "MM_2",
        "unit": "rev/min",
        "unitToDisplay": "rev/min",
        "label": oTranslations().UNIT_REVOLUTIONS_PER_MINUTE_rev_min
      },
      {
        "id": "MM_3",
        "unit": "rev/hr",
        "unitToDisplay": "rev/hr",
        "label": oTranslations().UNIT_REVOLUTIONS_PER_HOUR_rev_hr
      }
    ], 'label')
  }
};

export default oMeasurementMetricsAndImperial;


//NOTE:Script to get all base units (oAttributes = exported object)
/*
for(var key in oAttributes){
  var aAttributeUnits = oAttributes[key];
  var aSplits = key.split(".");
  var sAttributeName = aSplits[aSplits.length - 1];

  for(var i=0; i<aAttributeUnits.length; i++){
    var oUnit = aAttributeUnits[i];
    if(oUnit.isBase){
      console.log(sAttributeName + " : " + oUnit.label + " : " + oUnit.unit);
      break;
    }
  }
}*/


