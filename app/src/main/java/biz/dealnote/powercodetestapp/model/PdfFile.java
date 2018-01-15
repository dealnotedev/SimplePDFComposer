package biz.dealnote.powercodetestapp.model;

import java.io.File;
import java.util.Date;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public class PdfFile {

    private final File file;

    private final Date createDateTime;

    public PdfFile(File file, Date createDateTime) {
        this.file = file;
        this.createDateTime = createDateTime;
    }

    public File getFile() {
        return file;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }
}