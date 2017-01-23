package sample;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;

import java.io.IOException;

/**
 * Created by Andrey on 3.1.2017 г..
 */
public class Builder {

    Encoder Enc = new Encoder();

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    ProgressBar progressBar;

    public void setLabelPercent(Label labelPercent) {
        this.labelPercent = labelPercent;
    }

    Label labelPercent;



    FFmpegBuilder builder;
    FFmpeg ffmpeg = new FFmpeg("D:/Java/ffmpeg-3.2-win64-static/bin/ffmpeg");
    FFprobe ffprobe = new FFprobe("D:/Java/ffmpeg-3.2-win64-static/bin/ffprobe");

    public FFmpegProbeResult getProbeResult() throws IOException {
        probeResult = ffprobe.probe(pathIn);
        return probeResult;
    }

    FFmpegProbeResult probeResult;

    public FFmpegExecutor getExecutor() {
        executor = new FFmpegExecutor(ffmpeg, ffprobe);
        return executor;
    }

    FFmpegExecutor executor;

    public String getPathOut() {
        return pathOut;
    }

    public void setPathOut(String pathOut) {
        this.pathOut = pathOut;
    }

    String pathOut;

    public String getPathIn() {
        return pathIn;
    }

    public void setPathIn(String pathIn) {
        this.pathIn = pathIn;
    }

    String pathIn;

    public Builder() throws IOException {

    }

    public FFmpegBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(FFmpegBuilder builder) {
        this.builder = builder;
    }

    public String getFormat(){
        try {
            FFmpegProbeResult probeResult = ffprobe.probe(pathIn);
            FFmpegFormat format = probeResult.getFormat();
            return format.format_long_name;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getFileName() {
        FFmpegProbeResult probeResult = null;
        try {
            probeResult = ffprobe.probe(pathIn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FFmpegFormat format = probeResult.getFormat();
        return format.filename;
    }
    private void FFBuilder() throws IOException {
        //   FFmpegFormat format = probeResult.getFormat();

         probeResult = ffprobe.probe(pathIn);
         executor = new FFmpegExecutor(ffmpeg, ffprobe);

         builder = new FFmpegBuilder()


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
    }

    public void StartConverting() throws IOException {
        FFBuilder();
        SetersForEncoding();
        Enc.start();
    }
    private void SetersForEncoding(){
        Enc.setExecutor(executor);
        Enc.setBuilder(builder);
        Enc.setLabelPercent(labelPercent);
        Enc.setProgressBar(progressBar);
        Enc.setProbeResult(probeResult);
    }

}
