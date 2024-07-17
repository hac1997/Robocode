package CR7;

import robocode.*;
import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class CR03 extends AdvancedRobot {

    boolean movingForward;
    String trackName;
    int count = 0;
    double gunTurnAmt;

    public void run() {
        // Definem as cores do robô
        setColors(Color.white, Color.white, Color.yellow);
        setBodyColor(Color.black);
        setGunColor(Color.black);
        setRadarColor(Color.red);
        setScanColor(Color.red);
        
        setAdjustGunForRobotTurn(true);

        while (true) {
            turnRadarRight(360);
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360); // gira o canhão 360 graus
            movingForward = true;
            setAhead(40000); // move para frente por 40000 pixels
            execute(); // executa os comandos
            turnRight(90); // vira 90 graus para a direita
        }
    }

    // Detecta os outros robôs
    public void onScannedRobot(ScannedRobotEvent e) {
        double max = 100;

        // Faz um controle da energia que é gasta no que diz respeito à potência do tiro
        if (e.getEnergy() < max) {
            max = e.getEnergy();
            miraCanhao(e.getBearing(), max, getEnergy());
        } else if (e.getEnergy() >= max) {
            max = e.getEnergy();
            miraCanhao(e.getBearing(), max, getEnergy());
        } else if (getOthers() == 1) {
            max = e.getEnergy();
            miraCanhao(e.getBearing(), max, getEnergy());
        }

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

        // Se nosso alvo está muito longe, vire e mova-se em direção a ele
        if (e.getDistance() > 150) {
            gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
            turnGunRight(gunTurnAmt);
            turnRight(e.getBearing());
            ahead(e.getDistance() - 140);
            return;
        }

        // Nosso alvo está perto
        gunTurnAmt = normalRelativeAngleDegrees(e.getBearing() + (getHeading() - getRadarHeading()));
        turnGunRight(gunTurnAmt);
        fire(3);

        // Nosso alvo está muito perto! Recuar.
        if (e.getDistance() < 100) {
            if (e.getBearing() > -90 && e.getBearing() <= 90) {
                back(40);
            } else {
                ahead(40);
            }
        }
        scan();
    }

    // quando o seu robo colide com outro robo
    public void onHitRobot(HitRobotEvent e) {
        tiroFatal(e.getBearing(), e.getEnergy(), getEnergy());
    }

    // Quando seu robô leva um tiro
    public void onHitByBullet(HitByBulletEvent e) {
        turnLeft(90);
        back(100);
    }

    // Fornece as coordenadas para o ajuste do canhão
    public void miraCanhao(double PosIni, double energiaIni, double minhaEnergia) {
        double Distancia = PosIni;
        double Coordenadas = getHeading() + PosIni - getGunHeading();
        double PontoQuarenta = (energiaIni / 4) + .1;

        if (!(Coordenadas > -180 && Coordenadas <= 180)) {
            while (Coordenadas <= -180) {
                Coordenadas += 360;
            }
            while (Coordenadas > 180) {
                Coordenadas -= 360;
            }
        }

        turnGunRight(Coordenadas);

        if (Distancia > 200 || minhaEnergia < 15 || energiaIni > minhaEnergia) {
            fire(1);
        } else if (Distancia > 50) {
            fire(2);
        } else {
            fire(PontoQuarenta);
        }
    }

    // É chamado quando o robô bate na parede
    public void onHitWall(HitWallEvent e) {
        turnLeft(90);
        ahead(200);
        turnRight(90);
    }

    // Dança da vitória
    public void onWin(WinEvent e) {
        turnRight(72000);
    }

    public void tiroFatal(double PosIni, double energiaIni, double minhaEnergia) {
        double Distancia = PosIni;
        double Coordenadas = getHeading() + PosIni - getGunHeading();
        double PontoQuarenta = (energiaIni / 4) + .1;

        if (!(Coordenadas > -180 && Coordenadas <= 180)) {
            while (Coordenadas <= -180) {
                Coordenadas += 360;
            }
            while (Coordenadas > 180) {
                Coordenadas -= 360;
            }
        }

        turnGunRight(Coordenadas);
        fire(PontoQuarenta);
    }
}
