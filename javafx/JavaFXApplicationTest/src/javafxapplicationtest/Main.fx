/*
 * Main.fx
 *
 * Created on May 12, 2009, 11:22:08 AM
 */

package javafxapplicationtest;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

/**
 * @author rshi
 */

Stage {
    title: "Application title"
    width: 250
    height: 80
    scene: Scene {
        content: Text {
            font : Font {
                size : 16
            }
            x: 10, y: 30
            content: "Application content"
        }
    }
}