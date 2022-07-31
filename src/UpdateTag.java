import javax.swing.*;
import java.awt.*; 
import java.awt.event.*;  
import java.sql.*;
public class UpdateTag implements ActionListener{
	   static JFrame main_frame;
	   static JButton rename;
	   static JButton clear;
	   static JLabel text;
	   static JLabel note;
	   static JTextField source;
	   static JTextField dest;
	   static JToolBar tools;
	   static JPanel panel;
	   
	   public void actionPerformed(ActionEvent e) {
           String s = e.getActionCommand();
           if(s.equals("Clear"))
           {
        	   source.setText("");
        	   dest.setText("");
           }
           else if(s.equals("Rename"))
           {
        	   String sou = source.getText();
        	   String des = dest.getText();
        	   if(sou.isBlank()||sou.isEmpty()||des.isBlank()||des.isEmpty())
        	   {
        		   JOptionPane.showMessageDialog(main_frame, "Can't have empty fields!",
		                     "Update Tags", JOptionPane.WARNING_MESSAGE);
        		   
        		   return;
        	   }
        	   update();
        	   
           }
	   }
	   
	   
	   
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
	  
	   public void mergeRecords(String old_tag,String new_tag,ResultSet fetched_paths)
	   {
		   
		   if(new_tag.length()>1000000)
		   {
			   JOptionPane.showMessageDialog(main_frame, "Failed to Update Please try Again",
	                     "Update Tags", JOptionPane.WARNING_MESSAGE);
			   return;
		   }
		   
	   }
	   
	   public void update()
	    {
	      String old_tag = source.getText();
	      String new_tag = dest.getText();
	      
	    
			   try
		         {
		        	 Class.forName("com.mysql.cj.jdbc.Driver");
		        	 Connection connection  = DriverManager.getConnection("jdbc:mysql://localhost:3306/file_tags","root","root");
		        	 Statement state = connection.createStatement();
		        	 Statement state2 = connection.createStatement();
		        	 String qq = "'"+old_tag+"'";
		        	 String qq2 = "'"+new_tag+"'";
		        	 ResultSet rs = state.executeQuery("select tag from tags WHERE tag = "+qq);
		        	 ResultSet rs2 = state2.executeQuery("select path from tags WHERE tag = "+qq2);
		        	 if(!isItEmpty(rs))
		        	 {   
//		        		 while(rs.next())
//				        	{
//				        		System.out.println(rs.getString("tag"));
//				        	}
//		        		state.close();
		        		 if(!isItEmpty(rs2))
		        		 {
		        			 JOptionPane.showMessageDialog(main_frame, "New Tag is already present",
				                     "Update Tags", JOptionPane.WARNING_MESSAGE);
		        			 return;
		        		 }
		        		try { //Statement ps = connection.createStatement();
//		        		 ps.setString(1,old_tag);
//		        		 ps.setString(2,new_tag);
		        		String query = "UPDATE tags SET tag =" +qq2+" WHERE tag ="+qq;
//		        		System.out.println(query);
		        		 state.executeUpdate(query);
		        		 JOptionPane.showMessageDialog(main_frame, "Successfully Updated!",
		                         "Update Tags", JOptionPane.INFORMATION_MESSAGE);
		        		 state.close();
		        		 return;
		        		}
		        		catch(Exception e)
		        		{
//		        			System.out.println(e);
		        			JOptionPane.showMessageDialog(main_frame, "Failed to Update Please try Again",
				                     "Update Tags", JOptionPane.WARNING_MESSAGE);
		        		}
		        	 }
		        	 else         
		        	 {
		        		 JOptionPane.showMessageDialog(main_frame, "There is no file with old_tag!",
			                     "Update Tags", JOptionPane.WARNING_MESSAGE);
		        	 }
		   }
			   
			catch(Exception e)
			   {
				System.out.println(e);
				JOptionPane.showMessageDialog(main_frame, "Connection Failed Please try Again!",
	                    "Delete Tags", JOptionPane.WARNING_MESSAGE);
			   }
	    }

	  UpdateTag()
	  {
		
	  	rename = new JButton("Rename");
	  	clear= new JButton("Clear");
	  	rename.addActionListener(this);
	  	clear.addActionListener(this);
	  	text = new JLabel("Update the tag");
	  	String note_msg="<html>Note:First Field is for exisiting Tag <br/> Second Field is for new Tag";
	  	note = new JLabel(note_msg);
	  	note.setFont(new Font(note_msg, Font.BOLD, 14));
	    note.setForeground(Color.RED);
	  	source = new JTextField( "Old Tag");
	  	dest = new JTextField( "New Tag");
	  	tools = new JToolBar();
	  	tools.add(source);
	  	tools.add(dest);
	  	tools.add(rename);
	  	tools.add(clear);
	  	tools.setFloatable(false);
	  	main_frame = new JFrame("Update Tags");
	    panel =  new JPanel(new GridLayout(3,0));
	    main_frame.add(panel,BorderLayout.NORTH);	
	    panel.add(text);
	    panel.add(tools);
	    panel.add(note);
	  	    main_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
			main_frame.setSize(400,200);
			main_frame.setVisible(true);
			main_frame.setResizable(false);
	  }
//	  public static void main(String [] args)
//	  {
//		  new UpdateTag();
//	  }
}
