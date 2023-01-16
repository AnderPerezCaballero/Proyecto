package gui.juego;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import gestionUsuarios.Puntuacion;
import gestionUsuarios.Usuario;
import gui.sesion.VentanaSesion;

@SuppressWarnings("serial")
public class VentanaEstadisticas extends JFrame{
	
	private JPanel panelAbajo;
	
	private DefaultTableModel dft;
	private JTable tablaEstadisticas;
	private JScrollPane panelScroll;
	
	private JLabel titulo;
	private JButton atras;
	
	/** Crea una nueva ventana que contiene un JTable que muestra las estadísticas del usuario, las puntuaciones: FECHA, ESTRELLAS y NIVEL.
	 * También mostrará el nombre del usuario que está jugando la partida
	 * @param usuario usuario de la partida
	 */	
	public VentanaEstadisticas(Usuario usuario, VentanaOpcionesJuego ventanaAnterior) {
		
		setTitle("ESTADÍSTICAS");
		setSize(VentanaOpcionesJuego.ANCHURA, VentanaOpcionesJuego.ALTURA);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaSesion.class.getResource("/imgs/Icono.png")));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		panelAbajo = new JPanel(new FlowLayout());
		
		titulo = new JLabel(String.format("ESTADÍSTICAS DE %s", usuario.toString().toUpperCase()));
		titulo.setFont(new Font(Font.SERIF, Font.BOLD, 30));
		titulo.setHorizontalAlignment(JLabel.CENTER);
		titulo.setForeground(Color.WHITE);
		titulo.setBackground(VentanaSesion.getFondooscuro());
		titulo.setOpaque(true);
		
		panelAbajo.setBackground(VentanaSesion.getFondooscuro());
		
		dft = new DefaultTableModel();
		dft.addColumn("NIVEL");
		dft.addColumn("ESTRELLAS");
		dft.addColumn("FECHA");
		
		for (Puntuacion puntuacion : usuario.getPuntuaciones()) {
			String str = new String(puntuacion.getFecha());
			
			Object[] array = {puntuacion.getNivel(), puntuacion.getEstrellas(), str.substring(0, 10)};
			dft.addRow(array);	
		}
		
		tablaEstadisticas = new JTable(dft) {
			
			public boolean isCellEditable(int row, int column) {                
                return false;               
			};
		};
		panelScroll = new JScrollPane(tablaEstadisticas);
		
		atras = new JButton("Volver");
		atras.setBackground(Color.WHITE);
		atras.setForeground(Color.BLACK);
		atras.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ventanaAnterior.setVisible(true);
				dispose();
			}
		});
		panelAbajo.add(atras);
		
		tablaEstadisticas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				
				JLabel label = new JLabel(value.toString());
				
				if (isSelected) {
					label.setBackground(Color.CYAN);
					label.setForeground(Color.BLACK);
				}else {
					label.setBackground(VentanaSesion.getFondooscuro());
					label.setForeground(Color.WHITE);
				}
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setOpaque(true);
				return label;
			}
		});
		
		add(titulo, BorderLayout.NORTH);
		add(panelScroll, BorderLayout.CENTER);
		add(panelAbajo, BorderLayout.SOUTH);
	
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				atras.doClick();
			}
		});
		setVisible(true);
		ventanaAnterior.setVisible(false);
	}
}