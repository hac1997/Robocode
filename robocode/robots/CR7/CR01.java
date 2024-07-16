package CR7;

import robocode.*;
import java.awt.Color;

public class CR01 extends AdvancedRobot {
    
    boolean movingForward;

    public void run() {
        setColors(Color.red, Color.blue, Color.green); // define as cores do robô

        setAdjustGunForRobotTurn(true); // ajusta o canhão para girar independentemente do corpo

        while (true) {
            turnGunRight(360); // gira o canhão 360 graus
            movingForward = true;
            setAhead(40000); // move para frente por 40000 pixels
            execute(); // executa os comandos
            turnRight(90); // vira 90 graus para a direita
        }
    }

    public void onHitWall(HitWallEvent e) {
        // ao atingir a parede, faz um movimento de desvio
        turnRight(90);
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        // ajusta o canhão para mirar no inimigo
        turnGunRight(getHeading() - getGunHeading() + e.getBearing());
        
        // calcula a potência do tiro baseado na distância
        fire(Math.min(400 / e.getDistance(), 3));
        if (movingForward) {
            setBack(40); // recua 40 pixels
            movingForward = false;
        } else {
            setAhead(40); // move para frente 40 pixels
            movingForward = true;
        }
        execute(); // executa os comandos
    }
}
