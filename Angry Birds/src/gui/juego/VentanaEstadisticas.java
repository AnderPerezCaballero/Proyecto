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
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import gestionUsuarios.Puntuacion;
import gestionUsuarios.Usuario;
import gui.sesion.VentanaSesion;

@SuppressWarnings("serial")
public class VentanaEstadisticas extends JFrame{

	private JPanel panelFondo;
	private JPanel panelAbajo;
	private DefaultTableModel dft;
	private JTable tablaEstadisticas;
	private JScrollPane panelScroll;

	private JLabel titulo;
	private JButton volver;

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
		setBackground(VentanaSesion.getFondooscuro());
	

		panelAbajo = new JPanel(new FlowLayout());
		panelAbajo.setBackground(VentanaSesion.getFondooscuro());
		
		panelFondo = new JPanel();
		panelFondo.setBackground(VentanaSesion.getFondooscuro());
		panelFondo.setLayout(new BorderLayout());

		titulo = new JLabel(String.format("ESTADÍSTICAS DE %s", usuario.toString().toUpperCase()));
		titulo.setFont(new Font(Font.SERIF, Font.BOLD, 30));
		titulo.setHorizontalAlignment(JLabel.CENTER);
		titulo.setForeground(Color.WHITE);
		titulo.setBackground(VentanaSesion.getFondooscuro());
		titulo.setOpaque(true);

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

		tablaEstadisticas.getTableHeader().setBackground(Color.WHITE);
		tablaEstadisticas.getTableHeader().setForeground(Color.BLACK);
		tablaEstadisticas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

				JLabel label = new JLabel(value.toString());

				if (isSelected) {
					label.setBackground(Color.GREEN);
					label.setForeground(Color.BLACK);
				}else {
					label.setBackground(VentanaSesion.getFondooscuro().brighter());
					label.setForeground(Color.WHITE);
				}
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setOpaque(true);
				return label;
			}
		});

		panelScroll = new JScrollPane(tablaEstadisticas);
		panelScroll.getViewport().setBackground(VentanaSesion.getFondooscuro());

		panelScroll.setBorder(new LineBorder(VentanaSesion.getFondooscuro(), 0));

		panelFondo.add(panelScroll);

		volver = new JButton("Volver");
		volver.setBackground(Color.WHITE);
		volver.setForeground(Color.BLACK);
		volver.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ventanaAnterior.setVisible(true);
				dispose();
			}
		});

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				volver.doClick();
			}
		});

		panelAbajo.add(volver);

		add(panelFondo, BorderLayout.CENTER);
		add(titulo, BorderLayout.NORTH);
		add(panelAbajo, BorderLayout.SOUTH);

		setVisible(true);
		ventanaAnterior.setVisible(false);
	}
}