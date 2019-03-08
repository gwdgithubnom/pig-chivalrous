import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.jcodec.api.transcode.Transcoder;
import org.jcodec.common.Demuxer;
import org.jcodec.common.Format;
import org.jcodec.common.JCodecUtil;

/**
 * @Author gwd
 * @Time 01-16-2019 Wednesday
 * @Description: developer.tools:
 * @Target:
 * @More:
 */
public class DataMain {
    public static void main(String[] args) throws IOException {
        JCodecUtil jCodecUtil;
        URL url = ClassLoader.getSystemClassLoader().getResource("1.webp");
        File file = new File(url.getFile());
        if (file.exists()) {
            Demuxer demuxer = JCodecUtil.createDemuxer(Format.WEBP, file);
            List test = demuxer.getTracks();
            System.out.println(demuxer);
        }
        Transcoder.TranscoderBuilder transcoderBuilder = new Transcoder.TranscoderBuilder();

        // SourceImpl source=new SourceImpl("1.webp",Format.WEBP,Codec.VP8)
        // transcoderBuilder.addSource()
        // Transcoder transcoder = new Transcoder("image.webp", "image.png", Format.WEBP, Format.IMG, Codec.VP8,
        // Codec.PNG, null, null, false, false, new ArrayList<Filter>());
        // transcoder.transcode();
    }

}
