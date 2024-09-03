package org.ProjectGurukul.translator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.swing.JFrame;

import org.json.JSONObject;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class App {
    private JFrame frame;
    private String[] languages = {
            "en| English",
            "es| Spanish",
            "zh| Mandarin Chinese",
            "hi| Hindi",
            "ar| Arabic",
            "bn| Bengali",
            "ru| Russian",
            "pt| Portuguese",
            "ja| Japanese",
            "de| German",
            "fr| French",
            "ms| Malay",
            "sw| Swahili",
            "ko| Korean",
            "it| Italian"
        };
    public App() {
		initialize();
	}
    
	public String translate(String query,String sourceLang,String targetLang) {
        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String langPair = sourceLang+"|"+targetLang;
            langPair = URLEncoder.encode(langPair,"UTF-8");
            String apiUrl = "https://api.mymemory.translated.net/get?q=" + encodedQuery + "&langpair=" + langPair;
            
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                
                String jsonResponse = response.toString();
                // Parse the JSON response
                JSONObject object = new JSONObject(jsonResponse);
                JSONObject responseData = object.getJSONObject("responseData");
                String translatedText = responseData.getString("translatedText");
                
                System.out.println("Response Data: " + responseData);
                System.out.println("Translated Text: " + translatedText);
                connection.disconnect();
                return translatedText;
            } else {
                System.out.println("Request failed with response code: " + responseCode);
                connection.disconnect();
                return "Request failed with response code: " +responseCode;
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            
            return e.getMessage();
        }
    }
    
    /**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100,600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Translator by ProjectGurukul");
		frame.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));
		
		JPanel sourcePanel = new JPanel();
		frame.getContentPane().add(sourcePanel);
		sourcePanel.setLayout(null);
		
		final JTextArea txtrSourcetextarea = new JTextArea();
		txtrSourcetextarea.setLineWrap(true);
		txtrSourcetextarea.setWrapStyleWord(true);
		txtrSourcetextarea.setText("Enter Here to translate");
		txtrSourcetextarea.setBounds(22, 65, 248, 264);
		sourcePanel.add(txtrSourcetextarea);
		
		final JComboBox sourceComboBox = new JComboBox(languages);
		sourceComboBox.setBounds(32, 23, 216, 31);
		sourcePanel.add(sourceComboBox);
		
		JLabel lblTo = new JLabel("To->");
		lblTo.setBounds(257, 31, 70, 15);
		sourcePanel.add(lblTo);
		
		JPanel targetPanel = new JPanel();
		frame.getContentPane().add(targetPanel);
		targetPanel.setLayout(null);
		
		final JTextArea txtTargetTextarea = new JTextArea();
		txtTargetTextarea.setLineWrap(true);
		txtTargetTextarea.setWrapStyleWord(true);
		txtTargetTextarea.setBounds(12, 65, 248, 264);
		targetPanel.add(txtTargetTextarea);
		
		final JComboBox targetComboBox = new JComboBox(languages);
		targetComboBox.setBounds(22, 23, 216, 31);
		targetPanel.add(targetComboBox);
		
		JButton btnTranslate = new JButton("Translate");
		btnTranslate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtTargetTextarea.setText(translate(txtrSourcetextarea.getText(),
													((String)sourceComboBox.getSelectedItem()).substring(0,2),
													((String)targetComboBox.getSelectedItem()).substring(0,2)));
			}
		});
		btnTranslate.setBounds(40, 330, 200, 30);
		targetPanel.add(btnTranslate);
		
		frame.setVisible(true);
	}
    
    public static void main(String[] args) {
    	new App();
    	
    }
}
