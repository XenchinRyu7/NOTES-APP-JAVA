import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

abstract class Notes {
    protected String title, body;
    protected JLabel notesApp, titleLabel, bodyLabel;
    protected JTextField titleText;
    protected JTextArea areaBody;
    protected JScrollPane scrollBody;
    protected JButton saveNotes, listNotes, readNotes, exitButton;
    protected JFrame form;

    public Notes(String windowName, boolean visible) {
        form = new JFrame();
        form.setTitle(windowName);
        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form.setSize(500, 450);
        form.setLocation(200, 150);
        form.setLayout(null);

        notesApp = new JLabel("NOTES APP");
        notesApp.setFont(new Font("Fira Code", Font.BOLD, 20));
        notesApp.setBounds(200, 10, 150, 30);
        form.add(notesApp);

        titleLabel = new JLabel("Title:");
        titleLabel.setBounds(50, 50, 50, 30);
        form.add(titleLabel);

        titleText = new JTextField();
        titleText.setBounds(110, 50, 300, 30);
        form.add(titleText);

        bodyLabel = new JLabel("Body:");
        bodyLabel.setBounds(50, 90, 50, 30);
        form.add(bodyLabel);

        areaBody = new JTextArea();
        areaBody.setLineWrap(true);
        scrollBody = new JScrollPane(areaBody);
        scrollBody.setBounds(110, 90, 300, 250);
        form.add(scrollBody);

        saveNotes = new JButton("Save Notes");
        saveNotes.setBounds(50, 350, 100, 30);
        form.add(saveNotes);

        listNotes = new JButton("List Notes");
        listNotes.setBounds(180, 350, 100, 30);
        form.add(listNotes);

        readNotes = new JButton("Read Notes");
        readNotes.setBounds(310, 350, 100, 30);
        form.add(readNotes);

        exitButton = new JButton("Exit");
        exitButton.setBounds(410, 10, 60, 20);
        form.add(exitButton);

        form.setVisible(visible);
    }
}

interface NotesIO {
    void saveNote();
    void listNote();
    void readNote();
}

class NotesMain extends Notes implements NotesIO {

    public NotesMain(String windowName, boolean visible) {
        super(windowName, visible);

        saveNotes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveNote();
            }
        });

        listNotes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                listNote();
            }
        });

        readNotes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readNote();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        readNotes.setVisible(false);
    }

    public void saveNote() {
        String title = titleText.getText();
        String body = areaBody.getText();

        try {
            FileWriter writer = new FileWriter(title + ".txt");
            writer.write(body);
            writer.close();

            FileWriter dataWriter = new FileWriter("datanotes.txt", true);
            dataWriter.write(title + "\n");
            dataWriter.close();

            JOptionPane.showMessageDialog(null, "Note saved successfully.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void listNote() {
        ListNotes listForm = new ListNotes("List Notes", true);
    }

    public void readNote() {
        // tidak dipakai
    }
}

class ListNotes extends Notes implements NotesIO {

    private JComboBox<String> comboBox;

    public ListNotes(String windowName, boolean visible) {
        super(windowName, visible);

        comboBox = new JComboBox<>();
        comboBox.setBounds(50, 50, 300, 30);
        form.add(comboBox);

        loadComboBox();

        readNotes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                readNote();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                form.dispose();
            }
        });

        saveNotes.setVisible(false);
        listNotes.setVisible(false);
        titleText.setVisible(false);
        titleLabel.setVisible(false);
    }

    private void loadComboBox() {
        try {
            File file = new File("datanotes.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String title = scanner.nextLine();
                comboBox.addItem(title);
            }
            scanner.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void readNote() {
        String selectedTitle = (String) comboBox.getSelectedItem();

        try {
            File file = new File(selectedTitle + ".txt");
            Scanner scanner = new Scanner(file);
            ArrayList<String> bodies = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String body = scanner.nextLine();
                bodies.add(body);
            }
            scanner.close();

            if (!bodies.isEmpty()) {
                String selectedBody = bodies.get(0);
                areaBody.setText(selectedBody);
            } else {
                JOptionPane.showMessageDialog(null, "Note not found.");
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void saveNote() {
        // tidak dipakai
    }

    public void listNote() {
        // tidak dipakai
    }
}

public class NotesApp {
    public static void main(String[] args) {
        NotesMain mainForm = new NotesMain("Notes App", true);
    }
}
