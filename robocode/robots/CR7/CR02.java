package CR7;

import robocode.*;
import java.awt.Color;

import robocode.HitRobotEvent;
import robocode.Robot;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;
import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.*;

public class CR02 extends AdvancedRobot {
    
    boolean movingForward;

    public void run() {
        setColors(Color.white, Color.white, Color.yellow); 

        setAdjustGunForRobotTurn(true);

        while (true) {
		fire(100);

        }
    }

    public void onHitWall(HitWallEvent e) {
}

public void onScannedRobot(ScannedRobotEvent e) {

}
}
