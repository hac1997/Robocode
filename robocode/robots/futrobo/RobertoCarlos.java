package futrobo;

import robocode.*;
import java.awt.*;

public class RobertoCarlos extends AdvancedRobot {

    boolean movingForward = true;
    int direction = 1; // 1 for forward, -1 for backward

    public void run() {
        // Definem as cores do robô
        setColors(Color.black, Color.black, Color.red);
        setScanColor(Color.red);

        // Ajustes do robô
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);

        // Início do loop principal do robô
        while (true) {
            turnRadarRight(360); // Scans continuously
            setAhead(100 * direction);
            execute();
        }
    }

    // Detecta os outros robôs
    public void onScannedRobot(ScannedRobotEvent e) {
        double max = 100;

        // Controle da energia gasta no tiro
        if (e.getEnergy() < max || getOthers() == 1) {
            max = e.getEnergy();
            miraCanhao(e.getBearing(), max, getEnergy());
        } else {
            max = e.getEnergy();
            miraCanhao(e.getBearing(), max, getEnergy());
        }
    }

    // Quando o seu robô colide com outro robô
    public void onHitRobot(HitRobotEvent e) {
        tiroFatal(e.getBearing(), e.getEnergy(), getEnergy());
        // Ajusta a movimentação para evitar mais colisões
        if (e.getBearing() > -90 && e.getBearing() <= 90) {
            setBack(50);
        } else {
            setAhead(50);
        }
    }

    // Quando seu robô leva um tiro
    public void onHitByBullet(HitByBulletEvent e) {
  
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
        setBack(50);
        direction *= -1;
        setAhead(100 * direction);
    }

    // Dança da vitória
    public void onWin(WinEvent e) {
        for (int i = 0; i < 50; i++) {
            turnRight(360);
        }
    }

    // Tiro Fatal
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
