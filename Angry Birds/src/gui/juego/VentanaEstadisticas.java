package gui.juego;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.plaf.LayerUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import gestionUsuarios.Puntuacion;
import gui.componentes.BlurLayerUI;
import gui.componentes.MiBoton;
import gui.sesion.VentanaSesion;

@SuppressWarnings("serial")
public class VentanaEstadisticas extends JFrame{
	
	private JPanel panelAbajo;
	private JPanel panelArriba;
	private JLabel titulo;
	private DefaultTableModel dft;
	private JTable tablaEstadisticas;
	private JScrollPane panelScroll;
	
	/** Crea una nueva ventana que contiene un JTable que muestra las estadísticas del usuario, las puntuaciones: FECHA, ESTRELLAS y NIVEL.
	 * También mostrará el nombre del usuario que está jugando la partida
	 * 
	 */
	public VentanaEstadisticas(Set<Puntuacion> punt) {
		
		this.setTitle("ESTADÍSTICAS");
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaSesion.class.getResource("/imgs/Icono.png")));
		panelArriba = new JPanel();
		this.add(panelArriba, BorderLayout.NORTH);
		
//		JPanel panelCentro = new JPanel();
//		this.add(panelCentro, BorderLayout.CENTER);
		
		panelAbajo = new JPanel();
		this.add(panelAbajo, BorderLayout.SOUTH);
		
		titulo = new JLabel("ESTADÍSTICAS DEL USUARIO");
		titulo.setFont(new Font(Font.SERIF, Font.BOLD, 30));
		titulo.setForeground(Color.WHITE);
		titulo.setBackground(Color.WHITE);
		panelArriba.add(titulo);
		
		panelAbajo.setBackground(VentanaSesion.getFondooscuro());
//		panelCentro.setBackground(VentanaSesion.getFondooscuro());
		panelArriba.setBackground(VentanaSesion.getFondooscuro());
		
		dft = new DefaultTableModel();
		
		dft.addColumn("FECHA ");
		dft.addColumn("NIVEL ");
		dft.addColumn("ESTRELLAS ");
		
		for (Puntuacion puntuacion : punt) {
			String str = new String(puntuacion.getFecha());
			
			Object[] array = {str.substring(0, 10), puntuacion.getNivel(), puntuacion.getEstrellas()};
			dft.addRow(array);	
		}
		
		tablaEstadisticas = new JTable(dft) {
			private static final long serialVersionUID = 1L;
			
			public boolean isCellEditable(int row, int column) {                
                return false;               
			};
		};
		
		panelScroll = new JScrollPane(tablaEstadisticas);
		this.add(panelScroll);
//		panelCentro.add(tablaEstadisticas);
		
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
		
		tablaEstadisticas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				
				JLabel label = new JLabel(value.toString());
				
				if (isSelected) {
					label.setBackground(Color.cyan);
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