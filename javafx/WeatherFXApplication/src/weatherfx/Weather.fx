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

/*
 * Weather.fx
 *
 * Created on Nov 4, 2008, 12:03:04 PM
 */

package weatherfx;

import javafx.animation.*;
import javafx.animation.transition.*;
import javafx.scene.image.*;
import javafx.scene.*;
import javafx.scene.transform.*;
import javafx.scene.input.*;

/**
 *
 * This class contains animations and other logic for the weather widget
 *
 * @author breh
 */

public class Weather extends WeatherGfxUI {


    // public variables

    // today low temperature
    public var todayLow:String = "?" on replace {
        todaysLowsText.content = todayLow;
    }

    // today high temperature
    public var todayHigh:String = "?" on replace {
        todaysHighsText.content = todayHigh;
    }

    // current temperature
    public var currentTemp = " ?" on replace {
        currentTempText.content = currentTemp;
    }

    // tomorrow highs
    public var tomorrowHigh = "?" on replace {
        tomorrowHighsText.content = tomorrowHigh;
    }

    // tomorrow lows
    public var tomorrowLow = "?" on replace {
        tomorrowLowsText.content = tomorrowLow;
    }


    // today condition
    public var todayCondition:Integer on replace {
        if (isInitialized(todayCondition)) { // the default value is to show nothing
            todayConditionAnim = getTodayConditionAnims()[todayCondition];
        }
    }

    // tomorrow condition
    public var tomorrowCondition:Integer on replace {
        if (isInitialized(tomorrowCondition)) { // the default value is to show nothing
            tomorrowConditionAnim = getTomorrowConditionoAnims()[tomorrowCondition];
        }
    }

    public var cityName:String = "Unknown" on replace {
        cityText.content = cityName;
    }

    public var windInfo:String = "" on replace {
        windText.content = windInfo;
    }


    public var windDirection:Number = 0 on replace {
        windDirectionBlock.transforms = Transform.rotate(windDirection - 69, 192.0, 98.0)
    }

    // shows weather - turns off the loading screen
    public function showWeather() {
        // load the anims at this point ...
        insert getTodayConditionAnims() into weatherConditionGroup.content;
        insert getTomorrowConditionoAnims() into weatherConditionGroup.content;
        // show the weather screen (by moving away the loading screen ...)
        SequentialTransition {
            node: loadingScreen
            content: [
                FadeTransition { toValue: 0.7 duration: 300ms},
                PauseTransition { duration: 200ms }
                ParallelTransition { content: [
                    TranslateTransition{toX: 270 duration: 1.2s interpolate:Interpolator.EASEIN},
                    FadeTransition { toValue: 0.0 duration: 1.5s}
                ]}
            ]
            action: function() {
                // activate the UI interactivity
                tomorrowButton.cursor = Cursor.HAND;
                // install necessary mouse listeners
                tomorrowButton.onMouseClicked = tomorrowClickedFunction;
            }
        }.play();

    }


    
    // internal logic

    // condition holders
    var todayConditionAnims:Node[];
    var tomorrowConditionAnims:Node[];

    function getTodayConditionAnims():Node[] {
        if (todayConditionAnims == null) {
            todayConditionAnims = createConditionAnims();
        }
        return todayConditionAnims;
    }

    var todayConditionAnim:Node on replace oldValue {
        //println("todatCondition visiblity: old: {oldValue.visible}, new: {todayConditionAnim.visible}, for city {cityName}");
        if (oldValue != null) {
            oldValue.visible = false;
            oldValue.opacity = 0.0;
        }
        if (todayConditionAnim != null) {
            todayConditionAnim.visible = true;
            todayConditionAnim.opacity = 1.0;
        }
        //println("  --> updated old: {oldValue.visible}, new: {todayConditionAnim.visible}, for city {cityName}");
    }

    function getTomorrowConditionoAnims():Node[] {
        if (tomorrowConditionAnims == null) {
            tomorrowConditionAnims = createConditionAnims();
        }
        return tomorrowConditionAnims;
    }

    var tomorrowConditionAnim:Node on replace oldValue {
        if (oldValue != null) {
            oldValue.visible = false;
            oldValue.opacity = 0.0;
        }
        if (tomorrowConditionAnim != null) {
            tomorrowConditionAnim.visible = true;
            todayConditionAnim.opacity = 1.0;
        }
    }

    function createConditionAnims():ImageView[] {
        return [
                ImageView {},
                ImageView{image: Image{url:"{__DIR__}imgs/sun.png"} visible: false opacity: 0.0 visible: false},
                AnimatedImage { baseURL: "{__DIR__}imgs/cloud" baseName: "cloud_00", extension: "png", length: 33 visible: false opacity: 0.0 visible: false},
                AnimatedImage { baseURL: "{__DIR__}imgs/rain" baseName: "rain_00", extension: "png", length: 33 visible: false opacity: 0.0 visible: false},
                AnimatedImage { baseURL: "{__DIR__}imgs/snow" baseName: "snow_00", extension: "png", length: 33 visible: false opacity: 0.0 visible: false},
                AnimatedImage { baseURL: "{__DIR__}imgs/lightning" baseName: "lightning_00", extension: "png", length: 33 visible: false opacity:0.0 visible: false},
                ImageView{ image: Image{url:"{__DIR__}imgs/moon.png"} visible: false opacity: 0.0 visible: false},
            ];
    }


    // override the ipdate method from parent - called when the
    // graphics is loaded from the fxz file
    override protected function update() {
        // make sure the parent updates the gfx
        super.update();

        // update the gfx attribues
        todayButtonEdge.opacity = 0.0;
        loadingScreen.visible = true;
        // cache important nodes to get better performance
        loadingScreen.cache = true;
        background.cache = true;
        cityText.cache = true;
        currentTempBlock.cache = true;
        highsTriangle.cache = true;
        lowsTriangle.cache = true;
        todaysHighsText.cache = true;
        todaysLowsText.cache = true;
        tomorrowHighsText.cache = true;
        tomorrowLowsText.cache = true;
        windDirectionBlock.cache = true;
        //windText.cache = true; // cannot be cached because of the bug in the scene graph
    }


    //def tomorrowClickedFunction:function(e:MouseEvent):Void;

    def toadyClickedFunction:function(e:MouseEvent):Void = function(e: MouseEvent):Void {
        todayButton.cursor = null;
        todayButton.onMouseClicked = null;
        // play switch to today animation
        switchToTodayAnimation.playFromStart();        
    }


    def tomorrowClickedFunction:function(e:MouseEvent):Void = function(e: MouseEvent):Void {
        tomorrowButton.cursor = null;
        tomorrowButton.onMouseClicked = null;
        // play switch to today animation
        switchToTomorrowAnimation.playFromStart();
        
    }



    // animations
    
    // animation for switching to tomorrow panel
    def switchToTomorrowAnimation = ParallelTransition {  content: [
                // today button
                FadeTransition { duration: 1.5s node: todayButtonEdge toValue: 1.0},
                // tomorrow button
                FadeTransition { duration: 1.5s node: tomorrowButtonEdge toValue: 0.0},

                // cross fade for conditions - bind is needed, as the anim node variables can change
                FadeTransition {node : bind todayConditionAnim toValue: 0.0 duration: 1.5s}
                FadeTransition {node : bind tomorrowConditionAnim toValue: 1.0 duration: 1.5s}

                // today temps/wind disappear
                FadeTransition { node: currentTempBlock toValue: 0.0 duration: 0.5s},
                SequentialTransition { content: [
                    PauseTransition { duration: 0.2s},
                    FadeTransition { node: windDirectionBlock toValue: 0.0  duration: 0.5s}
                ]},

                SequentialTransition { content: [
                    PauseTransition { duration: 0.3s},
                    TranslateTransition { duration: 0.5s node: todaysLowsText byY: 50},
                ]},
                SequentialTransition { content: [
                    PauseTransition { duration: 0.4s},
                    TranslateTransition { duration: 0.5s node: todaysHighsText byY: -50},
                ]},

                // temp triangles morph
                SequentialTransition { content: [
                    PauseTransition { duration: 0.4s},
                    ParallelTransition { node: lowsTriangle content: [
                        TranslateTransition {toX: 105 toY:-7 duration: 0.5s}
                        ScaleTransition { toX: 2.0 toY: 2.0 duration: 0.5s}
                        RotateTransition { duration: 0.5s byAngle: 360}
                    ]},
                ]},
                SequentialTransition { content: [
                    PauseTransition { duration: 0.6s},
                    ParallelTransition { node: highsTriangle content: [
                        TranslateTransition {toX: 10 toY:11 duration: 0.3s}
                        ScaleTransition { toX: 2.0 toY: 2.0 duration: 0.3s}
                    ]},
                ]},
                
                // tomorrow temps appear
                SequentialTransition { content: [
                    PauseTransition { duration: 0.8s},
                    TranslateTransition {node: tomorrowHighsText toY: 70 duration: 0.5s},
                ]},
                SequentialTransition { content: [
                    PauseTransition { duration: 1.0s},
                    TranslateTransition {node: tomorrowLowsText toY: -70 duration: 0.5s},
                ]},
        ]
        action: function() {
            // switch the buttons interactity
            todayButton.cursor = Cursor.HAND;
            todayButton.onMouseClicked = toadyClickedFunction;
        }
    };


    // animation for switing to today panel
    def switchToTodayAnimation = ParallelTransition {  content: [
                // today button
                FadeTransition { duration: 1.5s node: tomorrowButtonEdge toValue: 1.0},
                // tomorrow button
                FadeTransition { duration: 1.5s node: todayButtonEdge toValue: 0.0},

                 // cross fade for conditions - bind is needed, as the anim node variables can change
                FadeTransition {node : bind todayConditionAnim toValue: 1.0 duration: 1.5s}
                FadeTransition {node : bind tomorrowConditionAnim toValue: 0.0 duration: 1.5s}


                // tomorrow nodes
                FadeTransition { node: currentTempBlock toValue: 0.0 duration: 0.5s},
                SequentialTransition { content: [
                    PauseTransition { duration: 0.2s},
                    FadeTransition { node: windDirectionBlock toValue: 0.0  duration: 0.5s}
                ]},

                // tomorrow temps disapper
                SequentialTransition { node: tomorrowHighsText content: [
                    PauseTransition { duration: 0.1s},
                    FadeTransition { toValue: 0.0 duration: 0.5s
                        action: function () {
                            // reset values
                            tomorrowHighsText.translateY = 0.0;
                            tomorrowHighsText.opacity = 1.0;
                        }
                    }
                ]},
                SequentialTransition { node: tomorrowLowsText content: [
                    PauseTransition { duration: 0.1s},
                    FadeTransition { toValue: 0.0 duration: 0.5s
                        action: function () {
                            // reset values
                            tomorrowLowsText.translateY = 0.0;
                            tomorrowLowsText.opacity = 1.0;
                        }
                    }
                ]},

                // temp triangles reposition
                SequentialTransition { node: lowsTriangle content: [
                    PauseTransition { duration: 0.1s},
                    TranslateTransition {toY:70 duration: 0.5s},
                    RotateTransition { toAngle: 0 duration: 1ms},
                    ScaleTransition { toX: 1.0 toY: 1.0 duration: 1ms},
                    TranslateTransition { fromX: 0.0 toX: 0.0 toY:0 duration: 0.5s}
                ]},
                SequentialTransition { node: highsTriangle content: [
                    PauseTransition { duration: 0.1s},
                    TranslateTransition {toY:-70 duration: 0.5s},
                    ScaleTransition { toX: 1.0 toY: 1.0 duration: 1ms},
                    TranslateTransition { fromX: 0.0 toX: 0.0 toY:0 duration: 0.5s}
                ]},

                // today temps/wind appear
                SequentialTransition { node: todaysHighsText content: [
                    FadeTransition { toValue: 0.0 duration: 0.7s},
                    TranslateTransition { toY: 0 duration: 1ms}
                    FadeTransition { toValue: 1.0 duration: 0.5s}
                ]},
                SequentialTransition { node: todaysLowsText content: [
                    FadeTransition { toValue: 0.0 duration: 0.9s},
                    TranslateTransition { toY: 0 duration: 1ms}
                    FadeTransition { toValue: 1.0 duration: 0.5s}
                ]},

                SequentialTransition { content: [
                    PauseTransition { duration: 1.0s},
                    FadeTransition { node: currentTempBlock toValue: 1.0 duration: 0.5s},
                ]},
                SequentialTransition { content: [
                    PauseTransition { duration: 1.2s},
                    FadeTransition { node: windDirectionBlock toValue: 1.0  duration: 0.5s}
                ]},

        ]
        action: function() {
            // switch the buttons interactity
            tomorrowButton.cursor = Cursor.HAND;
            tomorrowButton.onMouseClicked = tomorrowClickedFunction;
        }
    };


}
