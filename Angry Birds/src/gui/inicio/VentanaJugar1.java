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
		LayerUI<MiPanel> layerUI = new BlurLayerUI();
		jLayer = new JLayer<MiPanel>(panelPrincipal, layerUI);
		panelAbajo = new JPanel();
		panelAbajo.setOpaque(false);
		panelPrincipal.add(panelAbajo, BorderLayout.SOUTH);

		panelAbajo.setLayout(new FlowLayout());

		iniciarSesion = new MiBoton(Color.WHITE, Color.WHITE.brighter(), 75, 75);
		iniciarSesion.setText("Iniciar Sesión");
		iniciarSesion.setFont(new Font(Font.SERIF, Font.BOLD, 35));
		iniciarSesion.setPreferredSize(new Dimension(150, 75));
		panelAbajo.add(iniciarSesion, BorderLayout.WEST);

		registro = new MiBoton(Color.WHITE, Color.WHITE.darker(), 75, 75);
		registro.setText("Registrarse");
		registro.setFont(new Font(Font.SERIF, Font.BOLD, 35));
		registro.setPreferredSize(new Dimension(150, 75));
		panelAbajo.add(registro, BorderLayout.EAST);
		add(panelPrincipal, "Center");

		iniciarSesion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cambiarFondo();
				new VentanaInicioSesion(VentanaJugar1.this).iniciar();
			}
		});

		registro.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cambiarFondo();
				new VentanaRegistroSesion(VentanaJugar1.this).iniciar();
			}
		});

		iniciarSesion.addKeyListener(escCerrar);
		registro.addKeyListener(escCerrar);
		this.setVisible(true);		
	}
	
	private void cambiarFondo() {
		remove(panelPrincipal);
		add(jLayer);
		setEnabled(false);
	}
	
	public JLayer<MiPanel> getJLayer() {
		return jLayer;
	}
}
