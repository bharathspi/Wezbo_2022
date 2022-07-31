import javax.swing.*;
import java.awt.*; 
import java.awt.event.*;  
import java.sql.*;
public class TagChange implements ActionListener{
	   static JFrame main;
	   static JButton delete;
	   static JButton clear;
	   static JLabel text;
	   static JLabel note;
	   static JTextField given_tag;
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
			  
			  System.out.println(e);
		   }
		   return ret;
		   
	   }
	   
	   public boolean handle()
	   {
		   String tag_sup = given_tag.getText();
		   if(tag_sup.isEmpty()||tag_sup.isBlank())
		   {
			   JOptionPane.showMessageDialog(main, "Your tag is empty!",
	                     "Create Tags", JOptionPane.WARNING_MESSAGE);
			   return false;
		   }
		   return true;
		   
	   }

public void Delete()
	   {
		   if(handle())
		   {
			   try
		         {
		        	 Class.forName("com.mysql.cj.jdbc.Driver");
		        	 Connection connection  = DriverManager.getConnection("jdbc:mysql://localhost:3306/file_tags","root","root");
		        	 Statement state = connection.createStatement();
		        	 String qq = "'"+given_tag.getText()+"'";
		        	 ResultSet rs = state.executeQuery("select tag from tags WHERE tag = "+qq);
		        	
		        	 if(!isItEmpty(rs))
		        	 {   
		        		state.close();
		        		try { PreparedStatement ps = connection.prepareStatement("DELETE FROM tags WHERE tag ="+qq);
//		        		 ps.setString(1,qq);
		        		 ps.executeUpdate();
		        		 JOptionPane.showMessageDialog(main, "Successfully Deleted!",
		                         "Create Tags", JOptionPane.INFORMATION_MESSAGE);
		        		 ps.close();
		        		 return;
		        		}
		        		catch(Exception e)
		        		{
		        			JOptionPane.showMessageDialog(main, "Failed to Delete Please try Again",
				                     "Delete Tags", JOptionPane.WARNING_MESSAGE);
		        		}
		        	 }
		        	 else         
		        	 {
		        		 JOptionPane.showMessageDialog(main, "There is no file with given tag!",
			                     "Delete Tags", JOptionPane.WARNING_MESSAGE);
		        	 }
		   }
			   
			catch(Exception e)
			   {
				System.out.println(e);
				JOptionPane.showMessageDialog(main, "Connection Failed Please try Again!",
	                     "Delete Tags", JOptionPane.WARNING_MESSAGE);
			   }
		   
		  
	   }

	   }
	   public void actionPerformed(ActionEvent e) {
	            String s = e.getActionCommand();
	            if(s.equals("Clear"))
	            {
	            	given_tag.setText("");
	            }
	            if(s.equals("Delete"))
	            {
	            	Delete();
	            }
	            }
	 
	  TagChange()
	  {
		
	  	delete = new JButton("Delete");
	  	clear= new JButton("Clear");
	  	delete.addActionListener(this);
	  	clear.addActionListener(this);
	  	text = new JLabel("Delete Entire Tag");
	  	String note_msg="<html>Note:If you delete the tag  Entire Files associations<br/> with that tag will be deleted </html>";
	  	note = new JLabel(note_msg);
	  	note.setFont(new Font(note_msg, Font.BOLD, 14));
	    note.setForeground(Color.RED);
	  	given_tag = new JTextField( "Enter Tag");
	  	tools = new JToolBar();
	  	tools.add(given_tag);
	  	tools.add(delete);
	  	tools.add(clear);
	  	tools.setFloatable(false);
	  	main = new JFrame("DeleteTags");
	    panel =  new JPanel(new GridLayout(3,0));
	    main.add(panel,BorderLayout.NORTH);	
	    panel.add(text);
	    panel.add(tools);
	    panel.add(note);
	  	    main.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
			main.setSize(400,200);
			main.setVisible(true);
			main.setResizable(false);
	  }
}
