import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.*;

public class PatientInfoProduction {
    //declaring these objects first, in order to share them and their values (access to them, etc) across methods
    private JFrame frame;
    private CardLayout questionCardLayout;
    private JPanel cardPanel;
    private JButton backButton, nextOrSubButton;
    private JRadioButton yesButton, noButton;
    private JTextField FnameField, LnameField, AgeField, GenderField;
    private JTextField allergiesArea, medicationsArea;
    private JComboBox<Integer> painLevelBox;
    private String currentCard = "BasicQ"; //pre-set current card as basic question card
    public static void main(String[] args) {
        //splash screen
        JWindow window = new JWindow();
        JPanel content = (JPanel) window.getContentPane();
        JLabel label = new JLabel("Welcome", SwingConstants.CENTER);
        content.add(label, BorderLayout.CENTER);
        window.setSize(300, 300);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        try{
            Thread.sleep(2000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        window.setVisible(false);
        window.dispose();
        
        //actual program
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                PatientInfoProduction program = new PatientInfoProduction();
                program.createAndShowGUI();
            }
        });
    }

    
    private void createAndShowGUI(){
    
        frame = new JFrame("Patient Creation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700,600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JPanel mainBLpanel = new JPanel(new BorderLayout());
        JPanel boxPanel = new JPanel(new GridLayout(0,2));
        //labels
        JLabel FNlabel = new JLabel("First Name", JLabel.CENTER);
        JLabel LNlabel = new JLabel("Last Name", JLabel.CENTER);
        JLabel Alabel = new JLabel("Age", JLabel.CENTER);
        JLabel Glabel = new JLabel("Gender", JLabel.CENTER);
        //text fields
        FnameField = new JTextField();
        LnameField = new JTextField();
        AgeField = new JTextField();
        GenderField = new JTextField();
        //new text areas -> text fields
        //TO DO: Move allergies & medications to advanced and replace with yes/no radio

        // Yes/no radio
        ButtonGroup YNbuttonGroup = new ButtonGroup();
        
        yesButton = new JRadioButton("Yes");
        noButton = new JRadioButton("No");
        
        YNbuttonGroup.add(yesButton);
        YNbuttonGroup.add(noButton);
        //RadioButton panel
        JPanel radioButtonPanel = new JPanel(new GridLayout(0,2));
        radioButtonPanel.add(yesButton);
        radioButtonPanel.add(noButton);

        //Creating ActionListener (radioListener), which activates when a Rbutton is selected, that calls updateNavigationButtons()
        ActionListener radioListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                updateNavigationButtons();
            };
        };
        yesButton.addActionListener(radioListener);
        noButton.addActionListener(radioListener);
        
        //symptoms and painlevel dropdown 
        JTextField symptomsArea = new JTextField();
        //combobox for pain level
        painLevelBox = new JComboBox<>();
        for (int i = 0; i <= 10; i++) {
            painLevelBox.addItem(i);
        }

        //adding labels and textboxes to boxPanel
        boxPanel.add(FNlabel);
        boxPanel.add(LNlabel);
        boxPanel.add(FnameField);
        boxPanel.add(LnameField);
        boxPanel.add(Alabel);
        boxPanel.add(Glabel);
        boxPanel.add(AgeField);
        boxPanel.add(GenderField);

        boxPanel.add(new JLabel("Symptoms", JLabel.CENTER));
        boxPanel.add(symptomsArea);

        boxPanel.add(new JLabel("Pain Level", JLabel.CENTER));
        boxPanel.add(painLevelBox);        

        boxPanel.add(new JLabel("Additional Information?", JLabel.CENTER));
        boxPanel.add(radioButtonPanel);        

        //Advanced question card panel
        JPanel questionPanel2 = new JPanel(new GridLayout(0,2));
        allergiesArea = new JTextField();
        medicationsArea = new JTextField();
        questionPanel2.add(new JLabel("Allergies", JLabel.CENTER));
        questionPanel2.add(allergiesArea);
        questionPanel2.add(new JLabel("Medications", JLabel.CENTER));
        questionPanel2.add(medicationsArea);

        
        //adding panels to frame
        questionCardLayout = new CardLayout();
        cardPanel = new JPanel(questionCardLayout);
        cardPanel.add(boxPanel, "BasicQ");
        cardPanel.add(questionPanel2, "AdvancedQ");
        mainBLpanel.add(cardPanel, BorderLayout.CENTER);
        
        //Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(0,2));
        mainBLpanel.add(buttonPanel, BorderLayout.SOUTH);

        backButton = new JButton("Back");
        backButton.setEnabled(false);
        buttonPanel.add(backButton);

        nextOrSubButton = new JButton("Submit Patient");
        //set button to be submit if radio is no & next if radio is yes
        ActionListener nextOrSubListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                areBasicFieldsValid();
            };
        };
        nextOrSubButton.addActionListener(nextOrSubListener);        
        nextOrSubButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String firstName = FnameField.getText().trim();
                    String lastName = LnameField.getText().trim();
                    String gender = GenderField.getText().trim();
                    String allergies = allergiesArea.getText().trim();
                    String medications = medicationsArea.getText().trim();
                    String symptoms = symptomsArea.getText().trim();
                    int painLevel = (Integer) painLevelBox.getSelectedItem();

                    if (firstName.isEmpty() || lastName.isEmpty() || AgeField.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "First name, last name, and age are required.");
                        return;
                    }

                    int age = Integer.parseInt(AgeField.getText().trim());

                    sqlPatAdd(firstName, lastName, age, gender);

                    JOptionPane.showMessageDialog(frame,
                            "Patient saved successfully.\n\n" +
                            "Medical History Summary:\n" +
                            "Allergies: " + allergies + "\n" +
                            "Medications: " + medications + "\n" +
                            "Symptoms: " + symptoms + "\n" +
                            "Pain Level: " + painLevel
                    );

                    FnameField.setText("");
                    LnameField.setText("");
                    AgeField.setText("");
                    GenderField.setText("");
                    allergiesArea.setText("");
                    medicationsArea.setText("");
                    symptomsArea.setText("");
                    painLevelBox.setSelectedIndex(0);

                } catch (NumberFormatException error) {
                    JOptionPane.showMessageDialog(frame, "Age must be a valid number.");
                } catch (Exception error) {
                    JOptionPane.showMessageDialog(frame, "Error: " + error.getMessage());
                }
            }
        });

        buttonPanel.add(nextOrSubButton);
        frame.getContentPane().add(mainBLpanel);
    }
    //Radio button logic to update submit/next button
    private void updateNavigationButtons(){
        if (currentCard.equals("BasicQ")){
            backButton.setEnabled(false);
            if (yesButton.isSelected()){
                nextOrSubButton.setText("Next");
            } else if (noButton.isSelected()){
                nextOrSubButton.setText("Submit Patient");
            }
        } else {
            backButton.setEnabled(true);
            nextOrSubButton.setText("Submit Patient");
        }
    }
    private void areBasicFieldsValid(){
        if (FnameField.getText().isBlank() || LnameField.getText().isBlank() || AgeField.getText().isBlank()) {
                        JOptionPane.showMessageDialog(frame, "First name, last name, and age are required.");
                        return;
                    }

    }



    //sql logic
    public static void sqlPatAdd(String fName, String lName, Integer age, String gender) throws Exception {
        String jdbcURL = "jdbc:mysql://localhost:3306/its340LAB8db";
        String username = "root";
        String password = "toor";
        try{
            Connection conn = DriverManager.getConnection(jdbcURL, username, password);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO Patients(lastName, firstName, age, gender) VALUES ('"+fName+"', '"+lName+"', "+age+", '"+gender+"')");
            conn.close();
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
        //ResultSet rs = stmt.executeQuery("SELECT * FROM Patients"); //this is for select statements
        //while(rs.next()){ //while we still have rows to go through
        //    System.out.println("ID: "+rs.getInt("patientID")+ ", Last Name: " + rs.getString("lastName") + ", First Name: " + rs.getString("firstName") + "Age: "+rs.getInt("age") + ", gender: " + rs.getString("gender"));
        //}
    }
    





}
