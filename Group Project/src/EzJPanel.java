import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial") // serialization isn't necessary
public abstract class EzJPanel extends JPanel
        implements MouseListener, MouseMotionListener, KeyListener, ComponentListener {
    protected int windowWidth = 100;
    protected int windowHeight = 100;
    private String title = "EzJPanel";
    protected JFrame jf;
    private ArrayList<Integer> keyMap = new ArrayList<Integer>();
    private Color backgroundColor = new Color(0, 0, 0);
    private boolean shouldLoop = true;

    

    /**
     * 
     * @param width  width of window in pixels
     * @param height height of window in pixels
     * @param title  name shown at top of window
     */
    public EzJPanel(int width, int height, String title) {
        super();
        
        this.setup();
        
        // assign window title
        this.title = title;

        // create new JFrame
        this.jf = new JFrame(title);

        // set window size
        this.jf.setSize(width, height);

        // Set proper JPanel dimensions
        this.jf.getContentPane().setPreferredSize(new Dimension(width, height));
        this.jf.pack();

        // What to do when close
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.jf.add(this);

        // add components form subclass
        this.addComponents();

        // make JPanel visible
        this.jf.setVisible(true);

        // set background color
        this.jf.setBackground(Color.BLACK);

        // adds mouse listeners for mouse based interactions
        addMouseListener(this);
        addMouseMotionListener(this);

        // add key listener
        jf.addKeyListener(this);

        jf.addComponentListener(this);

        // assign window width
        this.windowWidth = getWidth();
        // assign window height
        this.windowHeight = getHeight();

        

    }

    /**
     * Produces a window of 100 by 100 pixels
     */
    public EzJPanel() {
        // create new JFrame
        this.jf = new JFrame(title);

        // set window size
        this.jf.setSize(this.windowWidth, this.windowHeight);

        // Set proper JPanel dimensions
        this.jf.getContentPane().setPreferredSize(new Dimension(this.windowWidth, this.windowHeight));
        this.jf.pack();
        this.setSize(this.windowWidth, this.windowHeight);
        setBounds(0, 0, 100, 100);

        // What to do when close
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add JPanel to JFrame
        setLayout(new BorderLayout());
        
        this.addComponents();

        // make JPanel visible
        this.jf.setVisible(true);

        // set background color
        this.jf.setBackground(Color.BLACK);

        // adds mouse listeners for mouse based interactions
        addMouseListener(this);
        addMouseMotionListener(this);

        // add key listener
        jf.addKeyListener(this);

        jf.addComponentListener(this);

        setup();
    }

    /**
     * Renames the window (will not change name in MacOS task bar)
     * 
     * @param name new name of the window
     */
    public void rename(String name) {
        this.jf.setTitle(name);
    }

    /**
     * Add a component to this jframe
     * All Swing components must be added in this function
     * 
     * @param component component to add
     */
    protected abstract void addComponents();

    /**
     * Function run at the end of constructor
     * <p>
     * If adding components look as {@code addComponents}
     */
    protected abstract void setup();

    /**
     * Draws onto the provided graphics
     * <p>
     * everything in this function will be executed at the end of paintComponent but
     * before before repaint()
     * 
     * @param g - Graphics to draw onto
     */
    protected abstract void draw(Graphics g);

    public void componentHidden(ComponentEvent ce) {
    };

    public void componentShown(ComponentEvent ce) {
    };

    public void componentMoved(ComponentEvent ce) {
    };

    public void componentResized(ComponentEvent ce) {
        this.windowHeight = this.getHeight();
        this.windowWidth = this.getWidth();
        repaint();
    };

    /**
     * stops repainting to the canvas
     */
    protected void stopLooping() {
        this.shouldLoop = false;
    }

    /**
     * stops repainting to the canvas if {@code stopLooping} is {@code true}
     * <p>
     * if {@code stopLooping} is {@code false} keep repainting
     * 
     * @param stopLooping - if {@code true} the JPanel will stop repainting at the
     *                    end of paintComponent
     */
    protected void stopLooping(boolean stopLooping) {
        this.shouldLoop = !stopLooping;
    }

    protected void startLooping() {
        this.shouldLoop = true;
        repaint();
    }

    /**
     * restarts painting to the canvas if {@code startLooping} is {@code true}
     * <p>
     * if {@code startLooping} is {@code true} repaint will be called
     * 
     * @param startLooping should the paintComponent method repaint at the end
     */
    protected void startLooping(boolean startLooping) {
        this.shouldLoop = startLooping;
        if (startLooping) {
            repaint();
        }
    }

    /**
     * returns the value of the boolean to determine whether or not to redraw the
     * graphics
     * 
     * @return {@code true} if the graphic should be repainting
     */
    public boolean isLooping() {
        return this.shouldLoop;
    }

    /**
     * Resizes the window to be {@code x} pixels wide and {@code y} pixels tall
     * <p>
     * suppressed deprecation warning since this overwrites the deprecated function
     * resize(int,int) from JPanel
     * 
     * @param x - desired width of the window
     * @param y - desired height of the window
     */
    @SuppressWarnings("deprecation")
    public void resizeWindow(int x, int y) {
        this.windowWidth = x;
        this.windowHeight = y;

        // set window size
        this.jf.setSize(this.windowWidth, this.windowHeight);

        // Set proper JPanel dimensions
        this.jf.getContentPane().setPreferredSize(new Dimension(this.windowWidth, this.windowHeight));
        this.jf.pack();
    }

    /**
     * 
     * @return the current {@code Color} used to fill in the background
     */
    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    /*
     * Add all painting/rendering functions to this section
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            // set background to black
            g.setColor(this.getBackgroundColor());
            g.fillRect(0, 0, windowWidth, windowHeight);

            this.draw(g);

            if (this.shouldLoop) {
                g.dispose();
                repaint();
            }

        } catch (Exception ex) {
            if (this.shouldLoop) {
                repaint();
            }
        }

    }

    // key press listener
    public void keyPressed(KeyEvent e) {
        /*
         * keyMap.indexOf(e.getKeyCode()) 37 - left 38 - up 39 - right 40 - down
         */

        if (keyMap.indexOf(e.getKeyCode()) == -1) {
            keyMap.add(e.getKeyCode());
        }
    }

    // key released listener
    public void keyReleased(KeyEvent e) {
        // System.out.println("Key released: ");
        if (keyMap.indexOf(e.getKeyCode()) != -1) {
            // remove from keymap when release
            keyMap.remove(keyMap.indexOf(e.getKeyCode()));
        }
    }

    // key typed listener
    public void keyTyped(KeyEvent e) {
    }

    // Mouse Motion Listener
    public void mouseDragged(MouseEvent mEvent) {
    }

    // mouse moved callback
    public void mouseMoved(MouseEvent mEvent) {
    }

    // Mouse click callback
    public void mouseClicked(MouseEvent mEvent) {
    }

    public void mouseEntered(MouseEvent mEvent) {
    }

    public void mouseExited(MouseEvent mEvent) {
    }

    public void mousePressed(MouseEvent mEvent) {
        this.mousePressedEz(mEvent.getX(), mEvent.getY());
    }

    public void mousePressedEz(int x, int y) {}

    public void mouseReleased(MouseEvent mEvent) {
    }

}