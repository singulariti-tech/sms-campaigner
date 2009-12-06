package com.alteregos.sms.campaigner.views;

import com.alteregos.sms.campaigner.Main;
import com.alteregos.sms.campaigner.data.dto.Contact;
import com.alteregos.sms.campaigner.data.validation.PhonebookEntryValidator;
import com.alteregos.sms.campaigner.exceptions.ExceptionParser;
import com.alteregos.sms.campaigner.exceptions.ITaskResult;
import com.alteregos.sms.campaigner.exceptions.SuccessfulTaskResult;
import com.alteregos.sms.campaigner.services.ContactService;
import javax.swing.JOptionPane;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;

/**
 *
 * @author  John Emmanuel
 */
public class PhonebookEntryCreatorPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;

    /** Creates new form PhonebookEntryCreatorPanel */
    public PhonebookEntryCreatorPanel() {
        initComponents();
        phoneBook = new Contact();
        this.validator = new PhonebookEntryValidator(nameTextField, mobileNoTextField);
    }

    //<editor-fold defaultstate="collapsed" desc="Actions">
    @Action
    public Task<ITaskResult, Void> saveEntry() {
        if (validator.validate()) {
            return new SaveEntryTask(org.jdesktop.application.Application.getInstance(com.alteregos.sms.campaigner.Main.class));
        }
        return null;
    }

    private class SaveEntryTask extends Task<ITaskResult, Void> {

        public SaveEntryTask(Application app) {
            super(app);
        }

        @Override
        protected ITaskResult doInBackground() {
            ITaskResult result = null;
            phoneBook.setAddress(postalAddressTextArea.getText().trim());
            phoneBook.setMobileNo(mobileNoTextField.getText().trim());
            phoneBook.setEmail(emailAddressTextField.getText().trim());
            phoneBook.setName(nameTextField.getText().trim());
            try {                
                contactService.newContact(phoneBook);
                result = new SuccessfulTaskResult();
            } catch (Exception rex) {
                result = ExceptionParser.getError(rex);
            }
            return result;
        }

        @Override
        protected void succeeded(ITaskResult object) {
            super.succeeded(object);
            ITaskResult result = object;
            if (result.isSuccessful()) {
                nameTextField.setText("");
                emailAddressTextField.setText("");
                mobileNoTextField.setText("");
                postalAddressTextArea.setText("");
            } else {
                JOptionPane.showMessageDialog(null, result.getResultMessage().getLabel());
            }
            phoneBook = new Contact();
        }
    }

    @Action
    public void clearEntry() {
        phoneBook = new Contact();
        nameTextField.setText("");
        postalAddressTextArea.setText("");
        mobileNoTextField.setText("");
        emailAddressTextField.setText("");

    }
    //</editor-fold>

    @SuppressWarnings("unchecked")
    private void initComponents() {

        contactService = Main.getApplication().getBean("contactService");
        nameLabel = new javax.swing.JLabel();
        mobileNoLabel = new javax.swing.JLabel();
        emailAddressLabel = new javax.swing.JLabel();
        postalAddressLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        mobileNoTextField = new javax.swing.JTextField();
        emailAddressTextField = new javax.swing.JTextField();
        postalAddressScrollPane = new javax.swing.JScrollPane();
        postalAddressTextArea = new javax.swing.JTextArea();
        clearButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        ResourceMap resourceMap = Application.getInstance(Main.class).getContext().getResourceMap(PhonebookEntryCreatorPanel.class);
        this.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("phoneBookEntryCreatorPanel.border.title"))); // NOI18N
        this.setName("phoneBookEntryCreatorPanel"); // NOI18N

        nameLabel.setText(resourceMap.getString("nameLabel.text")); // NOI18N
        nameLabel.setName("nameLabel"); // NOI18N

        mobileNoLabel.setText(resourceMap.getString("mobileNoLabel.text")); // NOI18N
        mobileNoLabel.setName("mobileNoLabel"); // NOI18N

        emailAddressLabel.setText(resourceMap.getString("emailAddressLabel.text")); // NOI18N
        emailAddressLabel.setName("emailAddressLabel"); // NOI18N

        postalAddressLabel.setText(resourceMap.getString("postalAddressLabel.text")); // NOI18N
        postalAddressLabel.setName("postalAddressLabel"); // NOI18N

        nameTextField.setText(resourceMap.getString("nameTextField.text")); // NOI18N
        nameTextField.setName("nameTextField"); // NOI18N

        mobileNoTextField.setText(resourceMap.getString("mobileNoTextField.text")); // NOI18N
        mobileNoTextField.setName("mobileNoTextField"); // NOI18N

        emailAddressTextField.setText(resourceMap.getString("emailAddressTextField.text")); // NOI18N
        emailAddressTextField.setName("emailAddressTextField"); // NOI18N

        postalAddressScrollPane.setName("postalAddressScrollPane"); // NOI18N

        postalAddressTextArea.setColumns(20);
        postalAddressTextArea.setFont(resourceMap.getFont("postalAddressTextArea.font")); // NOI18N
        postalAddressTextArea.setRows(5);
        postalAddressTextArea.setTabSize(1);
        postalAddressTextArea.setName("postalAddressTextArea"); // NOI18N
        postalAddressScrollPane.setViewportView(postalAddressTextArea);

        javax.swing.ActionMap actionMap = Application.getInstance(Main.class).getContext().getActionMap(PhonebookEntryCreatorPanel.class, this);
        clearButton.setAction(actionMap.get("clearEntry")); // NOI18N
        clearButton.setText(resourceMap.getString("clearButton.text")); // NOI18N
        clearButton.setName("clearButton"); // NOI18N

        saveButton.setAction(actionMap.get("saveEntry")); // NOI18N
        saveButton.setText(resourceMap.getString("saveButton.text")); // NOI18N
        saveButton.setName("saveButton"); // NOI18N

        this.setLayout(new MigLayout("fill, insets panel", "[min!][grow]", "[min!][min!][min!][min!][grow][min!]"));
        this.add(nameLabel);
        this.add(nameTextField, "grow, wrap");
        this.add(mobileNoLabel);
        this.add(mobileNoTextField, "grow, wrap");
        this.add(emailAddressLabel);
        this.add(emailAddressTextField, "grow, wrap");
        this.add(postalAddressLabel, "wrap");
        this.add(postalAddressScrollPane, "spanx 2, grow, top, left, wrap");
        this.add(saveButton, "spanx 2, split 2, right");
        this.add(clearButton);
    }    
    private ContactService contactService;
    private javax.swing.JButton clearButton;
    private javax.swing.JLabel emailAddressLabel;
    private javax.swing.JTextField emailAddressTextField;
    private javax.swing.JLabel mobileNoLabel;
    private javax.swing.JTextField mobileNoTextField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JLabel postalAddressLabel;
    private javax.swing.JScrollPane postalAddressScrollPane;
    private javax.swing.JTextArea postalAddressTextArea;
    private javax.swing.JButton saveButton;
    private Contact phoneBook;
    private PhonebookEntryValidator validator;
}
