package gui.juego;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import gestionUsuarios.GestionUsuarios;
import gestionUsuarios.Puntuacion;
import gestionUsuarios.Usuario;
import gui.componentes.MiBoton;
import gui.sesion.VentanaSesion;

@SuppressWarnings("serial")
public class VentanaEstadisticas extends JFrame{
	
	/** Crea una nueva ventana que contiene un JTable que muestra las estadísticas del usuario, las puntuaciones: FECHA, ESTRELLAS y NIVEL.
	 * También mostrará el nombre del usuario que está jugando la partida
	 * 
	 */	
	public VentanaEstadisticas(Usuario usuario) {
		
		this.setTitle("ESTADÍSTICAS");
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaSesion.class.getResource("/imgs/Icono.png")));
		JPanel panelArriba = new JPanel();
		this.add(panelArriba, BorderLayout.NORTH);
		
		JPanel panelAbajo = new JPanel();
		this.add(panelAbajo, BorderLayout.SOUTH);
		
		JLabel titulo = new JLabel(String.format("ESTADÍSTICAS DE %s", usuario.toString().toUpperCase()));
		titulo.setFont(new Font(Font.SERIF, Font.BOLD, 30));
		titulo.setForeground(Color.WHITE);
		titulo.setBackground(Color.WHITE);
		panelArriba.add(titulo);
		
		panelAbajo.setBackground(VentanaSesion.getFondooscuro());

		panelArriba.setBackground(VentanaSesion.getFondooscuro());
		
		DefaultTableModel dft = new DefaultTableModel();
		
		dft.addColumn("NIVEL ");
		dft.addColumn("FECHA ");
		dft.addColumn("ESTRELLAS ");
		
		for (Puntuacion puntuacion : usuario.getPuntuaciones()) {
			String str = new String(puntuacion.getFecha());
			
			Object[] array = {str.substring(0, 10), puntuacion.getNivel(), puntuacion.getEstrellas()};
			dft.addRow(array);	
		}
		
		JTable tablaEstadisticas = new JTable(dft);
		JScrollPane panelScroll = new JScrollPane(tablaEstadisticas);
		this.add(panelScroll);
//		panelCentro.add(tablaEstadisticas);
		
		JButton atras = new MiBoton(Color.WHITE, Color.WHITE.darker(), 50, 50);
		atras.setText("Volver");
		atras.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				new VentanaOpcionesJuego(FRAMEBITS, ERROR, ALLBITS, rootPaneCheckingEnabled, ABORT, null);
				dispose();
				
			}
		});
		
		panelAbajo.add(atras);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBackground(Color.WHITE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new VentanaEstadisticas(GestionUsuarios.getUsuario("Diego".hashCode()));
	}
}