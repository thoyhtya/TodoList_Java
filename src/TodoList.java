import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel; 

public class TodoList {
    
	/** 
	* Main function
	* Creates top level window
	*/
    public static void main(String[] args) {
    	//setup main window
    	String title = "Todo-list";
    	Integer width = 600;
    	Integer height = 400;
    	
        JFrame frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //create child elements and show contents
        initData();
        createTableLayout(frame.getContentPane());
        
        frame.setVisible(true);
    }
    
	/** 
	* Creates all UI elements:
	* Info text
	* Table for displaying actual tasks in flexible manner
	* Button to create new tasks
	* Button to delete existing tasks
	* 
	* Most CRUD operations are not working in this demo, but dummy functions are implemented
	* Original idea was to cut some corners by saving to csv-file, but it doesn't seem that clever in retrospect
	*/
    private static void createTableLayout(Container pane) {    	
    	//use boxlayout for placing elements vertically
    	pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));   
    	
    	//add infotext
    	JLabel infoLabel = new JLabel("Double click any cell to edit");
    	pane.add(infoLabel);
    	
    	//Init table view
    	//set columns
    	//populate table with some initial data for demo purposes
    	String[] columnNames = {"Description", "Priority", "Deadline", "Status"};
    	Object[][] data = {
    		    {"Shop things", new Integer(5), LocalDateTime.now(), new Boolean(false)},
    		    {"Do things", new Integer(3), LocalDateTime.now(), new Boolean(false)},
    		    {"Make food", new Integer(1), LocalDateTime.now(), new Boolean(false)}
    	        };
    	 	
    	JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        
        //add event listener to table
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
              // TODO save stuff somewhere when they are changed
            	System.out.println("tableChanged");
            	
            	int row = e.getFirstRow();
                int column = e.getColumn();
                TableModel model = (TableModel)e.getSource();
                //String columnName = model.getColumnName(column);
                Object data = model.getValueAt(row, column);
                
                System.out.println(data);
            }
        });
        
        //put table inside scrollable panel
        //allows displaying any number of tasks
        JScrollPane scrollPane = new JScrollPane(table);
        pane.add(scrollPane);
        
        //create button that creates new tasks when clicked
        JButton newButton = new JButton("New task");
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//TODO create new dummy task and update table
            	//This would also need to be done properly if using database
                System.out.println("New task pressed");
                
                String dummytask = "New task;1;now;not done";
                PrintWriter out;
				try {
					out = new PrintWriter("tasks.csv");
					out.println(dummytask);
	                out.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
            }
        });      
        pane.add(newButton);
        
        //Create button for deleting tasks
        JButton deleteButton = new JButton("Delete selected task");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//TODO delete selected task
                System.out.println("Delete task pressed");
                int column = e.getColumn();
            }
        });
        pane.add(deleteButton);
    }
    
	/** 
	* Creates some dummy data to tasks.csv
	* Not working properly yet since file is actually never read to UI.
	* Needs adapter to transform the data for table view anyway
	* At this point it would have been faster to do this properly with a database...
	*/
    private static void initData() {    	
    	String filestring = "tasks.csv";
    	
    	File f = new File(filestring);
    	if(f.isFile()) { 
    	    System.out.println("existing task file found");
    	    //TODO read from file
    	    try {
    	    	
    	    	try(BufferedReader br = new BufferedReader(new FileReader(filestring))) {
    	    	    StringBuilder sb = new StringBuilder();
    	    	    String line = br.readLine();

    	    	    while (line != null) {
    	    	        sb.append(line);
    	    	        sb.append(System.lineSeparator());
    	    	        line = br.readLine();
    	    	    }
    	    	    String everything = sb.toString();
    	    	    System.out.println(everything);
    	    	}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}else {
    		System.out.println("creating new task file with dummy data");
    		List<String> dummytasks = Arrays.asList("Shop things;5;2018-04-27;not done",
    												"Buy things;4;2018-04-28;not done",
    												"Shop more things;5;2018-04-29;not done");
    		Path file = Paths.get(filestring);
    		try {
				Files.write(file, dummytasks, Charset.forName("UTF-8"));
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
}