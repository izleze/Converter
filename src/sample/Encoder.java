package sample;

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

/**
 * Created by Аndrey on 16.12.2016 г..
 */
public class Encoder extends Thread {
    Thread t;
    ProgressBar progressBar;
    Label labelPercent;

    public void setLabelPercent(Label labelPercent) {
        this.labelPercent = labelPercent;
    }
    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
    String pathIn;

    public String getPathIn() {
        return pathIn;
    }
    public void setPathIn(String pathIn) {
        this.pathIn = pathIn;
    }

    String pathOut;

    public String getPathOut() {
        return pathOut;
    }
    public void setPathOut(String pathOut) {
        this.pathOut = pathOut;
    }

    public Encoder(String pathIn, String pathOut) throws IOException {
        this.pathIn = pathIn;
        this.pathOut = pathOut;
    }

    FFmpeg ffmpeg = new FFmpeg("D:/Java/ffmpeg-3.2-win64-static/bin/ffmpeg");
    FFprobe ffprobe = new FFprobe("D:/Java/ffmpeg-3.2-win64-static/bin/ffprobe");

    public void Encode() throws IOException {

        FFmpegProbeResult probeResult = ffprobe.probe(pathIn);
        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        FFmpegBuilder builder = Builder();
        progressBar.setProgress(0);
        labelPercent.setText(null);

        FFmpegJob job = executor.createJob(builder, new ProgressListener() {

            // Using the FFmpegProbeResult determine the duraction of the input
            final double duration_us = probeResult.getFormat().duration * 1000000.0;

            @Override
            public void progress(Progress progress) {
                double percentage = progress.out_time_ms / duration_us;
                progressBar.setProgress(percentage);

                //labelPercent.setText(Integer.toString((int) percentage*100));
                labelPercent.setText(String.format("%.2f", percentage*100));


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
        System.out.println("Finished");
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

    private FFmpegBuilder Builder() throws IOException {
        FFmpegProbeResult probeResult = ffprobe.probe(pathIn);
        FFmpegFormat format = probeResult.getFormat();
        //System.out.println(Path);
        FFmpegBuilder builder = new FFmpegBuilder()


                .setInput(probeResult)     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists

                .addOutput(pathOut)   // Filename for the destination
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
        return builder;
    }
    public void run(){
        try {
            Encode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(){
        if(t==null){
            t= new Thread(this, "Encoder");
            t.start();
        }
    }
}
