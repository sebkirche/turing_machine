import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The tape representation
 *
 * (This was ported from awt.Canvas)
 */
class TapeDisplay extends JPanel implements Runnable, StateChangeListener {

    private static final int MINW = 200;
    private static final int MINH = 50;
    private static final int CELLHEIGHT = 20;
    private static final int CELLWIDTH = 20;
    private int[] leftTriXs;
    private int[] rightTriXs;
    private int[] triYs;
    private TuringMachine controller;
    private volatile Thread scroller;
    private HeadDirection scrollDir;
    private HeadDirection scrollBtn;
    private int width;
    private int height;
    private int xpos;
    private int ypos;
    private int displayedCellsCount;
    private boolean scrolling = false;
    private int machinePos = 0;
    private final Font tapeFont = new Font("Helvetica", Font.BOLD, 12);
    private final Font cellNumfont = new Font("Helvetica", Font.PLAIN, 11);
    private final Color darkYellow = new Color(189, 193, 0);

    public TapeDisplay(TuringMachine controller) {
        this.controller = controller;
//        scroller = new Thread(this);
        Dimension minSize;
        minSize = new Dimension(MINW, MINH);
        setMinimumSize(minSize);
        setPreferredSize(minSize);
        addMouseListener(new MouseHandler());
    }

    public void initGraphics() {
        width = getSize().width;
        height = getSize().height;
        ypos = height / 2 - 10;
        displayedCellsCount = width / CELLWIDTH - 3;
        xpos = (width - displayedCellsCount * CELLWIDTH) / 2;
        leftTriXs = new int[]{20, 4, 20, 20};
        rightTriXs = new int[]{width - 21, width - 5, width - 21, width - 21};
        triYs = new int[]{ypos, height / 2, ypos + 20, ypos};
        repaint();
    }

    @Override
    public void stateChanged(StateChangedEvent evt) {
        machinePos = evt.getPos();
        scrollDir = evt.getDir();
        System.out.println(String.format("TD::stateChanged p=%d / s=%d / d=%s",evt.getPos(), evt.getState(), evt.getDir()));

//        if(evt.getSpeed() != Speed.compute)
            repaint();
    }

    private class MouseHandler extends MouseAdapter {
        public void mousePressed(MouseEvent e){
            int i = e.getX();
            int j = e.getY();
            System.out.println(String.format("MouseHandler / mouse pressed running = %s - scrolling = %s", controller.isMachineRunning(), scroller != null));
            if(!controller.isMachineRunning() && (scroller == null)) {
                if (i >= leftTriXs[1] && i <= leftTriXs[0] && j >= triYs[0] && j <= triYs[2]) {
                    scrollBtn = HeadDirection.left;
//                    if (controller.askScrolling(scrollBtn)) {
                        //repaint();
                        scroller = new Thread(TapeDisplay.this);
                        scroller.start();
//                    }
                } else if (i >= rightTriXs[0] && i <= rightTriXs[1] && j >= triYs[0] && j <= triYs[2]) {
                    scrollBtn = HeadDirection.right;
//                    if (controller.askScrolling(scrollBtn)) {
                        //repaint();
                        scroller = new Thread(TapeDisplay.this);
                        scroller.start();
//                    }
                }
               System.out.println("Clicked for "+scrollBtn);

            }
        }

        public void mouseReleased(MouseEvent e){
            if((scroller != null)){// && scroller.isAlive()) {
                scroller = null; //remplace scroller.stop();
//                //todo: encore nécessaire ? engine.machine.moving = HeadDirection.stay;
//                repaint();

            }
        }
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setFont(tapeFont);
        g.setColor(new Color(192, 0, 0));

        //dark red border
        g.drawRect(0, 0, width - 1, height - 1);
        g.drawRect(1, 1, width - 3, height - 3);

        //upper left corner
        g.fillPolygon(  new int[] {2, 2, 15, 2},
                        new int[] {15, 2, 2, 15},
                        4);

        //lower left corner
        g.fillPolygon(  new int[] {2, 2, 15, 2},
                        new int[] {height - 15, height - 2, height - 2, height - 15},
                        4);

        //upper right corner
        g.fillPolygon(  new int[] {width - 2, width - 2, width - 15, width - 2},
                        new int[] {15, 2, 2, 15},
                        4);

        //lower right corner
        g.fillPolygon(  new int[] {width - 2, width - 2, width - 15, width - 2},
                        new int[] {height - 15, height - 2, height - 2, height - 15},
                        4);

        //tape left scrolling arrow content
        if(controller.getMachineDirection() == HeadDirection.left)
            g.setColor(Color.yellow);
        else
            g.setColor(darkYellow);
        g.fillPolygon(leftTriXs, triYs, 4);

        //tape right scrolling arrow content
        if(controller.getMachineDirection() == HeadDirection.right)
            g.setColor(Color.yellow);
        else
            g.setColor(darkYellow);
        g.fillPolygon(rightTriXs, triYs, 4);

        //tape arrows borders
        g.setColor(Color.black);
        g.drawPolygon(leftTriXs, triYs, 4);
        g.drawPolygon(rightTriXs, triYs, 4);

        int i = displayedCellsCount;
        /*if(!controller.isMachineProgrammed()) {

            int j = xpos;
            for(int k = 0; k < displayedCellsCount; k++) {
                if(k == displayedCellsCount / 2) {
                    g.setColor(Color.red);
                    g.drawRect(j, ypos, CELLWIDTH, CELLHEIGHT);
                    g.drawRect(j - 1, ypos - 1, 22, 22);
                    g.setFont(cellNumfont);
						 g.drawString(String.format("%d", machinePos), j , ypos - 2 );
						 g.setFont(tapeFont);
                    g.setColor(Color.black);
                } else {
                    g.drawRect(j, ypos, CELLWIDTH, CELLHEIGHT);
                }

                if(k == displayedCellsCount / 2 + 1) {
                    g.setColor(Color.red);
                    g.drawLine(j, ypos, j, ypos + CELLHEIGHT);
                    g.setColor(Color.black);
                }
                j += CELLWIDTH;
            }

        } else*/ {

            //int currPos = machinePos;
            int halfCells = displayedCellsCount / 2;
            int j1 = xpos + CELLWIDTH * halfCells;
            int k1 = displayedCellsCount / 2;
            int l1 = displayedCellsCount % 2 != 0 ? displayedCellsCount / 2 : displayedCellsCount / 2 - 1;
            int firstVisibleCell;
            int k2;
            if(machinePos < k1) {
                firstVisibleCell = 0;
                k2 = xpos + CELLWIDTH * (k1 - machinePos);
                i -= k1 - machinePos;
            } else {
                firstVisibleCell = machinePos - k1;
                k2 = xpos;
            }
            if(machinePos + l1 >= TMEngineModel.TAPESIZE)
                i -= (machinePos + l1 + 1) - TMEngineModel.TAPESIZE;
            int lastVisibleCell = (firstVisibleCell + i) - 1;

            char ac[] = controller.getMachineMemory(firstVisibleCell, lastVisibleCell);

            int l2 = k2;
            for(int currPaintCell = firstVisibleCell; currPaintCell <= lastVisibleCell; currPaintCell++) {
                if(l2 == j1) {
                    g.setColor(Color.red);
                    g.drawRect(l2, ypos, CELLWIDTH, CELLHEIGHT);
                    g.drawRect(l2 - 1, ypos - 1, 22, 22);
                    g.setFont(cellNumfont);
						 g.drawString(String.format("%d", machinePos), l2 , ypos - 2 );
						 g.setFont(tapeFont);
                    g.setColor(Color.black);
                } else {
                    g.drawRect(l2, ypos, CELLWIDTH, CELLHEIGHT);
                }
                if(l2 == j1 + CELLWIDTH) {
                    g.setColor(Color.red);
                    g.drawLine(l2, ypos, l2, ypos + CELLHEIGHT);
                    g.setColor(Color.black);
                }
                if(ac[currPaintCell-firstVisibleCell] != ' ')
                    g.drawString(String.valueOf(ac[currPaintCell-firstVisibleCell]), (l2 + 10) - 4, ypos + 15);
                l2 += CELLWIDTH;
            }

        }
    }

    public void run() {
       Thread thisThread = Thread.currentThread();
        System.out.println("Running scroller thread");
        for(boolean flag = true; (scroller == thisThread) && flag;) {
            try {
                thisThread.sleep(200);
            } catch(InterruptedException _ex) { _ex.printStackTrace();}
            System.out.println("TapeDisplay::run - scrolling");
            flag = controller.askScrolling(scrollBtn);
            System.out.println("scroller :"+flag);
            //repaint();
        }
        scrollBtn = HeadDirection.stay;
        controller.askScrolling(HeadDirection.stay);

        //todo: encore nécessaire ? engine.machine.moving = HeadDirection.stay;
        repaint();
    }

    public boolean isScrolling(){
        return scrolling;
    }
}
