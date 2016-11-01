/**
 * Sample Skeleton for 'sample.fxml' Controller Class
 */

import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mozilla.universalchardet.UniversalDetector;
import org.w3c.dom.Document;

import java.io.*;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Controller {

    @FXML
    private StackPane editStackPane;
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
    private ComboBox<String> comboBoxLanguage;
    @FXML
    private ComboBox<String> compileSyntax;
    @FXML
    private BorderPane editborderPane;
    @FXML
    private BorderPane QRPANEQ;
    @FXML
    private JFXTabPane tabpane;
    @FXML
    private JFXTextArea textAreaOutput;
    @FXML
    private AnchorPane compilerPane;
    @FXML
    private SplitPane compilerSplitPane;
    @FXML
    private JFXTextArea consoleInput;
    @FXML
    private StackPane consoleBar;
    @FXML
    private TextArea consoleOutput;
    @FXML
    private JFXButton compileButton;

    private static File workingSourceCodeFile;
    private static String workingSourceCodeFileEncoding;
    private JFXSnackbar snackbar;
    private String[] themes = new String[]{"xcode", "ambiance", "chaos", "chrome", "clouds", "clouds_midnight", "cobalt", "crimson_editor", "dawn", "dreamweaver", "clipse", "github", "idle_fingers", "iplastic", "katzenmilch", "kr_theme", "kuroir", "merbivore", "merbivore_soft", "mono_industrial", "monokai", "pastel_on_dark", "solarized_dark", "solarized_light", "sqlserver", "terminal", "textmate", "tomorrow", "tomorrow_night", "tomorrow_night_blue", "tomorrow_night_bright", "tomorrow_night_eighties", "twilight", "vibrant_ink"};
    private String[] languages = new String[]{"java", "c_cpp", "csharp", "python", "swift", "lua", "javascript", "html", "jsp", "php", "perl", "css", "xml", "django", "json", "assembly_x86", "fortran", "powershell", "groovy", "abap", "abc", "actionscript", "ada", "apache_conf", "applescript", "asciidoc", "autohotkey", "atchfile", "behaviour", "c9search", "cirru", "clojure", "cobol", "coffee", "coffee_worker", "coldfusion", "oldfusion_test", "css_completions", "css_test", "css_worker", "css_worker_test", "curly", "d", "dart", "diff", "dockerfile", "dot", "drools", "eiffel", "ejs", "elixir", "elm", "erlang", "forth", "ftl", "gcode", "gherkin", "gitignore", "glsl", "gobstones", "golang", "haml", "handlebars", "haskell", "haskell_cabal", "haxe", "html_completions", "html_elixir", "html_ruby", "html_test", "html_worker", "ini", "io", "jack", "jade", "json_worker", "json_worker_test", "jsoniq", "jsx", "julia", "kotlin", "latex", "less", "liquid", "lisp", "livescript", "logiql", "logiql_test", "lsl", "lua_worker", "luapage", "lucene", "makefile", "markdown", "mask", "matching_brace_outdent", "matching_parens_outdent", "matlab", "maze", "mel", "mushcode", "mysql", "nix", "nsis", "objectivec", "ocaml", "pascal", "pgsql", "php_completions", "php_test", "php_worker", "plain_text", "lain_text_test", "praat", "prolog", "properties", "protobuf", "python_test", "r", "razor", "razor_completions", "rdoc", "rhtml", "rst", "ruby", "ruby_test", "rust", "sass", "scad", "scala", "scheme", "scss", "sh", "sjs", "smarty", "snippets", "oy_template", "space", "sql", "sqlserver", "stylus", "svg", "tcl", "tex", "text", "text_test", "textile", "toml", "tsx", "twig", "typescript", "vala", "vbscript", "velocity", "verilog", "vhdl", "wollok", "xml_test", "xml_worker", "query", "xquery_worker", "yaml"};
    private JFXSpinner spinner;

    public static final int SAVE_CODE_STATUS_SUCCESS = 0;
    public static final int SAVE_CODE_STATUS_CANCELED = 1;
    public static final int SAVE_CODE_STATUS_FAILED = 2;
    public static final int SAVE_MODE_ANOTHER = 3;
    public static final int SAVE_MODE_OVERWRITE = 4;
    private static int compile_used_language = 0;
    private JFXDialog closeDialog;
    private JFXDialog aboutDialog;
    private List<Pair<String, Integer>> availableLanguages;
    private JFXDialog compileDialog;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private Stage stage;

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        availableLanguages = null;
        workingSourceCodeFile = null;
        workingSourceCodeFileEncoding = null;
        spinner = new JFXSpinner();
        snackbar = new JFXSnackbar(editpane);
        snackbar.setPrefWidth(300);
        snackbar.getStylesheets().add(getClass().getResource("snackbar_styles.css").toExternalForm());
        snackbar.setId("snackbar");
        snackbar.setStyle("-fx-text-fill: #FFFFFF;");
        editwebview.getEngine().setJavaScriptEnabled(true);
        editwebview.getEngine().load(getClass().getResource("src-noconflict/index.html").toExternalForm());
     /*   editwebview.getEngine().documentProperty().addListener(new ChangeListener<Document>() {
            public void changed(ObservableValue<? extends Document> prop, Document oldDoc, Document newDoc) {
                enableFirebug(editwebview.getEngine());
            }
        });
*/
        try {
            if (InetAddress.getByName("176.119.35.132").isReachable(3000)) {
                System.out.println("getting...");
                availableLanguages = QRHttpUtils.getAvailableLanguages();

                for (Pair<String, Integer> p : availableLanguages) {
                    compileSyntax.getItems().add(p.getLeft());
                }
                compileSyntax.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        compile_used_language = compileSyntax.getSelectionModel().getSelectedIndex();
                    }
                });
                compileSyntax.getSelectionModel().selectFirst();
            } else {
                compilerSplitPane.getItems().remove(compilerPane);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        setListenerEditWebView(editwebview);
        setupcomboBoxStyle(comboBoxStyle);
        setupComboBoxSize(comboBoxSize);
        setupcomboBoxFont(comboBoxFont);
        setupcomboBoxLanguage(comboBoxLanguage);
        setupQRCode();

    }

    @FXML
    void openSourceCode(ActionEvent event) {
        System.out.println("OPEN");
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("소스 코드", "*.c", "*.cpp", "*.java", "*.php", "*.asp", "*.html", "*.htm", "*.js", "*.css", "*.py", "*.sh", "*.rua", "*.jsp", "*.pl", "*.fs", "*.bas", "*.ss", "*.s", "*.swift", "*.cc", "*.pdml", "*.lss", "*.lsp", "*.cp", "*.phps", "*.txt"));
        final File sourceCodeFile = chooser.showOpenDialog(editwebview.getParent().getScene().getWindow());
        loadSourceCodeFile2(sourceCodeFile);
    }

    @Deprecated
    private void loadSourceCodeFile(final File sourceCodeFile) {
        if (sourceCodeFile != null && eraseSourceCode()) {
            workingSourceCodeFile = sourceCodeFile;
            editborderPane.centerProperty().set(spinner);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        byte[] buf = new byte[4096];
                        FileInputStream fis = new FileInputStream(sourceCodeFile);
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
                            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceCodeFile)));
                        }
                        final StringBuilder stringBuilder = new StringBuilder();
                        String tmp;
                        while ((tmp = bufferedReader.readLine()) != null) {
                            stringBuilder.append(tmp + "\n");
                        }
                        detector.handleData(stringBuilder.toString().getBytes(), 0, stringBuilder.toString().getBytes().length);
                        detector.dataEnd();
                        System.out.println(FileUtils.readFileToString(sourceCodeFile, encoding));
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

    private void loadSourceCodeFile2(final File sourceCodeFile) {
        if (sourceCodeFile != null && eraseSourceCode()) {
            workingSourceCodeFile = sourceCodeFile;
            editborderPane.centerProperty().set(spinner);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        byte[] buf = new byte[4096];
                        FileInputStream fis = new FileInputStream(sourceCodeFile);
                        UniversalDetector detector = new UniversalDetector(null);
                        int nread;
                        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                            detector.handleData(buf, 0, nread);
                        }
                        detector.dataEnd();
                        workingSourceCodeFileEncoding = detector.getDetectedCharset();
                        if (workingSourceCodeFileEncoding != null) {
                            System.out.println("Detected Encoding = " + workingSourceCodeFileEncoding);
                        } else {
                            System.out.println("No Encoding detected.");
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        final String value = FileUtils.readFileToString(workingSourceCodeFile, workingSourceCodeFileEncoding);
                        Platform.runLater(new Runnable() {
                            public void run() {
                                editwebview.getEngine().executeScript("editor.setValue('" + StringEscapeUtils.escapeEcmaScript(value) + "')");
                                stage.setTitle(workingSourceCodeFile != null ? new String("QR Compiler - [" + workingSourceCodeFile.getAbsolutePath() + "] - [" + workingSourceCodeFile.getName() + "]") : "QR Compiler");
                            }
                        });
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
        int respondCode = saveSourceCode(SAVE_MODE_OVERWRITE);
        switch (respondCode) {
            case SAVE_CODE_STATUS_SUCCESS:
                snackbar.show("저장하였습니다.", 2000);
                break;
            case SAVE_CODE_STATUS_FAILED:
                snackbar.show("저장하는데 실패하였습니다.", 2000);
                break;
            default:
                break;
        }
    }

    @FXML
    void saveAnotherSource(ActionEvent event) {
        int respondCode = saveSourceCode(SAVE_MODE_ANOTHER);
        switch (respondCode) {
            case SAVE_CODE_STATUS_SUCCESS:
                snackbar.show("저장하였습니다.", 2000);
                break;
            case SAVE_CODE_STATUS_FAILED:
                snackbar.show("저장하는데 실패하였습니다.", 2000);
                break;
            default:
                break;
        }
    }

    @FXML
    void compileSource(ActionEvent event) {

        System.out.println("compile");
        final String sourceCodeContent = getSourceCodeValueFromEditor();
        final int compileLanguageCode = availableLanguages.get(compile_used_language).getRight();
        String tempCompileInputContent = consoleInput.getText();
        tempCompileInputContent = (tempCompileInputContent == null || tempCompileInputContent.isEmpty()) ? "" : tempCompileInputContent;
        final String compileInputContent = tempCompileInputContent;
        final JFXSpinner spinner = new JFXSpinner();
        consoleBar.getChildren().add(spinner);
        consoleBar.setDisable(true);
        compileButton.setDisable(true);
        new Thread(new Runnable() {
            public void run() {
                String submissionQuery = QRHttpUtils.createSubmissionQuery(sourceCodeContent, compileLanguageCode, compileInputContent);
                System.out.println(submissionQuery);
                try {
                    String respondId = QRHttpUtils.postSubmissionQuery(submissionQuery);
                    JSONObject object = (JSONObject) new JSONParser().parse(respondId);
                    System.out.println(object.get("id"));
                    String result;
                    while (true) {
                        result = QRHttpUtils.getSubmissionResult(object.get("id").toString());
                        if (QRHttpUtils.isExecuteDone(result)) {
                            break;
                        }
                        Thread.sleep(100);
                    }
                    System.out.println(result);
                    final QRHttpUtils.SubmissionDetail detail = new QRHttpUtils.SubmissionDetail(result);
                    Platform.runLater(new Runnable() {
                        public void run() {
                            consoleOutput.setText(detail.getOutputOrError());
                            consoleBar.getChildren().remove(spinner);
                            consoleBar.setDisable(false);
                            compileButton.setDisable(false);
                        }
                    });

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void setupQRCode() {
        Random r = new Random();
        for (int i = 0; i < 2; i++) {
            Label lbl = new Label("item" + String.valueOf(i));
//            lbl.setPrefSize(300, 300);
            lbl.setStyle("-fx-background-color:rgb(" + r.nextInt(255) + "," + r.nextInt(255) + "," + r.nextInt(255) + ");");
            qrlistView.getItems().add(lbl);
        }
    }

    private int saveSourceCode(int saveMode) {
        try {
            if (workingSourceCodeFile == null || saveMode == SAVE_MODE_ANOTHER) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("소스 코드", "*.c", "*.cpp", "*.java", "*.php", "*.asp", "*.html", "*.htm", "*.js", "*.css", "*.py", "*.sh", "*.rua", "*.jsp", "*.pl", "*.fs", "*.bas", "*.ss", "*.s", "*.swift", "*.cc", "*.pdml", "*.lss", "*.lsp", "*.cp", "*.phps", "*.txt"));
                File tempFile = fileChooser.showSaveDialog(editwebview.getParent().getScene().getWindow());
                if (tempFile == null) {
                    return SAVE_CODE_STATUS_CANCELED;
                }
                workingSourceCodeFile = tempFile;
                workingSourceCodeFileEncoding = null;
                if (!workingSourceCodeFile.exists()) {
                    workingSourceCodeFile.createNewFile();
                }
            }
            System.out.println("Saved as:" + workingSourceCodeFileEncoding);
            FileUtils.writeStringToFile(workingSourceCodeFile, getSourceCodeValueFromEditor(), false);
        } catch (IOException e) {
            e.printStackTrace();
            return SAVE_CODE_STATUS_FAILED;
        }
        return SAVE_CODE_STATUS_SUCCESS;
    }

    private String getSourceCodeValueFromEditor() {
        String sourceCodeValue = (String) editwebview.getEngine().executeScript("editor.getValue()");
        return sourceCodeValue;
    }

    private JFXDialog getCompileDialog() {
        try {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("컴파일하기"));
            ((Text) content.getHeading().get(0)).setFont(Font.font("Roboto", 17));

            Node body = new FXMLLoader(getClass().getResource("compileDialog.fxml")).load();
            content.setBody(body);

            compileDialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);

            final JFXTextArea compileInput = (JFXTextArea) body.lookup("#compileDialogInput");
            final ComboBox<String> compileSyntax = (ComboBox<String>) body.lookup("#compileDialogSyntax");
            for (Pair<String, Integer> p : availableLanguages) {
                compileSyntax.getItems().add(p.getLeft());
            }
            compileSyntax.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    compile_used_language = compileSyntax.getSelectionModel().getSelectedIndex();
                }
            });

            compileSyntax.getSelectionModel().select(compile_used_language);


            JFXButton ok = (JFXButton) body.lookup("#compileDialogCompile");
            ok.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {

                    System.out.println("compile");
                    String sourceCodeContent = getSourceCodeValueFromEditor();
                    int compileLanguageCode = availableLanguages.get(compile_used_language).getRight();
                    String compileInputContent = compileInput.getText();
                    compileInputContent = (compileInputContent == null || compileInputContent.isEmpty()) ? "" : compileInputContent;
                    String submissionQuery = QRHttpUtils.createSubmissionQuery(sourceCodeContent, compileLanguageCode, compileInputContent);
                    System.out.println(submissionQuery);
                    try {
                        String respondId = QRHttpUtils.postSubmissionQuery(submissionQuery);
                        JSONObject object = (JSONObject) new JSONParser().parse(respondId);
                        System.out.println(object.get("id"));
                        String result;
                        while (true) {
                            result = QRHttpUtils.getSubmissionResult(object.get("id").toString());
                            if (QRHttpUtils.isExecuteDone(result)) {
                                break;
                            }
                            Thread.sleep(100);
                        }

                        System.out.println(result);


                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            JFXButton cancel = (JFXButton) body.lookup("#compileDialogCancel");
            cancel.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    compileDialog.close();
                }
            });
            return compileDialog;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
                if (event.getCode() == KeyCode.ESCAPE) {
                    pressedEscape();
                }
                if (event.isControlDown() && event.getCode() == KeyCode.S) {
                    int respondCode = saveSourceCode(SAVE_MODE_OVERWRITE);
                    switch (respondCode) {
                        case SAVE_CODE_STATUS_SUCCESS:
                            snackbar.show("저장하였습니다.", 2000);
                            break;
                        case SAVE_CODE_STATUS_FAILED:
                            snackbar.show("저장하는데 실패하였습니다.", 2000);
                            break;
                        default:
                            break;
                    }
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

    private void setupcomboBoxLanguage(final ComboBox<String> comboBoxLanguage) {
        for (int i = 0; i < languages.length; i++) {
            this.comboBoxLanguage.getItems().add(languages[i].toUpperCase());
        }
        comboBoxLanguage.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                String program_language = comboBoxLanguage.getSelectionModel().getSelectedItem();
                System.out.println("editor.getSession().setMode(\"ace/mode/" + program_language.toLowerCase() + "\");\n");
                editwebview.getEngine().executeScript("editor.getSession().setMode(\"ace/mode/" + program_language.toLowerCase() + "\");\n");
            }
        });
//        comboBoxLanguage.getSelectionModel().selectFirst();

    }


    private boolean eraseSourceCode() {
        try {
            editwebview.getEngine().executeScript("editor.setValue('');  editor.getSession().setUndoManager(new ace.UndoManager());");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void enableFirebug(final WebEngine engine) {
        editwebview.getEngine().executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");
    }

    @FXML
    void save(ActionEvent event) {
        snackbar.show("저장되었습니다.", 2000);
        /*final Clipboard clipboard = Clipboard.getSystemClipboard();
        String content = (String) clipboard.getContent(DataFormat.PLAIN_TEXT);
        content = StringEscapeUtils.escapeEcmaScript(content);
        String query = "editor.insert('" + content + "');";
        System.out.print(query);
        editwebview.getEngine().executeScript(query);
        if (!editwebview.isFocused()) {
            editwebview.requestFocus();
        }*/
    }

    @FXML
    void about(ActionEvent event) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("QR컴파일러에 오신것을 환영합니다."));
        ((Text) content.getHeading().get(0)).setFont(Font.font("Roboto", 17));
        content.setBody(new Text("이 프로그램은 작성한 소스코드를 QR코드, 혹은 QR코드 묶음으로 변환하여 \n취급, 전달, 공유를 간편하게 만든게 특징입니다.\n\n또한, 서버에 저장하여 계정만 있다면 어디에나, 어느 컴퓨터, 어느 운영체제에서든\n사용이 가능하게하는 기능을 제공합니다.\n\nQR코드를 활용한 다양한 기능들을 사용해보세요!\n\nCreated By: 정승욱"));
        ((Text) content.getBody().get(0)).setFont(Font.font("Roboto", 12));
        aboutDialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("확인");
        button.setStyle("-fx-text-fill: #2196F3;");
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                aboutDialog.close();
            }
        });
        content.setActions(button);
        aboutDialog.show();
        editwebview.requestFocus();
    }

    @FXML
    void closeQRCompiler(ActionEvent event) {
        try {
            //noinspection Since15
            if (!((workingSourceCodeFile == null && (getSourceCodeValueFromEditor() == null || getSourceCodeValueFromEditor().isEmpty() || getSourceCodeValueFromEditor().length() <= 0)) || (workingSourceCodeFile != null && FileUtils.readFileToString(workingSourceCodeFile, workingSourceCodeFileEncoding).equals(getSourceCodeValueFromEditor())))) {
                JFXDialogLayout content = new JFXDialogLayout();
                if (workingSourceCodeFile != null) {
                    content.setHeading(new Text("'" + workingSourceCodeFile.getName() + "'에서 수정한 내용을 저장하겠습니까?"));
                } else {
                    content.setHeading(new Text("작성하신 내용을 저장하겠습니까?"));
                }
                ((Text) content.getHeading().get(0)).setFont(Font.font("Roboto", 17));
                content.setBody(new Text("저장하지 않으면 수정사항이 사라집니다."));
                ((Text) content.getBody().get(0)).setFont(Font.font("Roboto", 12));
                if (closeDialog != null) {
                    closeDialog.close();
                }
                closeDialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);

                JFXButton save = new JFXButton("저장");
                save.setStyle("-fx-text-fill: #2196F3;");
                save.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        int respondCode = saveSourceCode(SAVE_MODE_OVERWRITE);
                        switch (respondCode) {
                            case SAVE_CODE_STATUS_SUCCESS:
                                System.exit(0);
                                break;
                            case SAVE_CODE_STATUS_FAILED:
                                snackbar.show("저장하는데 실패하였습니다.", 2000);
                                break;
                            case SAVE_CODE_STATUS_CANCELED:
                                break;
                        }
                    }
                });

                JFXButton dontsave = new JFXButton("저장하지 않기");
                dontsave.setStyle("-fx-text-fill: #2196F3;");
                dontsave.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        System.exit(0);
                    }
                });

                JFXButton cancel = new JFXButton("취소");
                cancel.setStyle("-fx-text-fill: #2196F3;");
                cancel.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        closeDialog.close();
                        editwebview.requestFocus();
                    }
                });

                content.setActions(save, dontsave, cancel);
                closeDialog.show();
                editwebview.requestFocus();
            } else {
                JFXDialogLayout content = new JFXDialogLayout();
                content.setHeading(new Text("정말로 닫으시겠습니까?"));
                ((Text) content.getHeading().get(0)).setFont(Font.font("Roboto", 17));
                content.setBody(new Text("  "));
                ((Text) content.getBody().get(0)).setFont(Font.font("Roboto", 12));
                if (closeDialog != null) {
                    closeDialog.close();
                }
                closeDialog = new JFXDialog(root, content, JFXDialog.DialogTransition.CENTER);
                JFXButton button = new JFXButton("확인");
                button.setStyle("-fx-text-fill: #2196F3;");
                button.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        System.exit(0);
                    }
                });

                JFXButton cancel = new JFXButton("취소");
                cancel.setStyle("-fx-text-fill: #2196F3;");
                cancel.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        closeDialog.close();
                        editwebview.requestFocus();
                    }
                });

                content.setActions(button, cancel);

                closeDialog.show();
                editwebview.requestFocus();

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void pressedEscape() {
        if (closeDialog != null) {
            closeDialog.close();
        }
        if (aboutDialog != null) {
            aboutDialog.close();
        }
    }
}
