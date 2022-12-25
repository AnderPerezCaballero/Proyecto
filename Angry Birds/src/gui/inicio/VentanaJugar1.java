package gui.inicio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.plaf.LayerUI;

import gui.componentes.BlurLayerUI;
import gui.componentes.MiBoton;
import gui.componentes.MiPanel;
import gui.sesion.VentanaInicioSesion;
import gui.sesion.VentanaRegistroSesion;


@SuppressWarnings("serial")
public class VentanaJugar1 extends VentanaJugar {

	private static JLayer<MiPanel> jLayer;

	private JButton iniciarSesion;
	private JButton registro;

	public VentanaJugar1() {
		super();
		jLayer = new JLayer<MiPanel>(panelPrincipal);
		panelAbajo = new JPanel();
		panelAbajo.setOpaque(false);
		panelPrincipal.add(panelAbajo, BorderLayout.SOUTH);

		panelAbajo.setLayout(new FlowLayout());

		iniciarSesion = new MiBoton(Color.WHITE, Color.WHITE.darker(), 50, 50);
		iniciarSesion.setText("Iniciar Sesión");
		iniciarSesion.setFont(new Font(Font.SERIF, Font.BOLD, 35));
		iniciarSesion.setPreferredSize(new Dimension(300, 50));
		panelAbajo.add(iniciarSesion, BorderLayout.WEST);

		registro = new MiBoton(Color.WHITE, Color.WHITE.darker(), 50, 50);
		registro.setText("Registrarse");
		registro.setFont(new Font(Font.SERIF, Font.BOLD, 35));
		registro.setPreferredSize(new Dimension(300, 50));
		panelAbajo.add(registro, BorderLayout.EAST);
		add(jLayer, "Center");

		iniciarSesion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cambiarFondo(true);
				new VentanaInicioSesion(VentanaJugar1.this).iniciar();
			}
		});

		registro.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cambiarFondo(true);
				new VentanaRegistroSesion(VentanaJugar1.this).iniciar();
			}
		});

		iniciarSesion.addKeyListener(escCerrar);
		registro.addKeyListener(escCerrar);
		this.setVisible(true);		
	}

	/** Cambia el fondo de la ventana de borroso a no borroso y viceversa
	 * @param borroso true si el estado de la ventana es el default y se quiere aplicar el blur
	 */
	public void cambiarFondo(boolean borroso) {
		if(borroso) {
			jLayer.setUI(new BlurLayerUI());
			setEnabled(false);
		}else {
			jLayer.setUI(new LayerUI<>());
			setEnabled(true);
		}
	}

	public JLayer<MiPanel> getJLayer() {
		return jLayer;
	}
}
