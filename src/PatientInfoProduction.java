import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.*;

public class PatientInfoProduction {
    //declaring these objects first, in order to share them and their values (access to them, etc) across methods
    private JFrame frame;
    private CardLayout questionCardLayout;
    private JPanel cardPanel;
    private JButton backButton, nextOrSubButton;
    private ButtonGroup YNbuttonGroup;
    private JRadioButton yesButton, noButton;
    private JTextField FnameField, LnameField, AgeField, GenderField, bloodField;
    private JTextField allergiesArea, medicationsArea, symptomsArea, immunizationsArea, hereditaryArea ;
    private JComboBox<Integer> painLevelBox;
    private ArrayList<Answer> interviewAnswers = new ArrayList<>();
    private Boolean nextButtonPressed;
    private Integer patientID;
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
        JLabel Blabel = new JLabel("Blood Type", JLabel.CENTER);
        //text fields
        FnameField = new JTextField();
        LnameField = new JTextField();
        AgeField = new JTextField();
        GenderField = new JTextField();
        bloodField = new JTextField();
        //new text areas -> text fields
        //TO DO: Move allergies & medications to advanced and replace with yes/no radio

        // Yes/no radio
        YNbuttonGroup = new ButtonGroup();
        
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
        
        //symptoms, allergies, immunizations, hereditary deseases, and painlevel dropdown 
        symptomsArea = new JTextField();
        allergiesArea = new JTextField();
        medicationsArea = new JTextField();
        immunizationsArea = new JTextField();
        hereditaryArea = new JTextField();
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

        boxPanel.add(new JLabel("Pain Level", JLabel.CENTER));
        boxPanel.add(painLevelBox);    
        boxPanel.add(Blabel);
        boxPanel.add(bloodField);            

        boxPanel.add(new JLabel("Additional Information?", JLabel.CENTER));
        boxPanel.add(radioButtonPanel);        

        //Advanced question card panel
        JPanel questionPanel2 = new JPanel(new GridLayout(0,2));
        
        questionPanel2.add(new JLabel("Allergies (single comma seperated)", JLabel.CENTER));
        questionPanel2.add(allergiesArea);
        questionPanel2.add(new JLabel("Medications (single comma seperated)", JLabel.CENTER));
        questionPanel2.add(medicationsArea);
        questionPanel2.add(new JLabel("Symptoms (single comma seperated)", JLabel.CENTER));
        questionPanel2.add(symptomsArea);
        questionPanel2.add(new JLabel("Immunizations (comma separated)", JLabel.CENTER));
        questionPanel2.add(immunizationsArea);
        questionPanel2.add(new JLabel("Hereditary Diseases (comma separated)", JLabel.CENTER));
        questionPanel2.add(hereditaryArea);


        
        //adding panels to frame
        questionCardLayout = new CardLayout();
        cardPanel = new JPanel(questionCardLayout);
        cardPanel.add(boxPanel, "BasicQ");
        cardPanel.add(questionPanel2, "AdvancedQ");
        mainBLpanel.add(cardPanel, BorderLayout.CENTER);
        
        //Buttons -- Back button & logic
        JPanel buttonPanel = new JPanel(new GridLayout(0,2));
        mainBLpanel.add(buttonPanel, BorderLayout.SOUTH);

        backButton = new JButton("Back");
        backButton.setEnabled(false);
        buttonPanel.add(backButton);

        ActionListener backListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                changeQcardToBasic();
            };
        };    
        backButton.addActionListener(backListener);      

        //Next or submit button & logic
        nextOrSubButton = new JButton("Submit Patient");
        nextOrSubButton.setEnabled(false);
        nextButtonPressed = false;
        ActionListener nextOrSubListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if (nextOrSubButton.getText().equals("Next") && areBasicFieldsValid()){
                    changeQcard();
                    backButton.setEnabled(true);
                    if (!nextButtonPressed) {  
                        interviewAnswers.add(new Answer("firstName", FnameField.getText().trim()));
                        interviewAnswers.add(new Answer("lastName", LnameField.getText().trim()));
                        interviewAnswers.add(new Answer("age", AgeField.getText().trim()));
                        interviewAnswers.add(new Answer("gender", GenderField.getText().trim()));
                        interviewAnswers.add(new Answer("blood_type", bloodField.getText().trim()));
                        interviewAnswers.add(new Answer("pain_level", painLevelBox.getSelectedItem().toString())); 
                        nextButtonPressed = true;
                    }
                    
                } else if (nextOrSubButton.getText().equals("Submit Patient")){
                    if (currentCard.equals("BasicQ") && areBasicFieldsValid()){
                        if (!nextButtonPressed) {  
                            interviewAnswers.add(new Answer("firstName", FnameField.getText().trim()));
                            interviewAnswers.add(new Answer("lastName", LnameField.getText().trim()));
                            interviewAnswers.add(new Answer("age", AgeField.getText().trim()));
                            interviewAnswers.add(new Answer("gender", GenderField.getText().trim()));
                            interviewAnswers.add(new Answer("blood_type", bloodField.getText().trim()));
                            interviewAnswers.add(new Answer("pain_level", painLevelBox.getSelectedItem().toString())); 
                            nextButtonPressed = true;
                        }
                        submitPatientSQLinfo();
                    } else if (currentCard.equals("AdvancedQ") && areAdvancedFieldsValid()){
                        interviewAnswers.add(new Answer("allergies", allergiesArea.getText().trim()));
                        interviewAnswers.add(new Answer("medications", medicationsArea.getText().trim()));
                        submitPatientSQLinfo();

                    }
                }
            };
        };
        nextOrSubButton.addActionListener(nextOrSubListener);        
        //nextOrSubButton.addActionListener(submitPatientSQLinfo());
        buttonPanel.add(nextOrSubButton);
        frame.getContentPane().add(mainBLpanel);
    }
    //Radio button logic to update submit/next button
    private void updateNavigationButtons(){
        if (currentCard.equals("BasicQ")){
            backButton.setEnabled(false);
            if (yesButton.isSelected()){
                nextOrSubButton.setText("Next");
                nextOrSubButton.setEnabled(true);
            } else if (noButton.isSelected()){
                nextOrSubButton.setText("Submit Patient");
                nextOrSubButton.setEnabled(true);
            }
        } else {
            backButton.setEnabled(true);
            nextOrSubButton.setText("Submit Patient");
            //nextOrSubButton.setEnabled(areAdvancedFieldsValid());
        }
    }

    //Methods to check if required information is present
    private boolean areBasicFieldsValid(){
        try {
            int age = Integer.parseInt(AgeField.getText().trim()); //will throw exception if not possible & return false
            if (FnameField.getText().isBlank() || LnameField.getText().isBlank() || AgeField.getText().isBlank()){
                JOptionPane.showMessageDialog(frame, "First name, last name, and age are required.");
                return false;
            } else {
                return true;
            }            
        } catch (NumberFormatException error) {
            JOptionPane.showMessageDialog(frame, "Age must be a valid number.");
            return false;
        }
    }
    private boolean areAdvancedFieldsValid(){
        if (allergiesArea.getText().isBlank() || medicationsArea.getText().isBlank() || symptomsArea.getText().isBlank()){
            JOptionPane.showMessageDialog(frame, "Allergies, Medication, and Symptoms are required.");
            return false;
        } else {
            return true;
        }
    }
    
    //Method to change cards/question view
    private void changeQcard(){
        questionCardLayout.show(cardPanel, "AdvancedQ");
        currentCard = "AdvancedQ";
        updateNavigationButtons();
    }
    private void changeQcardToBasic(){
        questionCardLayout.show(cardPanel, "BasicQ");
        currentCard = "BasicQ";
        updateNavigationButtons();
    }    


    //sql form submission logic

    //update each submission to submit their respective basic or advanced info
    private void submitPatientSQLinfo(){
        try{
            int painLevel = (Integer) painLevelBox.getSelectedItem();
            int age = Integer.parseInt(AgeField.getText().trim());

            if (noButton.isSelected()) {
                sqlPatAdd(interviewAnswers);
                JOptionPane.showMessageDialog(frame,
                    "Patient saved successfully.\n\n" +
                    "Medical History Summary:\n" +
                    "Name: "+ FnameField.getText().trim()+" "+ LnameField.getText().trim()+"\n"+
                    "Age: " + age + "\n" +
                    "Gender: " + GenderField.getText().trim() + "\n" +
                    "Blood type: " + bloodField.getText().trim() + "\n" +
                    "Pain Level: " + painLevel
            );  
            } else{
                sqlPatAdd(interviewAnswers);
                JOptionPane.showMessageDialog(frame,
                    "Patient saved successfully.\n\n" +
                    "Medical History Summary:\n" +
                    "Name: "+ FnameField.getText().trim()+" "+ LnameField.getText().trim()+"\n"+
                    "Age: " + age + "\n" +
                    "Gender: " + GenderField.getText().trim() + "\n" +
                    "Allergies: " + allergiesArea.getText().trim() + "\n" +
                    "Medications: " + medicationsArea.getText().trim() + "\n" +
                    "Symptoms: " + symptomsArea.getText().trim() + "\n" +
                    "Pain Level: " + painLevel
                );
            }
            

            FnameField.setText("");
            LnameField.setText("");
            AgeField.setText("");
            GenderField.setText("");
            allergiesArea.setText("");
            medicationsArea.setText("");
            symptomsArea.setText("");
            bloodField.setText("");
            painLevelBox.setSelectedIndex(0);
            YNbuttonGroup.clearSelection();
            nextOrSubButton.setEnabled(false);
            interviewAnswers.clear();
            nextButtonPressed = false;
            changeQcardToBasic();

        } catch (NumberFormatException error){
            JOptionPane.showMessageDialog(frame, "Age must be a valid number.");
        } catch (Exception error){
            JOptionPane.showMessageDialog(frame, "Error: " + error.getMessage());
        } 
    }

    //sql logic
    //Universal/global class to store answers and their respective DB column names
    public class Answer{
        private String questionColumnName;
        private String userResponse;

        public Answer(String questionColumnName, String userResponse){
            this.questionColumnName = questionColumnName;
            this.userResponse = userResponse;
        }

        public String getQuestionName(){
            return questionColumnName;
        }

        public String getUserResponse(){
            return userResponse;
        }
    }
    //method to properly get user response
    private String getUserResFromQuestion(ArrayList<Answer> answers, String questColName){
        for (Answer a : answers){
            if (a.getQuestionName().equals(questColName)){
                return a.getUserResponse();
            }
        }
        return ""; //return nothing if column name not found
    }
    private void insertIntoCommaSepList(String rawList){
        String[] items = rawList.split(",");
    }

    //Actual insertion of SQL values
    public void sqlPatAdd(ArrayList<Answer> answerList) throws Exception {
        String jdbcURL = "jdbc:mysql://localhost:3306/its340ProjectDB";
        String username = "root";
        String password = "toor";

        String[] columnNames = {"firstName", "lastName", "age", "gender", "blood_type", "pain_level"};
        Set<String> numericColumnNames = Set.of("age", "pain_level");

        StringBuilder columnsToInsert = new StringBuilder();
        StringBuilder valuesToInsert = new StringBuilder();
        Boolean isFirstValue = true;

        for (String column : columnNames){
            String value = getUserResFromQuestion(answerList, column);
            if (value == null || value.isBlank()){
                continue;
                //skip null values since only 3 are required, so nulls may appear
            }

            if (!isFirstValue){
                columnsToInsert.append(", ");
                valuesToInsert.append(", ");
            } else {
                isFirstValue = false;
            }
            columnsToInsert.append(column);
            if(numericColumnNames.contains(column)){
                valuesToInsert.append(value);
            } else {
                valuesToInsert.append("'").append(value).append("'");
            }
        }

        String SQLquery = "INSERT INTO patients(" + columnsToInsert + ") VALUES (" + valuesToInsert + ")";
        System.out.println("running the query: "+SQLquery);

        try{
            Connection conn = DriverManager.getConnection(jdbcURL, username, password);
            PreparedStatement pstmt = conn.prepareStatement(SQLquery, Statement.RETURN_GENERATED_KEYS);
            pstmt.executeUpdate();

            try {
                ResultSet createdPatID = pstmt.getGeneratedKeys();
                if (createdPatID.next()){
                    patientID = createdPatID.getInt(1);
                    System.out.println(patientID);
                }
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
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
