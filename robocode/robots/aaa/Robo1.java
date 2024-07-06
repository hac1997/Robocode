package aaa;
import robocode.*;


public class Robo1 extends Robot


	public void run() {

		while(true) {

			ahead(100);
			turnGunRight(360);
			back(100);
			turnGunRight(360);
		}
	}


	public void onScannedRobot(ScannedRobotEvent e) {

		fire(1);
	}

	public void onHitByBullet(HitByBulletEvent e) {


		back(10);
	}

	public void onHitWall(HitWallEvent e) {

		back(20);
	}	
}
