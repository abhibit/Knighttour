package knighttour;
import java.awt.*;
import java.awt.event.*;

import static java.lang.Thread.sleep;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.TitledBorder;
// Class KnightTour contains main method.
class KnightTour extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
                    private static int[][] theBoard;                //represents the chess board
                    static int boardLength = 0;                     //stores the board length (rows)
                    static int boardWidth = 0;                      //stores the board width (columns)
                    static int[][] buttonCenters;                   //stores the center of the buttons
                    //boolean variable to determine whether the board is painted with lines
                    static boolean boardPainted = false;           
                    //GUI elements
                    private JButton[][] p = new JButton[400][400]; 
                    private JButton clear = new JButton("Clear");
                    private JTextArea field = new JTextArea(15,50);
                    private JButton trace = new JButton("Trace Path");
                    private JPanel bigPanel = new JPanel();
                    private JButton compute = new JButton("Start");
                    private JTextField rowField = new JTextField(3);
                    private JTextField colField = new JTextField(3);
                    private JLabel rowLabel = new JLabel("Enter Row No. : ");
                    private JLabel colLabel = new JLabel("Enter Col No. : ");
                  //KnightTourC constructor to create the GUI
                    public KnightTour(int a, int b) {
                                            boardLength = a;
                                            boardWidth = b;
                                            theBoard = new int[boardLength][boardWidth];                                            
                                            buttonCenters = new int[boardLength*boardWidth][2];
                                            field.setEditable(false);
                                            field.setBorder(new TitledBorder("Output"));
                                            field.setForeground(Color.BLUE);
                                            bigPanel.setLayout(new GridLayout(boardLength,boardWidth));
                                            clear.setBackground(Color.black);
                                            clear.setForeground(Color.yellow);
                                            trace.setBackground(Color.black);
                                            trace.setForeground(Color.yellow);
                                            compute.setBackground(Color.black);
                                            compute.setForeground(Color.yellow);
                                            setTitle("Chess Board");
                                            
                                            //adding buttons to the frame and setting their color
                                            for(int i=0; i<boardLength; i++) {
                                                for(int j=0; j<boardWidth; j++) {
                                                    JButton temp = new JButton();
                                                    temp.setPreferredSize(new Dimension(50,50));
                                                    temp.setVisible(true);
                                                    temp.setLayout(new FlowLayout(FlowLayout.CENTER));
                                                    if((i+j) % 2 == 0)
                                                        temp.setBackground(Color.WHITE);
                                                    else
                                                        temp.setBackground(Color.black);
                                                    p[i][j] = temp;
                                                    p[i][j].setForeground(Color.RED);
                                                    bigPanel.add(p[i][j]);                
                                                }
                                            }                                            
                                            add(bigPanel);
                                            
                                            //Adding the text area to a scroll panel to enable a vertical scrolling bar
                                            //when needed
                                            JScrollPane scroll = new JScrollPane(field);
                                            scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                                            add(scroll); 
                                            JPanel tempPanel = new JPanel();
                                            tempPanel.add(rowLabel);
                                            tempPanel.add(rowField);
                                            tempPanel.add(colLabel);
                                            tempPanel.add(colField);
                                            tempPanel.add(compute);
                                            tempPanel.add(clear);
                                            tempPanel.add(trace);
                                            tempPanel.setLayout(new GridLayout(4,2));
                                            add(tempPanel);
                                            setLayout(new FlowLayout(FlowLayout.LEFT));
                                            setVisible(true);
                                            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                            
                                            //add mouse listeners on the buttons
                                            for(int i=0; i<boardLength; i++) {
                                                for(int j=0; j<boardWidth; j++) {
                                                    p[i][j].addMouseListener(this);
                                                    p[i][j].addMouseMotionListener(this);
                                                }
                                            }
                                            trace.setEnabled(false);
                                            
                                            //adding Listeners to the buttons, panels
                                            trace.addActionListener(this);
                                            clear.addActionListener(this);
                                            bigPanel.addMouseMotionListener(this);
                                            bigPanel.addMouseListener(this);
                                            compute.addActionListener(this);
                                            
                                            //enable text wrapping on the text area
                                            field.setLineWrap(true);
                                            
                                            //pack the frame
                                            pack();
                    }
                      //main function
                    public static void main (String[] str) {
                                    
                                    JFrame frame = new KnightTour(8, 8);
                    }     
 // Move pattern on basis of the change of
 // x coordinates and y coordinates respectively                  
static int cx[] = {1,1,2,2,-1,-1,-2,-2};
static int cy[] = {2,-2,1,-1,2,-2,1,-1};//
static int ax,ay,N=8;
//function restricts the knight to remain within
//the 8x8 Chessboard.
boolean limits(int x, int y)
{
	return ((x >= 0 && y >= 0) && (x < N && y < N));
}
/* Checks whether a square is valid and empty or not */
boolean isempty(int a[], int x, int y)
{
	return (limits(x, y)) && (a[y*N+x] < 0);
}
/* Returns the number of empty squares adjacent
   to (x, y) */
int getDegree(int a[], int x, int y)
{
	int count = 0;
	for (int i = 0; i < N; ++i)
		if (isempty(a, (x + cx[i]), (y + cy[i])))
			count++;

	return count;
}
//Picks next point using Warnsdorff's heuristic.
//Returns false if it is not possible to pick
//next point.
boolean nextMove(int a[])
{
	int min_deg_idx = -1, c, min_deg = (N+1), nx, ny;
	//Try all N adjacent of (*x, *y) starting
	//from a random adjacent. Find the adjacent
	//with minimum degree.
        Random rn=new Random();
	int start = (rn.nextInt(10)+1)%N;
	for (int count = 0; count < N; ++count)
	{
		int i = (start + count)%N;
		nx = ax + cx[i];
		ny = ay + cy[i];
		if ((isempty(a, nx, ny)) &&
		(c = getDegree(a, nx, ny)) < min_deg)
		{
			min_deg_idx = i;
			min_deg = c;
		}
	}
	// IF we could not find a next cell
	if (min_deg_idx == -1)
		return false;
  // Store coordinates of next point
	nx = ax + cx[min_deg_idx];
	ny = ay + cy[min_deg_idx];
	// Mark next move
	a[ny*N + nx] = a[(ay)*N + (ax)]+1;
	// Update next point
	ax = nx;
	ay = ny;

	return true;
}
/* Enter values to theBoard */

void print(int a[])
{
    int k=0;
	for (int i = 0; i < N; ++i)
	{
		for (int j = 0; j < N; ++j){
			 theBoard[i][j]=a[k++];
                       
                }
	}
}
/* checks its neighbouring sqaures */
/* If the knight ends on a square that is one
   knight's move from the beginning square,
   then tour is closed */
boolean neighbour(int x, int y, int xx, int yy)
{
	for (int i = 0; i < N; ++i)
		if (((x+cx[i]) == xx)&&((y + cy[i]) == yy))
			return true;

	return false;
}
/* Generates the legal moves using warnsdorff's
  heuristics. Returns false if not possible */
boolean findClosedTour(int bx,int by)
{

	int a[]=new int[8*8];
	// Filling up the chess board matrix with -1's
	for (int i = 0; i< N*N; ++i)
		a[i] = -1;
	int sx = by;
	int sy = bx;
	int x = sx, y = sy;
	a[y*N+x] = 1;
	ax=x;ay=y;
	for (int i = 0; i < N*N-1; ++i)
		if (nextMove(a) == false)
			return false;
			x=ax;
			y=ay;
			// Check if tour is closed (Can end
			// at starting point)
	if (!neighbour(x, y, sx, sy))
		return false;

	print(a);
	return true;
}

void  run(int bx,int by)
{
     theBoard = new int[boardLength][boardWidth];

// While we don't get a solution
   while (!findClosedTour(bx,by))
	{
	;
	}
    printBoardOnButtons();
    
}
 
  public void printBoardOnButtons() {
   //Function to print the board values on the corresponding buttons
      for (int i=0; i < boardLength ; i++){
            for (int j=0; j < boardWidth ; j++){
               p[i][j].setText(String.valueOf(theBoard[i][j]));
                                          }                                                
                                  }
                    }
                 
                    public void calculateTrace() {
                                            //Function to calculate the trace of the knight tour
                                            //and calculate the button centers of the corresponding trace at the same time
                                            int check = 1;
                                            String trace = "\n\nTrace : \n";         
                                            while(true) {
                                                for(int i=0; i<boardLength; i++) {
                                                    for(int j=0; j<boardWidth; j++) {
                                                        if(theBoard[i][j] == check) {
                                                            
                                                            //Calculating the button centers
                                                            buttonCenters[check-1][0] = j*50 + 36;
                                                            buttonCenters[check-1][1] = i*50 + 62;
                                                            
                                                            //Adding trace
                                                            trace += "(" + (i+1) + "," + (j+1) + ")";
                                                            if(check < (boardLength) * (boardWidth))
                                                                trace += " --> ";
                                                            check++;
                                                        }
                                                    }
                                                }
                                                if(check > boardWidth * boardLength)
                                                break;
                                            }
                                            String temp = field.getText();
                                            temp += trace;
                                            
                                            //The final trace is displayed in the text area
                                            field.setText(temp);
                                            
                                            //Once the button centers have been calculated, the lines are drawn
                                            drawLines();                                            
                    }                    
                    public void drawLines() { 
                        //Function to draw the lines on the Board in the order of the move number 1->2->3...
                        Graphics g = getGraphics();                        
                        for(int i=0; i<boardWidth*boardLength-1; i++) {
                            
                            //Induce delay in between drawing each line
                            if(!boardPainted) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                Logger.getLogger(KnightTour.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            
                            //Draw red circles at the starting and final position
                            if(i == 0)
                            {   g.setColor(Color.red);
                                g.drawOval(buttonCenters[i][0] - 14, buttonCenters[i][1] - 14, 30, 30);
                            }
                            if(i == boardWidth*boardLength - 2) {
                                   g.setColor(Color.red);
                                g.drawOval(buttonCenters[i+1][0] - 14, buttonCenters[i+1][1] - 14, 30, 30);
                            }
                            
                            //Draw green lines
                            g.setColor(Color.GREEN);
                            g.drawLine(buttonCenters[i][0], buttonCenters[i][1], buttonCenters[i+1][0], buttonCenters[i+1][1]);
                        }
                    }
                    
                    public void disableClick() {
                        //Function to disable the buttons in the JFrame
                        for(int i=0; i<boardWidth; i++) {
                            for(int j=0; j<boardLength; j++) {
                                p[i][j].setEnabled(false);
                            }
                        }
                    }
                    
                    
                    private void reset() {
                        //Function to reset the board and the text area and drawn lines
                        for (int r=0; r<boardLength; r++) {
                            for (int c=0; c<boardWidth; c++) {
                                p[r][c].setText(""); 
                                theBoard[r][c] = -1;
                            }
                        }
                        
                        //Reset all text areas and fields, reset the board, diable trace button
                        boardPainted = false;
                        field.setText("");
                        trace.setEnabled(false);
                        rowField.setText("");
                        colField.setText("");
                    }
                    
                   
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //Overridden actionPerformed for the 2 buttons
                        //clicking trace prints the trace in text area and draws lines
                        if(e.getSource() == trace) {
                            calculateTrace();
                            boardPainted = true;
                        }
                        else if(e.getSource() == clear) {
                            reset();
                        }
                        else if(e.getSource() == compute) {
                            //Event listener to compute KnightTour on input from text field
                            try {
                                int x = Integer.parseInt(rowField.getText());
                                int y = Integer.parseInt(colField.getText());
                            
                                //Determine is source input is valid
                                if(x>0 && y>0 && x<=boardLength && y<=boardWidth) {                               
                                    reset();
                                    trace.setEnabled(true);
                                    field.setText("Board : " + boardLength + "x" + boardWidth + "\n\nSource : (" + (y) + "," + (x) + ")");
                                    rowField.setText(String.valueOf(x));
                                    colField.setText(String.valueOf(y));
                                    run(x-1, y-1);      
                                }
                                else {
                                    JOptionPane.showMessageDialog(null, "Invalid source!");
                                }
                            } catch (NumberFormatException exp) { JOptionPane.showMessageDialog(null, "Input not in correct format");}
                              
                        }
                    }
                    
                   
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        //MouseListener to allow the user to pass the starting location by clicking on
                        //the desired button
                        int[] position = new int[2];
                        for(int i=0; i<boardLength; i++) {
                            for(int j=0; j<boardWidth; j++) {
                                if(e.getSource() == p[i][j]) {   
                                    reset();
                                    position[0] = i;
                                    position[1] = j;    
                                    trace.setEnabled(true);
                                    field.setText("Board : " + boardLength + "x" + boardWidth + "\n\nSource : (" + (position[0]+1) + "," + (position[1]+1) + ")");
                                    run(i,j);                    
                                }
                            }
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
    
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
  
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                            
                    }   

                    @Override
                    public void mouseExited(MouseEvent e) {
                            
                    }

                    @Override
                    public void mouseDragged(MouseEvent e) {
     
                    }
                    
                    
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        //mouseMoved overriden to redraw lines if mouse moved over the buttons
                        for(int i=0; i<boardLength; i++) {
                            for(int j=0; j<boardWidth; j++) {
                                if(e.getSource() == p[i][j]) {
                                    if(boardPainted)   {    
                                        drawLines();
                                    }
                                }
                            }
                        }
                    }   
}
    
  
 
 


   