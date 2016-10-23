/**
 * Sample Skeleton for 'sample.fxml' Controller Class
 */

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Controller {

    @FXML
    private BorderPane bpane;
    @FXML
    private StackPane root;

    @FXML
    private VBox editpane;


    @FXML
    private JFXTabPane tabpane;

    private JFXSnackbar snackbar;


    @FXML
    void save(ActionEvent event) {
        snackbar.show("저장되었습니다.", 2000);

    }


    @FXML
    void about(ActionEvent event) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("QR컴파일러에 오신것을 환영합니다."));
        ((Text) content.getHeading().get(0)).setFont(Font.font("Roboto", 17));
        content.setBody(new Text("이 프로그램은 작성한 소스코드를 QR코드, 혹은 QR코드 묶음으로 변환하여 \n취급, 전달, 공유를 간편하게 만든게 특징입니다.\n\n또한, 서버에 저장하여 계정만 있다면 어디에나, 어느 컴퓨터, 어느 운영체제에서든\n사용이 가능하게하는 기능을 제공합니다.\n\nQR코드를 활용한 다양한 기능들을 사용해보세요!\n\nCreated By: 정승욱"));
        ((Text) content.getBody().get(0)).setFont(Font.font("Roboto", 12));
        final JFXDialog jfxDialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("확인");
        button.setStyle("-fx-text-fill: #2196F3;");
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                jfxDialog.close();
            }
        });
        content.setActions(button);
        jfxDialog.show();
    }

    @FXML
    void saveSource(ActionEvent event) {
        snackbar.show("저장되었습니다.", 2000);
    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        snackbar = new JFXSnackbar(editpane);
        snackbar.setPrefWidth(300);
        snackbar.getStylesheets().add(getClass().getResource("snackbar_styles.css").toExternalForm());
        snackbar.setId("snackbar");
        snackbar.setStyle("-fx-text-fill: #FFFFFF;");
    }
}
