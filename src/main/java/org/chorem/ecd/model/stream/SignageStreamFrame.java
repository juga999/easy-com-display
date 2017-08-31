package org.chorem.ecd.model.stream;

import java.nio.file.Path;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public class SignageStreamFrame {

    private String src;

    private String thumbnail;

    public SignageStreamFrame(Path src, Path thumbnail) {
        this(src.toString(), thumbnail.toString());
    }

    public SignageStreamFrame(String src, String thumbnail) {
        this.src = src;
        this.thumbnail = thumbnail;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
