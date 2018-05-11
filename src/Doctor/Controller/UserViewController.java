package Doctor.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class UserViewController implements Initializable
{
    @FXML
    private AnchorPane pane;

    @FXML
    private JFXListView<Label> list;

    @FXML
    private Label nameOrCPR;

    @FXML
    private JFXButton makeJournal;

    @FXML
    private JFXButton editJournal;

    @FXML
    private JFXButton deleteJournal;

    private String passedEZ = "123";

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        System.out.println("pass mysql connection to this controller from UserSearchController then use it");
    }

    @FXML
    public void deleteJournalButton(ActionEvent event)
    {

    }

    @FXML
    public void editJournalButton(ActionEvent event)
    {
      //  findTransactions();
        UserSearchController userSearchController = new UserSearchController();
        System.out.println(userSearchController.getCprTextField());
    }

    @FXML
    public void makeJournalButton(ActionEvent event)
    {
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Design/Journalmaker.fxml"));
            Parent root1 = fxmlLoader.load();

            JournalMakerController controller = fxmlLoader.getController(); // Pass params to PatientViewController using method

            Stage stage = new Stage();
            stage.setTitle("Journal Maker");
            stage.setScene(new Scene(root1, 830, 600));
            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMouseClick(MouseEvent mouseEvent)
    {
        if (mouseEvent.getClickCount() == 2)
            System.out.println("Double clicked");

        System.out.println("clicked on " + list.getSelectionModel().getSelectedItem().getText());
    }

    public void findTransactions(String searchedCPR)
    {
        try
        {
            Connection con = DriverManager.getConnection("jdbc:mysql://195.201.113.131:3306/p2?autoReconnect=true&useSSL=false","sembrik","lol123"); // p2 is db name
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT transid FROM TransDB WHERE cpr = "+ searchedCPR +"");

            while(rs.next())
                list.getItems().add(new Label(rs.getString(1)));
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
