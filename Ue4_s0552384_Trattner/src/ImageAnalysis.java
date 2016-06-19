// BV Ue04 WS2015/16 Vorgabe
//
// Copyright (C) 2015 by Klaus Jung

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.awt.*;
import java.io.File;

public class ImageAnalysis extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static final String author = "<Marcel>";		// TODO: type in your name here
	private static final String initialFilename = "mountains.png";
	private static final File openPath = new File(".");
	private static final int border = 10;
	private static final int maxWidth = 910; 
	private static final int maxHeight = 910; 
	private static final int graySteps = 256;

	
	private static JFrame frame;
	
	private ImageView imgView;						// image view
	private HistoView histoView = new HistoView();	// histogram view
	private StatsView statsView = new StatsView();	// statistics values view
	private JSlider brightnessSlider;				// brightness Slider
	private JSlider contrastSlider;					// contrast Slider
	
	// TODO: add an array to hold the histogram of the loaded image
	public int[] histo;
	
	// TODO: add an array that holds the ARGB-Pixels of the originally loaded image
	public int[] pixels;
	
	private JLabel statusLine;				// to print some status text
	
	/**
	 * Constructor. Constructs the layout of the GUI components and loads the initial image.
	 */
	public ImageAnalysis() {
        super(new BorderLayout(border, border));
        
        // load the default image
        File input = new File(initialFilename);
        
        if(!input.canRead()) input = openFile(); // file not found, choose another image
        
        imgView = new ImageView(input);
        imgView.setMaxSize(new Dimension(maxWidth, maxHeight));
        
        // TODO: set the histogram array of histView and statsView

        // TODO: initialize the original ARGB-Pixel array from the loaded image
        pixels = imgView.getPixels().clone();


        // load image button
        JButton load = new JButton("Open Image");
        load.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		File input = openFile();
        		if(input != null) {
        			imgView.loadImage(input);
        			imgView.setMaxSize(new Dimension(maxWidth, maxHeight));
        			
        	        // TODO: initialize the original ARGB-Pixel array from the newly loaded image
        			
        			frame.pack();
	                processImage();
        		}
        	}        	
        });
         
        JButton reset = new JButton("Reset Slider");
        reset.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		brightnessSlider.setValue(0);
        		contrastSlider.setValue(10);
        		// TODO: reset contrast slider

        		processImage();
	    	}        	
	    });
         
        
        // some status text
        statusLine = new JLabel(" ");
        
        // top view controls
        JPanel topControls = new JPanel(new GridBagLayout());
        topControls.add(load);
        topControls.add(reset);
        
        // center view
        JPanel centerControls = new JPanel();
        JPanel rightControls = new JPanel();
        rightControls.setLayout(new BoxLayout(rightControls, BoxLayout.Y_AXIS));
        centerControls.add(imgView);
        rightControls.add(histoView);
        rightControls.add(statsView);
        centerControls.add(rightControls);
        
        // bottom view controls
        JPanel botControls = new JPanel();
        botControls.setLayout(new BoxLayout(botControls, BoxLayout.Y_AXIS));
        
        // brightness slider
        brightnessSlider = new JSlider(-graySteps, graySteps, 0);
		TitledBorder titBorder = BorderFactory.createTitledBorder("Brightness");
		titBorder.setTitleColor(Color.GRAY);
        brightnessSlider.setBorder(titBorder);
        brightnessSlider.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		processImage();				
        	}        	
        });
        
        // contrast slider
        contrastSlider = new JSlider(0, 50, 10);
		TitledBorder titBorder2 = BorderFactory.createTitledBorder("Contrast");
		titBorder.setTitleColor(Color.GRAY);
		contrastSlider.setBorder(titBorder2);
		contrastSlider.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		processImage();				
        	}        	
        });
        
        // TODO: setup contrast slider
		botControls.add(contrastSlider);
        statusLine.setAlignmentX(Component.CENTER_ALIGNMENT);
        botControls.add(statusLine);
        
        
        botControls.add(brightnessSlider);
        statusLine.setAlignmentX(Component.CENTER_ALIGNMENT);
        botControls.add(statusLine);

        // add to main panel
        add(topControls, BorderLayout.NORTH);
        add(centerControls, BorderLayout.CENTER);
        add(botControls, BorderLayout.SOUTH);
               
        // add border to main panel
        setBorder(BorderFactory.createEmptyBorder(border,border,border,border));
        
        // perform the initial rotation
        processImage();
	}
	

	/**
	 * Set up and show the main frame.
	 */
	private static void createAndShowGUI() {
		// create and setup the window
		frame = new JFrame("Image Analysis - " + author);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JComponent contentPane = new ImageAnalysis();
        contentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(contentPane);

        // display the window
        frame.pack();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        frame.setLocation((screenSize.width - frame.getWidth()) / 2, (screenSize.height - frame.getHeight()) / 2);
        frame.setVisible(true);
	}

	/**
	 * Main method. 
	 * @param args - ignored. No arguments are used by this application.
	 */
	public static void main(String[] args) {
        // schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
	
	
	/**
	 * Open file dialog used to select a new image.
	 * @return The selected file object or null on cancel.
	 */
	private File openFile() {
		// file open dialog
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (*.jpg, *.png, *.gif)", "jpg", "png", "gif");
        chooser.setFileFilter(filter);
        chooser.setCurrentDirectory(openPath);
        int ret = chooser.showOpenDialog(this);
        if(ret == JFileChooser.APPROVE_OPTION) return chooser.getSelectedFile();
        return null;		
	}
	
    
	
    /**
     * Update image with new brightness and contrast values.
     * Update histogram, histogram view and statistics view.
     */
	
    protected void processImage() {
    	
		long startTime = System.currentTimeMillis();
		
		// TODO: add your processing code here
		int brightness = brightnessSlider.getValue();
		double contrast = contrastSlider.getValue();
		contrast = contrast/10;		
		
		for(int i = 0; i < imgView.pixels.length; i++){
			int argb = pixels[i];

			int r = (argb >> 16) & 0xff;
			int g = (argb >>  8) & 0xff;
			int b =  argb        & 0xff;
			
			int r0 = (int) (contrast * (r - 128) + 128 + brightness);
			int g0 = (int) (contrast * (g - 128) + 128 + brightness);
			int b0 = (int) (contrast * (b - 128) + 128 + brightness);

			if(r0 > 0 && r0 < 255){
				
				r = r0;
				
			}else if (r0 <= 0){
				r = 0;
			}else if(r0 >= 255){
				r = 255;
			}

			if(g0 > 0 && g0 < 255){
				
				g = g0;
				
			}else if (g0 <= 0){
				g = 0;
			}else if(g0 >= 255){
				g = 255;
			}

			if(b0 > 0 && b0 < 255){
				
				b = b0;
				
			}else if (b0 <= 0){
				b = 0;
			}else if(b0 >= 255){
				b = 255;
			}
			imgView.pixels[i] = (0xFF<<24) | (r<<16) | (g<<8) | b;
		}
		
		int[] greyPix =  imgView.makeGray();
		
		histo = new int[256];
    	for(int i = 0; i < greyPix.length; i++){
    		histo[greyPix[i]]++;
    	}
		
    	histoView.histogram = histo;
    	statsView.histogram = histo;
    	statsView.greyPix = greyPix;
    	
		imgView.applyChanges();
		histoView.update();
		statsView.update();
		
		// show processing time
		long time = System.currentTimeMillis() - startTime;
		statusLine.setText("Processing time = " + time + " ms.");
    }
    
 

}

