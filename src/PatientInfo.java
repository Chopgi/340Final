import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PatientInfo {
    public static void main(String[] args) {
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
        
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI(){
        
        JFrame frame = new JFrame("Patient Creation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700,600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel boxPanel = new JPanel(new GridLayout(0,2));

        JLabel FNlabel = new JLabel("First Name", JLabel.CENTER);
        JLabel LNlabel = new JLabel("Last Name", JLabel.CENTER);
        JLabel Alabel = new JLabel("Age", JLabel.CENTER);
        JLabel Glabel = new JLabel("Gender", JLabel.CENTER);


        JTextField FnameField = new JTextField();
        JTextField LnameField = new JTextField();
        JTextField AgeField = new JTextField();
        JTextField GenderField = new JTextField();

        JTextArea allergiesArea = new JTextArea(3, 20);
        JTextArea medicationsArea = new JTextArea(3, 20);
        JTextArea symptomsArea = new JTextArea(3, 20);

        JComboBox<Integer> painLevelBox = new JComboBox<>();
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
        boxPanel.add(new JLabel("Allergies", JLabel.CENTER));
        boxPanel.add(new JScrollPane(allergiesArea));

        boxPanel.add(new JLabel("Medications", JLabel.CENTER));
        boxPanel.add(new JScrollPane(medicationsArea));

        boxPanel.add(new JLabel("Symptoms", JLabel.CENTER));
        boxPanel.add(new JScrollPane(symptomsArea));

        boxPanel.add(new JLabel("Pain Level", JLabel.CENTER));
        boxPanel.add(painLevelBox);
        
        panel.add(new JScrollPane(boxPanel), BorderLayout.CENTER);

        
        JButton button = new JButton("Submit Patient");
        button.addActionListener(new ActionListener() {
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

        panel.add(button, BorderLayout.SOUTH);
        frame.getContentPane().add(panel);
    }

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
