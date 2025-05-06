import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class snake extends JFrame {

	private class Tile {
		private int x;
		private int y;

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public Tile(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	private class GameFieldPanel extends JPanel implements ActionListener, KeyListener {
		int boardWidth;
		int boardHeight;
		Tile snakeHead;
		ArrayList<Tile> snakeBody;
		Tile food;
		Random random;
		Timer timer;
		int vx;
		int vy;
		boolean gameOver;

		public GameFieldPanel(int boardWidth, int boardHeight) {
			this.boardWidth = boardWidth;
			this.boardHeight = boardHeight;
			this.setPreferredSize(new Dimension(boardWidth, boardHeight));
			this.setBackground(Color.BLACK);
			this.addKeyListener(this);
			this.setFocusable(true);
			// requestFocus();

			snakeHead = new Tile(5, 5);
			snakeBody = new ArrayList<>();

			random = new Random();
			placeFood();

			vx = 0;
			vy = 0;
			timer = new Timer(200, this);
			timer.start();

		}

		private void placeFood() {
			int x = random.nextInt(boardWidth / tileSize);
			int y = random.nextInt(boardHeight / tileSize);
			food = new Tile(x, y);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			draw(g);
		}

		private void draw(Graphics g) {
			for (int i = 0; i < boardWidth / tileSize; i++) {
				g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
				g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
			}

			// Snake
			g.setColor(Color.GREEN);
			g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);
			for (int i = 0; i < snakeBody.size(); i++) {
				// snakeBody.get(i);
				g.fill3DRect(snakeBody.get(i).x * tileSize, 
						snakeBody.get(i).y * tileSize, 
						tileSize, tileSize, true);

			}

			// Food
			g.setColor(Color.RED);
			g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

			
			//Score
			String score = String.valueOf(snakeBody.size());
			g.setFont(new Font ("Monospaced",Font.PLAIN,16));
			if (gameOver) {
				g.setColor(Color.RED);
				g.drawString("ТЫ ЛОХ "+score, tileSize - 16, tileSize );
				
			}
			else {
				g.setColor(Color.GREEN);
				g.drawString("МОЛОДЕЦ "+score, tileSize - 16, tileSize );
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			move();
			repaint();
			if (gameOver) {
				timer.stop();
			}

		}

		private void move() {
			// eat food
			if (collision(snakeHead, food)) {
				snakeBody.add(new Tile(food.x, food.y));
				placeFood();
			}


			//snake Body
			for (int i = snakeBody.size() - 1; i >=0; i-- ) {
				Tile part = snakeBody.get(i);
				if (i==0) {
					part.setX (snakeHead.x);
					part.setY (snakeHead.y);
				}
				else {
					Tile prev = snakeBody.get(i-1);
					part.setX (prev.x);
					part.setY (prev.y);
				}



			}

			// snake Head
			snakeHead.setX(snakeHead.x + vx);
			snakeHead.setY(snakeHead.y + vy);
			
			//game over conditions
			for (int i=0; i < snakeBody.size(); i++){
				Tile part = snakeBody.get(i);
				if (collision(part,snakeHead)) {
					gameOver = true;
				}
			}
			
			if(snakeHead.x  < 0 || snakeHead.x * tileSize > boardWidth ||
					snakeHead.y  < 0 || snakeHead.y * tileSize > boardHeight) {
				gameOver = true;
				
			}
			
		}

		private boolean collision(Tile snakeHead2, Tile food2) {
			return snakeHead2.x == food2.x && snakeHead2.y == food2.y;
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_UP && vy != 1) {
				vx = 0;
				vy = -1;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN && vy != -1) {
				vx = 0;
				vy = 1;
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT && vx != 1) {
				vx = -1;
				vy = 0;
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT && vx != -1) {
				vx = 1;
				vy = 0;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}
	}

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	int boardWidth = 600;
	int boardHeight = boardWidth;
	int tileSize = 25;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					snake frame = new snake();
					frame.pack();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public snake() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(boardWidth, boardHeight);
		setLocationRelativeTo(null);
		setResizable(false);
		contentPane = new GameFieldPanel(boardWidth, boardHeight);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
	}

}
