import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

class Contact {
    private String name;
    private String phoneNumber;
    private String emailAddress;

    public Contact(String name, String phoneNumber, String emailAddress) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }



    @Override
    public String toString() {
        return "Name: " + name + ", Phone: " + phoneNumber + ", Email: " + emailAddress;
    }

    public String getName() {

        return name;
    }
}

class AddressBook {
    private ArrayList<Contact> contacts;

    public AddressBook() {
        contacts = new ArrayList<>();
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
    }

    public Contact searchContact(String name) {
        for (Contact contact : contacts) {
            if (contact.getName().equalsIgnoreCase(name)) {
                return contact;
            }
        }
        return null;
    }

    public ArrayList<Contact> getAllContacts() {
        return contacts;
    }

    public void saveToFile(String fileName) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(contacts);
        }
    }

    public void loadFromFile(String fileName) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            contacts = (ArrayList<Contact>) ois.readObject();
        }
    }
}

public class AddressBookGUI extends JFrame {
    private AddressBook addressBook;
    private JTextField nameField, phoneField, emailField;
    private JTextArea displayArea;

    public AddressBookGUI() {
        addressBook = new AddressBook();
        initComponents();
    }

    private void initComponents() {
        setTitle("Address Book");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        JButton addButton = new JButton("Add");
        JButton searchButton = new JButton("Search");
        JButton displayButton = new JButton("Display All");
        JButton exitButton = new JButton("Exit");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchContact();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayContacts();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveAndExit();
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        buttonPanel.add(addButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(exitButton);

        displayArea = new JTextArea();
        displayArea.setEditable(false);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(displayArea, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addContact() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Contact contact = new Contact(name, phone, email);
        addressBook.addContact(contact);
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        displayArea.setText("Contact added:\n" + contact.toString());
    }

    private void searchContact() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name to search!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Contact contact = addressBook.searchContact(name);
        if (contact != null) {
            displayArea.setText("Contact found:\n" + contact.toString());
        } else {
            displayArea.setText("Contact not found for the given name.");
        }
    }

    private void displayContacts() {
        ArrayList<Contact> contacts = addressBook.getAllContacts();
        if (contacts.isEmpty()) {
            displayArea.setText("No contacts to display.");
        } else {
            StringBuilder sb = new StringBuilder("All contacts:\n");
            for (Contact contact : contacts) {
                sb.append(contact.toString()).append("\n");
            }
            displayArea.setText(sb.toString());
        }
    }

    private void saveAndExit() {
        try {
            addressBook.saveToFile("addressbook.dat");
            JOptionPane.showMessageDialog(this, "Address Book saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error while saving Address Book.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        AddressBookGUI addressBookGUI = new AddressBookGUI();
        addressBookGUI.setVisible(true);
    }
}
