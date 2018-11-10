import java.awt.Color;
import java.awt.Point;

public class PointColor extends Point {

	/** Classe que extende a biblioteca <Point>, acrescentando atributos e metodos para gerenciamento de cor.
	 *  Necessario para registro de cores nos objetos de cada desenho.
	 */
	private static final long serialVersionUID = 1L;
	private Color corBorda;
	private Color corInterna;
	
	public PointColor(double x, double y) {
		super();
	}

	public Color getCorBorda() {
		return corBorda;
	}
	
	public void setCorBorda(Color corBorda) {
		this.corBorda = corBorda;
	}
	
	public Color getCorInterna() {
		return corInterna;
	}
	
	public void setCorInterna(Color corInterna) {
		this.corInterna = corInterna;
	}
	
}
