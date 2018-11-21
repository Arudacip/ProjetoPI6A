import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class DialogMarker extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel lHabilitar, lDiametro;
	private JButton bCancelar, bDefinir;
	private JRadioButton rHabilitar;
	private JTextField tDiametro;
	private int vDiametro;
	private boolean habilitado;
	private PainelDeDesenho painel;

	public DialogMarker(PainelDeDesenho painel, JFrame parent, String title, String message, int diametro, boolean habilitar) {
		super(parent, title);
		this.painel = painel;
		// Define a posicao do Dialog
		Point p = new Point(400, 400);
		setLocation(p.x, p.y);
		vDiametro = diametro;
		habilitado = habilitar;
		
		// Cria a janela
		JPanel messagePane = new JPanel();
		messagePane.add(new JLabel(message));
		getContentPane().add(messagePane);

		// Cria as opcoes de configuracao
		JPanel configPane = new JPanel(new GridLayout(1, 4));
		lHabilitar = new JLabel("Habilitado:");
		lDiametro = new JLabel("Diametro:");
		rHabilitar = new JRadioButton();
		rHabilitar.addActionListener(this);
		rHabilitar.setSelected(habilitar);
		tDiametro = new JTextField(10);
		tDiametro.setText("" + diametro);
		configPane.add(lHabilitar);
		configPane.add(rHabilitar);
		configPane.add(lDiametro);
		configPane.add(tDiametro);

		// Cria os botoes
		JPanel buttonPane = new JPanel();
		bDefinir = new JButton("Definir");
		buttonPane.add(bDefinir);
		bCancelar = new JButton("Cancelar");
		buttonPane.add(bCancelar);
		bDefinir.addActionListener(this);
		bCancelar.addActionListener(this);

		getContentPane().add(configPane, BorderLayout.CENTER);
		getContentPane().add(buttonPane, BorderLayout.PAGE_END);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Trata a interacao do usuario
		String sDiametro = tDiametro.getText();

		if (e.getSource() == bDefinir) {
			// Habilitado pelo usuario
			vDiametro = Integer.parseInt(sDiametro);
			painel.setMkDiam(vDiametro);
			
			//System.out.println("Diametro definido como >" + vDiametro);
			if (rHabilitar.isSelected()) {
				habilitado = true;
				painel.setHabilitado(habilitado);
				//System.out.println("Marcador habilitado");
			} else {
				habilitado = false;
				painel.setHabilitado(habilitado);
				//System.out.println("Marcador desabilitado");
			}
			clearAndHide();

		} else if (e.getSource() == bCancelar) {
			clearAndHide();
		}
	}

	/** This method clears the dialog and hides it. */
	public void clearAndHide() {
		setVisible(false);
	}

}
