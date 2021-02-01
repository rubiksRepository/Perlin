import java.awt.Color;

import javax.swing.JLabel;

public class Coordinate {
	
	private int x, z;
	private int alt;
	public static final int MAP_SIZE = 199;
	private JLabel ter = new JLabel("");
	private JLabel str = new JLabel("");
	
	public JLabel getTer() {
		return ter;
	}

	public void setTer(JLabel ter) {
		this.ter = ter;
	}

	public JLabel getStr() {
		return str;
	}

	public void setStr(JLabel str) {
		this.str = str;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getAlt() {
		return alt;
	}

	public void setAlt(int alt) {
		this.alt = alt;
	}

	public Coordinate(int x0, int z0) {
		this.x = x0;
		this.z = z0;
		
		int a = (int) Noise.mapTo(-1 * Math.sqrt(0.5), Math.sqrt(0.5), 0, 100, Noise.noise(x*0.1, z*0.1));
		int b = (int) Noise.mapTo(-1 * Math.sqrt(0.5), Math.sqrt(0.5), 0, 100, Noise.noise(x*0.05, z*0.05));
		int c = (int) Noise.mapTo(-1 * Math.sqrt(0.5), Math.sqrt(0.5), 0, 100, Noise.noise(x*0.01, z*0.01));
		this.alt = (int) ((0.55*c) + (0.25*b) + (0.20*a));
		
		str.setOpaque(false);		str.setBackground(new Color(0,0,0,0));
		str.setIcon(Main.nullstructure);
		
		if(alt >= 0 && alt < 50) {
			ter.setIcon(Main.water);
		}
		else if(alt >= 50 && alt < 52) {
			ter.setIcon(Main.brightenImage(Main.sand, (float) ((float) 0.95 + ((alt - 50) * 0.025)), (float) 0.36));
		}
		else if(alt >= 52 && alt < 70) {
			ter.setIcon(Main.brightenImage(Main.grass, (float) ((float) 0.85 + ((alt - 52) * 0.025)), (float) 0.36));
			if(Math.random() < 0.05) {
				str.setIcon(Main.oak);
			}
		}
		else if(alt >= 80 && alt < 90) {
			ter.setIcon(Main.brightenImage(Main.stone, (float) ((float) 0.65 + ((alt - 70) * 0.05)), (float) 0.36));
		}
		else {
			ter.setIcon(Main.ice);
		}
	}
}
