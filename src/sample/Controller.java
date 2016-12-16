package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller {

    String Path,Name;

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
        FileChooser fileChooser= new FileChooser();
        fileChooser.setTitle("Open Video File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video File" , "*.mp4"));
        File selectedFile=fileChooser.showOpenDialog(new Stage());
        Path = selectedFile.getPath();
        Name = selectedFile.getName();
    }
    public void BtnFormat(ActionEvent actionEvent) throws IOException {

        File file;
        String path;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        file = fileChooser.showSaveDialog( new Stage());
        path = file.getPath();
       // System.out.println(path);
        Encode(Path, path);

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


    FFmpeg ffmpeg = new FFmpeg("D:/Java/ffmpeg-3.2-win64-static/bin/ffmpeg");
    FFprobe ffprobe = new FFprobe("D:/Java/ffmpeg-3.2-win64-static/bin/ffprobe");

    @FXML private ProgressBar progressBar;

    public void Encode(String Path, String outPath) throws IOException {

        FFmpegProbeResult probeResult = ffprobe.probe(Path);
        FFmpegFormat format = probeResult.getFormat();
        System.out.println(Path);
        FFmpegBuilder builder = new FFmpegBuilder()


                .setInput(probeResult)     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists

                .addOutput(outPath)   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                // .setTargetSize(20_250_000)  // Aim for a 20MB 250KB file

                .disableSubtitle()       // No subtiles

                .setAudioChannels(1)         // Mono audio
                .setAudioCodec("aac")        // using the aac codec
                .setAudioSampleRate(48_000)  // at 48KHz
                .setAudioBitRate(32768)      // at 32 kbit/s

                .setVideoCodec("libx264")     // Video using x264
                .setVideoFrameRate(24, 1)     // at 24 frames per second
                .setVideoResolution(640, 480) // at 640x480 resolution

                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

         progressBar.setProgress(0);
        FFmpegJob job = executor.createJob(builder, new ProgressListener() {

            // Using the FFmpegProbeResult determine the duraction of the input
            final double duration_us = probeResult.getFormat().duration * 1000000.0;

            @Override
            public void progress(Progress progress) {
                double percentage = progress.out_time_ms / duration_us;
                progressBar.setProgress(percentage);
                System.out.println(percentage*100);
                // Print out interesting information about the progress
              /* String locale = null;
                System.out.println(String.format(locale,
                        "[%.0f%%] status:%s frame:%d time:%d ms fps:%.0f speed:%.2fx",
                        percentage * 100,
                        progress.progress,
                        progress.frame,
                        progress.out_time_ms,
                        progress.fps.doubleValue(),
                        progress.speed
                ));*/
            }
        });
        job.run();

       // progressBar.progressProperty().unbind();
       // progressBar.progressProperty().bind();


        // Run a one-pass encode
        //executor.createJob(builder).run();

        // Or run a two-pass encode (which is slower at the cost of better quality)
        // executor.createTwoPassJob(builder).run();

        /*get process
         FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

         */
     /*   @Override
        public  void initialize(URL url, ResourceBundle rb){

        }*/
    }
}
