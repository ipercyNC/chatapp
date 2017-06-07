import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.*;

public class HistoryFrame extends JPanel {
	private final Client client;
	private DefaultListModel<String> listModel = new DefaultListModel<>();
	private JList<String> historyList = new JList<>(listModel);
	
	public HistoryFrame(Client client, String user){
		this.client= client;
		setLayout(new BorderLayout());
		add(new JScrollPane(historyList),BorderLayout.CENTER);
		populate(user);
	}
	private void populate(String user){
		String line  =null;
		String path = user + ".txt";
		try{
			FileReader fileReader = new FileReader("ian.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine())!=null)
				listModel.addElement(line);
			bufferedReader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
