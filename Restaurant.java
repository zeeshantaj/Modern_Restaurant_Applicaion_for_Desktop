package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FoodOrderApp extends JPanel {

    private JButton[] buttons;
    List<Integer> itemPrices = new ArrayList<>();
    private String[] foodNames;
    private int[] foodPrices;
    private ImageIcon[] foodImages;
    private JProgressBar jb;
    private JFrame frame;
    List<String> itemsAdded = new ArrayList<>();
    private JList<String> list;
    int totalPrice,price;
    private StringBuilder sb;


    public FoodOrderApp() {



        list = new JList<>(new DefaultListModel<>());
        DefaultListModel<String> model = new DefaultListModel<>();


        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(300, 200));


        // Add the text area to the frame

        frame = new JFrame("Restaurant");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.add(scrollPane, BorderLayout.EAST);


        JPanel foodPanel = new JPanel();
        foodPanel.setLayout(new GridLayout(3, 2, 10, 10));
        foodPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createTitledBorder("Menu")));



        // Initialize food names, prices, and images
        foodNames = new String[]{"Pizza", "Pizza chicken fajita", "Pasta Plain",
                "Chicken Pasta", "Zinger Burger", "kentucky burger", "Crispy chicken","Vitnames Souce",
                "Tea","Coffe","Cold Drink"};
        foodPrices = new int[]{399, 499, 240, 320, 350, 530, 580,60,90,110,70};



        foodImages = new ImageIcon[]{
                new ImageIcon(getClass().getResource("/images/pizza.png")),
                new ImageIcon(getClass().getResource("/images/pizza.png")),
                new ImageIcon(getClass().getResource("/images/pasta_chicken.png")),
                new ImageIcon(getClass().getResource("/images/pasta_chicken.png")),
                new ImageIcon(getClass().getResource("/images/done.png")),
                new ImageIcon(getClass().getResource("/images/br4.png")),
                new ImageIcon(getClass().getResource("/images/crispy_chicken.png")),
                new ImageIcon(getClass().getResource("/images/souce.png")),
                new ImageIcon(getClass().getResource("/images/tea.png")),
                new ImageIcon(getClass().getResource("/images/coffe.png")),
                new ImageIcon(getClass().getResource("/images/coke.jpg"))

        };

        // Set the layout to a 2x2 grid
        setLayout(new GridLayout(2, 2));

        // Initialize buttons
        buttons = new JButton[foodNames.length];

        for (int i = 0; i < foodNames.length; i++) {
            // Create a new button with the food name, price, and image
            JButton button = new JButton();
            button.setSize(200,200);
            //button.setPreferredSize(new Dimension(100, 50));
            button.setFont(new Font("Arial", Font.BOLD, 24));
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setText("<html><center>" + foodNames[i] + "<br>" +"Rs "+ foodPrices[i] + "</center></html>");
            //button.setIcon(foodImages[i]);

            ImageIcon icon = foodImages[i];
            Image img = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            icon.setImage(img);
            button.setIcon(icon);

            JScrollPane scrollPaneBtn = new JScrollPane(button);
            frame.add(scrollPaneBtn,BorderLayout.WEST);

            final int finalIt = i;
            // Add an action listener to the button
            button.addActionListener(e -> {

                price = foodPrices[finalIt];


                //selectedButtons.add(button);

                itemsAdded.add(foodNames[finalIt] + " - " + "Rs" + foodPrices[finalIt]);
                itemPrices.add(price);

                totalPrice += price;
                //to show text of order
                sb = new StringBuilder(textArea.getText());
                sb.append(foodNames[finalIt]).append(" - ").append("Rs"+foodPrices[finalIt]).append("\n");
                textArea.setFont(new Font("Arial", Font.BOLD, 24));
                textArea.setText(sb.toString());


            });
            this.add(button,BorderLayout.WEST);



            // Save the button to the buttons array for later use
            buttons[i] = button;
        }

        this.setLayout(new GridLayout(4, 3, 10, 10));

// Add a border and title to the panel
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createTitledBorder("Menu")));


        JTextField totalPriceField = new JTextField(10);
        totalPriceField.setPreferredSize(new Dimension(300,100));
        totalPriceField.setMargin(getInsets());
        totalPriceField.setFont(new Font("Arial",Font.BOLD,24));
        totalPriceField.setEditable(false); // make it read-only
        frame.add(totalPriceField,BorderLayout.SOUTH);


        JButton removeItem = new JButton("Clear Last Added Item");
        removeItem.setBackground(Color.RED);
        removeItem.setFont(new Font("Aria",Font.BOLD,30));
        removeItem.addActionListener(e -> {

//            String text = textArea.getText();
//            if (!text.isEmpty()) {
//                int lastNewLineIndex = text.lastIndexOf("\n");
//                if (lastNewLineIndex != -1) {
//                    textArea.setText(text.substring(0, lastNewLineIndex));
//                } else {
//                    textArea.setText("");
//                }
//            }
            if (itemsAdded.size() > 0) {
                // Get the last item added to the textArea
                String lastItemAdded = itemsAdded.get(itemsAdded.size() - 1);

                // Get the index of the last occurrence of the last item added in the textArea
                int lastItemIndex = textArea.getText().lastIndexOf(lastItemAdded);

                if (lastItemIndex != -1) {
                    // Remove the last item from the textArea
                    sb = new StringBuilder(textArea.getText());
                    sb.delete(lastItemIndex, lastItemIndex + lastItemAdded.length() + 1);
                    textArea.setText(sb.toString());

                    // Remove the last item from the itemsAdded list
                    itemsAdded.remove(itemsAdded.size() - 1);

                    int lastItemPrice = itemPrices.get(itemPrices.size() - 1);
                    itemPrices.remove(itemPrices.size() - 1);

                    // Update the total price
                    totalPrice -= lastItemPrice;

                }
            }


        });

        add(removeItem);




        JButton orderBtn = new JButton("Order");
        orderBtn.setSize(150,75);
        orderBtn.setFont(new Font("Aria",Font.BOLD,30));
        orderBtn.setBackground(Color.yellow);
        orderBtn.addActionListener(e -> {

            if (textArea.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please select an item from the list.");
            } else {
                int choice = JOptionPane.showConfirmDialog(frame, "Are you sure you want to print this order?", "Order Confirm", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
                        String fileName = "Restaurant_orders_" + dateFormat.format(new Date()) + ".txt";
                        File file = new File(fileName);

                        // check if file exists, if not create a new file with header
                        if (!file.exists()) {
                            FileWriter headerWriter = new FileWriter(file);
                            headerWriter.write("Order Details\n");
                            headerWriter.close();
                        }

                        // read last order no. from file
                        int orderNo = 1;
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        String line = reader.readLine(); // skip header line
                        while ((line = reader.readLine()) != null) {
                            // use a regular expression to match the order number at the beginning of each line
                            String pattern = "^Order No(\\d+)";
                            Pattern r = Pattern.compile(pattern);
                            Matcher m = r.matcher(line);
                            if (m.find()) {
                                try {
                                    int orderNoFromFile = Integer.parseInt(m.group(1));
                                    if (orderNoFromFile >= orderNo) {
                                        orderNo = orderNoFromFile + 1;
                                    }
                                } catch (NumberFormatException ex) {
                                    // ignore line if order number is not a valid integer
                                }
                            }
                        }
                        reader.close();

                        // write new order details to file
                        FileWriter writer = new FileWriter(file, true); // append mode
                        writer.write("\nOrder No " + orderNo + "\n");
                        String[] orderDetails = textArea.getText().split("\n");

                        int orderTotalInt = totalPrice;

                        for (String orderDetail : orderDetails) {
                            writer.write(orderDetail + "\n");

                        }
                        writer.write("Total Price Rs" + orderTotalInt + "\n"); // write the total to the file

                        writer.close();
                        System.out.println("Order details saved to " + file.getAbsolutePath());

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    textArea.setText("");
                    totalPriceField.setText("Total Price Rs "+totalPrice);
                    DefaultListModel listModel = (DefaultListModel) list.getModel();
                    listModel.removeAllElements();
                    totalPrice = 0;
                    for (int i = 0; i < buttons.length; i++) {
                        buttons[i].setSelected(false);
                    }
                }
            }

        });
        add(orderBtn);

        frame.add(this);
        frame.pack();
    }

    public static void main(String[] args) {


        FoodOrderApp  foodOrderApp = new FoodOrderApp();
        SwingUtilities.invokeLater(() -> foodOrderApp.frame.setVisible(true));

    }
}
