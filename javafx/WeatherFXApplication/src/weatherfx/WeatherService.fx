
/*
 * Copyright (c) 2007,2008 Sun Microsystems, Inc.
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

import javafx.io.http.HttpRequest;
import javafx.data.pull.PullParser;
import javafx.data.xml.QName;
import java.io.InputStream;
import java.lang.Double;


 /**
 * Loads weather from Yahoo Weather web service 
 *
 * @author breh
 */


// conditions the app understands
public def UNKNOWN:Integer = 0;
public def CLEAR:Integer = 1;
public def CLOUDS:Integer = 2;
public def RAIN:Integer = 3;
public def SNOW:Integer = 4;
public def THUNDER:Integer = 5;
public def MOON:Integer = 6;

/**
 * An encapsulation of yahoo weather service using FX web service APIs
 */
public class WeatherService {

    // location in as a string understood by yahoo weather service
    public var location: String on replace {
        // reload the data
        loadFromYWS();
    }
    

    public-read var cityName:String;
    public-read var temperature:Number;
    public-read var windSpeed:Number;
    public-read var windDirection:Number;
    public-read var windInformation:String;

    public-read var todayLows:Number;
    public-read var todayHighs:Number;
    public-read var todayConditionCode: Integer;

    public-read var tomorrowLows:Number;
    public-read var tomorrowHighs:Number;
    public-read var tomorrowConditionCode: Integer;


    // called when the data has been sucesfully loaded -> used to switch from loading gfx to weather gfx
    public var onLoaded: function(service:WeatherService);
    
    // load the data from yahoo weather service
    function loadFromYWS() {
        var request:HttpRequest = HttpRequest {
            location: "http://weather.yahooapis.com/forecastrss?p={location}"
            onException: function(e) {
                e.printStackTrace();
            }
            onResponseCode: function(responseCode: Integer) {
                if (responseCode != 200 and responseCode != 304) {
                    println("failed to fetch feed, response: "
                    "{responseCode} {request.responseMessage}");
                }
            }
            onInput: function(input) {
                try {
                    parse(input);
                } finally {
                    input.close();
                }
            }
        };
        request.enqueue();
    }


    // parse the loaded result
    function parse(is: InputStream) {
        def yweather = "http://xml.weather.yahoo.com/ns/rss/1.0";
        def parser = PullParser { input: is }

        parser.seek(QName{ name:"location" namespace:yweather}, 2);
        cityName = parseStringValue(parser, "city");

        parser.seek(QName{ name:"wind" namespace:yweather}, 2);
        windSpeed = parseNumberValue(parser, "speed");
        windDirection = parseNumberValue(parser, "direction");
        windInformation = WeatherService.getWindInformation(windDirection, windSpeed);

        parser.seek(QName{ name:"condition" namespace:yweather}, 3);
        temperature = parseNumberValue(parser, "temp");
        todayConditionCode = translateConditionCode(parseNumberValue(parser, "code"));

        parser.seek(QName{name:"forecast" namespace:yweather}, 3);
        todayLows = parseNumberValue(parser, "low");
        todayHighs = parseNumberValue(parser, "high");

        parser.seek(QName{name:"forecast" namespace:yweather}, 3);
        tomorrowLows = parseNumberValue(parser, "low");
        tomorrowHighs = parseNumberValue(parser, "high");
        tomorrowConditionCode = translateConditionCode(parseNumberValue(parser, "code"));

        onLoaded(this);
    }

}


// helper for parsinfg strings from XML stream
function parseStringValue(parser:PullParser, name: String):String {
    return parser.event.getAttributeValue(QName{name:name}) as String
}

// helper for parsing numbers from XML stream
function parseNumberValue(parser:PullParser, name: String):Number {
    try {
        return Double.parseDouble(parser.event.getAttributeValue(QName{name:name}));
    } catch (e) {
        return 0;
    }
}

// return the wind information aggregated from direction and speed
function getWindInformation(direction:Number, speed:Number): String {
    return "{translateDirectionToString(direction)} {%02.0f speed}";
}

// translates direction from number to string (0->N, etc ...)
function translateDirectionToString(dir:Number):String {
    var windDirs = ["N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"];
    var tmpdir = dir mod 360;
    var i = ((
    tmpdir + 22.5 ) / 45).intValue();
    return windDirs[i];
}


// translates yahoo condition code to out condition code
function translateConditionCode(code:Integer):Integer {
    return
        if ((code >= 0) and ( code < 5))  then
            THUNDER
        else if ((code >= 5) and (code < 9)) then
            SNOW
        else if ((code >= 9) and (code < 13)) then
            RAIN
        else if ((code >= 13) and (code < 19)) then
            SNOW
        else if ((code >= 19) and (code < 26)) then
            CLEAR
        else if ((code >= 26) and (code < 31)) then
            CLOUDS
        else if (code == 31) then
            MOON
        else if (code == 32) then
            CLEAR
        else if (code == 33) then
            MOON
        else if (code == 34) then
            CLEAR
        else if (code == 35) then
            RAIN
        else if (code == 36) then
            CLEAR
        else if ((code >= 37) and (code < 40)) then
            THUNDER
        else if ((code >= 41) and (code < 44)) then
            SNOW
        else if (code == 44) then
            CLOUDS
        else if (code == 45) then
            THUNDER
        else if (code == 46) then
            SNOW
        else if (code == 47) then
            THUNDER
        else UNKNOWN
        ;
    }
