package email.sender;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class EmailApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        TextField emailField = new TextField();
        emailField.setPromptText("Your Gmail address");

        PasswordField appPasswordField = new PasswordField();
        appPasswordField.setPromptText("App password");

        TextField newEmailField = new TextField();
        newEmailField.setPromptText("Add recipient email");

        Button addEmailButton = new Button("Add Email");

        ListView<CheckBox> emailListView = new ListView<>();
        loadEmailsFromFiles(emailListView);


        TextField subjectField = new TextField();
        subjectField.setPromptText("Subject");

        TextArea messageArea = new TextArea();
        messageArea.setPromptText("Message body");

        Button sendButton = new Button("Send Emails");
        Label statusLabel = new Label();

        addEmailButton.setOnAction(e -> {
            String newEmail = newEmailField.getText().trim();
            if (!newEmail.isEmpty()) {
                emailListView.getItems().add(new CheckBox(newEmail));
                newEmailField.clear();

                saveEmailsToFile(emailListView);

            }
        });

        sendButton.setOnAction(e -> {
            String email = emailField.getText().trim();
            String appPassword = appPasswordField.getText().trim();
            String subject = subjectField.getText().trim();
            String message = messageArea.getText().trim();

            List<String> recipients = emailListView.getItems().stream()
                    .filter(CheckBox::isSelected)
                    .map(CheckBox::getText)
                    .collect(Collectors.toList());

            if (recipients.isEmpty()) {
                statusLabel.setText(" No recipients selected.");
                return;
            }

            EmailSender sender = new EmailSender(email, appPassword);
            String resultLog = sender.sendBatchEmails(recipients, subject, message);
            statusLabel.setText(resultLog);

        });

        VBox layout = new VBox(10,
                emailField,
                appPasswordField,
                newEmailField,
                addEmailButton,
                emailListView,
                subjectField,
                messageArea,
                sendButton,
                statusLabel);
        VBox sendTabContent = new VBox(10,
                emailField,
                appPasswordField,
                subjectField,
                messageArea,
                sendButton,
                statusLabel);
        sendTabContent.setStyle("-fx-padding: 15;");

        Tab sendTab = new Tab("Send Emails", sendTabContent);
        sendTab.setClosable(false);

// Tab Manage Recipients
        VBox manageTabContent = new VBox(10,
                newEmailField,
                addEmailButton,
                emailListView);
        manageTabContent.setStyle("-fx-padding: 15;");

        Tab manageTab = new Tab("Manage Recipients", manageTabContent);
        manageTab.setClosable(false);

//  Create TabPane
        TabPane tabPane = new TabPane(sendTab, manageTab);

        Scene scene = new Scene(tabPane, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Batch Email Sender");
        primaryStage.show();


    }
    public void loadEmailsFromFiles(ListView<CheckBox> emailListView) {
        try {
            java.nio.file.Files.lines(java.nio.file.Path.of("emails.txt"))
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .forEach(email -> emailListView.getItems().add(new CheckBox(email)));

        } catch (Exception e) {
            System.err.println("Could not load saved emails: " + e.getMessage());

        }
    }

    private void saveEmailsToFile(ListView<CheckBox> emailListView) {
        try{
            List<String> emails = emailListView.getItems().stream()
                    .map(CheckBox::getText)
                    .distinct()
                    .toList();
            java.nio.file.Files.write(java.nio.file.Path.of("emails.txt"), emails);
        }catch (Exception e) {
            System.err.println("Faild to send emails: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
