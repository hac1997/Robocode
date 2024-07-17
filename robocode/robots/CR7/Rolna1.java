package CR7;

import robocode.*;
import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Rolna1 extends AdvancedRobot {

    boolean movingForward;
    String trackName;
    int count = 0;
    double gunTurnAmt;
    int direction = 1;  // 1 for forward, -1 for backward

    public void run() {
        // Definem as cores do robô
        setColors(Color.white, Color.white, Color.yellow);
        setBodyColor(Color.black);
        setGunColor(Color.black);
        setRadarColor(Color.red);
        setScanColor(Color.red);
        
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);

        while (true) {
            turnRadarRight(360);  // Scans continuously
            if (movingForward) {
                setAhead(100 * direction);  // Move ahead 100 pixels
            } else {
                setBack(100 * direction);  // Move back 100 pixels
            }
            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        // Se já temos um alvo e este não é ele, retorne imediatamente
        if (trackName != null && !e.getName().equals(trackName)) {
            return;
        }

        // Se não temos um alvo, agora temos!
        if (trackName == null) {
            trackName = e.getName();
            out.println("Tracking " + trackName);
        }

        // Este é nosso alvo. Redefinir contagem (veja o método run)
        count = 0;

        // Ajusta o canhão para mirar no inimigo
        double gunTurn = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
        turnGunRight(gunTurn);

        // Calcula a potência do tiro baseado na distância
        double firePower = Math.min(400 / e.getDistance(), 3);
        fire(firePower);

        // Evita tiros
        if (e.getDistance() < 100) {
            if (e.getBearing() > -90 && e.getBearing() <= 90) {
                setBack(40 * direction);
            } else {
                setAhead(40 * direction);
            }
        }

        // Ajuste da direção para movimentação evasiva
        direction *= -1;
        setTurnRight(45 * direction);
        setAhead(100 * direction);

        scan();  // Continua a varredura
    }

    // quando o seu robo colide com outro robo
    public void onHitRobot(HitRobotEvent e) {
        // Tira com maior potência quando colide
        if (e.getBearing() > -90 && e.getBearing() <= 90) {
            setBack(50);
        } else {
            setAhead(50);
        }
        fire(3);
    }

    // Quando seu robô leva um tiro
    public void onHitByBullet(HitByBulletEvent e) {
        // Move-se para evitar mais tiros
        turnRight(normalRelativeAngleDegrees(90 - (getHeading() - e.getHeading())));
        setAhead(150);
    }

    // É chamado quando o robô bate na parede
    public void onHitWall(HitWallEvent e) {
        // Inverte a direção ao bater na parede
        setBack(50);
        direction *= -1;
    }

    // Dança da vitória
    public void onWin(WinEvent e) {
        // Dança da vitória girando
        for (int i = 0; i < 50; i++) {
            turnRight(360);
        }
    }
}
