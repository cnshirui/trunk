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

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.*;
import javafx.util.*;

/**
 * Animates a set of images using specified frame delay
 * 
 * @author breh
 */

public class AnimatedImage extends ImageView {

    
    public-init var baseURL: String;
    public-init var baseName: String;
    public-init var extension: String;

    public-init var length: Integer = 0;

    postinit {
        loadImages();
    }


    var images: Image[];
    
    var currentImageIndex:Integer ;
    
    var movieTimeline = Timeline {
        repeatCount: Timeline.INDEFINITE
        keyFrames: KeyFrame {
            time: 1s/12
            canSkip: true
            action: function() {                
                currentImageIndex += 1;
                if (currentImageIndex >= length) {
                    currentImageIndex = 0;
                }
                image = images[currentImageIndex];
            }
        },
    };    


    /*
    override var opacity on replace {
        if (opacity > 0.0) {
            movieTimeline.play();
        } else {
            movieTimeline.pause();
        }
    }*/


    override var visible  = false on replace {
        if (visible) {
            movieTimeline.play();
        } else {
            movieTimeline.pause();
        }
    }
          
    
     function loadImages():Void {
        var count = length - 1;
        if (baseURL != null) {
            images =  for (i in [0..count]) {
                getCachedImage("{baseURL}/{baseName}{%03d i}.{extension}");
            };    
        }
    }   
}


var cachedUrls:String[];
var cachedImages:Image[];

function getCachedImage(url:String):Image {
    var img:Image = null;
    if (url != null) {
        var imgIndex = Sequences.indexOf(cachedUrls, url);
        if (imgIndex >=0) {
            img = cachedImages[imgIndex];
        } else {
            img = Image {url:url};
            insert url into cachedUrls;
            insert img into cachedImages;
        }
    }
    return img;
}