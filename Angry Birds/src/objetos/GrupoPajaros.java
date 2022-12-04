package objetos;

import java.util.List;

import gui.juego.VentanaJuego;
import objetos.pajaros.Pajaro;

public class GrupoPajaros extends GrupoOP {
	protected List<Pajaro> listaPajaros;
	public GrupoPajaros() {
		super();
		// TODO Auto-generated constructor stub
	}
	public boolean compruebaChoqueLimites(VentanaJuego v) {
		for(Pajaro p: listaPajaros) {
			return p.choqueConLimitesLaterales(v)|| p.choqueConLimitesVertical(v);
		}return false;
	}
	public boolean compruebaChoqueCon(GrupoOP op) {
		for(Pajaro p: listaPajaros) {
			for(ObjetoPrimitivo op1: op.getGrupoOP())
			return p.choqueCon(op1);
		}return false;
	}
}
