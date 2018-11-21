
/**
 *   A classe que gera os desenhos e implementa as ferramentas de uso para desenhar figuras em geral.
 */

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class PainelDeDesenho extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private InterfaceGrafica aplicacao;
	private DialogMarker dialogMark;
	private ArrayList<Point> pontos, histPontos, ret; // Pontos de uma figura
	private ArrayList<ArrayList<Point>> pilha, histPilha; // Pilha de figuras da imagem
	private Point ultimoPonto, coordAtual;
	private int funcaoAtiva, mkDiam;
	private boolean mkMarker, mkLine, holdCtrl, holdAlt;

	public PainelDeDesenho(InterfaceGrafica p) {
		aplicacao = p;
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
		pontos = new ArrayList<Point>();
		pilha = new ArrayList<>();
		histPontos = new ArrayList<Point>();
		histPilha = new ArrayList<>();
		ultimoPonto = new Point(0, 0);
		funcaoAtiva = 0;
		mkDiam = 10;
		mkMarker = true;
		mkLine = false;
		ret = new ArrayList<Point>();
	}

	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create(); // nunca trabalhe diretamente sobre g, apenas sobre uma copia de g
		g2d.setColor(aplicacao.getCorAtual());

		// Desenha TODAS as figuras presentes na PILHA
		for (int j = 0; j < pilha.size(); j++) {
			// cria uma lista de pontos temporaria para desenhar
			ArrayList<Point> fixo = new ArrayList<Point>();
			fixo = pilha.get(j);
			for (int i = 1; i < fixo.size(); i++) {
				Point anterior = fixo.get(i - 1);
				Point atual = fixo.get(i);
				g2d.drawLine(anterior.x, anterior.y, atual.x, atual.y);
			}
			for (int i = 0; i < fixo.size(); i++) {
				Point p = fixo.get(i);
				if (mkMarker) {
					g2d.fillOval(p.x - 5, p.y - 5, mkDiam, mkDiam);
				}
			}
		}

		// Desenha os pontos da figura ATUAL
		for (int i = 1; i < pontos.size(); i++) {
			Point anterior = pontos.get(i - 1);
			Point atual = pontos.get(i);
			g2d.drawLine(anterior.x, anterior.y, atual.x, atual.y);
		}

		// Desenha o MARCADOR, se habilitado
		if (mkMarker) {
			for (int i = 0; i < pontos.size(); i++) {
				Point p = pontos.get(i);
				if (mkMarker) {
					g2d.fillOval(p.x - (mkDiam/2), p.y - (mkDiam/2), mkDiam, mkDiam);
				}
			}
		}

		// Desenha uma LINHA do ultimo ponto ate o cursor, se habilitado
		if (mkLine) {
			if (pontos.size() > 0) {
				// Desenhando uma linha tracejada, com a cor do desenho, com as extremidades
				// redondas
				float[] dash = { 20f };
				g2d.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dash, 0f));
				g2d.setColor(aplicacao.getCorAtual());
				g2d.drawLine(ultimoPonto.x, ultimoPonto.y, coordAtual.x, coordAtual.y);
			}
		}

		g2d.dispose(); // toda vez que voce usa "create" e necessario usar "dispose"
	}

	// Implementacao da interface MouseListener
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		requestFocusInWindow(); // Isso e necessario para que o KeyListener funcione adequadamente

		// FUNCOES POR MOUSE
		switch (funcaoAtiva) {
		case 0:
			// FUNCAO DE DESENHAR UM POLIGONO LIVRE

			if (e.getButton() == MouseEvent.BUTTON1) {
				// Botao 1
				Point p = e.getPoint();
				pontos.add(p);
				ultimoPonto = p;
				histPontos.clear();
			} // Outro botao do mouse apertado
			else {
				// fechar a figura
				pontos.add(pontos.get(0));
				ultimoPonto = pontos.get(pontos.size() - 1);
			}
			break;
		case 1:
			// FUNCAO DE DESENHAR UMA SEQUENCIA DE FREEMAN

			if (e.getButton() == MouseEvent.BUTTON1) {
				// Botao 1 como primeiro ponto
				if (pontos.isEmpty()) {
					Point p = e.getPoint();
					pontos.add(p);
					ultimoPonto = p;
					histPontos.clear();
				} else {
					// nao faz nada
				}
			} // Outro botao do mouse apertado
			else {
				// nao faz nada
			}
			break;
		case 2:
			if (e.getButton() == MouseEvent.BUTTON1) 
			{
				if(ret.isEmpty()) // primeiro ponto
				{
					Point p = e.getPoint();
					ret.add(p);
					pontos.add(p);
				}
				else
				{
					Point p = e.getPoint();
					Point P1 = new Point();
					P1.setLocation(ret.get(0).getX(), p.getY());
					Point P2 = new Point();
					P2.setLocation(p.getX(), ret.get(0).getY());
					ret.add(P2);
					ret.add(p);
					ret.add(P1);
					ret.add(ret.get(0));
					for(Point item : ret)
					{
						pontos.add(item);
					}
					ret.clear();
				}
			}
			break;
		case 3:
			if (e.getButton() == MouseEvent.BUTTON1) 
			{
				if(ret.isEmpty())
				{
					Point p = e.getPoint();
					ret.add(p);
					pontos.add(p);
				}
				else
				{
					Point p = e.getPoint();
					Point P1 = new Point();
					P1.setLocation(ret.get(0).getX(), p.getY());
					Point P2 = new Point();
					P2.setLocation(p.getX(), ret.get(0).getY());
					ret.add(p);
					ret.add(P1);
					ret.add(ret.get(0));
					for(Point item : ret)
					{
						pontos.add(item);
					}
					ret.clear();
				}
			}
			break;
		default:
			// FUNCAO DE DESENHAR UM POLIGONO LIVRE

			if (e.getButton() == MouseEvent.BUTTON1) {
				// Botao 1
				Point p = e.getPoint();
				pontos.add(p);
				ultimoPonto = p;
				histPontos.clear();
			} // Outro botao do mouse apertado
			else {
				// fechar a figura
				pontos.add(pontos.get(0));
				ultimoPonto = pontos.get(pontos.size() - 1);
			}
			break;
		}

		repaint();
		proxFigura();
	}

	// Armazena a figura na pilha e libera o atributo principal para a proxima
	// figura na pilha
	private void proxFigura() {
		if (!pontos.isEmpty()) {
			if (pontos.get(0).getX() == pontos.get(pontos.size() - 1).getX()
					&& pontos.get(0).getY() == pontos.get(pontos.size() - 1).getY() && pontos.size() > 1) {
				pilha.add(pontos);
				pontos = new ArrayList<Point>();
				histPilha.clear();
			}
		}
	}

	public void undo() {
		// FUNCAO DE UNDO
		// System.out.println("UNDO");
		ret.clear();
		if (!pontos.isEmpty()) {
			histPontos.add(pontos.get(pontos.size() - 1));
			pontos.remove(pontos.size() - 1);
		} else {
			if (!pilha.isEmpty()) {
				histPilha.add(pilha.get(pilha.size() - 1));
				pilha.remove(pilha.size() - 1);
			}
		}
	}

	public void redo() {
		// FUNCAO DE REDO
		// System.out.println("REDO");
		if (!histPontos.isEmpty()) {
			pontos.add(histPontos.get(histPontos.size() - 1));
			histPontos.remove(histPontos.size() - 1);
		} else {
			if (!histPilha.isEmpty()) {
				pilha.add(histPilha.get(histPilha.size() - 1));
				histPontos.remove(histPontos.size() - 1);
			}
		}
	}

	public void alteraMarcador() {
		// ALTERA O MARCADOR DOS PONTOS
		String titulo = "Definir Marcador";
		String message = "Configure se o marcador deve aparecer e seu diametro:";
		dialogMark = new DialogMarker(this, new JFrame(), titulo, message, mkDiam, mkMarker);
		dialogMark.setSize(300, 110);
		//System.out.println("Diametro > " + mkDiam + " / Habilitado? " + mkMarker);
	}

	public void mouseMoved(MouseEvent e) {
		coordAtual = e.getPoint();
		repaint();
	}

	// Implementacao da interface KeyListener
	/**
	 * Quando a tecla ESC for pressionada, limpo a figura sendo desenhada no momento
	 *
	 * @param e
	 *            objeto relativo ao evento de teclas
	 */
	public void keyPressed(KeyEvent e) {
		// EVENTOS DE TECLADO
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			// Tecla ESC
			pontos.clear();
		}
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			holdCtrl = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_ALT) {
			holdAlt = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_Z) {
			// Tecla Ctrl+Z - Cancela o último passo
			undo();
			holdCtrl = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_Y && holdCtrl) {
			// Tecla Ctrl+Y - Refaz o último passo
			redo();
			holdCtrl = false;
		}

		// FUNCOES POR TECLADO
		switch (funcaoAtiva) {
		case 0:
			// FUNCAO DE DESENHAR UM POLIGONO LIVRE
			// NAO FAZ NADA
			break;
		case 1:
			// FUNCAO DE DESENHAR UMA SEQUENCIA DE FREEMAN

			if (e.getKeyCode() == KeyEvent.VK_0) {
				// Tecla 0
				Point p = new Point(ultimoPonto.x + 50, ultimoPonto.y);
				pontos.add(p);
				histPontos.clear();
				ultimoPonto = p;
			}
			if (e.getKeyCode() == KeyEvent.VK_1) {
				// Tecla 1
				Point p = new Point(ultimoPonto.x + 35, ultimoPonto.y - 35);
				pontos.add(p);
				histPontos.clear();
				ultimoPonto = p;
			}
			if (e.getKeyCode() == KeyEvent.VK_2) {
				// Tecla 2
				Point p = new Point(ultimoPonto.x, ultimoPonto.y - 50);
				pontos.add(p);
				histPontos.clear();
				ultimoPonto = p;
			}
			if (e.getKeyCode() == KeyEvent.VK_3) {
				// Tecla 3
				Point p = new Point(ultimoPonto.x - 35, ultimoPonto.y - 35);
				pontos.add(p);
				histPontos.clear();
				ultimoPonto = p;
			}
			if (e.getKeyCode() == KeyEvent.VK_4) {
				// Tecla 4
				Point p = new Point(ultimoPonto.x - 50, ultimoPonto.y);
				pontos.add(p);
				histPontos.clear();
				ultimoPonto = p;
			}
			if (e.getKeyCode() == KeyEvent.VK_5) {
				// Tecla 5
				Point p = new Point(ultimoPonto.x - 35, ultimoPonto.y + 35);
				pontos.add(p);
				histPontos.clear();
				ultimoPonto = p;
			}
			if (e.getKeyCode() == KeyEvent.VK_6) {
				// Tecla 6
				Point p = new Point(ultimoPonto.x, ultimoPonto.y + 50);
				pontos.add(p);
				histPontos.clear();
				ultimoPonto = p;
			}
			if (e.getKeyCode() == KeyEvent.VK_7) {
				// Tecla 7
				Point p = new Point(ultimoPonto.x + 35, ultimoPonto.y + 35);
				pontos.add(p);
				histPontos.clear();
				ultimoPonto = p;
			}
			break;
		case 2:
			// FUNCAO DE DESENHAR UM RETANGULO
			// NAO FAZ NADA
			break;
		case 3:
			// FUNCAO DE DESENHAR UM TRIANGULO
			// NAO FAZ NADA
			break;
		default:
			// FUNCAO DE DESENHAR UM POLIGONO LIVRE
			// NAO FAZ NADA
			break;
		}

		repaint();
		proxFigura();
	}

	public void keyReleased(KeyEvent e) { }

	public void keyTyped(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			holdCtrl = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_ALT) {
			holdAlt = true;
		}
	}

	// Metodo de acesso e modificador para a colecao de pontos
	public ArrayList<Point> getPontos() {
		return pontos;
	}

	public void setPontos(ArrayList<Point> pontos) {
		this.pontos = pontos;
		if (!pontos.isEmpty()) {
			ultimoPonto = pontos.get(pontos.size() - 1);
		}
	}

	// Metodo de acesso e modificador para a PILHA
	public ArrayList<ArrayList<Point>> getPilha() {
		return pilha;
	}

	public void setPilha(ArrayList<ArrayList<Point>> pilha) {
		this.pilha = pilha;
	}

	public void setFuncao(int funcao) {
		funcaoAtiva = funcao;
	}
	
	public int getMkDiam() {
		return mkDiam;
	}

	public void setMkDiam(int diametro) {
		this.mkDiam = diametro;
	}

	public boolean isHabilitado() {
		return mkMarker;
	}

	public void setHabilitado(boolean habilitado) {
		this.mkMarker = habilitado;
	}

}
