package gui.juego;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gestionUsuarios.GestionUsuarios;
import gestionUsuarios.Puntuacion;
import gestionUsuarios.Usuario;
import gui.sesion.VentanaSesion;

@SuppressWarnings("serial")
public class Ranking extends JFrame{

	private static List<Usuario> usuarios;
	private static boolean tiempo;

	private JLabel titulo;

	private JPanel panelCentral;
	private JPanel panelInferior;

	private JButton volver;

	public Ranking(VentanaOpcionesJuego ventanaAnterior) {
		usuarios = new ArrayList<>();

		setLocation(ventanaAnterior.getLocation());
		setIconImage(Toolkit.getDefaultToolkit().getImage(VentanaSesion.class.getResource("/imgs/Icono.png")));
		setSize(VentanaOpcionesJuego.ANCHURA, VentanaOpcionesJuego.ALTURA);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		panelCentral = new JPanel(new GridLayout(10, 1));
		panelCentral.setBackground(VentanaSesion.getFondooscuro());

		panelInferior = new JPanel(new FlowLayout());
		panelInferior.setBackground(VentanaSesion.getFondooscuro());

		titulo = new JLabel();
		titulo.setHorizontalAlignment(JLabel.HORIZONTAL);
		titulo.setBackground(VentanaSesion.getFondooscuro());
		titulo.setForeground(Color.WHITE);
		titulo.setOpaque(true);

		opciones();

		añadirUsuarios(tiempo);

		volver = new JButton("Volver");
		volver.setBackground(Color.WHITE);
		volver.setForeground(Color.BLACK);
		volver.addActionListener(e -> {
			ventanaAnterior.setVisible(true);
			dispose();
		});

		panelInferior.add(volver);

		add(titulo, "North");
		add(panelCentral, "Center");
		add(panelInferior, "South");

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				volver.doClick();
			}
		});

		setVisible(true);
		ventanaAnterior.setVisible(false);

		visibilizarUsuarios();
	}


	private void opciones() {
		String[] s = {"Usuarios con más tiempo Jugado", "Usuarios con mejores puntuaciones"}; //Opciones del JOptionPane
		if (JOptionPane.showOptionDialog(Ranking.this, "¿Sobre qué quieres que sea el ranking?", "Ranking", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, VentanaSesion.imagenReescalada("/imgs/Icono.png", 30, 30), s, 0) == 0) {
			top10Tiempo(GestionUsuarios.getUsuarios(), 0);
			titulo.setText("USUARIOS CON MÁS TIEMPO JUGADO");
			setTitle("Ranking de Tiempo");
			tiempo = true;
			titulo.setFont(new Font(Font.SERIF, Font.BOLD, 35));
		}else {
			top10Puntuaciones(GestionUsuarios.getUsuarios());
			titulo.setText("USUARIOS CON MEJORES PUNTUACIONES");
			setTitle("Ranking de Puntuaciones");
			tiempo = false;
			titulo.setFont(new Font(Font.SERIF, Font.BOLD, 30));
		}
	}

	/** Configura el panel central, añadiendo los textos correspondientes a los usuarios a la ventana, sin que estos sean visibles aún
	 * @param puntuaciones true si el ranking se hace sobre puntuaciones, false si se hace sobre tiempo
	 */
	private void añadirUsuarios(boolean puntuaciones) {
		for(int i = 0; i < usuarios.size(); i++) {
			JLabel jl = new JLabel();
			Usuario usuario = usuarios.get(i);
			if(puntuaciones) {
				jl.setText(String.format("%d. %s(%.2f minutos)", i +1, usuario.toString(), usuario.getTiempoJugado()/60000.0));
			}else {
				jl.setText(String.format("%d. %s", i +1, usuario.toString()));
			}
			jl.setHorizontalAlignment(JLabel.CENTER);
			jl.setForeground(VentanaSesion.getFondooscuro());
			jl.setFont(new Font(Font.SERIF, Font.BOLD, 20));
			panelCentral.add(jl);
		}
	}

	/** Método que a traves de la recursividad múltiplo añade a la lista de usuarios los 10 usuarios con más tiempo jugado
	 * @param usuariosBD lista total de usuarios de la base de datos
	 * @param n índice (inicialmente 0)
	 */
	private void top10Tiempo(List<Usuario> usuariosBD, int n) {
		if (n == usuariosBD.size() || usuarios.size() == 10 || usuariosBD.size() == 0) {
			return;
		}

		Usuario max = usuariosBD.get(n);
		for (int i = n + 1; i < usuariosBD.size(); i++) {
			if(max.getTiempoJugado() < usuariosBD.get(i).getTiempoJugado()) {
				max = usuariosBD.get(i);
			}
		}
		usuarios.add(max);
		usuariosBD.remove(usuariosBD.indexOf(max));

		top10Tiempo(usuariosBD, n);
		top10Tiempo(usuariosBD, n + 1);
	}

	/** Inicia una animación para visibilizar los usuarios del ranking
	 * 
	 */
	private void visibilizarUsuarios() {
		new Thread(() -> {
			for(int i = 0; i < usuarios.size(); i++) {
				JLabel jl = (JLabel) panelCentral.getComponent(i);
				while(true) {
					Color foreground = jl.getForeground();

					if(foreground.equals(Color.WHITE)) {
						break;
					}

					int r = foreground.getRed();
					int g = foreground.getGreen();
					int b = foreground.getBlue();

					if(r < 255) {
						r += 1;
					}
					if(g < 255) {
						g += 1;
					}
					if(b < 255) {
						b += 1;
					}

					final int RED = r;
					final int GREEN = g;
					final int BLUE = b;

					SwingUtilities.invokeLater(() -> {
						jl.setForeground(new Color(RED, GREEN, BLUE));
					});

					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {}
				}
			}
		}).start();
	}

	/** Método que añade a la lista de usuarios los 10 usuarios con mejores puntuaciones
	 * @param usuariosBD lista total de usuarios de la base de datos
	 */
	private List<Usuario> top10Puntuaciones(List<Usuario> usuariosBD) {

		for(Usuario usuario : usuariosBD) {
			Collections.sort(usuario.getPuntuaciones(), new Comparator<Puntuacion>() {
				@Override
				public int compare(Puntuacion o1, Puntuacion o2) {
					return -Integer.compare(o1.getEstrellas(), o2.getEstrellas());
				}
			});
		}

		Collections.sort(usuariosBD, new Comparator<Usuario>() {
			@Override
			public int compare(Usuario o1, Usuario o2) {
				return -Integer.compare(o1.getPuntuaciones().get(0).getEstrellas(), o2.getPuntuaciones().get(0).getEstrellas());
			}
		});

		for(int i = 0; i < Math.min(10, usuariosBD.size()); i++) {
			Usuario usuario = usuariosBD.get(i);

			if(i > 0 && usuario.getPuntuaciones().get(0).getEstrellas() == usuariosBD.get(i - 1).getPuntuaciones().get(0).getEstrellas()) {
				for(int j = 1; j < usuario.getPuntuaciones().size(); j++) {
					if(usuario.getPuntuaciones().get(j).getEstrellas() > usuariosBD.get(i - 1).getPuntuaciones().get(j).getEstrellas()) {
						usuarios.add(usuario);
						break;
					}else if(usuario.getPuntuaciones().get(j).getEstrellas() < usuariosBD.get(i - 1).getPuntuaciones().get(j).getEstrellas()) {
						break;
					}
				}
			}else {
				usuarios.add(usuario);
			}
		}
		return usuarios;
	}

}
