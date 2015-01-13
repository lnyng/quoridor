package yang.leon.quoridor.state;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import yang.leon.quoridor.AbstractView;
import yang.leon.quoridor.DefaultModel;
import yang.leon.quoridor.DefaultView;
import yang.leon.quoridor.IModelAdapter;
import yang.leon.quoridor.Location;

public class HoldingWallState extends IViewState {

    private int direction;
    private Point mouseLocation;
    private static final int DELAY = 350;
    private Timer timer;
    private boolean showPuttingLoc;

    public HoldingWallState(AbstractView context) {
	super(context);
	direction = DefaultModel.HORIZONTAL_WALL;
	timer = new Timer(DELAY, new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		showPuttingLoc = true;
		getContext().repaint();
	    }
	});
	timer.setRepeats(false);
    }

    @Override
    public void mousePressed(MouseEvent e) {
	if (SwingUtilities.isRightMouseButton(e)) {
	    direction = (direction == DefaultModel.HORIZONTAL_WALL) ? DefaultModel.VERTICAL_WALL
		    : DefaultModel.HORIZONTAL_WALL;
	    showPuttingLoc = false;
	    timer.restart();
	    getContext().repaint();
	    return;
	}
	if (!SwingUtilities.isLeftMouseButton(e))
	    return;
	Location loc = DefaultView.getCrsLocAtPoint(e.getPoint());
	IModelAdapter adpt = getContext().getModelAdapter();
	try {
	    if (adpt.isCanPutWall(loc, direction)) {
	        adpt.putWall(loc, direction);
	        adpt.nextPlayer(getContext());
	    }
	} catch (RemoteException e1) {
	    e1.printStackTrace();
	}
    }

    @Override
    public void mouseMoved(MouseEvent e) {
	mouseLocation = e.getPoint();
	showPuttingLoc = false;
	timer.restart();
	getContext().update();
    }

    @Override
    public void update(Graphics g) {
	drawCurrPlayer(g);
	if (mouseLocation == null)
	    return;
	Location loc = DefaultView.getCrsLocAtPoint(mouseLocation);
	try {
	    if (showPuttingLoc
	    	&& getContext().getModelAdapter().isCanPutWall(loc, direction)) {
	        Point putting = DefaultView.getPointFromCrsLoc(loc);
	        getContext().drawWall(g, putting, direction, "light");
	    }
	} catch (RemoteException e) {
	    e.printStackTrace();
	}
	getContext().drawWall(g, mouseLocation, direction, null);
    }

    public String toString() {
	return "Holding wall state; direction: "
		+ ((direction == DefaultModel.HORIZONTAL_WALL) ? "horizontal."
			: "vertical.");
    }
}
