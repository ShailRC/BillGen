package billing;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class BillingSystem {

    public static void main(String[] args) {
        // Set up the frame
        JFrame frame = new JFrame("Billing System");
        frame.setSize(1200, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Head
        JLabel head = new JLabel("BILLING SYSTEM", JLabel.CENTER);
        head.setFont(new Font("Bodoni", Font.BOLD, 20));
        head.setOpaque(true);
        head.setBackground(Color.GRAY);
        frame.add(head, BorderLayout.NORTH);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        frame.add(mainPanel, BorderLayout.CENTER);

        // Client details and invoice info
        JPanel clientPanel = new JPanel(new GridBagLayout());
        clientPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), "Client Details:", 0, 0, new Font("Bodoni", Font.BOLD, 15), Color.BLACK));
        clientPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.add(clientPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Bodoni", Font.BOLD, 12));
        clientPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField nameE = new JTextField(20);
        clientPanel.add(nameE, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel gstLabel = new JLabel("GSTIN:");
        gstLabel.setFont(new Font("Bodoni", Font.BOLD, 12));
        clientPanel.add(gstLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField gstE = new JTextField(20);
        clientPanel.add(gstE, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        JLabel billLabel = new JLabel("Bill Number:");
        billLabel.setFont(new Font("Bodoni", Font.BOLD, 12));
        clientPanel.add(billLabel, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        JTextField billE = new JTextField(20);
        clientPanel.add(billE, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setFont(new Font("Bodoni", Font.BOLD, 12));
        clientPanel.add(dateLabel, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        JTextField dateE = new JTextField(10);
        clientPanel.add(dateE, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JButton generatePDFButton = new JButton("Generate PDF");
        generatePDFButton.setFont(new Font("Times New Roman", Font.BOLD, 12));
        clientPanel.add(generatePDFButton, gbc);

        // Client address
        JPanel addressPanel = new JPanel(new BorderLayout());
        addressPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), "Address:", 0, 0, new Font("Bodoni", Font.BOLD, 15), Color.BLACK));
        addressPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.add(addressPanel);

        JTextField addrE = new JTextField();
        addressPanel.add(addrE, BorderLayout.NORTH);

        JPanel statePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel stateLabel = new JLabel("State of service:");
        stateLabel.setFont(new Font("Bodoni", Font.BOLD, 14));
        String[] states = {"Bihar", "Jharkhand"};
        JComboBox<String> stateComboBox = new JComboBox<>(states);
        statePanel.add(stateLabel);
        statePanel.add(stateComboBox);
        addressPanel.add(statePanel, BorderLayout.SOUTH);

        // Service description and amount
        JPanel servicePanel = new JPanel(new GridBagLayout());
        servicePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2), "Services:", 0, 0, new Font("Bodoni", Font.BOLD, 15), Color.BLACK));
        servicePanel.setBackground(Color.DARK_GRAY);
        mainPanel.add(servicePanel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        for (int i = 0; i < 3; i++) {
            JLabel descLabel = new JLabel((i + 1) + ". Description:");
            descLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
            descLabel.setForeground(Color.LIGHT_GRAY);
            gbc.gridx = 0;
            gbc.gridy = i;
            servicePanel.add(descLabel, gbc);

            JTextField descE = new JTextField(20);
            gbc.gridx = 1;
            gbc.gridy = i;
            servicePanel.add(descE, gbc);

            JLabel taxableLabel = new JLabel("Taxable Value:");
            taxableLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
            taxableLabel.setForeground(Color.LIGHT_GRAY);
            gbc.gridx = 2;
            gbc.gridy = i;
            servicePanel.add(taxableLabel, gbc);

            JTextField taxableE = new JTextField(10);
            gbc.gridx = 3;
            gbc.gridy = i;
            servicePanel.add(taxableE, gbc);
        }

        // PDF generation logic
        generatePDFButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Fetch data from JTextField components
                String name = nameE.getText();
                String gst = gstE.getText();
                String billNumber = billE.getText();
                String date = dateE.getText();
                String address = addrE.getText();
                String state = (String) stateComboBox.getSelectedItem();
                String[] descriptions = new String[3];
                String[] taxValues = new String[3];

                for (int i = 0; i < 3; i++) {
                    descriptions[i] = ((JTextField) servicePanel.getComponent((i * 4) + 1)).getText();
                    taxValues[i] = ((JTextField) servicePanel.getComponent((i * 4) + 3)).getText();
                }

                // Validate if all necessary fields are filled
                if (name.isEmpty() || billNumber.isEmpty() || date.isEmpty() || address.isEmpty() || descriptions[0].isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Choose location to save the PDF
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save PDF As");
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF files", "pdf"));
                int userSelection = fileChooser.showSaveDialog(frame);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    String pdfFilename = fileChooser.getSelectedFile().getAbsolutePath();
                    if (!pdfFilename.endsWith(".pdf")) {
                        pdfFilename += ".pdf";
                    }

                    // Create PDF
                    try {
                        Document document = new Document();
                        PdfWriter.getInstance(document, new FileOutputStream(pdfFilename));
                        document.open();

                        document.add(new Paragraph("Client Details:"));
                        document.add(new Paragraph("Name: " + name));
                        document.add(new Paragraph("GSTIN: " + gst));
                        document.add(new Paragraph("Bill Number: " + billNumber));
                        document.add(new Paragraph("Date: " + date));
                        document.add(new Paragraph("Address: " + address));
                        document.add(new Paragraph("State of service: " + state));
                        document.add(new Paragraph(" ")); // Add a blank line
                        document.add(new Paragraph("Services:"));
                        for (int i = 0; i < descriptions.length; i++) {
                            if (!descriptions[i].isEmpty()) {
                                document.add(new Paragraph((i + 1) + ". Description: " + descriptions[i]));
                                document.add(new Paragraph("   Taxable Value: " + taxValues[i]));
                            }
                        }

                        document.close();
                        JOptionPane.showMessageDialog(frame, "PDF generated successfully: " + pdfFilename, "Success", JOptionPane.INFORMATION_MESSAGE);
                    } catch (DocumentException | java.io.IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error generating PDF: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        frame.setVisible(true);
    }
}
