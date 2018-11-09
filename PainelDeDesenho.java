
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
    private ArrayList<Point> pontos; // Pontos de uma figura
    private ArrayList<ArrayList<Point>> pilha; // Pilha de figuras da imagem
    private Point ultimoPonto;
    protected int funcaoAtiva = 0;

    public PainelDeDesenho(InterfaceGrafica p) {
        aplicacao = p;
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        pontos = new ArrayList<Point>();
        pilha = new ArrayList<>();
        ultimoPonto = new Point(0, 0);
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
                g2d.fillOval(p.x - 5, p.y - 5, 10, 10);
            }
        }

        // Desenha os pontos da figura ATUAL
        for (int i = 1; i < pontos.size(); i++) {
            Point anterior = pontos.get(i - 1);
            Point atual = pontos.get(i);
            g2d.drawLine(anterior.x, anterior.y, atual.x, atual.y);
        }
        for (int i = 0; i < pontos.size(); i++) {
            Point p = pontos.get(i);
            g2d.fillOval(p.x - 5, p.y - 5, 10, 10);
        }

        // DESABILITADO
        /* //Desenha uma linha do ultimo ponto ate o cursor
		if(pontos.size()>0) {
			// Desenhando uma linha grossa e tracejada, verde, com as extremidades redondas
			float[] dash = {20f};
        	g2d.setStroke(new BasicStroke (10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dash, 0f));
        	g2d.setColor(Color.GREEN);
        	g2d.drawLine(ultimoPonto.x, ultimoPonto.y, coordAtual.x, coordAtual.y);
		} */
        g2d.dispose(); //toda vez que voce usa "create" e necessario usar "dispose"
    }

    //Implementacao da interface MouseListener
    public void mouseClicked(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseDragged(MouseEvent e) { }

    public void mousePressed(MouseEvent e) {
        requestFocusInWindow(); //Isso e necessario para que o KeyListener funcione adequadamente
        
        // FUNCOES POR MOUSE
        switch (funcaoAtiva) {
        	case 0:
        		// FUNCAO DE DESENHAR UM POLIGONO LIVRE
        		
        		if (e.getButton() == MouseEvent.BUTTON1) {
                    //Botao 1
                    Point p = e.getPoint();
                    pontos.add(p);
                    ultimoPonto = p;
                } //Outro botao do mouse apertado 
                else {
                    // fechar a figura
                    pontos.add(pontos.get(0));
                    ultimoPonto = pontos.get(pontos.size() - 1);
                }
        		break;
        	case 1:
        		// FUNCAO DE DESENHAR UMA SEQUENCIA DE FREEMAN
        		
        		if (e.getButton() == MouseEvent.BUTTON1) {
                    //Botao 1 como primeiro ponto
                    if (pontos.isEmpty()) {
                    	Point p = e.getPoint();
                        pontos.add(p);
                        ultimoPonto = p;
                    } else {
                    	// nao faz nada
                    }
                } //Outro botao do mouse apertado 
                else {
                    // nao faz nada
                }
        		break;
        	case 2:
        		// TODO: FUNCAO DE DESENHAR UM RETANGULO
        		
        		break;
        	case 3:
        		// TODO: FUNCAO DE DESENHAR UM TRIANGULO
        		
        		break;
        	default:
        		// FUNCAO DE DESENHAR UM POLIGONO LIVRE
        		
        		if (e.getButton() == MouseEvent.BUTTON1) {
                    //Botao 1
                    Point p = e.getPoint();
                    pontos.add(p);
                    ultimoPonto = p;
                } //Outro botao do mouse apertado 
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

    // Armazena a figura na pilha e libera o atributo principal para a proxima figura na pilha
    private void proxFigura() {
        if (pontos.get(0).getX() == pontos.get(pontos.size() - 1).getX()
                && pontos.get(0).getY() == pontos.get(pontos.size() - 1).getY()
                && pontos.size() > 1) {
            pilha.add(pontos);
            pontos = new ArrayList<Point>();
        }
    }
    
    public void undo() {
    	// TODO: FUNCAO DE UNDO
    }
    
    public void redo() {
    	// TODO: FUNCAO DE REDO
    }
    
    public void alteraMarcador() {
    	// TODO: ALTERA O MARCADOR DOS PONTOS
    }

    public void mouseMoved(MouseEvent e) {
        // DESATIVADO
        //coordAtual = e.getPoint();
        //repaint();
    }

    //Implementacao da interface KeyListener
    /**
     * Quando a tecla ESC for pressionada, limpo a figura sendo desenhada no momento
     *
     * @param e objeto relativo ao evento de teclas
     */
    public void keyPressed(KeyEvent e) {
        // Tecla Apertada
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            //Tecla ESC
            pontos.clear();
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
                    //Tecla 0
                    Point p = new Point(ultimoPonto.x + 50, ultimoPonto.y);
                    pontos.add(p);
                    ultimoPonto = p;
                }
                if (e.getKeyCode() == KeyEvent.VK_1) {
                    //Tecla 1
                    Point p = new Point(ultimoPonto.x + 35, ultimoPonto.y - 35);
                    pontos.add(p);
                    ultimoPonto = p;
                }
                if (e.getKeyCode() == KeyEvent.VK_2) {
                    //Tecla 2
                    Point p = new Point(ultimoPonto.x, ultimoPonto.y - 50);
                    pontos.add(p);
                    ultimoPonto = p;
                }
                if (e.getKeyCode() == KeyEvent.VK_3) {
                    //Tecla 3
                    Point p = new Point(ultimoPonto.x - 35, ultimoPonto.y - 35);
                    pontos.add(p);
                    ultimoPonto = p;
                }
                if (e.getKeyCode() == KeyEvent.VK_4) {
                    //Tecla 4
                    Point p = new Point(ultimoPonto.x - 50, ultimoPonto.y);
                    pontos.add(p);
                    ultimoPonto = p;
                }
                if (e.getKeyCode() == KeyEvent.VK_5) {
                    //Tecla 5
                    Point p = new Point(ultimoPonto.x - 35, ultimoPonto.y + 35);
                    pontos.add(p);
                    ultimoPonto = p;
                }
                if (e.getKeyCode() == KeyEvent.VK_6) {
                    //Tecla 6
                    Point p = new Point(ultimoPonto.x, ultimoPonto.y + 50);
                    pontos.add(p);
                    ultimoPonto = p;
                }
                if (e.getKeyCode() == KeyEvent.VK_7) {
                    //Tecla 7
                    Point p = new Point(ultimoPonto.x + 35, ultimoPonto.y + 35);
                    pontos.add(p);
                    ultimoPonto = p;
                }
        		break;
        	case 2:
        		// TODO: FUNCAO DE DESENHAR UM RETANGULO
        		
        		break;
        	case 3:
        		// TODO: FUNCAO DE DESENHAR UM TRIANGULO
        		
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

    public void keyTyped(KeyEvent e) { }

    //Metodo de acesso e modificador para a colecao de pontos
    public ArrayList<Point> getPontos() {
        return pontos;
    }

    public void setPontos(ArrayList<Point> pontos) {
        this.pontos = pontos;
        ultimoPonto = pontos.get(pontos.size() - 1);
    }

    //Metodo de acesso e modificador para a PILHA
    public ArrayList<ArrayList<Point>> getPilha() {
        return pilha;
    }

    public void setPilha(ArrayList<ArrayList<Point>> pilha) {
        this.pilha = pilha;
    }

}
