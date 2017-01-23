package sample;

import com.google.common.io.Files;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.print.attribute.standard.MediaSize;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class Controller {

    String pathOut = null,Name = null, ext=null;
    String pathIn = null;
  //  Encoder Enc = new Encoder();
    //Encoder Enc = new Encoder();
    Builder Build = new Builder();

    public Controller() throws IOException {
    }

    public void BtnConvert(ActionEvent actionEvent){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/Stage_1.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stageOld = (Stage) BtnConvert.getScene().getWindow();
            stageOld.close();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Stage 1");
            stage.setScene(new Scene(root1));
            stage.show();
            //((Node)(.getSource())).getScene().getWindow().hide();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void BtnOpenSource(ActionEvent actionEvent) throws FileNotFoundException {

        FileChooser fileChooser= new FileChooser();
        fileChooser.setTitle("Open Video File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP4" , "*.mp4"),
                new FileChooser.ExtensionFilter("AVI", "*.avi"));
        File selectedFile=fileChooser.showOpenDialog(new Stage());
        pathIn= selectedFile.getPath();
        Name = selectedFile.getName();
        ext = Files.getFileExtension(Name);
        Build.setPathIn(pathIn);
        tFFormat.setEditable(false);
        fileName.setEditable(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tFFormat.setText(ext);
                fileName.setText(Name.replaceFirst("[.][^.]+$", ""));
            }
        });


    }

        //Enc.setPathIn(Path);
    public void BtnFormat(ActionEvent actionEvent) throws IOException {

        if(pathIn==null){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("You have to choose a file to convert");
            alert.setContentText("Choose the file !");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                FileChooser fileChooser= new FileChooser();
                fileChooser.setTitle("Open Video File");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("MP4" , "*.mp4"),
                        new FileChooser.ExtensionFilter("AVI", "*.avi"));
                File selectedFile=fileChooser.showOpenDialog(new Stage());
                pathIn= selectedFile.getPath();
                Build.setPathIn(pathIn);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        tFFormat.setText(Build.getFormat());
                    }
                });
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
        else {
            File file;
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("MP4","*.mp4")
            );
            file = fileChooser.showSaveDialog(new Stage());
            pathOut = file.getPath();
            // System.out.println(path);
            Build.setPathOut(pathOut);
            Build.setProgressBar(progressBar);
            Build.setLabelPercent(labelPercent);
            Build.StartConverting();
        }
    }
        /*
            get process


        get media information
        FFprobe ffprobe = new FFprobe("/path/to/ffprobe");
        FFmpegProbeResult probeResult = ffprobe.probe("input.mp4");

        FFmpegFormat format = probeResult.getFormat();
        System.out.format("%nFile: '%s' ; Format: '%s' ; Duration: %.3fs",
            format.filename,
            format.format_long_name,
            format.duration
        );

        FFmpegStream stream = probeResult.getStreams().get(0);
        System.out.format("%nCodec: '%s' ; Width: %dpx ; Height: %dpx",
            stream.codec_long_name,
            stream.width,
            stream.height
        );*/
    @FXML private ProgressBar progressBar;
    @FXML private Label labelPercent;
    @FXML private Button BtnConvert;
    @FXML private TextField tFFormat;
    @FXML private TextField fileName;
  //  @FXML private AnchorPane pane;

  //  public void BtnClearSelection(MouseEvent mouseEvent) {
  //
   // }
}
