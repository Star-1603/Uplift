public AuthenticationFrame() {
    // Set up the frame
    setTitle("Authentication");
    setSize(300, 200);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Create a panel for the username and password fields
    JPanel fieldsPanel = new JPanel();
    fieldsPanel.setLayout(new GridLayout(2, 2));

    // Create the username field
    JLabel usernameLabel = new JLabel("Username:");
    usernameField = new JTextField();

    // Create the password field
    JLabel passwordLabel = new JLabel("Password:");
    passwordField = new JPasswordField();

    // Add the fields to the panel
    fieldsPanel.add(usernameLabel);
    fieldsPanel.add(usernameField);
    fieldsPanel.add(passwordLabel);
    fieldsPanel.add(passwordField);

    // Create a panel for the buttons
    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new FlowLayout());

    // Create the login button
    loginButton = new JButton("Login");
    loginButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            login();
        }
    });

    // Create the register button
    registerButton = new JButton("Register");
    registerButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            register();
        }
    });

    // Add the buttons to the panel
    buttonsPanel.add(loginButton);
    buttonsPanel.add(registerButton);

    // Add the panels to the frame
    add(fieldsPanel, BorderLayout.CENTER);
    add(buttonsPanel, BorderLayout.SOUTH);

    // Connect to the database
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "username", "password");
    } catch (SQLException e) {
        System.out.println("Error connecting to database: " + e.getMessage());
    }
}

private void login() {
    // Get the username and password from the fields
    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());

    // Check if the username and password are valid
    if (authenticateUser(username, password)) {
        // Login successful, open the main frame
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
        dispose();
    } else {
        // Login failed, show an error message
        JOptionPane.showMessageDialog(this, "Invalid username or password");
    }
}

private void register() {
    // Get the username and password from the fields
    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());

    // Check if the username is already taken
    if (isUsernameTaken(username)) {
        // Username is taken, show an error message
        JOptionPane.showMessageDialog(this, "Username is already taken");
    } else {
        // Register the user
        registerUser(username, password);
        // Login the user
        login();
    }
}

private boolean authenticateUser(String username, String password) {
    try {
        // Prepare a SQL query to check the user's credentials
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        // Create a prepared statement
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Set the username and password parameters
        pstmt.setString(1, username);
        pstmt.setString(2, password);

        // Execute the query
        ResultSet rs = pstmt.executeQuery();

        // Check if the user exists
        if (rs.next()) {
            // User exists, return true
            return true;
        } else {
            // User does not exist, return false
            return false;
        }
    } catch (SQLException e) {
        System.out.println("Error authenticating user: " + e.getMessage());
        return false;
    }
}

private boolean isUsernameTaken(String username) {
    try {
        // Prepare a SQL query to check if the username is taken
        String query = "SELECT * FROM users WHERE username = ?";

        // Create a prepared statement
        PreparedStatement pstmt = conn.prepareStatement(query);

        // Set the username parameter
        pstmt.setString(1, username);

        // Execute the query
        ResultSet rs = pstmt.executeQuery();

        // Check if the username is taken
        if (rs.next()) {
            // Username is taken, return true
            return true;
        } else {
            // Username is not taken, return false
            return false;
       