package Doctor.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;


public class UserSearchController implements Initializable
{
    @FXML
    private JFXTextField usernameField;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        System.out.println("do nothing here");
    }

    public static String encrypt(byte[] key, byte[] initVector, String value)
    {
        // TODO: Move to own class
        try
        {
            IvParameterSpec iv = new IvParameterSpec(initVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.encodeBase64String(encrypted);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

    public void CPRButtonAction(ActionEvent event)
    {
        //System.out.println("establish mysql connection and save connection. if failed, notify using messagebox");

        // TODO: Put this shit in JournalMakerController
        String cprString = usernameField.getText();

        SecureRandom random = new SecureRandom();

        byte key[] = new byte[32]; // 256 bits
        random.nextBytes(key);

        byte IV[] = new byte[16]; // 128 bits
        random.nextBytes(IV);

        System.out.println(encrypt(key, IV, cprString));

        byte[] keyIV = new byte[key.length + IV.length];
        System.arraycopy(key, 0, keyIV, 0, key.length);
        System.arraycopy(IV, 0, keyIV, key.length, IV.length);

        String aesKeyBase64 = Base64.encodeBase64String(keyIV);

        try
        {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/p2","root","ascent"); // p2 is db name
            Statement stmt = con.createStatement();

            // Execute SQL query to insert the "transaction"
            stmt.executeUpdate("INSERT INTO tran (cpr, trans_id, aes_key) VALUES (3112999999,'f9777aae-ef68-44e7-bc9b-8758c20069a3','"+aesKeyBase64+"')");
        }
        catch(Exception e)
        {
            System.out.printf(e.getMessage());
        }



        /*
        if (cprString.length() != 10)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Søgningsfejl");
            alert.setHeaderText("Fejl med input af CPR-nummer");
            alert.setContentText("CPR-nummeret skal være på 10 cifre");
            alert.show();
            return;
        }

        if (!cprString.matches("[0-9]+"))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Søgningsfejl");
            alert.setHeaderText("Fejl med input af CPR-nummer");
            alert.setContentText("Der må kun være tal i et CPR-nummer");
            alert.show();
            return;
        }

        */


        /*

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database forbindelsesfejl");
            alert.setHeaderText("Fejl med forbindelse til database");
            alert.setContentText("Der opstod et problem med forbindelsen til databasen, venligst prøv igen.");
            alert.show();
         */


        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Design/UserView.fxml"));
            Parent root1 = fxmlLoader.load();

            UserViewController controller = fxmlLoader.getController(); // Pass params to UserViewController using method
            controller.test(usernameField.getText());

            Stage stage = new Stage();
            stage.setTitle("User View");
            stage.setScene(new Scene(root1,500,500));
            stage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
