import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

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
    void saveNote() throws IOException;
    void listNote() throws IOException;
    void readNote() throws IOException;
}

class NotesMain extends Notes implements NotesIO {

    public NotesMain(String windowName, boolean visible) {
        super(windowName, visible);

        saveNotes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    saveNote();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            }
        });

        listNotes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    listNote();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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

    @Override
    public void saveNote() throws FileNotFoundException {

        String title = titleText.getText();
        String body = areaBody.getText();
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dateFormat.format(currentDate);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String timeString = timeFormat.format(currentDate);

        FileOutputStream outFileTitle =  new FileOutputStream(title + ".txt");

        try {
            DataOutputStream outputStream = new DataOutputStream(outFileTitle);
            outputStream.writeUTF("Tanggal dibuat : " + dateString + "\n");
            outputStream.writeUTF("Waktu dibuat : " + timeString + "\n");
            outputStream.writeUTF("\n" + body);
            outputStream.close();

            JOptionPane.showMessageDialog(null, "Note saved successfully.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        FileOutputStream outFileData =  new FileOutputStream("datanotes.txt", true);
        try {
            DataOutputStream outputStream = new DataOutputStream(outFileData);
            outputStream.writeUTF(title);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void listNote() throws IOException {
        ListNotes listForm = new ListNotes("List Notes", true);
    }

    @Override
    public void readNote() {
        // tidak dipakai
    }
}

class ListNotes extends Notes implements NotesIO {

    private JComboBox<String> comboBox;

    public ListNotes(String windowName, boolean visible) throws IOException {
        super(windowName, visible);

        comboBox = new JComboBox<>();
        comboBox.setBounds(50, 50, 300, 30);
        form.add(comboBox);

        try {
            loadComboBox();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        readNotes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    readNote();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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

    private void loadComboBox() throws IOException {
        FileInputStream fileData  = new FileInputStream("datanotes.txt");
        try {
            DataInputStream dataStream = new DataInputStream(fileData);
            while(dataStream.available() > 0) {
                String listNotes = dataStream.readUTF();
                comboBox.addItem(listNotes);
            }
            dataStream.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void readNote() throws IOException {
        String selectedTitle = (String) comboBox.getSelectedItem();
        StringBuilder sb = new StringBuilder();

        try {
            FileInputStream inFile = new FileInputStream(selectedTitle + ".txt");
            DataInputStream inputStream = new DataInputStream(inFile);

            while(inputStream.available() > 0) {
                String bodies = inputStream.readUTF();

                sb.append(bodies);
                areaBody.setText(sb.toString());
            }
            inputStream.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void saveNote() {
        // tidak dipakai
    }

    @Override
    public void listNote() {
        // tidak dipakai
    }
}

public class NotesApp {
    public static void main(String[] args) {
        NotesMain mainForm = new NotesMain("Notes App", true);
    }
}
