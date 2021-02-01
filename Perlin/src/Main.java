import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {

	private JFrame frame;
	public static ImageIcon water = new ImageIcon(new ImageIcon(Main.class.getResource("/textures/terrain/water.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	public static ImageIcon sand = new ImageIcon(new ImageIcon(Main.class.getResource("/textures/terrain/sand.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	public static ImageIcon grass = new ImageIcon(new ImageIcon(Main.class.getResource("/textures/terrain/grass.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	public static ImageIcon stone = new ImageIcon(new ImageIcon(Main.class.getResource("/textures/terrain/stone.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	public static ImageIcon ice = new ImageIcon(new ImageIcon(Main.class.getResource("/textures/terrain/ice.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	public static ImageIcon oak = new ImageIcon(new ImageIcon(Main.class.getResource("/textures/structure/oak.png")).getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT));
	public static ImageIcon nullstructure = new ImageIcon(new ImageIcon(Main.class.getResource("/textures/structure/nullstructure.png")).getImage().getScaledInstance(32, 32,  Image.SCALE_DEFAULT));
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws InterruptedException 
	 */
	public Main() throws InterruptedException {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws InterruptedException 
	 */
	private void initialize() throws InterruptedException {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(0, 0, 1280, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLayeredPane layeredPane = new JLayeredPane();
		
		layeredPane.setBounds(0, 0, 1280, 720);
		frame.getContentPane().add(layeredPane);
		layeredPane.setLayout(null);
		
		JPanel terrainGrid = new JPanel();
		terrainGrid.setBounds(0, 0, 1280, 720);
		layeredPane.add(terrainGrid);
		GridBagLayout gbl_terrainGrid = new GridBagLayout();
		gbl_terrainGrid.columnWidths = new int[]{0};
		gbl_terrainGrid.rowHeights = new int[]{0};
		gbl_terrainGrid.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_terrainGrid.rowWeights = new double[]{Double.MIN_VALUE};
		terrainGrid.setLayout(gbl_terrainGrid);
		
		JPanel structureGrid = new JPanel();
		layeredPane.setLayer(structureGrid, Integer.valueOf(1));
		structureGrid.setBounds(0, 0, 1280, 720);
		structureGrid.setBackground(new Color(0,0,0,0));
		structureGrid.setOpaque(false);
		layeredPane.add(structureGrid);
		GridBagLayout gbl_structureGrid = new GridBagLayout();
		gbl_structureGrid.columnWidths = new int[]{0};
		gbl_structureGrid.rowHeights = new int[]{0};
		gbl_structureGrid.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_structureGrid.rowWeights = new double[]{Double.MIN_VALUE};
		structureGrid.setLayout(gbl_structureGrid);
				
		Coordinate[][] map = new Coordinate[Coordinate.MAP_SIZE][Coordinate.MAP_SIZE];
		for(int i = 0; i < Coordinate.MAP_SIZE; i++) {
			for(int j = 0; j < Coordinate.MAP_SIZE; j++) {
				map[i][j] = new Coordinate(i - ((Coordinate.MAP_SIZE - 1)/2) , j - ((Coordinate.MAP_SIZE - 1)/2));
			}
		}
		
		Coordinate playerPos = new Coordinate(0,0);
		JLabel[][] terrainArray = new JLabel[41][23];
		JLabel[][] structureArray = new JLabel[41][23];
		
		for(int i = 0; i < 41; i++) {
			for(int j = 0; j < 23; j++) {
				terrainArray[i][j] = new JLabel("");
				structureArray[i][j] = new JLabel("");
				structureArray[i][j].setOpaque(false);
				structureArray[i][j].setBackground(new Color(0,0,0,0));
				structureArray[i][j].setIcon(Main.nullstructure);
				terrainArray[i][j].setIcon(findEntry(map, new Coordinate(playerPos.getX() - 20 + i, playerPos.getZ() - 11 + j)).getTer().getIcon());
				structureArray[i][j].setIcon(findEntry(map, new Coordinate(playerPos.getX() - 20 + i, playerPos.getZ() - 11 + j)).getStr().getIcon());
				
				GridBagConstraints gbc = new GridBagConstraints();
					gbc.gridx = i; gbc.gridy = j;
					terrainGrid.add(terrainArray[i][j], gbc);
					structureGrid.add(structureArray[i][j],gbc);
			}
		}
		
		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_UP:
					playerPos.setZ(playerPos.getZ() - 1);
					update(terrainArray, structureArray, map, playerPos);
					break;
				case KeyEvent.VK_DOWN:
					playerPos.setZ(playerPos.getZ() - 1);
					update(terrainArray, structureArray, map, playerPos);
					break;
				case KeyEvent.VK_RIGHT:
					playerPos.setX(playerPos.getX() + 1);
					update(terrainArray, structureArray, map, playerPos);
					break;
				case KeyEvent.VK_LEFT:
					playerPos.setX(playerPos.getX() + 1);
					update(terrainArray, structureArray, map, playerPos);
					break;
				}
				
			}
		});
	}

	public static ImageIcon brightenImage(ImageIcon input, float brightness, float offset) {
		BufferedImage bI = new BufferedImage(input.getImage().getWidth(null), 		input.getImage().getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D bIgr = bI.createGraphics();
		bIgr.drawImage(input.getImage(), 0, 0, null);
		bIgr.dispose();
		BufferedImage bO = new BufferedImage(input.getImage().getWidth(null), input.getImage().getHeight(null), BufferedImage.TYPE_INT_RGB);
		
		RescaleOp rop = new RescaleOp(brightness, offset, null);
		
		bO = rop.filter(bI, null);
		ImageIcon output = new ImageIcon(bO);
		return output;
	}

	
	public void update(JLabel[][] terrainArray, JLabel[][] structureArray, Coordinate[][] map, Coordinate playerPos) {
		for(int i = 0; i < 41; i++) {
			for(int j = 0; j < 23; j++) {
//				terrainArray[i][j] = findEntry(map, new Coordinate(playerPos.getX() - 20 + i, playerPos.getZ() - 11 + j)).getTer();
//				structureArray[i][j] = findEntry(map, new Coordinate(playerPos.getX() - 20 + i, playerPos.getZ() - 11 + j)).getStr();
//				terrainArray[i][j].setIcon(findEntry(map, new Coordinate(playerPos.getX() - 20 + i, playerPos.getZ() - 11 + j)).getTer().getIcon());
//				structureArray[i][j].setIcon(findEntry(map, new Coordinate(playerPos.getX() - 20 + i, playerPos.getZ() - 11 + j)).getStr().getIcon());
				terrainArray[i][j].setIcon(findEntry(map, new Coordinate(playerPos.getX() - 20 + i, playerPos.getZ() - 11 + j)).getTer().getIcon());
				structureArray[i][j].setIcon(findEntry(map, new Coordinate(playerPos.getX() - 20 + i, playerPos.getZ() - 11 + j)).getStr().getIcon());
			}
		}
	}
	
	public Coordinate findEntry(Coordinate[][] map, Coordinate pos) {
		Coordinate entry = null;
		for(int x = 0; x < Coordinate.MAP_SIZE; x++) {
			for(int z = 0; z < Coordinate.MAP_SIZE; z++) {
				if(pos.getX() == map[x][z].getX() && pos.getZ() == map[x][z].getZ()) {
					entry = map[x][z];
				}
			}
		}
		return entry;
	}
	
}
