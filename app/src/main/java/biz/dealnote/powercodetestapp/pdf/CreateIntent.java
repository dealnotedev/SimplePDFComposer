package biz.dealnote.powercodetestapp.pdf;

import java.io.File;
import java.util.List;

/**
 * Created by r.kolbasa on 29.12.2017.
 * PowercodeTestApp
 */
public class CreateIntent {

    private final String fileName;

    private final List<Page> pages;

    public CreateIntent(String fileName, List<Page> pages) {
        this.pages = pages;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public List<Page> getPages() {
        return pages;
    }
}