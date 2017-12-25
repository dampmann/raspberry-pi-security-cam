/*
    This program is using pi4j in order to listen
    for a gpio change event caused by a function call
    to a particle photon (particle.io). The photon will
    set the gpio pin 0 on the raspberry pi to high for 1
    second. If this happens then the program will take a
    picture using the camera connected to the raspberry pi,
    then upload this picture to aws s3, create a pre-signed
    link and send the result to a slack channel.
*/
package com.dampmann.picam;

import com.pi4j.io.gpio.*;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class Main {
    public static void main(String[] args) 
        throws InterruptedException, PlatformAlreadyAssignedException {
        PlatformManager.setPlatform(Platform.RASPBERRYPI);
        final GpioController gpio = GpioFactory.getInstance();
        final Pin pin = RaspiPin.getPinByName("GPIO 0");
        System.out.println(pin);
        System.out.println("Using pin: " + pin.toString());
        GpioPinDigitalInput inputPin = gpio.provisionDigitalInputPin(pin);
        inputPin.addListener(new GpioPinListenerDigital() {
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                }
                });

        try {
        while(true) {
            Thread.sleep(500);
        }
        } catch(InterruptedException e) {
            System.err.println("Interrupted");
            gpio.shutdown();
        }
    }

}
