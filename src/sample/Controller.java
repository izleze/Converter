package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class Controller {

    String Path = null,Name = null;
  //  Encoder Enc = new Encoder();


    public Controller() throws IOException {
    }

    public void BtnConvert(ActionEvent actionEvent){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/Stage_1.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Stage 1");
            stage.setScene(new Scene(root1));
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void BtnOpenSource(ActionEvent actionEvent) throws FileNotFoundException {
        FileChooser();

        //Enc.setPathIn(Path);
    }
    public void BtnFormat(ActionEvent actionEvent) throws IOException {

        if(Path==null){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("You have to choose a file to convert");
            alert.setContentText("Choose the file !");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                FileChooser();
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
        else {
            File file;
            String path;
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("MP4","*.mp4")
            );
            file = fileChooser.showSaveDialog(new Stage());
            path = file.getPath();
            // System.out.println(path);
            Encoder Enc = new Encoder(Path, path);
            Enc.setPathIn(Path);
            Enc.setPathOut(path);
            Enc.setProgressBar(progressBar);
            Enc.setLabelPercent(labelPercent);
            Enc.start();
            //Enc.Encode();
        }
    }

    public  void FileChooser(){
        FileChooser fileChooser= new FileChooser();
        fileChooser.setTitle("Open Video File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video File" , "*.mp4"));
        File selectedFile=fileChooser.showOpenDialog(new Stage());
        Path = selectedFile.getPath();
        Name = selectedFile.getName();
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

}
