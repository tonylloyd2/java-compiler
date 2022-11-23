

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class SimpleCompiler
	extends JFrame   {

	public static final String APP_NAME = "Simple Compiler Version 1.0";

	
	protected String user_text_code;
	protected JTextArea m_editor;
	protected JTextArea errorMsg;
	
	

	protected JFileChooser m_chooser;
	protected File  m_currentFile;

	protected boolean m_textChanged = false;

	protected JToolBar m_toolBar;
        protected JComboBox m_cbFonts;
        String status;
   

	public SimpleCompiler() {
		super(APP_NAME+": Part IV - Custom Menu Components");
		setSize(500, 350);

	         	
        
		m_editor = new JTextArea(12,10);
		errorMsg =new JTextArea(4,10);
		JScrollPane pl = new JScrollPane(errorMsg);
		JScrollPane ps = new JScrollPane(m_editor);
	
		getContentPane().add(ps, BorderLayout.CENTER);
		getContentPane().add(pl, BorderLayout.SOUTH);


		JMenuBar menuBar = createMenuBar();
		setJMenuBar(menuBar);

		m_chooser = new JFileChooser();
		try {
			File dir = (new File(".")).getCanonicalFile();
			m_chooser.setCurrentDirectory(dir);
		} catch (IOException ex) {}

		
		newDocument();

		WindowListener wndCloser = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (!promptToSave())
					return;
				System.exit(0);
			}
		};
		addWindowListener(wndCloser);
	}

	protected JMenuBar createMenuBar() {
		final JMenuBar menuBar = new JMenuBar();

		JMenu mFile = new JMenu("File");
		mFile.setMnemonic('f');

		ImageIcon iconNew = new ImageIcon("icons/New.gif");
		Action actionNew = new AbstractAction("New", iconNew) {
			public void actionPerformed(ActionEvent e) {
				if (!promptToSave())
					return;
				newDocument();
			}
		};
		JMenuItem item = new JMenuItem(actionNew);
		item.setMnemonic('n');
		item.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mFile.add(item);

		ImageIcon iconOpen = new ImageIcon("icons/Open.gif");
		Action actionOpen = new AbstractAction("Open...", iconOpen) {
			public void actionPerformed(ActionEvent e) {
				if (!promptToSave())
					return;
				openDocument();
			}
		};
		item = new JMenuItem(actionOpen);
		item.setMnemonic('o');
		item.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mFile.add(item);

		ImageIcon iconSave = new ImageIcon("icons/Save.gif");
		Action actionSave = new AbstractAction("Save", iconSave) {
			public void actionPerformed(ActionEvent e) {
				if (!m_textChanged)
					return;
				saveFile(false);
			}
		};
		item = new JMenuItem(actionSave);
		item.setMnemonic('s');
		item.setAccelerator(KeyStroke.getKeyStroke(
			KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mFile.add(item);

		ImageIcon iconSaveAs = new ImageIcon("icons/saveall.gif");
		Action actionSaveAs = new AbstractAction("Save As...", iconSaveAs) {
			public void actionPerformed(ActionEvent e) {
				saveFile(true);
			}
		};
		item = new JMenuItem(actionSaveAs);
		item.setMnemonic('a');
		mFile.add(item);

		mFile.addSeparator();

		Action actionExit = new AbstractAction("Exit") {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
		item = new JMenuItem(actionExit);
		item.setMnemonic('x');
		mFile.add(item);
		menuBar.add(mFile);

             // Add Project Menu
		JMenu mOpt = new JMenu("Project");
		mOpt.setMnemonic('p');

    ImageIcon iconCompile = new ImageIcon("icons/compile.gif");
		Action actionCompile = new AbstractAction("Compile File...", iconCompile) {
			public void actionPerformed(ActionEvent e) {
				// status="ok";
				// // pile();\
				user_text_code = m_editor.getText();
				if(user_text_code.isBlank() ){//check if file contains code
                    errorMsg.setText("cannot compile an empty file !!!");
				}
				else{//file has some code in it proceeding to compile
					errorMsg.setText("compiling...");
					try {
						Scanner filereader = new Scanner(new FileReader("run.java"));
						// while(filereader.hasNextLine() == true) {
						// 	System.out.println(filereader.nextLine());		  
						//   }
						PrintWriter write = new PrintWriter("run.java");
						write.append(user_text_code);
						write.close();
						filereader.close();	

						String[] command = {
							"cmd.exe",
							 "/C",
							 "Start",
							 "run.bat"
							};
                        Process p =  Runtime.getRuntime().exec(command); 
						// System.out.println(p.getText());
						//proceed to execute the batch file
						errorMsg.append("file compiled successfully..");
					} catch (Exception file_exception_error) {
						file_exception_error.printStackTrace();
					}
					
					
				}


				
			}
		};
           
                 item = new JMenuItem(actionCompile);
		item.setMnemonic('C');
		mOpt.add(item);
		menuBar.add(mOpt);


  ImageIcon iconPrint = new ImageIcon("icons/print.gif");
		Action actionPrint = new AbstractAction("Print...", iconPrint) {
			public void actionPerformed(ActionEvent e) {
				
				Print.printComponent((m_editor));
			}
		};
           
                 item = new JMenuItem(actionPrint);
		item.setMnemonic('P');
		

		mOpt.add(item);



            // Help Project Menu
                JMenu mHelp = new JMenu("Help");
		mFile.setMnemonic('H');


ImageIcon iconAbout = new ImageIcon("icons/help.gif");
		Action actionAbout = new AbstractAction("About ?", iconAbout) {
			public void actionPerformed(ActionEvent e) {
				
				JOptionPane.showMessageDialog(null, new About(),"About Simple Compiler",JOptionPane.PLAIN_MESSAGE);
			}
		};
           
                 item = new JMenuItem(actionAbout);
		 item.setMnemonic('A');

                mHelp.add(item);
		menuBar.add(mHelp);

		// Create toolbar
		m_toolBar = new JToolBar("Commands");
		JButton bNew = new SmallButton(actionNew,
			"New File");
		m_toolBar.add(bNew);
		JButton bOpen = new SmallButton(actionOpen,
			"Open File");
		m_toolBar.add(bOpen);
		JButton bSave = new SmallButton(actionSave,
			"Save file");
		m_toolBar.add(bSave);

		m_toolBar.addSeparator();
 		
		JButton bPrint=new SmallButton(actionPrint,"Print File");
		m_toolBar.add(bPrint);

		JButton bCompile = new SmallButton(actionCompile,"Compile program");
		m_toolBar.add(bCompile);

		m_toolBar.addSeparator();

		JButton bAbout = new SmallButton(actionAbout,"About Compiler");
		m_toolBar.add(bAbout);


		getContentPane().add(m_toolBar, BorderLayout.NORTH);

		
		

		
		
		

		return menuBar;
	}

	protected String getDocumentName() {
		return m_currentFile==null ? "Untitled" :
			m_currentFile.getName();
	}

	protected void newDocument() {
		m_editor.setText("");
		m_currentFile = null;
		setTitle(APP_NAME+" ["+getDocumentName()+"]");
		m_textChanged = false;
		m_editor.getDocument().addDocumentListener(new UpdateListener());
	}

	protected void openDocument() {
		if (m_chooser.showOpenDialog(SimpleCompiler.this) !=
			JFileChooser.APPROVE_OPTION)
			return;
		File f = m_chooser.getSelectedFile();
		if (f == null || !f.isFile())
			return;
		m_currentFile = f;
		try {
			FileReader in = new FileReader(m_currentFile);
			m_editor.read(in, null);
			in.close();
			setTitle(APP_NAME+" ["+getDocumentName()+"]");
		}
		catch (IOException ex) {
			showError(ex, "Error reading file "+m_currentFile);
		}
		m_textChanged = false;
		m_editor.getDocument().addDocumentListener(new UpdateListener());
	}

	protected boolean saveFile(boolean saveAs) {
		if (saveAs || m_currentFile == null) {
			if (m_chooser.showSaveDialog(SimpleCompiler.this) !=
				JFileChooser.APPROVE_OPTION)
				return false;
			File f = m_chooser.getSelectedFile();
			if (f == null)
				return false;
			m_currentFile = f;
			setTitle(APP_NAME+" ["+getDocumentName()+"]");
		}

		try {
			FileWriter out = new
				FileWriter(m_currentFile);
			m_editor.write(out);
			out.close();
		}
		catch (IOException ex) {
			showError(ex, "Error saving file "+m_currentFile);
			return false;
		}
		m_textChanged = false;
		return true;
	}

	protected boolean promptToSave() {
		if (!m_textChanged)
			return true;
		int result = JOptionPane.showConfirmDialog(this,
			"Save changes to "+getDocumentName()+"?",
			APP_NAME, JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.INFORMATION_MESSAGE);
		switch (result) {
		case JOptionPane.YES_OPTION:
			if (!saveFile(false))
				return false;
			return true;
		case JOptionPane.NO_OPTION:
			return true;
		case JOptionPane.CANCEL_OPTION:
			return false;
		}
		return true;
	}

	

	public void showError(Exception ex, String message) {
		ex.printStackTrace();
		JOptionPane.showMessageDialog(this,
			message, APP_NAME,
			JOptionPane.WARNING_MESSAGE);
	}

	public static void main(String argv[]) {
		SimpleCompiler frame = new SimpleCompiler();
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
	}
class SmallButton extends JButton implements MouseListener {
	protected Border m_raised = new SoftBevelBorder(BevelBorder.RAISED);
	protected Border m_lowered = new SoftBevelBorder(BevelBorder.LOWERED);
	protected Border m_inactive = new EmptyBorder(3, 3, 3, 3);
	protected Border m_border = m_inactive;
	protected Insets m_ins = new Insets(4,4,4,4);

	public SmallButton(Action act, String tip) {
		super((Icon)act.getValue(Action.SMALL_ICON));
		setBorder(m_inactive);
		setMargin(m_ins);
		setToolTipText(tip);
		setRequestFocusEnabled(false);
		addActionListener(act);
		addMouseListener(this);
	}

	public float getAlignmentY() {
		return 0.5f;
	}

	public Border getBorder() {
		return m_border;
	}

	public Insets getInsets() {
		return m_ins;
	}

	public void mousePressed(MouseEvent e) {
		m_border = m_lowered;
		setBorder(m_lowered);
	}

	public void mouseReleased(MouseEvent e) {
		m_border = m_inactive;
		setBorder(m_inactive);
	}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {
		m_border = m_raised;
		setBorder(m_raised);
	}

	public void mouseExited(MouseEvent e) {
		m_border = m_inactive;
		setBorder(m_inactive);
	}
}
	class UpdateListener implements DocumentListener {

		public void insertUpdate(DocumentEvent e) {
			m_textChanged = true;
		}

		public void removeUpdate(DocumentEvent e) {
			m_textChanged = true;
		}

		public void changedUpdate(DocumentEvent e) {
			m_textChanged = true;
		}
	}


    public void pile( )
       {
		errorMsg.setText("");
        	StringTokenizer st=new StringTokenizer(m_editor.getText());
	 	String akhir=new String();
	 
         	errorMsg.append("Compiling 1 file.....\n");


		String mula=new String(st.nextToken());
  
       		if(mula.equals("Start"))
  		 {
 
             	   while (st.hasMoreTokens())
		     {
		      akhir=st.nextToken();
                     }
            	 if(akhir.equals("End"))
		     DecState(m_editor.getText());
	    	 else
		   errorMsg.append("ERROR Missing End statement\n");
                 }             
     		else
	          errorMsg.append("ERROR Missing Start statement\n");


       }  
  
  public void DecState(String stat)
	{
	   String pencam;
           StringTokenizer st=new StringTokenizer(stat);
	   st.nextToken();
	   pencam=st.nextToken();
	
	   if (("Set").equals(pencam))
	     {
		pencam=st.nextToken();
	        if (Character.isLetter(pencam.charAt(0)))
		  {
		     checkPencam(pencam);
                     if(status.equals("ok"))
		       {
	                 pencam=st.nextToken();

		         if (("As").equals(pencam))
			    {
                              pencam=st.nextToken();

       			      if( (("int").equals(pencam))|| (("char").equals(pencam)) ||(("double").equals(pencam)) || (("String").equals(pencam)) )
				{
				  st.nextToken();
				  if(st.hasMoreTokens())
				    errorMsg.append("\nERROR : Syntax Error --> Set VariableName As DataType");

				  else
				   errorMsg.append("\nCompiler Successful");
				}
			      else
			        errorMsg.append("ERROR "+pencam+" Not a valid datatype");
			    }
			 else
	                   errorMsg.append("ERROR "+pencam + " Not reconized");
		       }
		     else; 	
		   }
	       
	        else
	          errorMsg.append("ERROR "+pencam + " Illegal start of variable name!!");
	       } 

	   else
	     errorMsg.append("ERROR "+pencam + " Not reconized");
	}
  
   public String checkPencam(String s)
        {
	  int i;

	  for(i=1;i<s.length();i++)
             {
              if((Character.isLetterOrDigit(s.charAt(i))) || (s.charAt(i)=='_') );

	      else
		{
		  errorMsg.append("\nERROR "+ s +" - Illegal character ("+ s.charAt(i)+") in variable name");
		  status="error";
	     	}
	     }
        return status;}

}