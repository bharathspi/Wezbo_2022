import javax.swing.*;
import java.awt.*; 
import java.awt.event.*;  
import java.util.*;
import java.io.*;
import java.sql.*;
public class Main implements ActionListener
{
  static JFrame main_frame;// Home page of the application
  static JButton select_files;//button to select files
  static JButton select_folders;//button to select folders
  static JButton search;//button to search for tags
  static JTextField query;//Textfield to enter keywords
  static JButton clear;//button to clear content on text field
  static JToolBar first;//tool bar added at the top of the main_frame
  static JButton delete_tags;
  static JPanel panel;
  static Tag give_tags;
  static Set<String>path_set;
  static JScrollPane scrollPane;
  static JScrollPane jsp1;
  static JButton rename_tags;

// to print the contents of arraylist which is the paths of selected files and folders
static void print(ArrayList<String> arr) 
{
	for(int i=0;i<arr.size();i++)
	{
		System.out.println(arr.get(i));
	}
}

//Function to choose folders
public ArrayList<String> chooseFolders()
{
	
    ArrayList<String> folder_paths = new ArrayList<String>();
	JFileChooser folderChooser = new JFileChooser();
    folderChooser.setDialogTitle("Choose Folders");
    folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//Make sure that it will only select directories
    folderChooser.setAcceptAllFileFilterUsed(false);
    folderChooser.setMultiSelectionEnabled(true);//to select multiple folders
    int option = folderChooser.showOpenDialog(main_frame);
    String paths = "";
    if(option == JFileChooser.APPROVE_OPTION){
      File[] files = folderChooser.getSelectedFiles();
               for(File file: files){
            	   String temp = file.getAbsolutePath();
                  folder_paths.add(temp);
                  paths = paths+"?"+temp;
                    }
               give_tags = new Tag(paths);
               
  }
//  else
//  	    System.out.println("Nothing is selected");
//       print(folder_paths);
       
       return folder_paths;  
 }
//Function to select the files
public ArrayList<String> chooseFiles()
{
 ArrayList<String> file_paths = new ArrayList<String>();
  JFileChooser fileChooser = new JFileChooser();
  fileChooser.setDialogTitle("Choose Files");
  fileChooser.setMultiSelectionEnabled(true);//to select multiple files
  int option = fileChooser.showOpenDialog(main_frame);
  String paths = "";
  if(option == JFileChooser.APPROVE_OPTION){
       File[] files = fileChooser.getSelectedFiles();
               for(File file: files){
            	   String temp = file.getAbsolutePath();
                   file_paths.add(temp);
                   paths = paths+"?"+temp;
                 }
               give_tags = new Tag(paths);
      }
//  else
//  {
//  	System.out.println("Nothing is selected");
//  }
//  print(file_paths);
  
 return file_paths;
}


//Function to clear textfield i.e query field
public void  clear_query_field()
{      
	// System.out.println("Clear button is clicked");
	if(query==null)
		System.out.println("Query still no initialized");
	else
		query.setText("");
}

//Function to trigger some actions based on the button that is clicked
 public void actionPerformed(ActionEvent e) {
            String s = e.getActionCommand();
            if(s.equals("Select and Tag Files"))
            {
                chooseFiles();
            }
            else if(s.equals("Select and Tag Folders"))
             {
             		chooseFolders();
             }
            else if(s.equals("Clear"))
            {
                 clear_query_field();
            }  
            else if(s.equals("Search"))
            {
            	if(panel!=null)
          		     panel.removeAll();
            	if(jsp1!=null)
             	{
          		    jsp1.removeAll();
          		    main_frame.remove(jsp1);
          	    }
            	if(panel!=null)
            		panel.repaint();
            	if(jsp1!=null)
            		jsp1.repaint();
          	    main_frame.repaint();
            	
            	
            	if(query.getText().isEmpty()||query.getText().isBlank())
           	  {
//           		 System.out.println("Empty##$#$#$#$#$#$#$#$#$#$#");
           		 JOptionPane.showMessageDialog(main_frame, "Search box is Empty",
                            "Search", JOptionPane.WARNING_MESSAGE);
           		
           	  }
            	else {
//            	if(panel!=null)
//            		  panel.removeAll();
//            	if(jsp1!=null)
//            	{
//            		jsp1.removeAll();
//            		main_frame.remove(jsp1);
//            	}
            		 
            	searchResults();
            	}
            }   
            else if(path_set!=null&&path_set.size()>0&&path_set.contains(s))
            {
            	
            File file = new File (s);
//            System.out.println(s+" from action listener");
            Desktop desktop = Desktop.getDesktop();
            try
            {
            	desktop.open(file);
            }
            catch(Exception ex)
            {
            	JOptionPane.showMessageDialog(main_frame, "File Not Found!!!",
                        "File Locator", JOptionPane.WARNING_MESSAGE);
            }
            }
            else if(s.equals("Delete_tags"))
            {
            	new TagChange();
            }
            else if(s.equals("Rename_tags"))
            {
            	new UpdateTag();
            }
        }

//Function to initialize entire GUI
public void initializeUI()
{
	main_frame = new JFrame("File Tagger");

    select_files=new JButton("Select and Tag Files");
    select_folders=new JButton("Select and Tag Folders");
    clear = new JButton("Clear");
    search = new JButton("Search");
    delete_tags = new JButton("Delete_tags");
    rename_tags = new JButton("Rename_tags");
    rename_tags.addActionListener(new Main());
    delete_tags.addActionListener(new Main());
    select_files.addActionListener(new Main());
    select_folders.addActionListener(new Main());
    clear.addActionListener(new Main());
    search.addActionListener(new Main());

    
    query = new JTextField("Enter your query");
    
    first=new JToolBar();


        first.add(select_files);
        first.add(select_folders);
        first.add(query);
        first.add(search);
        first.add(clear);  
        first.add(delete_tags);
        first.add(rename_tags);
        first.setFloatable(false);
        main_frame.add(first, BorderLayout.NORTH); 
        // main_frame.add(panel,BorderLayout.CENTER);	
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		main_frame.setSize(800,600);
		main_frame.setVisible(true);
		main_frame.setResizable(false);

}



public void searchResults()
{
	
	ArrayList<String> paths_from_db = new ArrayList<String>();
	 String search_text = query.getText();
//	 System.out.println(search_text+">>>>>>>>>>>>>>>>>>");
	 if(search_text.isEmpty()||search_text.isBlank())
	 {
//		 System.out.println("Empty##$#$#$#$#$#$#$#$#$#$#");
		 JOptionPane.showMessageDialog(main_frame, "Search box is Empty",
                 "Search", JOptionPane.WARNING_MESSAGE);
//		 if(panel!=null)
//		 {
//			 System.out.println("panel is not null");
//			 panel.removeAll();
//			 System.out.println("panel is not null2");
//			 panel.revalidate();
////			 if(jsp1!=null)
////				 jsp1.removeAll();
////			 main_frame.remove(jsp1);
//			 main_frame.revalidate();
//		 }
			 
		return;
	 }
	 try
     {
    	 Class.forName("com.mysql.cj.jdbc.Driver");
    	 Connection connection  = DriverManager.getConnection("jdbc:mysql://localhost:3306/file_tags","root","root");
    	 PreparedStatement fetch = connection.prepareStatement("SELECT path FROM tags WHERE tag=?");
    	 fetch.setString(1,search_text);    	 
    	 ResultSet fetched_paths = fetch.executeQuery();
    	 if (!fetched_paths.isBeforeFirst() ) {    //To check if we got empty resultSet
    		 JOptionPane.showMessageDialog(main_frame, "No Records Found!!!",
                     "Fetch Records", JOptionPane.WARNING_MESSAGE);
    		 return;
    		} 

    	 while(fetched_paths.next())
    		paths_from_db.add( fetched_paths.getString("path"));

     }
     catch(Exception e)
     {
    	 System.out.println(e);
    	 JOptionPane.showMessageDialog(main_frame, "Failed to fetch, Please try Again!",
                 "Create Tags", JOptionPane.WARNING_MESSAGE);
     }
//	 System.out.println(paths_from_db.get(0));
//	 System.out.println(paths_from_db.size()+"*******************");
     String[] fetched_array_paths = paths_from_db.get(0).split("\\?",0);
//	 System.out.println(fetched_array_paths.length);
	 path_set = new HashSet<String>();
	 for(int i=0;i<fetched_array_paths.length;i++)
	 {
		  path_set.add(fetched_array_paths[i]);
//		 System.out.println(fetched_array_paths[i]);
	 }
	 int no_of_records_fetched = path_set.size();
     panel =  new JPanel(new GridLayout(no_of_records_fetched,0));

	 int v=ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
	 int h=ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
	  jsp1 = new JScrollPane(panel,v,h);	 
     main_frame.add(jsp1, BorderLayout.CENTER);
     main_frame.revalidate();

//  main_frame.add(panel,BorderLayout.CENTER);
  JButton [] file_buttons = new JButton[no_of_records_fetched];
  //System.out.println(fetched_array_paths[1]+"******");
  int k=0;
  Set<String> check = new HashSet<String>();
  for(int i=0;i<fetched_array_paths.length;i++)
  {
      if(fetched_array_paths[i].length()>2)//just to check if it is not a empty space
      {
    	  String pt =  fetched_array_paths[i];
    	  if(!check.contains(pt))
    	  {
    	  file_buttons[k] = new JButton(pt);
    	  file_buttons[k].addActionListener(new Main());
    	  file_buttons[k].setPreferredSize(new Dimension(800, 40));

    	  panel.add(file_buttons[k]);
    	  k++;
    	  }
    	  check.add(pt);
      }
  }
  panel.revalidate();

}

public static void main(String[] args) {
		try {
            
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//To get look and feel of platform we are using
           } 
    catch (Exception e) {
         System.out.println(e);
       }
     Main main = new Main();
      main.initializeUI();
	}
}