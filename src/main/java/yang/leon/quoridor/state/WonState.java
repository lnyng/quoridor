package yang.leon.quoridor.state;

import java.awt.Graphics;
import java.awt.Point;
import java.rmi.RemoteException;

import yang.leon.quoridor.AbstractView;
import yang.leon.quoridor.DefaultView;
import yang.leon.quoridor.IModelAdapter;
import yang.leon.quoridor.Location;

public class WonState extends WaitingState {

    public WonState(AbstractView context) {
	super(context);
    }

    public void update(Graphics g) {
	IModelAdapter adpt = getContext().getModelAdapter();
	Location pawnLoc = null;
	try {
	    pawnLoc = adpt.getPlayer(adpt.getCurrPlayerIndex())
	    	.getPawnLoc();
	} catch (RemoteException e) {
	    e.printStackTrace();
	    return;
	}
	Point pawnPoint = DefaultView.getPointFromSqrLoc(pawnLoc);
	g.drawImage(getContext().getImage("winner"), pawnPoint.x, pawnPoint.y,
		null);
    }

    public String toString() {
	return "Won state.";
    }

}
