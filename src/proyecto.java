import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class proyecto {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                createAndShowGUI();
            }
        });
    }
    private static void createAndShowGUI(){
        JFrame frame = new JFrame("Medical Interview");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Question 1: \n Question?", JLabel.CENTER);
        panel.add(label, BorderLayout.CENTER);

        JButton yButton = new JButton("Yes");
        JButton nButton = new JButton("No");
        //yButton.setPreferredSize(new Dimension(195, 50));
        //nButton.setPreferredSize(new Dimension(195, 50));
        yButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                label.setText("You clicked yes");
            }
        });
        nButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                label.setText("You clicked no");
            }
        });

        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;
        constraints.gridwidth = 2;
        constraints.gridx = 0;
        bottomPanel.add(yButton, constraints);
        constraints.gridx = 2;
        bottomPanel.add(nButton, constraints);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }
}
