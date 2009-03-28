
/*
 * Copyright (c) 2007, Sun Microsystems, Inc.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in 
 *    the documentation and/or other materials provided with the distribution.
 *  * Neither the name of Sun Microsystems, Inc. nor the names of its 
 *    contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED 
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package weatherfx;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;


/**
 * Main class of the app
 * 
 * @author breh
 */

var weatherWidget1 = Weather{ transforms: Transform.translate(10,10) };
var weatherWidget2 = Weather{ transforms: Transform.translate(10,100) };
var weatherWidget3 = Weather{ transforms: Transform.translate(10,190) };

Stage {
    scene: Scene {
        fill: Color.BLACK
        content:[
            // bounding rectanlgle - computes the correct size of the window based on the size
            // does not have to be visible - used to specify app graphics extent 
            Rectangle {
                x:0 y:0
                width: weatherWidget3.boundsInScene.maxX+10
                height: weatherWidget3.boundsInScene.maxY + 10
                visible: false
            }
            // weather widtgets
            weatherWidget1,
            weatherWidget2,
            weatherWidget3
        ]
    }
    title: "WeatherFX"
    resizable: false
};


// show the weather information
showWeather("EZXX0012",weatherWidget1);
showWeather("FRXX0076",weatherWidget2);
showWeather("94303",weatherWidget3);


// calls weather service and asssigns the values to weather widget
function showWeather(location:String, weatherWidget:Weather):Void {
    WeatherService {
        location: location
        onLoaded: function(ws) {
            weatherWidget.cityName = ws.cityName;
            weatherWidget.todayHigh = "{ws.todayHighs as Integer}";
            weatherWidget.todayLow = "{ws.todayLows as Integer}";
            weatherWidget.currentTemp = "{ws.temperature as Integer}";
            weatherWidget.todayCondition = ws.todayConditionCode;
            weatherWidget.tomorrowCondition = ws.tomorrowConditionCode;
            weatherWidget.windDirection = ws.windDirection;
            weatherWidget.windInfo = ws.windInformation;
            weatherWidget.tomorrowLow = "{ws.tomorrowLows as Integer}";
            weatherWidget.tomorrowHigh = "{ws.tomorrowHighs as Integer}";
            weatherWidget.showWeather();
        }
    }
}



