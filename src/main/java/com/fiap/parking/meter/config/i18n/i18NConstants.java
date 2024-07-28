package com.fiap.parking.meter.config.i18n;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class i18NConstants {

    //Error messages
    public static final String PAYMENT_METHOD_NOT_FOUND = "error.payment.not.found";
    public static final String VEHICLE_NOT_FOUND = "error.vehicle.not.found";
    public static final String PARKING_NOT_FOUND = "error.parking.not.found";
    public static final String DRIVER_NOT_FOUND = "error.driver.not.found";
    public static final String VEHICLE_NOT_ASSOCIATED_DRIVER = "error.vehicle.not.associated.driver";

    //BusinessException
    public static final String PARKING_DURATION_REQUIRED_FIXED_PERIOD_PARKING= "error.parking.duration.required.for.fixed.period.parking";

    //Messages for notifications
    public static final String FIXED_PERIOD_PARKING_EXPIRATION = "message.fixed.period.parking.about.to.expire";
    public static final String SYSTEM_AUTOMATICALLY_EXTEND_PARKING_ANOTHER_HOUR_UNLESS_DRIVER_TURNS_OFF_REGISTRATION="message.system.will.automatically.extend";


}
