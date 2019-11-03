/*
 * SNAKE
 * Very basic example of the snake game.
 * by Guillermo Sampallo - 2019
 * www.gsampallo.com
 */
package gsampallo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Random;


public class Snake extends JFrame {
    
    ImagenSnake imagenSnake;
    Point snake;
    //Point lastSnake;
    Point comida;
    ArrayList<Point> listaPosiciones = new ArrayList<Point>();

    int longitud = 2;

    int width = 640;
    int height = 480;

    int widthPoint = 10;
    int heightPoint = 10;

	String direccion = "RIGHT";
	long frequency = 50;

    boolean gameOver = false;

    public Snake() {
		setTitle("Snake");

        startGame();
        imagenSnake = new ImagenSnake();

        this.getContentPane().add(imagenSnake);

		setSize(width,height);
		
		this.addKeyListener(new Teclas());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFrame.setDefaultLookAndFeelDecorated(false);
		setUndecorated(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        setVisible(true);
        Momento momento = new Momento();
		Thread trid = new Thread(momento);
		trid.start();
    }

    public void startGame() {
		comida = new Point(200,100);	
        snake = new Point(320,240);
		listaPosiciones = new ArrayList<Point>();
        listaPosiciones.add(snake);

		longitud = listaPosiciones.size();        
    }

	public void generarComida() {
		Random rnd = new Random();
		
		comida.x = (rnd.nextInt(width)) + 5;
		if((comida.x % 5) > 0) {
			comida.x = comida.x - (comida.x % 5);
		}

		if(comida.x < 5) {
			comida.x = comida.x + 10;
		}
		if(comida.x > width) {
			comida.x = comida.x - 10;
		}

		comida.y = (rnd.nextInt(height)) + 5;
		if((comida.y % 5) > 0) {
			comida.y = comida.y - (comida.y % 5);
		}	

		if(comida.y > height) {
			comida.y = comida.y - 10;
		}
		if(comida.y < 0) {
			comida.y = comida.y + 10;
		}

	}

	public void actualizar() {

        listaPosiciones.add(0,new Point(snake.x,snake.y));
		listaPosiciones.remove(listaPosiciones.size()-1);

        for (int i=1;i<listaPosiciones.size();i++) {
            Point point = listaPosiciones.get(i);
            if(snake.x == point.x && snake.y  == point.y) {
                gameOver = true;
            }
        }

		if((snake.x > (comida.x-10) && snake.x < (comida.x+10)) && (snake.y > (comida.y-10) && snake.y < (comida.y+10))) {
            listaPosiciones.add(0,new Point(snake.x,snake.y));
			System.out.println(listaPosiciones.size());
			generarComida();
		}
        imagenSnake.repaint();

	}

	public static void main(String[] args) {
		Snake snake1 = new Snake();
	}

    public class ImagenSnake extends JPanel {
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if(gameOver) {
                g.setColor(new Color(0,0,0));
            } else {
                g.setColor(new Color(255,255,255));
            }
            g.fillRect(0,0, width, height);
            g.setColor(new Color(0,0,255));
    
            if(listaPosiciones.size() > 0) {
                for(int i=0;i<listaPosiciones.size();i++) {
                    Point p = (Point)listaPosiciones.get(i);
                    g.fillRect(p.x,p.y,widthPoint,heightPoint);
                }
            }
    
            g.setColor(new Color(255,0,0));
            g.fillRect(comida.x,comida.y,widthPoint,heightPoint);    
            
            if(gameOver) {
                g.setFont(new Font("TimesRoman", Font.BOLD, 40));
                g.drawString("GAME OVER", 300, 200);
                g.drawString("SCORE "+(listaPosiciones.size()-1), 300, 240);

                g.setFont(new Font("TimesRoman", Font.BOLD, 20));
                g.drawString("N to Start New Game", 100, 320);
                g.drawString("ESC to Exit", 100, 340);
            }

        }
    }

	public class Teclas extends java.awt.event.KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {

			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(0);
			} else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {

				if(direccion != "LEFT") {
                    direccion = "RIGHT";

				}
			} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				if(direccion != "RIGHT") {
                    direccion = "LEFT";
				}
			} else if(e.getKeyCode() == KeyEvent.VK_UP) {
				if(direccion != "DOWN") {
                    direccion = "UP";
				}
			} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
				if(direccion != "UP") {
                    direccion = "DOWN";
				}				
			
			} else if(e.getKeyCode() == KeyEvent.VK_N) {
                gameOver = false;
                startGame();				
			}
		}

	}

	public class Momento extends Thread {
		
		private long last = 0;
		
		public Momento() {
			
		}

		public void run() {
			while(true) {
				if((java.lang.System.currentTimeMillis() - last) > frequency) {
					if(!gameOver) {

                        if(direccion == "RIGHT") {
                            snake.x = snake.x + widthPoint;
                            if(snake.x > width) {
                                snake.x = 0;
                            }
                        } else if(direccion == "LEFT") {
                            snake.x = snake.x - widthPoint;
                            if(snake.x < 0) {
                                snake.x = width - widthPoint;
                            }                        
                        } else if(direccion == "UP") {
                            snake.y = snake.y - heightPoint;
                            if(snake.y < 0) {
                                snake.y = height;
                            }                        
                        } else if(direccion == "DOWN") {
                            snake.y = snake.y + heightPoint;
                            if(snake.y > height) {
                                snake.y = 0;
                            }                        
                        }
                    }
                    actualizar();
					
					last = java.lang.System.currentTimeMillis();
				}

			}
		}
	}

}