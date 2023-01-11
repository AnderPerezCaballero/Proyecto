package gui.juego;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.plaf.LayerUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import gestionUsuarios.Puntuacion;
import gui.componentes.BlurLayerUI;
import gui.componentes.MiBoton;
import gui.sesion.VentanaSesion;

@SuppressWarnings("serial")
public class VentanaEstadisticas extends JFrame{
	
	/** Crea una nueva ventana que contiene un JTable que muestra las estadísticas del usuario, las puntuaciones: FECHA, ESTRELLAS y NIVEL.
	 * También mostrará el nombre del usuario que está jugando la partida
	 * 
	 */
	private static Font fuentePrincipal;
	
	public VentanaEstadisticas(Set<Puntuacion> punt) {
		
		this.setTitle("ESTADÍSTICAS");
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		
		JPanel panelArriba = new JPanel();
		this.add(panelArriba, BorderLayout.NORTH);
		
		JPanel panelCentro = new JPanel();
		this.add(panelCentro, BorderLayout.CENTER);
		
		JPanel panelAbajo = new JPanel();
		this.add(panelAbajo, BorderLayout.SOUTH);
		
		JLabel titulo = new JLabel("ESTADÍSTICAS DEL USUARIO");
		titulo.setFont(new Font(Font.SERIF, Font.BOLD, 30));
		panelArriba.add(titulo);
		
		DefaultTableModel dft = new DefaultTableModel();
		
		dft.addColumn("FECHA ");
		dft.addColumn("NIVEL ");
		dft.addColumn("ESTRELLAS ");
		
		for (Puntuacion puntuacion : punt) {
			Object[] array = {puntuacion.getFecha(), puntuacion.getNivel(), puntuacion.getEstrellas()};
			dft.addRow(array);	
		}
		
		JTable tablaEstadisticas = new JTable(dft);
		panelCentro.add(tablaEstadisticas);
		
		JButton atras = new MiBoton(Color.WHITE, Color.WHITE.darker(), 50, 50);
		atras.setText("Volver");
		atras.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				VentanaOpcionesJuego vop = new VentanaOpcionesJuego(FRAMEBITS, ERROR, ALLBITS, rootPaneCheckingEnabled, ABORT, null);
				dispose();
				
			}
		});
		panelAbajo.add(atras);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBackground(Color.WHITE);
		setVisible(true);
	}

	
//	public static void main(String[] args) {
//		Puntuacion punt = new Puntuacion(null, ALLBITS, ABORT);
//		VentanaEstadisticas ve = new VentanaEstadisticas(punt);
//		
//	}
}