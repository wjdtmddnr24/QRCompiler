/**
 * Sample Skeleton for 'sample.fxml' Controller Class
 */

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.StringEscapeUtils;
import org.mozilla.universalchardet.UniversalDetector;
import org.w3c.dom.Document;
import sun.plugin.javascript.ocx.JSObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;


public class Controller {

    @FXML
    private BorderPane bpane;
    @FXML
    private StackPane root;

    @FXML
    private VBox editpane;

    @FXML
    private WebView editwebview;


    @FXML
    private JFXListView<Label> qrlistView;


    @FXML
    private ComboBox<String> comboBoxStyle;
    @FXML
    private ComboBox<Integer> comboBoxSize;
    @FXML
    private ComboBox<Label> comboBoxFont;
    @FXML
    private BorderPane editborderPane;
    @FXML
    private BorderPane QRPANEQ;


    @FXML
    private JFXTabPane tabpane;

    private JFXSnackbar snackbar;
    private String[] themes = new String[]{"xcode", "ambiance", "chaos", "chrome", "clouds", "clouds_midnight", "cobalt", "crimson_editor", "dawn", "dreamweaver", "clipse", "github", "idle_fingers", "iplastic", "katzenmilch", "kr_theme", "kuroir", "merbivore", "merbivore_soft", "mono_industrial", "monokai", "pastel_on_dark", "solarized_dark", "solarized_light", "sqlserver", "terminal", "textmate", "tomorrow", "tomorrow_night", "tomorrow_night_blue", "tomorrow_night_bright", "tomorrow_night_eighties", "twilight", "vibrant_ink"};
    private JFXSpinner spinner;

    @FXML
    void save(ActionEvent event) {
        snackbar.show("저장되었습니다.", 2000);
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        String content = (String) clipboard.getContent(DataFormat.PLAIN_TEXT);
        content = StringEscapeUtils.escapeEcmaScript(content);
        String query = "editor.insert('" + content + "');";
        System.out.print(query);
        editwebview.getEngine().executeScript(query);
        if (!editwebview.isFocused()) {
            editwebview.requestFocus();
        }

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
    void openSourceCode(ActionEvent event) {
        System.out.println("OPEN");
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("소스 코드", "*.c", "*.cpp", "*.java", "*.php", "*.asp", "*.html", "*.htm", "*.js", "*.css", "*.py", "*.sh", "*.rua", "*.jsp", "*.pl", "*.fs", "*.bas", "*.ss", "*.s", "*.swift", "*.cc", "*.pdml", "*.lss", "*.lsp", "*.cp", "*.phps", "*.txt"));
        final File sourceCodeFile = chooser.showOpenDialog(null);
        if (sourceCodeFile != null && eraseSourceCode()) {
            editborderPane.centerProperty().set(spinner);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        byte[] buf = new byte[4096];
                        java.io.FileInputStream fis = new java.io.FileInputStream(sourceCodeFile);
                        UniversalDetector detector = new UniversalDetector(null);
                        int nread;
                        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                            detector.handleData(buf, 0, nread);
                        }
                        detector.dataEnd();
                        String encoding = detector.getDetectedCharset();
                        if (encoding != null) {
                            System.out.println("Detected encoding = " + encoding);
                        } else {
                            System.out.println("No encoding detected.");
                        }
                        BufferedReader bufferedReader;
                        if (encoding != null) {
                            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceCodeFile), encoding));
                        } else {
                            Platform.runLater(new Runnable() {
                                public void run() {
                                    snackbar.show("인코딩 타입을 감지하지 못하였습니다. 문자가 정상적으로 나오지 않을수 있습니다.", 2000);
                                }
                            });
                            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceCodeFile)));
                        }
                        final StringBuilder stringBuilder = new StringBuilder();
                        String tmp;
                        while ((tmp = bufferedReader.readLine()) != null) {
                            stringBuilder.append(tmp + "\n");
                        }
                        detector.handleData(stringBuilder.toString().getBytes(), 0, stringBuilder.toString().getBytes().length);
                        detector.dataEnd();
                        System.out.println(detector.getDetectedCharset());

                        Platform.runLater(new Runnable() {
                            public void run() {
                                editwebview.getEngine().executeScript("editor.setValue('" + StringEscapeUtils.escapeEcmaScript(stringBuilder.toString()) + "')");

                            }
                        });

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        Platform.runLater(new Runnable() {
                            public void run() {
                                editborderPane.centerProperty().set(editwebview);
                            }
                        });
                    }
                }
            }).start();
        }


    }

    @FXML
    void saveSource(ActionEvent event) {
        snackbar.show("저장되었습니다.", 2000);
    }

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        spinner = new JFXSpinner();

        snackbar = new JFXSnackbar(editpane);
        snackbar.setPrefWidth(300);
        snackbar.getStylesheets().add(getClass().getResource("snackbar_styles.css").toExternalForm());
        snackbar.setId("snackbar");
        snackbar.setStyle("-fx-text-fill: #FFFFFF;");
        editwebview.getEngine().setJavaScriptEnabled(true);
        editwebview.getEngine().load(getClass().getResource("src-noconflict/index.html").toExternalForm());
       /* editwebview.getEngine().documentProperty().addListener(new ChangeListener<Document>() {
            public void changed(ObservableValue<? extends Document> prop, Document oldDoc, Document newDoc) {
                enableFirebug(editwebview.getEngine());
            }
        });
*/

        setListenerEditWebView(editwebview);
        setupcomboBoxStyle(comboBoxStyle);
        setupComboBoxSize(comboBoxSize);
        setupcomboBoxFont(comboBoxFont);
        setupQRCode();

    }

    private void setupQRCode() {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            Label lbl = new Label();
            lbl.setPrefSize(300, 300);
            lbl.setStyle("-fx-background-color:rgb(" + r.nextInt(255) + "," + r.nextInt(255) + "," + r.nextInt(255) + ");");
            qrlistView.getItems().add(lbl);
        }
    }

    private void enableFirebug(final WebEngine engine) {
        editwebview.getEngine().executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");
    }

    private void setListenerEditWebView(final WebView editwebview) {
        editwebview.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                if (event.isControlDown() && event.getCode() == KeyCode.V) {
                    final Clipboard clipBoard = Clipboard.getSystemClipboard();
                    String content = (String) clipBoard.getContent(DataFormat.PLAIN_TEXT);
                    if (content == null) {
                        return;
                    }
                    content = StringEscapeUtils.escapeEcmaScript(content);
                    String query = "editor.insert('" + content + "');";
                    editwebview.getEngine().executeScript(query);
                }
                if (event.isControlDown() && event.getCode() == KeyCode.S) {
                    //TODO 저장 기능
                    snackbar.show("저장되었습니다.", 2000);
                }
            }
        });
        editwebview.setOnScroll(new EventHandler<ScrollEvent>() {
            public void handle(ScrollEvent event) {
                if (event.isControlDown() && event.getDeltaY() < 0) {
                    System.out.println("down");
                    if (comboBoxSize.getSelectionModel().getSelectedIndex() > 0) {
                        comboBoxSize.getSelectionModel().select(comboBoxSize.getSelectionModel().getSelectedIndex() - 1);
                        int size = comboBoxSize.getSelectionModel().getSelectedItem();
                        System.out.println("document.getElementById('editor').style.fontSize='" + size + "px';");
                        editwebview.getEngine().executeScript("document.getElementById('editor').style.fontSize='" + size + "px';");
                    }
                }
                if (event.isControlDown() && event.getDeltaY() > 1) {
                    System.out.println("up");
                    if (comboBoxSize.getSelectionModel().getSelectedIndex() < comboBoxSize.getItems().size() - 1) {
                        comboBoxSize.getSelectionModel().select(comboBoxSize.getSelectionModel().getSelectedIndex() + 1);
                        int size = comboBoxSize.getSelectionModel().getSelectedItem();
                        System.out.println("document.getElementById('editor').style.fontSize='" + size + "px';");
                        editwebview.getEngine().executeScript("document.getElementById('editor').style.fontSize='" + size + "px';");
                    }
                }
            }
        });
    }

    private void setupComboBoxSize(final ComboBox<Integer> comboBoxSize) {
        for (int i = 0; i < 11; i++) {
            this.comboBoxSize.getItems().add(10 + i * 2);
        }
        comboBoxSize.getSelectionModel().select(3);
        comboBoxSize.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                int size = comboBoxSize.getSelectionModel().getSelectedItem();
                System.out.println("document.getElementById('editor').style.fontSize='" + size + "px';");
                editwebview.getEngine().executeScript("document.getElementById('editor').style.fontSize='" + size + "px';");
            }
        });
    }

    private void setupcomboBoxStyle(final ComboBox<String> comboBoxStyle) {
        for (int i = 0; i < themes.length; i++) {
            this.comboBoxStyle.getItems().add(themes[i]);
        }
        comboBoxStyle.getSelectionModel().selectFirst();
        comboBoxStyle.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String theme = comboBoxStyle.getSelectionModel().getSelectedItem();
                System.out.println("editor.setTheme(\"ace/theme/" + theme + "\");");
                editwebview.getEngine().executeScript("editor.setTheme(\"ace/theme/" + theme + "\");");
            }
        });

    }

    private void setupcomboBoxFont(final ComboBox<Label> comboBoxFont) {
        for (int i = 0; i < Font.getFamilies().size(); i++) {
            Label label = new Label(Font.getFamilies().get(i));
            label.setFont(Font.font(Font.getFamilies().get(i)));
            label.setStyle("-fx-text-fill: rgb(100.0, 100.0, 100.0)");
            comboBoxFont.getItems().add(label);
        }
        comboBoxFont.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String fontfamily = comboBoxFont.getSelectionModel().getSelectedItem().getText();
                editwebview.getEngine().executeScript("editor.setOptions({fontFamily: \"" + fontfamily + "\"});");
            }
        });
    }

    private boolean eraseSourceCode() {
        editwebview.getEngine().executeScript("editor.setValue('');  editor.getSession().setUndoManager(new ace.UndoManager());");
        return true;
    }
}
