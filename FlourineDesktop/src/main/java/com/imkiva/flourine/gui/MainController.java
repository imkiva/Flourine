package com.imkiva.flourine.gui;

import com.imkiva.flourine.script.FlourineScriptRuntime;
import com.imkiva.flourine.script.parser.file.SourceFileFactory;
import com.imkiva.flourine.script.runtime.Interpreter;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author kiva
 * @date 2019-08-03
 */
public class MainController implements Initializable {
    public TextArea codeEditor;
    public Text answerView;

    private FlourineScriptRuntime runtime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        runtime = new FlourineScriptRuntime();
        runtime.setSolveOutput(value ->
                answerView.setText(value == null ? "" : value.toString()));

        codeEditor.textProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                Interpreter interpreter = runtime.newInterpreter();
                interpreter.evaluate(SourceFileFactory.fromCode(newValue));
            });
        });
    }
}
