package net.badata.protobuf.example.client.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.text.NumberFormatter;

/**
 * @author jsjem
 * @author Roman Gushel
 */
public class AddBookView implements ActionListener {

    private final JPanel form;
    private final JTextField titleText;
    private final JTextField authorText;
    private final JFormattedTextField pageText;

    private OnAddBookActionListener listener;

    public AddBookView(final JFrame windowFrame) {
        form = new JPanel();
        form.setLayout(null);
        form.setBounds(0, 210, 250, 250);

        final JLabel titleLabel = new JLabel("Title");
        titleLabel.setBounds(10, 10, 80, 25);
        form.add(titleLabel);

        titleText = new JTextField(20);
        titleText.setBounds(80, 10, 160, 25);
        form.add(titleText);

        final JLabel authorLabel = new JLabel("Author");
        authorLabel.setBounds(10, 40, 80, 25);
        form.add(authorLabel);
        authorText = new JTextField(20);
        authorText.setBounds(80, 40, 160, 25);
        form.add(authorText);

        final JLabel pageLabel = new JLabel("Pages");
        pageLabel.setBounds(10, 70, 80, 25);
        form.add(pageLabel);
        NumberFormatter numberFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        numberFormatter.setValueClass(Integer.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(1);
        pageText = new JFormattedTextField(numberFormatter);
        pageText.setBounds(80, 70, 160, 25);
        form.add(pageText);

        final JButton addButton = new JButton("Add");
        addButton.setBounds(160, 100, 80, 25);
        form.add(addButton);
        addButton.addActionListener(this);

        windowFrame.add(form);
    }

    public void setListener(final OnAddBookActionListener listener) {
        this.listener = listener;
    }

    public void setVisible(final boolean visible) {
        form.setVisible(visible);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (listener != null) {
            listener.onAddBook(titleText.getText(), authorText.getText(), (Integer) pageText.getValue());
        }

        clearForm();
    }

    private void clearForm() {
        titleText.setText("");
        authorText.setText("");
        pageText.setValue(null);
    }

    public interface OnAddBookActionListener {
        void onAddBook(final String title, final String author, final int pages);
    }
}
