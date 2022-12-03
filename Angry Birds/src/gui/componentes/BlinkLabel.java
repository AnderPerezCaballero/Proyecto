package gui.componentes;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

public class BlinkLabel extends JLabel {
	  private static final long serialVersionUID = 1L;
	  
	  private static final int PERIODO_BLINK = 1000; //en ms

	  private boolean parpadeoOn = true;
	  
	  public BlinkLabel(String text) {
	    super(text);
	    Timer timer = new Timer( PERIODO_BLINK , new TimerListener(this));
	    timer.setInitialDelay(0);
	    timer.start();
	  }
	  
	  public void setBlinking(boolean flag) {
	    this.parpadeoOn = flag;
	  }
	  public boolean getBlinking(boolean flag) {
	    return this.parpadeoOn;
	  }

	  
	  private class TimerListener implements ActionListener {
	    private BlinkLabel bl;
	    private Color bg;
	    private Color fg;
	    private boolean isForeground = true;
	    
	    public TimerListener(BlinkLabel bl) {
	      this.bl = bl;
	      fg = bl.getForeground();
	      bg = bl.getBackground();
	    }
	 
	    public void actionPerformed(ActionEvent e) {
	      if (bl.parpadeoOn) {
	        if (isForeground) {
	          bl.setForeground(fg);
	        }
	        else {
	          bl.setForeground(bg);
	        }
	        isForeground = !isForeground;
	      }
	      else {
	        // Aqui queremos asegurarnos de que el label es visible
	        // aunque el parpadeo esté apagado, por eso se seguirán viendo las letras en color FONDOOSCURO.
	        if (isForeground) {
	          bl.setForeground(fg);
	          isForeground = false;
	        }
	      }
	    }
	  } 
	  }