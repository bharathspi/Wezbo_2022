import javax.swing.*;
import java.awt.*; 
import java.awt.event.*;  
import java.sql.*;
public class Tag implements ActionListener
{
   private static String paths;
   static JFrame main_win;
   static JButton clear;
   static JButton ok;
   static JLabel text;
   static JLabel note;
   static JTextField tag;
   static JToolBar tools;
   static JPanel panel;
 
   public boolean isItEmpty(ResultSet rs)
   {
	   boolean ret =true;
	   try
	   {
	   ret= !rs.isBeforeFirst();
      }
	   catch(Exception e)
	   {
		   
	   }
	   return ret;
	   
   }

   public boolean addToDatabase(String path)
   {
         String tag_name = tag.getText();
         
         if(tag_name.isBlank() || tag_name.isEmpty())
         {
        	 JOptionPane.showMessageDialog(main_win, "Your tag is empty!",
                     "Create Tags", JOptionPane.WARNING_MESSAGE);
        	 return true;
         }
//         if(tag_name.contains(" "))
//         {
//        	 JOptionPane.showMessageDialog(main_win, "Tag cannot contain whitespaces!",
//                     "Create Tags", JOptionPane.WARNING_MESSAGE);
//        	 return true;
//         }
         if(tag_name.length()>100000)
         {
        	 JOptionPane.showMessageDialog(main_win, "Tag is too lengthy!",
                     "Create Tags", JOptionPane.WARNING_MESSAGE);
        	 return true;
         }
         try
         {
        	 Class.forName("com.mysql.cj.jdbc.Driver");
        	 Connection connection  = DriverManager.getConnection("jdbc:mysql://localhost:3306/file_tags","root","root");
        	 Statement state = connection.createStatement();
        	 String qq = "'"+tag_name+"'";
        	 ResultSet rs = state.executeQuery("select path from tags WHERE tag = "+qq);
        	 if(!isItEmpty(rs))
        	 {   //System.out.println("****************");
        	  String rec="";
        	   while(rs.next())
        		 rec = rs.getString("path");
//        	   System.out.println(rec);
        		 path = rec+"?"+path;
        		try { PreparedStatement ps = connection.prepareStatement("UPDATE tags set path=? where tag =?");
        		 ps.setString(1,path);
        		 ps.setString(2, tag_name);
        		 ps.executeUpdate();
        		 JOptionPane.showMessageDialog(main_win, "Successfully Tagged!",
                         "Create Tags", JOptionPane.INFORMATION_MESSAGE);
        		 ps.close();
        		 return false;
        		}
        		catch(Exception e)
        		{
        			return false;
        		}
        	 }
        	 PreparedStatement insert_query = connection.prepareStatement("INSERT INTO tags VALUES (?,?)");
        	 insert_query.setString(1,tag_name);
        	 insert_query.setString(2, path);
        	 
        	 insert_query.executeUpdate();
        	 JOptionPane.showMessageDialog(main_win, "Successfully Tagged!",
                     "Create Tags", JOptionPane.INFORMATION_MESSAGE);
        	 connection.close();

         }
         catch(Exception e)
         {
        	 System.out.println(e);
        	 JOptionPane.showMessageDialog(main_win, "Failed to Tag, Please try Again!",
                     "Create Tags", JOptionPane.WARNING_MESSAGE);
         }
         return false;
   }
   public void actionPerformed(ActionEvent e) {
            String s = e.getActionCommand();
            if(s.equals("Clear"))
            {
               tag.setText("");
            }
            if(s.equals("Ok"))
            {
            	boolean TorF = addToDatabase(paths);
            	if(!TorF)
            	    main_win.dispose();
            }
        }
  Tag(String path)
  {
	paths =path;
  	clear = new JButton("Clear");
  	ok = new JButton("Ok");
  	clear.addActionListener(this);
  	ok.addActionListener(this);
  	text = new JLabel("Assign tags to files selected in below text field");
//  	String note_msg="<html>Note: The tag cannot contains spaces <br/> and  special characters except under_score '_' </html>";
//  	    String note_msg = "The tag cannot contains white spaces";
  	String note_msg = "";
  	note = new JLabel(note_msg);
//  	 note.setUI(MultiLineLabelUI.labelUI);
  	note.setFont(new Font(note_msg, Font.BOLD, 14));
    note.setForeground(Color.RED);
  	tag = new JTextField( "Enter Tag");
  	tools = new JToolBar();
  	tools.add(tag);
  	tools.add(ok);
  	tools.add(clear);
  	tools.setFloatable(false);
  	main_win = new JFrame("Create Tags");
    panel =  new JPanel(new GridLayout(3,0));
    main_win.add(panel,BorderLayout.NORTH);	
    panel.add(text);
    panel.add(tools);
    panel.add(note);
  	    main_win.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		main_win.setSize(400,200);
		main_win.setVisible(true);
		main_win.setResizable(false);
  }


}