package sample;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.progress.Progress;
import net.bramp.ffmpeg.progress.ProgressListener;

import java.io.IOException;
import java.io.StreamCorruptedException;

/**
 * Created by Аndrey on 16.12.2016 г..
 */
public class Encoder extends Thread {
    Thread t;
    ProgressBar progressBar;
    Label labelPercent;
    private double percentage;


    //FFmpeg ffmpeg = new FFmpeg("D:/Java/ffmpeg-3.2-win64-static/bin/ffmpeg");
  //  FFprobe ffprobe = new FFprobe("D:/Java/ffmpeg-3.2-win64-static/bin/ffprobe");

    public void setProbeResult(FFmpegProbeResult probeResult) {
        this.probeResult = probeResult;
    }

    FFmpegProbeResult probeResult;

    public void setExecutor(FFmpegExecutor executor) {
        this.executor = executor;
    }

    FFmpegExecutor executor;

    public void setBuilder(FFmpegBuilder builder) {
        this.builder = builder;
    }

    FFmpegBuilder builder;


    public void setLabelPercent(Label labelPercent) {
        this.labelPercent = labelPercent;
    }
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
    public Encoder() throws IOException {

    }


    public void Encode() throws IOException {

        progressBar.setProgress(0);
        FFmpegJob job = executor.createJob(builder, new ProgressListener() {

            // Using the FFmpegProbeResult determine the duration of the input
            final double duration_us = probeResult.getFormat().duration * 1000000.0;

            @Override
            public void progress(Progress progress) {
                percentage = progress.out_time_ms / duration_us;
                progressBar.setProgress(percentage);
                progressBar.progressProperty().addListener((observable, oldValue, newValue)
                        -> Platform.runLater(()
                        -> labelPercent.setText(Integer.toString((int)(percentage*100)))));
               // labelPercent.setText(Integer.toString((int) percentage));

                // Print out interesting information about the progress
                  //      percentage * 100,
                   //     progress.progress,
                   //     progress.frame,
                   //     progress.out_time_ms,
                    //    progress.fps.doubleValue(),
                     //   progress.speed
            }
        });
        job.run();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Your conversation has been completed");
                alert.showAndWait();
            }
        });

        // progressBar.progressProperty().unbind();
        // progressBar.progressProperty().bind();


        // Run a one-pass encode
        //executor.createJob(builder).run();

        // Or run a two-pass encode (which is slower at the cost of better quality)
        // executor.createTwoPassJob(builder).run();

       // valueProp.addListener(new ChangeListener<String>() {
        //    @Override
       //     public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
       //         if( newValue!= null){
       //            // System.out.println("change... \n");
       //           //  labelPercent.setText(newValue);
       //             //labelPercent.textProperty().setValue(newValue);
        //        }
       //     }
       // });

       //get process
        // FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
    }

    public void run(){
        try {
                // task.run();
                 Encode();
           //Platform.runLater(new Runnable() {
             //   @Override
            //    public void run() {
             //       labelPercent.setText(Integer.toString((int)percentage*100));
             //   }
          //  });*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   // labelPercent.accessibleTextProperty().bind();

   // Task task = new Task<Void>() {
//
    //    @Override
      //  public Void call() throws Exception {
      //      while (true) {
       //         Platform.runLater(new Runnable() {
        //            @Override
       //             public void run() {
        //                labelPercent.setText(Integer.toString((int) percentage));
        //            }
        //        });

        //       // return null;
        //    }
       //  }
   // };*/

    public void start(){
        if(t==null){
            t= new Thread(this, "Encoder");
            t.start();
        }
    }
}
