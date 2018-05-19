package Doctor.Controller;

import Doctor.Block;
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
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Collections;
import java.util.List;
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

    private PrivateKey privateKey;
    private String patientPublicKey;
    private List<Block> blockList;

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
            controller.passPrivateKey(privateKey);
            controller.passPatientPublicKey(patientPublicKey);

            if (blockList.isEmpty())
                controller.passBlockId(null);
            else
                controller.passBlockId(blockList.get(0).id);

            Stage stage = new Stage();
            stage.setTitle("Journal Maker");
            stage.setScene(new Scene(root1, 830, 600));
            stage.show();
        }
        catch (IOException e)
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

    public void passBlockList(List<Block> blockListParam)
    {
        // A blockchain goes from oldest to newest, therefore reverse the list to get the latest journal at the top
        Collections.reverse(blockListParam);

        for (Block block : blockListParam)
            list.getItems().add(new Label(block.data));

        blockList = blockListParam;
    }

    public void passPrivateKey(PrivateKey privKey)
    {
        privateKey = privKey;
    }

    public void passPatientPublicKey(String pubKey)
    {
        patientPublicKey = pubKey;
    }
}
