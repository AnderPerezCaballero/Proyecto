package gui.inicio;

import java.awt.Font;
import java.awt.event.KeyListener;

import gui.componentes.BlinkLabel;

@SuppressWarnings("serial")
public class VentanaInicioPlay2 extends VentanaInicio{
private KeyListener kl;
	
	public VentanaInicioPlay2() {
		super();
		
		BlinkLabel bl = new BlinkLabel("PULSA CUALQUIER TECLA PARA JUGAR");
		panelAbajo.add(bl);
		bl.setFont(new Font("Arial", Font.CENTER_BASELINE,15));
		
	}

}
